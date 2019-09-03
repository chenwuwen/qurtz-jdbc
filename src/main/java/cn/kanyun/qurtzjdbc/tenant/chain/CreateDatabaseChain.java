package cn.kanyun.qurtzjdbc.tenant.chain;

import cn.kanyun.qurtzjdbc.entity.Tenant;
import cn.kanyun.qurtzjdbc.util.MysqlScriptUtil;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * 创建数据库 责任链
 * @author KANYUN
 */
@Slf4j
public class CreateDatabaseChain extends TenantInitChain {

    public CreateDatabaseChain(TenantInitChain nextTenantInitChain) {
        super(nextTenantInitChain);
    }

    @Override
    public void handler(Tenant tenant)  {
        log.debug("Tenant处理责任链：为租户创建数据库");
        String driverName = "com.mysql.jdbc.Driver";
        try {
            MysqlScriptUtil.createDatabase(driverName, tenant.getDbUrl(), tenant.getDbUser(), tenant.getDbPass(), tenant.getSimplicity());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}