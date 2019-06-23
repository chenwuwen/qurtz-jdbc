package cn.kanyun.qurtzjdbc.util;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * 自定义一个编译之后的字节码对象
 * @author KANYUN
 */
public class ByteJavaFileObject  extends SimpleJavaFileObject {

    /**
     *  存放编译后的字节码
     */
    private ByteArrayOutputStream outPutStream;

    /**
     * Construct a SimpleJavaFileObject of the given kind and with the
     * given URI.
     *
     * @param uri  the URI for this file object
     * @param kind the kind of this file object
     */
    protected ByteJavaFileObject(String className, Kind kind) {
        super(URI.create("string:///" + className.replaceAll("\\.", "/") + Kind.SOURCE.extension), kind);
    }

    /**
     * StringJavaFileManage 编译之后的字节码输出会调用该方法（把字节码输出到outputStream）
     */
    @Override
    public OutputStream openOutputStream() {
        outPutStream = new ByteArrayOutputStream();
        return outPutStream;
    }

    /**
     *  在类加载器加载的时候需要用到
     */
    public byte[] getCompiledBytes() {
        return outPutStream.toByteArray();
    }
}