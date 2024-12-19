package kr.co.moodtracker.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.moodtracker.handler.DetectBrowserEnv;

@Configuration
public class MVCConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
	        .allowedOrigins("http://127.0.0.1:3000", "http://127.0.0.1:8080",
	        				"http://localhost:3000", "http://localhost:8080",
	        				"http://43.203.220.226:3000"
	        				,"http://3.38.99.65:3000"
	        )
	        //.allowedOrigins("*")
	        .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE")
	        .allowCredentials(true)
	        .maxAge(3600);
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new DetectBrowserEnv())
				.addPathPatterns("/**");
	}
	
}
