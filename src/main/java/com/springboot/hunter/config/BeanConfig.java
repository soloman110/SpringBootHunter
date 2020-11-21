package com.springboot.hunter.config;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.springboot.hunter.config.properties.MailPropertiesExample;

@Configuration
/**
 *	如果一个配置类只配置@ConfigurationProperties注解，而没有使用@Component，那么在IOC容器中是获取不到properties 配置文件转化的bean。
 *	说白了 @EnableConfigurationProperties 相当于把使用 @ConfigurationProperties 的类进行了一次注入。
 */
@EnableConfigurationProperties(MailPropertiesExample.class)
public class BeanConfig {
	
}
