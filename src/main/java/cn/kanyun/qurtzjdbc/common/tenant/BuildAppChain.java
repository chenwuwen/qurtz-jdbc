package cn.kanyun.qurtzjdbc.common.tenant;

import cn.kanyun.qurtzjdbc.entity.Tenant;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Properties;

/**
 * 构建App 责任链
 * @author KANYUN
 */
@Slf4j
public class BuildAppChain extends TenantInitChain {

    public BuildAppChain(TenantInitChain nextTenantInitChain) {
        super(nextTenantInitChain);
    }

    @Override
    public void handler(Tenant tenant)  {
        log.debug("Tenant处理责任链：为租户构建专属App");
        try {
            String propertiesPath = "F:\\AndroidStudioProjects\\util_box\\gradle.properties";
//            使用Spring的PropertiesLoaderUtils无法读到项目外的properties文件
//            Properties properties = PropertiesLoaderUtils.loadAllProperties("");
            File file = new File(propertiesPath);
            Properties properties = new Properties();
            properties.load(new FileInputStream(file));
            Enumeration enumeration = properties.propertyNames();
            log.debug("访问Properties文件中的所有属性开始");
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                String value = properties.getProperty(key);
                log.debug("[ {}  : {} ]", key, value);
            }
            log.debug("访问Properties文件中的所有属性结束");
//            setProperty()方法：key存在则替换,不存在则新增
            properties.setProperty("sda", "kanyun");

//            修改完后要调用store()方法,否则修改的不保存
            Writer w = new FileWriter(propertiesPath);
            properties.store(w,"");
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        BuildAppChain chain = new BuildAppChain(null);
        chain.handler(null);
    }
}