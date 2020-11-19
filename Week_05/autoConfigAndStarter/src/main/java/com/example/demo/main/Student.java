package com.example.demo.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "student")
public class Student {

    private Long id;

    private String name;
}
