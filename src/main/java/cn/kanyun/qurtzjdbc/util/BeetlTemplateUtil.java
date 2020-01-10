package cn.kanyun.qurtzjdbc.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import java.io.File;
import java.io.IOException;

public class BeetlTemplateUtil {

    /**
     * 生成的文件名的长度,不包括后缀,路径
     * 利用Beetl模板生成文件(占位符)
     *
     * @param length
     * @return
     */
    public static File generateJobTemplateFile(int length) throws IOException {
//        指定了模板根目录，即搜索模板的时候从根目录开始
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("template");
        org.beetl.core.Configuration cfg = null;
        try {
            cfg = org.beetl.core.Configuration.defaultConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        Template t = gt.getTemplate("/JobDemo.java");
//        生成文件名
        String fileName = generateRandomJavaFileName(length);

//        将文件名绑定到模板变量中
        t.binding("fileName", fileName);

//        返回渲染结果,返回String类型,也可以将渲染结果输出到Writer里,或者OutputStream
        String ret = t.render();

//        创建临时文件,三个参数 前缀(文件名,不能小于3个字符) 后缀
//        File.createTempFile()用来生成临时文件,虽然可以添加前缀,但是生成的文件名为前缀加上时间戳,因此需要重命名文件
        File file = File.createTempFile("xxx", null);
        File targetFile = new File(file.getParent() + File.separator + fileName + ".java");

//        复制文件,源文件的文件名不符合逻辑
        com.google.common.io.Files.copy(file, targetFile);

//        往文件中写入数据
        com.google.common.io.Files.write(ret.getBytes(), targetFile);
        return targetFile;
    }


    /**
     * 随机生成java文件名,同时这个名字也是Class的类名
     *
     * @return
     */
    public static String generateRandomJavaFileName(int length) {
//         产生5位长度的随机字符串，中文环境下是乱码 (小心使用)
        String r = RandomStringUtils.random(length);

        // 使用指定的字符生成5位长度的随机字符串
//        r = RandomStringUtils.random(5, new char[] { 'a', 'b', 'c', 'd', 'e',
//                'f', '1', '2', '3' });

        // 生成指定长度的字母和数字的随机组合字符串
//        r = RandomStringUtils.randomAlphanumeric(5);

        // 生成随机数字字符串
//        r = RandomStringUtils.randomNumeric(5);

        // 生成随机[a-z]字符串，包含大小写
        r = RandomStringUtils.randomAlphabetic(length);

        // 生成从ASCII 32到126组成的随机字符串
//        r = RandomStringUtils.randomAscii(4);

//        首字母大写
        return r.substring(0,1).toUpperCase().concat(r.substring(1).toLowerCase());
    }
}
