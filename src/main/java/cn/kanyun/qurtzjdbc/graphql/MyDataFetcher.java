package cn.kanyun.qurtzjdbc.graphql;

import graphql.schema.DataFetchingEnvironment;

/**
 * GraphQL查询接口
 * @author Kanyun
 */
public interface MyDataFetcher {

    /**
     * GraphQL中的查询名称
     * @return
     */
    String fieldName();

    /**
     * 数据查询
     * @param environment
     * @return
     */
    Object dataFetcher(DataFetchingEnvironment environment);
}
