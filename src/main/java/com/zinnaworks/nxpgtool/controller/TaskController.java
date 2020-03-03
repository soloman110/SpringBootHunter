package com.zinnaworks.nxpgtool.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zinnaworks.nxpgtool.config.AsyncTask;

import javax.annotation.Resource;

@RestController
public class TaskController {
    @Resource
    private AsyncTask asyncTask ;
    

	@Autowired
	ThreadPoolTaskExecutor asyncExecutor;
    
    @RequestMapping("/asyncTask")
    public String asyncTask (){
        asyncTask.asyncTask0();
        asyncTask.asyncTask1();
        return "success" ;
    }
}
