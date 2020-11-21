package com.springboot.hunter.config;


import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;

import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.filter.CharacterEncodingFilter;


@Controller
public class ApplicationContextConfig {

    @RequestMapping("/")
    public String home() {
        return "redirect:/deploy/index";
    }
    /*
     * 看样子是配置Tomcat容器的东西，如下所示：
     * 	- 配置了错误页面
     *  - 配置了会话的过期时间
     * 
     * Spring Boot2.0以上的版本已经没有EmbeddedServletContainerCustomizer这个了，而换成了WebServerFactoryCustomizer
     * Spring Boot会自动发现customizer并且调用customize(..)方法，然后将信息放置到servlet的容器中
    */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return (container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/webapp/static/pages/error/401.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/webapp/static/pages/error/404.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/webapp/static/pages/error/500.html");
            container.setSessionTimeout(1, TimeUnit.MINUTES);
            container.addErrorPages(error401Page, error404Page, error500Page);
        });
    }
    
    /*
     * WEB开发人员通过Filter技术，对web服务器管理的所有web资源：
     * 	例如Jsp, Servlet, 静态图片文件或静态 html 文件等进行拦截，从而实现一些特殊的功能。
     * 	例如实现URL级别的权限访问控制、过滤敏感词汇、压缩响应信息等一些高级功能
     * 
     */
    
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return characterEncodingFilter;
    }
}
