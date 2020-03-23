package com.zinnaworks.nxpgtool.common;

public enum ServerEnum {
	DEV("https://xpg-nxpg-dev.skb-doj-dev01.mybluemix.net", "dev"),
	STG("https://xpg-nxpg-stg.skb-doj-dev01.mybluemix.net", "stg"),
	SSU("https://xpg-nxpg-svc.skb-ssu-prd02.mybluemix.net", "ssu"),
	SUY("https://xpg-nxpg-svc.skb-suy-prd01.mybluemix.net", "suy");
	
	private String uri;
	private String name;
	ServerEnum(String uri) {
		this.uri = uri;
	}
	ServerEnum(String uri, String name) {
		this.uri = uri;
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public String getName() {
		return name;
	}
	public static ServerEnum getServer(String name) {
		for(ServerEnum server : ServerEnum.values()) {
			if(name.equals(server.toString())) {
				return server;
			}
		}
		return null;
	}
}
