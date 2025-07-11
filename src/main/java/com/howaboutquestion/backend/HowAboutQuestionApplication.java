package com.howaboutquestion.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class HowAboutQuestionApplication {

	public static void main(String[] args) {
		SpringApplication.run(HowAboutQuestionApplication.class, args);
	}

}
