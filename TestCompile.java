package com.test.compile;

/**
 * 这个类主要是为了测试Java的JavaCompiler的动态编译
 * 需要注意的是,编译完成后,生成的.class文件在其 全限定名所在的路径下
 * 如该类编译后的.class文件所在目录为 JavaCompiler指定的输出目录/com/test/compile/TestCompile.class
 * 同时也说明了,一个java类,可以有包名,但是不一定会在其包所在的目录里
 */
public class TestCompile {

    public static void main(String[] args) {
        System.out.println("Hello World");
    }
}