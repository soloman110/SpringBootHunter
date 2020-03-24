package com.zinnaworks.nxpgtool.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import lombok.Data;

//https://mkyong.com/spring-boot/spring-boot-configurationproperties-example/


@Data
@Component
@ConfigurationProperties
public class Servers {
	private List<ServerInfo> servers = new ArrayList<>();
	private Map<String, ServerInfo> serverMap = new HashMap<>();

	@PostConstruct
	public void c() {
		for(ServerInfo server : servers) {
			serverMap.put(server.getType(), server);
		}
	}
	
	@Data
	public static class ServerInfo {
		private String type;
		private String url;
		
	}
	//예외처리를 안 했다...
	public ServerInfo getServerInfo(String name) {
		name = name.toLowerCase();
		return serverMap.get(name);
	}
}
