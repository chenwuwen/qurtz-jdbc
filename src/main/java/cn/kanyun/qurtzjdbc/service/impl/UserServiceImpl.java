package cn.kanyun.qurtzjdbc.service.impl;

import cn.kanyun.qurtzjdbc.dao.UserMapper;
import cn.kanyun.qurtzjdbc.entity.UserEntity;
import cn.kanyun.qurtzjdbc.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Kanyun
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<Long, UserEntity> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public UserEntity login(UserEntity userEntity) {
        QueryWrapper<UserEntity> userEntityQueryWrapper = new QueryWrapper<>();
        userEntityQueryWrapper.eq("USER_NAME", userEntity.getUserName())
                .eq("PASSWORD", userEntity.getPassword());
        UserEntity user = userMapper.selectOne(userEntityQueryWrapper);

        return user;
    }


}
