package cn.kanyun.qurtzjdbc.controller;

import cn.kanyun.qurtzjdbc.entity.UserEntity;
import graphql.GraphQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author Kanyun
 */
@Controller
public class UserController {

    @Autowired
    private GraphQL graphQL;

    /**
     * GraphQL.execute  最终查询执行器
     *
     * @param query
     * @return
     */
    @GetMapping
    @ResponseBody
    public Map<String, Object> list(@RequestParam("query") String query) {
        return graphQL.execute(query).toSpecification();
    }


}
