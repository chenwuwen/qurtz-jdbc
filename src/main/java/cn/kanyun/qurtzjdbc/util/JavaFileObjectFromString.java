package cn.kanyun.qurtzjdbc.util;

import javax.tools.SimpleJavaFileObject;
import java.net.URI;

/**
 * https://blog.csdn.net/lmy86263/article/details/59742557
 * 自定义JavaFileObject对象
 * 作用就是当不是上传java文件,而是传过来一个字符串时,将字符串转换为JavaFileObject对象
 * 使用方法：
 * JavaFileObject javaFileObject = new JavaFileObjectFromString("com.stone.generate.Hello", sourceCode.toString());
 * JavaCompiler.CompilationTask task = compiler.getTask(null, null, null,
 *                 Arrays.asList("-d", distDir.getAbsolutePath()), null,
 *                 Arrays.asList(javaFileObject));
 */
public class JavaFileObjectFromString extends SimpleJavaFileObject {

    /**
     * Java源文件的内容
     * 等待编译的源码字段
     */
    private String contents;

    /**
     * java源代码 => StringJavaFileObject对象 的时候使用
     */
    protected JavaFileObjectFromString(String className, String contents) {
        //uri为类的资源定位符号, 如: com/stone/generate/Hello.java
        super(URI.create(className.replaceAll("\\.", "/") + Kind.SOURCE.extension), Kind.SOURCE);
        //uri值为com.stone.generate.Hello时, 下面注释的代码导致异常 => com.stone.generate.Hello:1: 错误: 类Hello是公共的, 应在名为 Hello.java 的文件中声明
//        super(URI.create(className), Kind.SOURCE);
        this.contents = contents;
    }

    /**
     * 字符串源码会调用该方法
     * @param ignoreEncodingErrors
     * @return
     */
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        return contents;
    }
}