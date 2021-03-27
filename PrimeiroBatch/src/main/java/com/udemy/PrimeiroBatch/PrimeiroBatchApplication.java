package com.udemy.PrimeiroBatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class PrimeiroBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrimeiroBatchApplication.class, args);
	}

}
