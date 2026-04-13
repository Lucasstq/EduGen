package dev.lucas.edugen.EduGen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EduGenApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduGenApplication.class, args);
	}

}
