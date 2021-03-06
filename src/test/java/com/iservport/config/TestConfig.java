package com.iservport.config;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.helianto.core.config.HeliantoServiceConfig;
import org.helianto.core.test.TestDataSourceConfig;
import org.helianto.security.config.CryptoConfig;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuração de teste.
 * 
 * @author mauriciofernandesdecastro
 *
 */
@Configuration
@Import({TestDataSourceConfig.class
		, HeliantoServiceConfig.class
		, CryptoConfig.class})
@ComponentScan(
		basePackages = {"com.iservport.*.service"})
@EnableJpaRepositories(
		basePackages={
				"com.iservport.*.repository", "org.helianto.*.repository"
		})
@PropertySource("classpath:/test.properties")
public class TestConfig  {
	
	@Inject
	private DataSource dataSource;
	
	@Inject
	private JpaVendorAdapter vendorAdapter;
	
	/**
	 * Substitui a configuração original do <code>EntityManagerFactory</code>
	 * para incluir novos pacotes onde pesquisar por entidades persistentes.
	 */
	@Bean 
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
		bean.setDataSource(dataSource);
		bean.setPackagesToScan("org.helianto.*.domain", "com.iservport.*.domain");
		bean.setJpaVendorAdapter(vendorAdapter);
		bean.setPersistenceProvider(new HibernatePersistence());
		bean.afterPropertiesSet();
        return bean.getObject();
	}
	
	@Autowired
	private Environment env;
	
	/**
	 * Password encoder.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
