package cn.kanyun.qurtzjdbc.controller;


import cn.kanyun.qurtzjdbc.entity.Constant;
import cn.kanyun.qurtzjdbc.entity.Result;
import cn.kanyun.qurtzjdbc.util.JavaFileUtil;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;

/**
 * @author Kanyun
 * @date 2019/6/21
 */
@Controller
@RequestMapping("/file")
public class FileController {


    @GetMapping("/downloadTemplateFile")
    public void downloadTemplateFile(HttpServletResponse response) {

    }


    /**
     * 上传文件
     *
     * @param files
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public Result upload(@Param("files") MultipartFile[] files) {
        if (files.length == 0) {
            return Result.errorHandler("上传失败，请选择文件");
        }
        try {
            for (MultipartFile file : files) {
                String fileName = file.getOriginalFilename();
                File dest = new File(Constant.uploadFilePath + fileName);
                file.transferTo(dest);
            }
            return Result.successHandler();
        } catch (IOException e) {
            e.printStackTrace();
            return Result.errorHandler(e.getMessage());
        }
    }


    /**
     * 上传依赖:当我上传一个任务时,如果这个任务引入了一个系统中不存在的类,那么编译将
     * 不会通过,因此,假如新任务引入了一个系统不存在的类/类库,那么应该在上传新任务之前
     * 将依赖加入进系统classpath中
     * <p>
     * 上传依赖有3种情况：
     * 1.上传的是个 .java文件 (需要进行编译,在扔进classpath)
     * 1.上传的是个 .class文件 (不需要进行编译,直接扔进classpath,但是需要知道.class的全限定名,才能放进classpath下的指定文件夹)
     * 2.上传的是个 .jar文件 (不需要进行编译,直接扔进classpath)
     * javassist:https://www.cnblogs.com/scy251147/p/11100961.html
     *
     * @param dependentFile
     * @return
     */
    @PostMapping("/upload/dependent")
    @ResponseBody
    public Result upload(@Param("dependentFile") MultipartFile dependentFile) {
        if (dependentFile.isEmpty()) {
            return Result.errorHandler("上传失败，请选择文件");
        }
//        临时目录
        File tempDir = Files.createTempDir();
//        文件名
        String fileName = dependentFile.getOriginalFilename();
        String filePath = tempDir.getAbsolutePath() + File.separator + fileName;
        try {
            if (fileName.endsWith(".java")) {
                dependentFile.transferTo(new File(filePath));
                if (JavaFileUtil.isJavaFile(filePath)) {
                    return Result.successHandler("依赖文件已上传");
                }
                return Result.errorHandler("提交的文件不是Java文件");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.errorHandler(e.getMessage());
        }

        try {
            if (fileName.endsWith(".jar")) {
                dependentFile.transferTo(new File(filePath));
                String classPth = ResourceUtils.getURL("classpath:").getPath();
                String targetFilePath = classPth + File.separator + fileName;
                Files.copy(new File(filePath), new File(targetFilePath));
                return Result.successHandler("依赖文件已上传");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Result.errorHandler(e.getMessage());
        }

        try {
            if (fileName.endsWith(".class")) {

//                先转存一次文件,用于ClassFile读取该文件,获得类文件的class全限定名
                dependentFile.transferTo(new File(filePath));

//                先确定URLClassLoader加载类的路径,需要注意最后一个字符应该为系统文件分隔符
//                比如有一个类的全限定名为 cn.kanyun.Test 该class文件在 C:\\wuwen 目录下
//                那么此时应该设置URLClassLoader的加载路径为  C:\\wuwen\\ 也就是下面URL的值
//                同时还需要将该class文件 移动到 C:\\wuwen\\cn\\kanyun\\ 目录下 URLClassLoader才可以加载到该类
//                所以class文件的最终路径应该为 C:\\wuwen\\cn\\kanyun\\Test.class
                URL fileUrl = new File(filePath).getParentFile().toURI().toURL();

//                如果寻找的是class文件,URLClassLoader构造方法的值应该是目录(并且不包含class包名的目录)而不是具体到文件
                URLClassLoader classLoader = new URLClassLoader(new URL[]{fileUrl});
//                这一步需要注意,如果上传的class文件的文件名,与编译后的.class文件名不一致,会产生问题
//                参数name代表全限定类名(必须),因此直接通过ClassLoader来实现 是行不通的
//                Class clazz = classLoader.loadClass(fileName);

//                使用javassist来实现
                BufferedInputStream fin = new BufferedInputStream(new FileInputStream(filePath));
//                得到ClassFile对象
                ClassFile classFile = new ClassFile(new DataInputStream(fin));
//                得到了该class文件的全限定名[得到全限定名之后,就可以使用ClassLoader了]
                String className = classFile.getName();


//                这一步得到的是一个字符串,包名
                String packagePath = className.substring(0, className.lastIndexOf("."));
//                将包名中的 . 替换为 系统文件分隔符
                packagePath = StringUtils.replace(packagePath, ".", File.separator);

//                得到了class文件的全限定名,接着看ClassLoad是否能加载这个类,此时需要再次更改确定.class文件存放位置
                String tempPath = tempDir.getAbsolutePath() + File.separator + packagePath + File.separator;
                if (!new File(tempPath).exists()) {
//                    如果文件夹不存在,则创建
                    new File(tempPath).mkdirs();
                }

                String tempFilePath = tempPath + fileName;
//                复制文件(需要注意的是此时文件还在临时目录下)
                Files.copy(new File(filePath), new File(tempFilePath));

//                测试是否可以加载到类文件,如果可以加载到(不抛出异常),就可以将类文件放到classpath下了
                Class clazz = classLoader.loadClass(className);

//                开始创建目录
//                第一步：找到classpath
                String classPth = ResourceUtils.getURL("classpath:").getPath();
//                 第二步:根据上面的包名,创建目录
                String targetPath = classPth + File.separator + packagePath;
                if (!new File(targetPath).exists()) {
//                    如果文件夹不存在,则创建
                    new File(targetPath).mkdirs();
                }
//                第三步：将转存后的文件复制到新创建的目录中
                String targetFilePath = targetPath + File.separator + fileName;
                Files.copy(new File(tempFilePath), new File(targetFilePath));

                return Result.successHandler("依赖文件已上传");
            }
        } catch (IOException | ClassNotFoundException e) {
            return Result.errorHandler(e.getMessage());
        }

        return null;
    }
}
