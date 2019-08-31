package cn.kanyun.qurtzjdbc.service;

import cn.kanyun.qurtzjdbc.entity.UserEntity;


/**
 * @author Kanyun
 */
public interface UserService extends BaseService<Long, UserEntity> {

    /**
     * 登录
     *
     * @param userEntity
     * @return
     */
    UserEntity login(UserEntity userEntity);
}
