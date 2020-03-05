package com.zinnaworks.nxpgtool.util;

import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
 
/**
 * 执行Shell工具类
 *
 * @author JustryDeng
 * @date 2019/4/29 16:29
 */
public class ExecuteShellUtil {
 
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecuteShellUtil.class);
 
    private static final String DONOT_INIT_ERROR_MSG = "please invoke init(...) first!";
 
    private Session session;
 
    private Channel channel;
 
    private ChannelExec channelExec;
    
    private static final int CONNECT_TIMEOUT = 10 * 1000;
 
    private ExecuteShellUtil() {
    }
 
    /**
     * 获取ExecuteShellUtil类实例对象
     *
     * @return 实例
     * @date 2019/4/29 16:58
     */
    public static ExecuteShellUtil getInstance() {
        return new ExecuteShellUtil();
    }
 
    /**
     * 初始化
     *
     * @param ip
     *         远程Linux地址
     * @param port
     *         端口
     * @param username
     *         用户名
     * @param password
     *         密码
     * @throws JSchException
     *         JSch异常
     * @date 2019/3/15 12:41
     */
    public void init(String ip, Integer port, String username, String password) throws JSchException {
        JSch jsch = new JSch();
        jsch.getSession(username, ip, port);
        session = jsch.getSession(username, ip, port);
        session.setPassword(password);
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.connect(CONNECT_TIMEOUT);
        LOGGER.info("Session connected!");
        // 打开执行shell指令的通道
        channel = session.openChannel("exec");
        channelExec = (ChannelExec) channel;
    }
    
    public Session getSession(String ip, Integer port, String username, String password) throws JSchException {
    	 JSch jsch = new JSch();
         jsch.getSession(username, ip, port);
         session = jsch.getSession(username, ip, port);
         session.setPassword(password);
         Properties sshConfig = new Properties();
         sshConfig.put("StrictHostKeyChecking", "no");
         session.setConfig(sshConfig);
         return session;
    }
    
    public ChannelExec getChannel(Session session) throws JSchException {
    	  session.connect(10 * 1000);
          LOGGER.info("Session connected!");
          // 打开执行shell指令的通道
          channel = session.openChannel("exec");
          channelExec = (ChannelExec) channel;
          return channelExec;
    }
 
    /**
     * 명령 한줄 만 실행..
     */
    public String execCmd(String command) throws Exception {
        if (session == null || channel == null || channelExec == null) {
            throw new Exception(DONOT_INIT_ERROR_MSG);
        }
        LOGGER.info("execCmd command - > {}", command);
        channelExec.setCommand(command);
        channel.setInputStream(null);
        channelExec.setErrStream(System.err);
        channel.connect();
        StringBuilder sb = new StringBuilder(16);
        try (InputStream in = channelExec.getInputStream();
             InputStreamReader isr = new InputStreamReader(in, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            String buffer;
            while ((buffer = reader.readLine()) != null) {
                sb.append("\n").append(buffer);
            }
            // 释放资源
            close();
            LOGGER.info("execCmd result - > {}", sb);
            return sb.toString();
        }
    }
 
    /**
     * 释放资源
     *
     * @date 2019/3/15 12:47
     */
    public void close() {
        if (channelExec != null && channelExec.isConnected()) {
            channelExec.disconnect();
        }
        if (channel != null && channel.isConnected()) {
            channel.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }
    
    public List<String> remoteExecute(String command) throws JSchException {
    	LOGGER.debug(">> {}", command);
        List<String> resultLines = new ArrayList<>();
        ChannelExec channel = null;
        try{
            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);
            InputStream input = channel.getInputStream();
            channel.connect(CONNECT_TIMEOUT);
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(input));
                String inputLine = null;
                while((inputLine = inputReader.readLine()) != null) {
                	LOGGER.debug("   {}", inputLine);
                    resultLines.add(inputLine);
                }
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e) {
                    	LOGGER.error("JSch inputStream close error:", e);
                    }
                }
            }
        } catch (IOException e) {
        	LOGGER.error("IOcxecption:", e);
        } finally {
            if (channel != null) {
                try {
                    channel.disconnect();
                } catch (Exception e) {
                	LOGGER.error("JSch channel disconnect error:", e);
                }
            }
        }
        return resultLines;
    }
}
