package cn.kanyun.qurtzjdbc.graphql.exec;

import cn.kanyun.qurtzjdbc.entity.UserEntity;
import cn.kanyun.qurtzjdbc.service.UserService;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
/**
 * GraphQl查询类
 * 其必须实现GraphQLQueryResolver接口,否则打开graphql 的Endpoint会提示404
 * @author KANYUN
 */
@Component
public class UserQuery implements GraphQLQueryResolver {
    @Autowired
    private GraphQL graphQL;

    @Autowired
    private UserService userService;

    /**
     * GraphQL.execute  最终查询执行器
     *
     * @param query
     * @return
     */
//    @GetMapping()
//    @ResponseBody
    public Map<String, Object> list(@RequestParam("query") String query) {
        return graphQL.execute(query).toSpecification();
    }

    /**
     * 对应.graphqls 文件中的getUserList 方法
     * @return
     */
    public List<UserEntity> getUserList() {
        return userService.queryAll();
    }
}