package tn.esprit.exam.coursclassroom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableScheduling 
@EnableWebMvc   //@EnableSwagger2
public class ExamenCoursClassroomApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExamenCoursClassroomApplication.class, args);
	}

}
