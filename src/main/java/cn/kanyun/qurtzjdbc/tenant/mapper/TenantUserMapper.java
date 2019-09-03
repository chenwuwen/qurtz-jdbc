package cn.kanyun.qurtzjdbc.tenant.mapper;

import cn.kanyun.qurtzjdbc.tenant.entity.TenantUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Kanyun
 */
@Mapper
public interface TenantUserMapper extends BaseMapper<TenantUser> {
}
