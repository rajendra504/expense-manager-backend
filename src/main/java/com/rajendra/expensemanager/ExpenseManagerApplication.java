package com.rajendra.expensemanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ExpenseManagerApplication {
//	@Bean
//	CommandLineRunner runner(PasswordEncoder encoder) {
//		return args -> {
//			System.out.println(encoder.encode("admin123"));
//		};
//	}
	public static void main(String[] args) {
		SpringApplication.run(ExpenseManagerApplication.class, args);
	}

}
