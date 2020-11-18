package org.example.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JavabeanConfig {

    @Bean
    public Teacher teachers(){// 函数名是 IOC 容器中 bean 实例的名字
        return new Teacher();
    }
}
