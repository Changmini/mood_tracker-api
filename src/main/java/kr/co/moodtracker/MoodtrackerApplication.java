package kr.co.moodtracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class MoodtrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoodtrackerApplication.class, args);
	}

}
