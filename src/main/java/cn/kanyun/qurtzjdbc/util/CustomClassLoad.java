package cn.kanyun.qurtzjdbc.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 自定义类加载器 为什么要自定义类加载器？
 * 1。加密：.class文件可以轻易的被反编译，如果你需要把自己的代码进行加密以防止反编译，可以先将编译后的代码用某种加密算法加密，
 * 类加密后就不能再用Java的ClassLoader去加载类了，这时就需要自定义ClassLoader在加载类的时候先解密类，然后再加载
 * 2.从非标准的来源加载代码(也即非ClassPath)：如果你的.class文件是放在数据库、甚至是在云端，就可以自定义类加载器，从指定的来源加载类。
 *
 * 如何自定义类加载器？
 * ①如果你看系统进行类加载的源码,调用loadClass时会先根据双亲委派模型在父加载器中加载，如果加载失败，则会调用当前加载器的findClass来完成加载。
 * ②因此我们自定义的类加载器只需要继承ClassLoader，并覆盖findClass方法
 * ③其中defineClass方法可以把二进制流字节组成的文件转换为一个java.lang.Class（只要二进制字节流的内容符合Class文件规范）。
 *
 * 实现自定义类加载器的三步： 1.继承ClassLoader 2.重写findClass()方法 3.调用defineClass()方法
 *
 * 关于findClass和loadClass的区别
 * 如果要符合双亲委派机制就重载findClass方法，如果要破坏双亲委派机制就重载loadClass方法，两种机制各有利弊。
 * 双亲委派可以保证同一个类只被加载一次，这样同一个类不会重复出现.class文件；
 * 而破坏双亲委派机制，执行loadClass的时候并不提交给父类加载器，而是当前类加载器首先尝试加载，如果加载失败再提交给父类加载器，
 * 这种机制可以存在多个重名的类.class文件。
 * @author KANYUN
 *
 */
@Slf4j
public class CustomClassLoad extends ClassLoader {

	/**
	 * 生成.class文件的路径,注意是文件的目录
	 */
	private String classFilePath;

	public CustomClassLoad() {
		super();
	}

	public CustomClassLoad(String classFilePath) {
		this.classFilePath = classFilePath;
	}

	/**
	 * 重写ClassLoader的findClass方法,为了不破坏双亲委派模型
	 * @param name 类的全限定名
	 * @return
	 * @throws ClassNotFoundException
	 */
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		File file = getClassFile(name);
		try {
			byte[] bytes = getClassBytes(file);
			// defineClass方法将字节码转化为类  defineClass的第一个参数name是类的全限定名
			Class<?> c = this.defineClass(name, bytes, 0, bytes.length);
			return c;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return super.findClass(name);
	}

	/**
	 * 得到.class文件的字节数组
	 *
	 * @param file
	 * @return
	 */
	private byte[] getClassBytes(File file) {
		// 这里要读入.class的字节，因此要使用字节流,使用try-with-resource语法糖简化代码,不需要显示关闭流 fis.close()
		try (FileInputStream fis = new FileInputStream(file)) {
			FileChannel fc = fis.getChannel();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			WritableByteChannel wbc = Channels.newChannel(baos);
			ByteBuffer by = ByteBuffer.allocate(1024);
			while (true) {
				int i = fc.read(by);
				if (i == 0 || i == -1) {
					break;
				}
				by.flip();
				wbc.write(by);
				by.clear();
			}
			return baos.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;

	}

	/**
	 * 从自定义的路径得到.class文件
	 *
	 * @return
	 */
	private File getClassFile(String name) {
		String classFile = classFilePath + File.separator + name.replace(".", File.separator) + ".class";
		log.debug("编译后的classFilePath: [{}]",  classFile);
		File file = new File(classFile);
		return file;
	}

}
