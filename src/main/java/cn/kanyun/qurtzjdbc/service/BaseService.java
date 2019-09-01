package cn.kanyun.qurtzjdbc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;

/**
 * @author Kanyun
 */
public interface BaseService<K extends Serializable, T extends Serializable> {

    /**
     * 查询所有数据
     *
     * @return
     */
    List<T> queryAll();

    /**
     * 查询数据,分页形式展现
     *
     * @return
     */
    IPage<T> queryPage(IPage page, T t);

    /**
     * 根据id查询数据
     *
     * @param id
     * @return
     */
    T queryById(K id);

    /**
     * 插入
     * @param t
     * @return
     */
    int insert(T t);

    /**
     * 根据ID删除
     * @param k
     * @return
     */
    int remove(K k);

    /**
     * 更新
     * @param t
     * @return
     */
    int update(T t);
}
