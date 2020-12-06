package io.sharpzhang.dynamicswitchDB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("io.sharpzhang.dynamicswitchDB.dao")
public class DynamicSwitchDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(DynamicSwitchDbApplication.class, args);
	}

}