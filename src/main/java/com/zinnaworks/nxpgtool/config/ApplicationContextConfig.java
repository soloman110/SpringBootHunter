package com.zinnaworks.nxpgtool.config;


import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.zinnaworks.nxpgtool.Application;
import com.zinnaworks.nxpgtool.common.FileStorageProperties;

@EnableConfigurationProperties({
	FileStorageProperties.class
})
@Controller
@SpringBootApplication(scanBasePackageClasses = Application.class)
public class ApplicationContextConfig {

    @RequestMapping("/")
    public String home() {
        return "redirect:/nxpgtool/index";
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {

        return (container -> {
            ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/webapp/static/pages/error/401.html");
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/webapp/static/pages/error/404.html");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/webapp/static/pages/error/500.html");

            container.addErrorPages(error401Page, error404Page, error500Page);
        });
    }
    
//    @Bean
//    public Filter characterEncodingFilter() {
//        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//        characterEncodingFilter.setEncoding("UTF-8");
//        characterEncodingFilter.setForceEncoding(true);
//        return characterEncodingFilter;
//    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationContextConfig.class, args);
    }
}
