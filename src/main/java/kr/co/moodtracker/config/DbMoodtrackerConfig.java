package kr.co.moodtracker.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"kr.co.moodtracker.mapper"})
public class DbMoodtrackerConfig {

	@Value("${spring.datasource.url}")
	private String url;
	
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
	
	@Value("${spring.datasource.username}")
	private String username;
	
	@Value("${spring.datasource.password}")
	private String password;
	
	@Autowired
	ApplicationContext applicationContext; 
	
	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder.create()
				.url(url)
				.driverClassName(driverClassName)
				.username(username)
				.password(password)
				.build();
	}
	
	@Bean
	  public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
	    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
	    factoryBean.setDataSource(dataSource);
	    factoryBean.setConfigLocation(applicationContext.getResource("classpath:/mybatis/config/mybatis-config.xml"));
	    factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mybatis/mapper/*Mapper.xml"));
	    return factoryBean.getObject();
	  }
	
	@Bean
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
