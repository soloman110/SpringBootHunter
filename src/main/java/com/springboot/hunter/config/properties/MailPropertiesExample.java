package com.springboot.hunter.config.properties;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 
 * 此类为了生效，必须让Springboot容器知道。那么有那种方法：
 * 	1.1 添加@Component注解
 *  1.2 在BeanConfiguration中添加MailPropertiesExample的相关bean
 *  	@Bean
 *  	public MailPropertiesExample mailPropertiesExample() {
 *  		return new MailPropertiesExample();
 *  	}
 *  1.3 在BeanConfiguration中使用@EnableConfigurationProperties(MailPropertiesExample.class)
 * 
 * 我们可以简单地用一个值初始化一个字段来定义一个默认值
 * 类本身可以是包私有的
 * 类的字段必须有公共 setter 方法
 * 
 * 参考网站： https://blog.csdn.net/justry_deng/article/details/90758250
 * 
 * @author sikongming
 * @date 20201111
 */

@Data
@ConfigurationProperties(prefix = "myapp.mail")
public class MailPropertiesExample {
	private List<String> smtpServers;
	private boolean enabled;
	private String user;
	private String password;
	
	private Map<String, String> maps;
	private User userBean;
	private List<User> userList;
	
	@Data
	public static class User{
		private String name;
		private int age;
	}
}
