package cn.kanyun.qurtzjdbc.common.tenant;

import cn.kanyun.qurtzjdbc.entity.Tenant;
import cn.kanyun.qurtzjdbc.util.MysqlScriptUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * 创建租户用表
 * 同时进行初始化
 *
 * @author KANYUN
 */
@Slf4j
public class InitTenantTableChain extends TenantInitChain {

    private static String driverName = "com.mysql.jdbc.Driver";

    public InitTenantTableChain(TenantInitChain nextTenantInitChain) {
        super(nextTenantInitChain);
    }

    @Override
    public void handler(Tenant tenant) {
        log.debug("Tenant处理责任链：为租户创建数据库表");
        createTable(tenant);
        initTableData(tenant);
    }

    /**
     * 创建表
     *
     * @param tenant
     */
    public static void createTable(Tenant tenant) {
        StringBuilder sb1 = new StringBuilder()
                .append("CREATE TABLE TENANT_USER ( id bigint(20) NOT NULL AUTO_INCREMENT,")
                .append("USER_NAME varchar(255) NOT NULL , ")
                .append("PASSWORD varchar(255) NOT NULL , PRIMARY KEY (id) )");

        StringBuilder sb2 = new StringBuilder()
                .append("CREATE TABLE TENANT_BIZ ( id bigint(20) NOT NULL AUTO_INCREMENT,")
                .append("BIZ_NAME varchar(255) NOT NULL ,")
                .append("BIZ_COMMENT varchar(10000) NOT NULL , PRIMARY KEY (id) )");

        try {
            MysqlScriptUtil.handlerDatabaseScript(driverName, tenant.getDbUrl() + tenant.getSimplicity(), tenant.getDbUser(), tenant.getDbPass(), sb1.toString());
            MysqlScriptUtil.handlerDatabaseScript(driverName, tenant.getDbUrl() + tenant.getSimplicity(), tenant.getDbUser(), tenant.getDbPass(), sb2.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建表
     *
     * @param tenant
     */
    public static void initTableData(Tenant tenant) {
        StringBuilder sb1 = new StringBuilder()
                .append("INSERT INTO TENANT_USER ( USER_NAME,PASSWORD) VALUES")
                .append("('admin','123456');");

        StringBuilder sb2 = new StringBuilder()
                .append("INSERT INTO TENANT_BIZ ( BIZ_NAME,BIZ_COMMENT) VALUES")
                .append("('任务1','任务1的描述'),")
                .append("('任务2','任务2的描述'),")
                .append("('任务3','任务3的描述');");


        try {
            MysqlScriptUtil.handlerDatabaseScript(driverName, tenant.getDbUrl() + tenant.getSimplicity(), tenant.getDbUser(), tenant.getDbPass(), sb1.toString());
            MysqlScriptUtil.handlerDatabaseScript(driverName, tenant.getDbUrl() + tenant.getSimplicity(), tenant.getDbUser(), tenant.getDbPass(), sb2.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}