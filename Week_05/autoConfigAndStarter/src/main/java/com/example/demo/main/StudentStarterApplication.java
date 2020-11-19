package com.example.demo.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class StudentStarterApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(StudentStarterApplication.class, args);
		Klass klass = context.getBean(Klass.class);
		System.out.println(klass);
	}

}
