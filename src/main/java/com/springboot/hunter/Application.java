package com.springboot.hunter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
 * 
 *	@SpringBootApplication，其实它就是由下面三个注解组成的：
	- @Configuration
	- @ComponentScan
	- @EnableAutoConfiguration
	其中 @EnableAutoConfiguration 是实现自动配置的入口，该注解又通过 @Import 注解导入了AutoConfigurationImportSelector，
	在该类中加载 META-INF/spring.factories 的配置信息。然后筛选出以 EnableAutoConfiguration 为 key 的数据，加载到 IOC 容器中，实现自动配置功能！
 * */
@SpringBootApplication(scanBasePackageClasses = Application.class)
@EnableAsync  
@EnableScheduling
@EnableCaching
@EnableAutoConfiguration

/**
 * 
 * @author sikongming
 * @date 20201111
 * 
 * 1. BeanFactory 和 ApplicationContext的区别是什么
 * 		- BeanFactory 的实现是使用懒加载的方式。ApplicationContext它是预加载，所以，每一个 bean 都在 ApplicationContext 启动之后实例化
 *		- ApplicationContext 是 Spring 应用程序中的中央接口，用于向应用程序提供配置信息
 *		它继承了 BeanFactory 接口，所以 ApplicationContext 包含 BeanFactory 的所有功能以及更多功能！它的主要功能是支持大型的业务应用的创建
 */

public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
