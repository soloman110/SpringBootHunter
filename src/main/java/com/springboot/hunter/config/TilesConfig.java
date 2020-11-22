package com.springboot.hunter.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.ViewResolver;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.tiles2.dialect.TilesDialect;
import org.thymeleaf.extras.tiles2.spring4.web.configurer.ThymeleafTilesConfigurer;
import org.thymeleaf.extras.tiles2.spring4.web.view.ThymeleafTilesView;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

/*
 * 1. 我把Springboot的版本  从1升级到2时候发生了如下的错误：
 * 		- ThymeleafTilesConfigurer ttc = new ThymeleafTilesConfigurer();
 * 	  		ttc.setDefinitions报错
 * 		- final SpringTemplateEngine engine = new SpringTemplateEngine();
			engine.setTemplateResolver(templateResolver());报错原因是 参数从SpringResourceTemplateResolver变为了ITemplateResolver
		- SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
 * 			templateResolver的所有方法都报错
 * 
 *  2. 解决办法 ：
 *  	- 在google和百度上一顿乱搜索，试来试去都不行
 *  	- 找官方文档，没有
 *  	- tiles和Thymeleaf单独对Springboot的整合有看考资料，但是一起整合的资料没有
 *  	- todolist：
 *  		-- 打算看看内部原理？
 *  		-- 确认tiles和springboot的版本
 *  		-- ThymeleafTilesConfigurer是什么东西，查看文档
 *  		-- SpringTemplateEngine， ViewResolver， SpringResourceTemplateResolver的设计原理
 * */

@Configuration
public class TilesConfig {
	//利用ThymeleafTilesConfigurer 设置了tiles的配置文件tiles.xml
	@Bean
	public ThymeleafTilesConfigurer tilesConfigurer() {
		ThymeleafTilesConfigurer ttc = new ThymeleafTilesConfigurer();
		ttc.setDefinitions("classpath:/webapp/tiles.xml");
		ttc.setCheckRefresh(true);
		return ttc;
	}
	// 配置tiles的ViewResovler, 处理一切以tiles开头的
	// 并且设置的Template Engine也就是 SpringTemplateEngine
	// SpringTemplateEngine利用ServletContextTemplateResolver处理Template的请求
	@Bean
	public ViewResolver tilesViewResolver() {
		ThymeleafViewResolver vr = new ThymeleafViewResolver();
		vr.setTemplateEngine(templateEngine1());
		//vr.setViewNames(new String[] { "tiles/jsp/*" });
		vr.setViewClass(ThymeleafTilesView.class);
		vr.setCharacterEncoding("UTF-8");
		vr.setOrder(Ordered.LOWEST_PRECEDENCE);
		vr.setCache(false);
		return vr;
	}	

	@Bean
	public SpringTemplateEngine templateEngine() {
		final SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver());
		engine.setAdditionalDialects(dialects());
		return engine;
	}
	
	@Bean
	public SpringTemplateEngine templateEngine1() {
		final SpringTemplateEngine engine = new SpringTemplateEngine();
		engine.setTemplateResolver(templateResolver2());
		engine.setAdditionalDialects(dialects());
		return engine;
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {

		//ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix("classpath:webapp/templates/");
		templateResolver.setCacheable(false);
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("LEGACYHTML5"); 
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}
	
	@Bean
	public SpringResourceTemplateResolver templateResolver1() {

		//ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix("/WEB-INF/");
		templateResolver.setCacheable(false);
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("LEGACYHTML5");
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}
	
	@Bean
	public SpringResourceTemplateResolver templateResolver2() {

		//ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix("classpath:webapp/");
		templateResolver.setCacheable(false);
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode("LEGACYHTML5");
		templateResolver.setCharacterEncoding("UTF-8");
		return templateResolver;
	}

	//集成 ThymeLeaf 模板框架 
	@Bean
	public ViewResolver thymeleafViewResolver() {
		ThymeleafViewResolver vr = new ThymeleafViewResolver();
		vr.setTemplateEngine(templateEngine());
		vr.setCharacterEncoding("UTF-8");
		vr.setOrder(Ordered.HIGHEST_PRECEDENCE);
		vr.setExcludedViewNames(new String[] { "tiles/jsp/*", "tiles/thymeleaf/*" });
		return vr;
	}

	private Set<IDialect> dialects() {
		final Set<IDialect> set = new HashSet<IDialect>();
		set.add(new TilesDialect());
		return set;
	}
}
