package com.example.demo.autoConfig;

import com.example.demo.main.Klass;
import com.example.demo.main.School;
import com.example.demo.main.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;


@Configuration
@AutoConfigureBefore(Student.class) // StudentAutoConfiguration 在 Student 之前加载
@ConditionalOnProperty( name = "enable", havingValue = "true", matchIfMissing = true) // 开关：判断这个配置类是否开启
@EnableConfigurationProperties(Student.class) // 让使用 @ConfigurationProperties 的配置类生效，相当于将配置类注入到 IOC 容器中
public class StudentAutoConfiguration {

    @Autowired
    public Student student;

    @Bean
    @ConditionalOnMissingBean
    public Klass getKlass() {
        Klass klass = new Klass();
        List<Student> students = new ArrayList<>();
        students.add(student);
        klass.setStudents(students);
        return klass;
    }

    @ConditionalOnMissingBean
    public School getSchool() {
        School school = new School();
        List<Klass> klassList = new ArrayList<>();
        Klass klass = getKlass();
        klassList.add(klass);
        school.setKlassList(klassList);
        return school;
    }
}
