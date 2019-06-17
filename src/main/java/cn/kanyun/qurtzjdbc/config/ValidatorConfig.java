package cn.kanyun.qurtzjdbc.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 配置hibernate Validator为快速失败返回模式
 * @author Kanyun
 * @date 2019/6/17
 */

@Configuration
public class ValidatorConfig {

    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
//                true  快速失败返回模式    false 普通模式
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        return validator;
    }
}
