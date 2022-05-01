package com.datacenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.com.datacenter.dao")
@EnableTransactionManagement
@EnableSwagger2
public class DatacenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatacenterApplication.class, args);
		System.setProperty("tomcat.util.http.parser.HttpParser.requestTargetAllow","|{}");
	}

}
