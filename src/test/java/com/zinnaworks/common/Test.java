package com.zinnaworks.common;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.ssh.JschUtil;
import cn.hutool.extra.ssh.Sftp;

public class Test {
	private String name = "111";
	public static void main(String[] args) throws JSchException, UnsupportedEncodingException, InstantiationException, IllegalAccessException {
		Session session = JschUtil.getSession("10.211.55.10", 22, "oracle", "oracle");
		OutputStream out = new ByteArrayOutputStream();
		String result = JschUtil.exec(session, "ls -al /tmp", CharsetUtil.CHARSET_UTF_8, out );
		String str1 = new String(result.getBytes("ISO-8859-1"), "utf-8");
		System.out.println(str1);
		
		Sftp sftp = JschUtil.createSftp(session);
		//sftp.put("/Users/sikongming/Downloads/usb_bid-at200_V15.521.5.zip", "/tmp");
		System.out.println("====================");
		
		JschUtil.close(session);
		System.out.println(Test.class.getName());
		System.out.println(Test.class.getDeclaredConstructors());
		Test c = Test.class.newInstance();
		System.out.println(c.name);
				
	}
	public static int floor(int remainAmt) {
		return (int) (remainAmt * 1.1);
	}
	
	public static List<String> getCommandList() {
		List<String> list = new ArrayList<>();
		list.add("");
		return list;
	}
	private interface A {
		public void pirnt();
	}
	private static class Aimpl implements A {

		@Override
		public void pirnt() {
			
		}
		
	}
}

