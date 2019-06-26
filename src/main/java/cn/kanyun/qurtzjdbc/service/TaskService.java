package cn.kanyun.qurtzjdbc.service;

import java.io.FileNotFoundException;

/**
 * @author Kanyun
 */
public interface TaskService {

    /**
     * 是否是java文件,如何判断是否是java文件呢？那就是使用JavaCompiler的API
     * 去编译这个.java文件,可以编译成功则表示是java文件,不能编译成功,则说明该文件有问题
     * 需要注意的是,如果编译不过去并不代表了该文件的写法有问题,有一些其他问题是不可控的
     * 比如上传的java文件中引入了第三方库,而本项目中并没有引入这个库,所以在进行编译的时候
     * 会提示找不到这个类,所以在这一点上需要注意
     * @param filePath
     * @return
     */
    boolean isJavaFile(String filePath) throws FileNotFoundException;

    /**
     * 该java文件是否符合规范，这里的规范是自定义的规范
     * 比如该文件中必须存在一个名为 xx 的方法或变量,因为系统要根据
     * 制定规则名称进行反射调用
     * @param o
     * @return
     */
    boolean isStander(Object o);
}
