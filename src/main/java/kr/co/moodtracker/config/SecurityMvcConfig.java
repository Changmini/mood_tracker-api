package kr.co.moodtracker.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import kr.co.moodtracker.handler.DetectBrowserEnv;
import kr.co.moodtracker.handler.SessionCheckHandler;

@Configuration
@EnableWebSecurity
public class SecurityMvcConfig implements WebMvcConfigurer {
		
	private ApplicationContext applicationContext;
	
	public SecurityMvcConfig() { super(); }
	public SecurityMvcConfig(final ApplicationContext applicationContext
			) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(login -> login.disable())
        	.csrf(csrf -> csrf.disable())
        	.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	/**
	 * CORS를 적용할 Origins 등록
	 * Request >> CORS 설정 적용 >> 인터셉터 적용 >> Controller
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
	        .allowedOrigins("http://127.0.0.1:3000", "http://127.0.0.1:8080",
	        				"http://localhost:3000", "http://localhost:8080",
	        				"http://43.203.220.226:3000"
	        				,"http://3.38.99.65:3000"
	        )
	        //.allowedOrigins("*")
	        .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
	        .allowedHeaders("*")
	        .allowCredentials(true)
	        .maxAge(3600);
	}
	
	/**
	 * 로킹, 세션체크 관련 인터셉터 등록
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new DetectBrowserEnv())
				.addPathPatterns("/**");
		
		registry.addInterceptor(new SessionCheckHandler())
				.addPathPatterns("/**")
				.excludePathPatterns(
						"/"
						,"/index"
						,"/css/**", "/images/**", "/js/**"
						,"/login", "/login/status"
						,"/user"
						//,"/actuator/**"
					);
	}
	
	/* ******************************************************************* */
	/* GENERAL CONFIGURATION ARTIFACTS */
	/* Static Resources */
	/* ******************************************************************* */
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		/* thymeleaf에서는 th:src="@{/js/test.js}" 속성으로 접근
		 * 404 or MIME error와 같은 문제는
		 *   파일을 못 찾거나 접근이 막혔을(=interceptor 등으로) 때 발생. 
		 * */
		registry.addResourceHandler("/js/**")
				.addResourceLocations("classpath:/static/js/");
		registry.addResourceHandler("/css/**")
				.addResourceLocations("classpath:/static/css/");
		registry.addResourceHandler("/images/**")
				.addResourceLocations("classpath:/static/images/");
	}
	
	
	/* **************************************************************** */
	/* THYMELEAF-SPECIFIC ARTIFACTS */
	/* TemplateResolver <- TemplateEngine <- ViewResolver */
	/* **************************************************************** */
	@Bean
	public SpringResourceTemplateResolver templateResolver(){
		// SpringResourceTemplateResolver automatically integrates with Spring's own
		// resource resolution infrastructure, which is highly recommended.
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setApplicationContext(this.applicationContext);
		templateResolver.setPrefix("classpath:/templates/");
		templateResolver.setSuffix(".html");
		// HTML is the default value, added here for the sake of clarity.
		templateResolver.setTemplateMode(TemplateMode.HTML);
		// Template cache is true by default. Set to false if you want
		// templates to be automatically updated when modified.
		templateResolver.setCacheable(false);
		return templateResolver;
	}
	
	@Bean
	public SpringTemplateEngine templateEngine(){
		// SpringTemplateEngine automatically applies SpringStandardDialect and
		// enables Spring's own MessageSource message resolution mechanisms.
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		// Enabling the SpringEL compiler with Spring 4.2.4 or newer can
		// speed up execution in most scenarios, but might be incompatible
		// with specific cases when expressions in one template are reused
		// across different data types, so this flag is "false" by default
		// for safer backwards compatibility.
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}
	
	@Bean
	public ThymeleafViewResolver viewResolver(){
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		return viewResolver;
	}

	
}
