package cn.kanyun.qurtzjdbc.util;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import java.io.IOException;

/**
 * 自定义一个JavaFileManage来控制编译之后字节码的输出位置
 * @author KANYUN
 */
public class StringJavaFileManager extends ForwardingJavaFileManager {
    /**
     * Creates a new instance of ForwardingJavaFileManager.
     *
     * @param fileManager delegate to this file manager
     */
    protected StringJavaFileManager(JavaFileManager fileManager) {
        super(fileManager);
    }

    /**
     * 获取输出的文件对象，它表示给定位置处指定类型的指定类
     * @param location
     * @param className
     * @param kind
     * @param sibling
     * @return
     * @throws IOException
     */
    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) throws IOException {
        ByteJavaFileObject javaFileObject = new ByteJavaFileObject(className, kind);
        return javaFileObject;
    }
}