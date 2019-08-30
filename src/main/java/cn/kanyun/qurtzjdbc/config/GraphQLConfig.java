package cn.kanyun.qurtzjdbc.config;

import cn.kanyun.qurtzjdbc.graphql.MyDataFetcher;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * DataFetcher
 * 数据返回获取器，实现Field字段上的数据返回接口，可在environment中获取查询参数
 * DataFetchingEnvironment
 * 数据获取上下文，可以拿到对应的查询参数
 *
 * @author Kanyun
 */
@Configuration
public class GraphQLConfig {

    private GraphQL graphQL;

    @Resource
    private List<MyDataFetcher> dataFetchers;

    /**
     * GraphQL对象初始化
     * 先加载.graphqls文件,在使用GraphQL.newGraphQL().build()进行初始化
     *
     * @PostConstruct 在spring初始化时执行
     */
    @PostConstruct
    public void init() throws FileNotFoundException {
        File schemaFile = ResourceUtils.getFile("classpath:qurtz_jdbc.graphqls");
        this.graphQL = GraphQL.newGraphQL(buildGraphQLSchema(schemaFile)).build();
    }

    /**
     * 构造GraphQLSchema
     *
     * @param file
     * @return
     */
    private GraphQLSchema buildGraphQLSchema(File file) {
        TypeDefinitionRegistry registry = new SchemaParser().parse(file);
        return new SchemaGenerator().makeExecutableSchema(registry, buildWiring());
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("", builder -> {
                            for (MyDataFetcher fetcher : dataFetchers) {
                                builder.dataFetcher(fetcher.fieldName(), environment -> fetcher.dataFetcher(environment));
                            }
                            return builder;
                        }
                ).build();
    }

    @Bean
    public GraphQL graphQL() {
        return this.graphQL;
    }
}
