package com.example.microstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
//		(exclude = { SecurityAutoConfiguration.class })
public class MicrostoneApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(MicrostoneApplication.class, args);
	}

}
