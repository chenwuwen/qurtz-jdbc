package cn.kanyun.qurtzjdbc.tenant.chain;

import cn.kanyun.qurtzjdbc.entity.Tenant;
import cn.kanyun.qurtzjdbc.util.CMDUtil;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.CharSetUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import sun.nio.cs.UnicodeEncoder;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 构建App 责任链
 *
 * @author KANYUN
 */
@Slf4j
public class BuildAppChain extends TenantInitChain {

    public BuildAppChain(TenantInitChain nextTenantInitChain) {
        super(nextTenantInitChain);
    }

    @Override
    public void handler(Tenant tenant) {
        log.debug("Tenant处理责任链：为租户构建专属App");
        try {
            String propertiesPath = "C:\\Users\\HLWK-06\\Desktop\\tenant\\gradle.properties";
//            使用Spring的PropertiesLoaderUtils无法读到项目外的properties文件
//            Properties properties = PropertiesLoaderUtils.loadAllProperties("");
            File file = new File(propertiesPath);

            InputStream is = new BufferedInputStream(new FileInputStream(file));
            //解决读取properties文件中产生中文乱码的问题
            BufferedReader bf = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            Properties properties = new Properties();
            properties.load(bf);
            Enumeration enumeration = properties.propertyNames();
            log.debug("访问Properties文件中的所有属性开始");
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = properties.getProperty(key);
                log.debug("[ {}  : {} ]", key, value);
            }
            log.debug("访问Properties文件中的所有属性结束");
//            setProperty()方法：key存在则替换,不存在则新增
            properties.setProperty("APP_DYM_ID", tenant.getDomain());
            properties.setProperty("APP_DYM_NAME", URLDecoder.decode(URLEncoder.encode(tenant.getTenantName(),"UTF-8"),"UTF-8"));
//            这个保存需要注意,要保存成带双引号的形式,否则App读取到后无法识别为字符串,具体查看Android的buildConfigField配置
//            需要注意的是,转义完成并且保存到properties文件中,打开properties文件会发现 : 冒号前面多了一个反斜杠
//            但是当你再次从properties文件中把把该key的value读出来的时候,冒号前面的反斜杠就没有了,所以这个反斜杠
//            在读出来的时候被转义了,写的时候也转义了,在这里卡了很长时间,后来想到,写进去之后再读出来看是什么样,发现是正常的
//            所以需要注意一下
            properties.setProperty("APP_DYM_URL", "\"http://" + tenant.getDomain() + "/\"");

            Writer w = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(propertiesPath), "UTF-8"));
            w.flush();
//            修改完后要调用store()方法,否则修改的不保存
            properties.store(w, "动态构建App配置");
            w.close();
            String batPath = ResourceUtils.getFile("classpath:buildApp.bat").getAbsolutePath();
            log.debug("构建App脚本位置：{}", batPath);
//            调用系统CMD是阻塞的,开启线程执行调用[生产环境应当调用CI去构建]
            new Thread(() -> CMDUtil.excuteBatFileWithNewWindow(batPath, false)).start();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            如果下一个处理者不是null的话,往下传递执行,如果设定的是中间环节出错则终止处理,则应该将此代码放入try块中
            if (getNextTenantInitChain() != null) {
                getNextTenantInitChain().handler(tenant);
            }
        }

    }

    public static void main(String[] args) {
        BuildAppChain chain = new BuildAppChain(null);
        chain.handler(null);
    }
}