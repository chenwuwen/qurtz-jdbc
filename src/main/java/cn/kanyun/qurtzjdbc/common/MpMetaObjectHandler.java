package cn.kanyun.qurtzjdbc.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * MybatisPlus自动填充处理类
 * @author KANYUN
 */
@Component
public class MpMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增时 字段的填充处理
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
//        将registerDate预填充为时间戳,这样在Insert时不用再为该字段赋值
        this.setFieldValByName("registerDate", Instant.now().getEpochSecond(), metaObject);
    }

    /**
     * 更新时 字段的填充处理
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("registerDate", Instant.now().getEpochSecond(), metaObject);
    }
}