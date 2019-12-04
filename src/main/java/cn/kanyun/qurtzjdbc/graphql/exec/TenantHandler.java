package cn.kanyun.qurtzjdbc.graphql.exec;

import cn.kanyun.qurtzjdbc.entity.Tenant;
import cn.kanyun.qurtzjdbc.service.TenantService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Observable;

/**
 * GraphQl查询API - > Tenant
 * 对于Mutation(异变)其必须实现GraphQLMutationResolver接口
 * 对于Query(查询)其必须实现GraphQLQueryResolver接口,否则打开graphql 的Endpoint会提示404
 *
 * @author KANYUN
 */
@Component
public class TenantHandler extends Observable implements GraphQLMutationResolver, GraphQLQueryResolver {

    @Resource
    private TenantService tenantService;

    /**
     * 创建一个Tenant
     *
     * @param tenant
     * @return
     */
    public int createTenant(Tenant tenant) {
//        insert默认返回的是影响数据库表的行数，通过对象可以获得Id
        int rowCount = tenantService.insert(tenant);
        if (rowCount > 0) {
//        被观察者数据发生改变
            this.sendMessage(tenant);
        }
        return tenant.getId().intValue();
    }


    /**
     * Tenant修改后，通知观察者
     *
     * @param tenant
     */
    private void sendMessage(Tenant tenant) {
        super.setChanged();
        super.notifyObservers(tenant);
    }

    /**
     * 删除一个Tenant
     *
     * @param id
     * @return
     */
    public int removeTenant(Long id) {
        return tenantService.remove(id);
    }

    /**
     * 修改一个Tenant
     *
     * @param tenant
     * @return
     */
    public int updateTenant(Tenant tenant) {
        return tenantService.update(tenant);
    }


    /**
     * 返回全部的Tenant列表
     *
     * @return
     */
    public List<Tenant> getTenantList() {
        return tenantService.queryAll();
    }

    /**
     * 根据Id查找Tenant
     *
     * @param id
     * @return
     */
    public Tenant getTenant(Long id) {
        return tenantService.queryById(id);
    }
}