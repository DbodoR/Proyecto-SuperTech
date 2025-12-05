package com.code.tienda_supertech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class TiendaSupertechApplication {

	public static void main(String[] args) {
		SpringApplication.run(TiendaSupertechApplication.class, args);
	}

}
