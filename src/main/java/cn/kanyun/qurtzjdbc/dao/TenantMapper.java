package cn.kanyun.qurtzjdbc.dao;

import cn.kanyun.qurtzjdbc.entity.Tenant;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 在接口类上添加了@Mapper，在编译之后会生成相应的接口实现类
 * 这也就是为什么Mybatis只写接口进行调用方法的原因,被@Mapper注解的接口会
 * 动态生成该接口的实现类
 * @MapperScan 指定要变成实现类的接口所在的包，然后包下面的所有接口在编译之后都会生成相应的实现类
 * 添加位置：是在Springboot启动类上面添加
 * @author Kanyun
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {
}
