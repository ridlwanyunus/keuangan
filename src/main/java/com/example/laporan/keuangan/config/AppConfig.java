package com.example.laporan.keuangan.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public ExecutorService executorService() {
		ExecutorService executor = Executors.newFixedThreadPool(12);
		return executor;
	}
	
}
