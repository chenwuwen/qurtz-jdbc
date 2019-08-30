package cn.kanyun.qurtzjdbc.graphql;

import cn.kanyun.qurtzjdbc.service.UserService;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 使用GraphQL查询实现子类
 * @author Kanyun
 */
@Component
public class UserDataFetcher implements MyDataFetcher {

    @Resource
    private UserService userService;

    /**
     * 这里返回的名称要与 .graphqls文件中配置的名称相同
     *
     * @return
     */
    @Override
    public String fieldName() {
        return "UserQuery";
    }

    /**
     * 获取数据,条件在DataFetchingEnvironment中
     * @param environment
     * @return
     */
    @Override
    public Object dataFetcher(DataFetchingEnvironment environment) {
        return userService.queryAll();
    }
}
