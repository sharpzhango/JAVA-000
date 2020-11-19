学习笔记

## Spring 中 Bean 的三种装配方式

1. xml 定义(_spirng-context.xml_)
2. spring Component-Scan(_spring-bean.xml_)
3. 配置类 @Configuration @Bean 通过方法返回值定义 bean(_JavabeanConfig_)

## 自动化装配和starter

> yml --> Configuration --> bean
>
> 自定义装配的核心是@Configuration

### 流程

@SpringbootApplication 开启 @EnableAutoConfiguration，通过importSelector 找到 spring.factories(_记录配置类_)-->@Configuration,AutoConfiguration通过方法生成 Bean

### 遇到的问题： 

> spring.factories的内容我给注释掉了，但是还是能拿到StudengtAutoConfiguration 中的@Bean： Klass对象

原因：**@AutoConfigurationPackage将spring boot主配置类所在的包及其子包下的所有的组件扫描到spring容器中去。**

### starter

starter依赖于自动化配置实现，其中META-INF下有三个文件，spring.factories 标记自动装配类 spring.provides 表明 starter 信息，metadata-json 储存配置文件信息(_可用spring-boot-configuration-processor 生成_)

