package com.zinnaworks.nxpgtool.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.zinnaworks.nxpgtool.entity.Properties;

/**
 * Created by dmshin on 06/02/2017.
 */
@Configuration
@ComponentScan(basePackages={"com.zinnaworks.nxpgtool.config"})
public class CommonConfiguration {
	
	@Autowired
	private Environment environment;
	
	@Bean()
    @RefreshScope
    public Properties properties() {
        return new Properties();
    }

	@Bean(name = "firstProfile")
	public String getFirstProfile() {
		String[] profiles = environment.getActiveProfiles();
		String profile = "";
		if (profiles != null) {
			if (profiles.length > 0) {
				profile = profiles[0];
			}
		}
		return profile;
	}
	
	@Bean(name = "activeProfile")
	public String getActiveProfile() {
		String[] profiles = environment.getActiveProfiles();
		String profile = "";
		if (profiles != null) {
			if (profiles.length > 0) {
				for (String p : profiles) {
					profile += "," + p; 
				}
			}
		
			if (profile != null && !profile.isEmpty() && profile.length() > 1) {
				profile = profile.substring(1);
			}
		}
		return profile;
	}
}
