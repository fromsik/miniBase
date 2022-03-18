package com.kmsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.kmsystem.member.mapper",
							"com.kmsystem.file.mapper",
							"com.kmsystem.document.mapper"
})
public class KmsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(KmsystemApplication.class, args);
	}

}
