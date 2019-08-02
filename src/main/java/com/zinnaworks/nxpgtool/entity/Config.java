package com.zinnaworks.nxpgtool.entity;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "my")
public class Config {

    private List<String> servers = new ArrayList<String>();

    private String desc;

    private Properties properties;

    private List<String> langIDs;

    public List<String> getLangIDs() {
        return langIDs;
    }

    public void setLangIDs(List<String> langIDs) {
        this.langIDs = langIDs;
    }

    @Override
    public String toString() {
        return "Config{" +
                "servers=" + servers +
                ", desc='" + desc + '\'' +
                ", properties=" + properties +
                ", langIDs=" + langIDs +
                '}';
    }

    public Properties getProperties() {
        return properties;
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    public List<String> getServers() {
        return servers;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
