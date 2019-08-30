package cn.kanyun.qurtzjdbc.service.impl;

import cn.kanyun.qurtzjdbc.dao.UserMapper;
import cn.kanyun.qurtzjdbc.entity.UserEntity;
import cn.kanyun.qurtzjdbc.service.UserService;
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
    public boolean login(UserEntity userEntity) {
        return false;
    }


}
