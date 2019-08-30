package cn.kanyun.qurtzjdbc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
}
