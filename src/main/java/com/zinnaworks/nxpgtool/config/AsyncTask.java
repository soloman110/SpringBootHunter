package com.zinnaworks.nxpgtool.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class AsyncTask {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTask.class) ;
    /*
     * [ asyncTask1-2] com.boot.task.config.AsyncTask : ======异步任务结束1======
     * [ asyncTask1-1] com.boot.task.config.AsyncTask : ======异步任务结束0======
     */
    // 只配置了一个 asyncExecutor1 不指定也会默认使用
    @Async
    public void asyncTask0 () {
        try{
            Thread.sleep(5000);
        }catch (Exception e){
            e.printStackTrace();
        }
        LOGGER.info("======异步任务结束0======");
    }
    @Async("asyncExecutor")
    public void asyncTask1 () {
        try{
            Thread.sleep(5000);
        }catch (Exception e){
            e.printStackTrace();
        }
        LOGGER.info("======异步任务结束1======");
    }
}
