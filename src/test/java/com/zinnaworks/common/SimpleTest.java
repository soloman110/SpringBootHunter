package com.zinnaworks.common;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.springboot.hunter.Application;
import com.springboot.hunter.config.properties.MailPropertiesExample;
import com.springboot.hunter.config.properties.MailPropertiesExample.User;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class SimpleTest {
	
	@Autowired
	MailPropertiesExample mail;
	
    @Test
    public void doTest() {
    	List<String> servers = mail.getSmtpServers();
    	for (String string : servers) {
			System.out.println(string);
		}
    	Map<String, String> maps = mail.getMaps();
    	for(Map.Entry<String, String> entry: maps.entrySet()) {
    		System.out.println(entry.getKey()+"  " + entry.getValue());
    	}
    	MailPropertiesExample.User user = mail.getUserBean();
    	System.out.println(user);
    	for(MailPropertiesExample.User u: mail.getUserList()) {
    		System.out.println("UUUU: " +u);
    	}
    }
}
