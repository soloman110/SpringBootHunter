package com.zinnaworks.nxpgtool.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "language")
public class Language {
    List<Map<String,String>> ids = new ArrayList<>();

    public List<Map<String, String>> getIds() {
        return ids;
    }

    public void setIds(List<Map<String, String>> ids) {
        this.ids = ids;
    }
}
