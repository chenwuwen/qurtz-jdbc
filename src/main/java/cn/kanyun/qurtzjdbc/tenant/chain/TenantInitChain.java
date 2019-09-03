package cn.kanyun.qurtzjdbc.tenant.chain;

import cn.kanyun.qurtzjdbc.entity.Tenant;

/**
 * 租户 预处理 抽象类
 * @author KANYUN
 */
public abstract class TenantInitChain {

    private TenantInitChain nextTenantInitChain;

     public TenantInitChain(TenantInitChain nextTenantInitChain) {
          this.nextTenantInitChain = nextTenantInitChain;
     }

     /**
      * 设置下一个处理者
      * @param chain
      */
     public void setNextTenantInitChain(TenantInitChain chain) {
          this.nextTenantInitChain = chain;
     }

     /**
      * 抽象方法 子类实现
      * @param tenant
      */
     public abstract void handler(Tenant tenant);
}
