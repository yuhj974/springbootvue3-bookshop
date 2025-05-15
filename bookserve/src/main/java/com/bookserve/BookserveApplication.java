package com.bookserve;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan //开启了SpringBoot对Servlet组件的支持
@SpringBootApplication
public class BookserveApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookserveApplication.class, args);
	}

}
