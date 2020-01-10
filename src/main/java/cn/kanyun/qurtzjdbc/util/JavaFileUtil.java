package cn.kanyun.qurtzjdbc.util;

import lombok.extern.slf4j.Slf4j;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 检测Java文件
 * https://www.cnblogs.com/andysd/p/10081443.html
 * https://www.cnblogs.com/xuyatao/p/6914769.html
 * https://wcp88888888.iteye.com/blog/998366
 * http://www.360doc.com/content/17/1120/08/9200790_705450580.shtml
 * https://www.jianshu.com/p/44395ef6406f
 * https://seanwangjs.github.io/2018/03/13/java-runtime-compile.html
 * JavaCompiler- 表示java编译器,run方法执行编译操作. 还有一种编译方式是先生成编译任务(CompilationTask),
 * 然后调用CompilationTask的call方法执行编译任务
 *
 * @author KANYUN
 */
@Slf4j
public class JavaFileUtil {

    /**
     * 编译生成的class文件的位置(需要注意的是生成后.class文件的真实位置是：classFileOutputPath+包名组成的目录)
     * 这个位置不能随便指定,除非仅仅只是为了编译生成.class文件,如果编译完成后还需要使用,则需要将生成的.class文件放到ClassPath中
     * 否则在使用的时候会报错:ClassNotFoundException 异常,因为默认类加载器是ApplicationClassLoad
     * 如果需要确实需要将生成的.class文件放到其他位置,并且之后要使用它,那么需要自定义类加载器,并加载它
     */
    private static String classFileOutputPath = JavaFileUtil.class.getResource("/").getPath() + File.separator;

//    对于不在classPath位置的.class文件(包括数据库,网络等等),需要自定义类加载器加载,所以.class文件生成目录需要额外注意
//    private static String classFileOutputPath = System.getProperty("user.dir");

    /**
     * 包名PATTERN
     * 利用预编译功能，可以有效加快正则匹配速度 不要在方法体内定义规则
     */
    private static Pattern PACKAGE_NAME_PATTERN = Pattern.compile("package\\s+\\S+\\s*;");

    /**
     * 类名PATTERN
     * 利用预编译功能，可以有效加快正则匹配速度 不要在方法体内定义规则
     */
    private static Pattern CLASS_NAME_PATTERN = Pattern.compile("class\\s+\\S+\\s+\\{");

    /**
     * 检测是否是Java文件
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static boolean isJavaFileSimPle(String filePath) throws FileNotFoundException {
        // 检测文件是否存在,使用NIO中的Files工具类
        Path path = Paths.get(filePath);
        // 第二个参数是个不定长参数,LinkOption.NOFOLLOW_LINKS：表示检测时不包含符号链接文件
        boolean exist = Files.exists(path, LinkOption.NOFOLLOW_LINKS);
        if (exist) {
            // 如果文件存在,则进行动态编译
            // JAVA1.6之后JDK提供了一套compiler API，定义在JSR199中,
            // 提供在运行期动态编译java代码为字节码的功能,需要强调的是，compiler
            // API的相关实现被放在tools.jar中，JDK默认会将tools.jar放入classpath而jre没有，因此如果发现compiler
            // API相关类找不到，那么请检查一下tools.jar是否已经在classpath中

            // 获得编译器对象
            JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

            // 对源文件进行编译,Run 方法的前三个参数，分别可以用来重定向标准输入、标准输出和标准错误输出，null 值表示使用默认值,返回0表示编译成功
            // 通过JavaCompiler进行编译都是在当前目录(被加载java文件目录)下生成.class文件，而使用编译选项可以改变这个默认目录。编译选项是一个元素为String类型的Iterable集合
            // 使用编译参数-d指定字节码输出目录【此时class文件的目录为 classFileOutputPath+包名 组成的目录】
            // int ret = javac.run(null, null, null, "-d", classFileOutputPath, filePath);
            int ret = javac.run(null, null, null, filePath);
            return ret == 0;
        }
        throw new FileNotFoundException();
    }

    /**
     * 检测是否是Java文件
     * 如果是java文件,会进行编译,并且将编译后的.class文件保存到项目的classpath目录下
     * 如果java文件的文件名,与类名不一致,会编译不通过
     * @param sourceFileInputPath
     * @return
     * @throws FileNotFoundException
     */
    public static boolean isJavaFile(String sourceFileInputPath) throws FileNotFoundException {
        // 检测文件是否存在,使用NIO中的Files工具类
        Path path = Paths.get(sourceFileInputPath);
        // 第二个参数是个不定长参数,LinkOption.NOFOLLOW_LINKS：表示检测时不包含符号链接文件
        boolean exist = Files.exists(path, LinkOption.NOFOLLOW_LINKS);
        if (exist) {
            // 如果文件存在,则进行动态编译
            // JAVA1.6之后JDK提供了一套compiler API，定义在JSR199中,
            // 提供在运行期动态编译java代码为字节码的功能,需要强调的是，compiler
            // API的相关实现被放在tools.jar中，JDK默认会将tools.jar放入classpath而jre没有，因此如果发现compiler
            // API相关类找不到，那么请检查一下tools.jar是否已经在classpath中
            // 通过JavaCompiler进行编译都是在当前目录下生成.class文件，而使用编译选项可以改变这个默认目录。编译选项是一个元素为String类型的Iterable集合
            // 获得编译器对象
            JavaCompiler javac = ToolProvider.getSystemJavaCompiler();

            // 创建诊断信息监听器,用于监听编译过程中的信息,诊断信息监听器, 编译过程触发. 生成编译, JavaFileObject - 表示一个java源文件对象
            // DiagnosticListener则是从编译器中获取诊断信息，当出现诊断信息时则会调用其中的report方法，DiagnosticCollector则是进一步实现了DiagnosticListener，并将诊断信息收集到一个list中以便处理
            DiagnosticCollector<JavaFileObject> diagnosticListeners = new DiagnosticCollector<>();

            // 一个广义的、管理“文件”资源的接口，并不一定指“操作系统的磁盘文件系统”其实JavaFileManager只是一个接口，只要行为正确，那么就无所谓“文件”到底以何种形式、实际被存放在哪里
            // StandardJavaFileManager是基于磁盘文件的JavaFileManager实现,
            // 所有的文件查找、新文件输出位置都在磁盘上完成；也就是说，如果直接使用默认的StandardJavaFileManager来做动态编译，那么得到的效果就跟命令行中直接使用javac编译差不多
            JavaFileManager standardJavaFileManager = javac.getStandardFileManager(diagnosticListeners, null, null);

            // 设置编译选项，配置class文件输出路径,需要注意的是,生成的.class文件 在 classFileOutputPath+包名组成的文件夹 下面,更多参数可以查询javac命令详解
            Iterable<String> options = Arrays.asList("-d", classFileOutputPath, "-target", "1.8");

            // 获取路径下的java文件
            Iterable<? extends JavaFileObject> compilerFiles = ((StandardJavaFileManager) standardJavaFileManager)
                    .getJavaFileObjectsFromFiles(Arrays.asList(new File(sourceFileInputPath)));

            JavaCompiler.CompilationTask task = javac.getTask(null, standardJavaFileManager, null, options, null,
                    compilerFiles);

            // 方法就是进行动态编译可执行的代码，编译完成后,会返回boolean 为true表示编译成功

            if (task.call()) {
                return true;
            }

            // 输出诊断信息 Diagnostic - 表示一个诊断信息
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnosticListeners.getDiagnostics()) {
                // 可以在此处自定义编译诊断信息(错误)的输出格式
                log.debug("Error on line {} in {}", diagnostic.getLineNumber(),
                        diagnostic.getSource().toUri());
            }

            // 关闭FileManager
            try {
                standardJavaFileManager.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return false;
        }
        throw new FileNotFoundException();
    }

    /**
     * 获取类的全名称
     *
     * @param sourceCode 源码
     * @return 类的全名称
     */
    public static String getFullClassName(String sourceCode) {
        String className = "";
        Matcher matcher = PACKAGE_NAME_PATTERN.matcher(sourceCode);
        if (matcher.find()) {
            className = matcher.group().replaceFirst("package", "").replace(";", "").trim() + ".";
        }

        matcher = CLASS_NAME_PATTERN.matcher(sourceCode);
        if (matcher.find()) {
            className += matcher.group().replaceFirst("class", "").replace("{", "").trim();
        }
        return className;
    }

    public static void main(String[] args) throws IOException {
        // 获取工程路径,只到工程名。工作目录。

        // 利用System.getProperty()函数获取当前路径：user.dir指定了当前的路径
        log.debug(System.getProperty("user.dir"));
        // 设定为当前文件夹
        File directory = new File("");
        // 获取标准的路径
        log.debug(directory.getCanonicalPath());
        // 获取绝对路径
        log.debug(directory.getAbsolutePath());

        // 操作系统的classPath路径
        log.debug(System.getProperty("java.class.path"));
        // 获取项目的classPath的绝对位置。不深入底层的包名
        log.debug(JavaFileUtil.class.getResource("/").getPath());
        log.debug(Thread.currentThread().getContextClassLoader().getResource("").getPath());

        String path = System.getProperty("user.dir") + File.separator + "TestCompile" + ".java";
        log.debug("待编译的java文件的路径是：{}", path);
        log.debug("编译结果：{}", isJavaFile(path));
        log.debug("编译后的.class文件的路径是：{}", classFileOutputPath);
        try {
//            自定义类加载器实例化时传入编译生成.class文件后的路径
            CustomClassLoad classLoad = new CustomClassLoad(classFileOutputPath);

            // Class.forName有一个三个参数的重载方法，可以指定类加载器，平时我们使用的Class.forName("XX.XX.XXX")都是使用的系统类加载器Application
//            传入类的全限定名
            // Class clazz = Class.forName("com.test.compile.TestCompile");
            Class clazz = Class.forName("com.test.compile.TestCompile", true, classLoad);
            log.debug("当前使用的类加载器是：" + clazz.getClassLoader());

            // Class.getMethod(String name, Class<?>... parameterTypes)的作用是获得对象所声明的公开方法
            // 该方法的第一个参数name是要获得方法的名字，第二个参数parameterTypes是按声明顺序标识该方法形参类型。
            Method method = clazz.getMethod("main", String[].class);

            // 构造main方法所需参数
            Object[] pars = new Object[]{1};
            pars[0] = new String[]{};

            // 执行类的静态方法可以不需要获取实例
            // method.invoke(null, pars);
            method.invoke(clazz.newInstance(), pars);

        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}