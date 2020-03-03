package com.zinnaworks.nxpgtool.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

public class FileUtils {
	
	public static String strFileName = "/Volumes/Macintosh HD/temp.txt";
	
	public static void fileDelete(String fileName) {
		try {
			File file = new File(fileName);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
		}
	}
	
	public static String fileRead(String fileName) {
		String result = "";
		try {
			
			InputStream is = new FileInputStream(new File(fileName));
			result = IOUtils.toString(is, "UTF-8");
            
		} catch (Exception e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
			result = "";
		}
		return result;
	}
	
	public static void fileWrite(String fileName, String text) {
	    try {
            // 파일 객체 생성
            File file = new File(fileName) ;
             
            // true 지정시 파일의 기존 내용에 이어서 작성
            FileWriter fw = new FileWriter(file, true) ;
             
            // 파일안에 문자열 쓰기
            fw.write(text);
            fw.flush();
 
            // 객체 닫기
            fw.close();
	      } catch (Exception e) {
	          System.err.println(e); // 에러가 있다면 메시지 출력
	      }
	}
	
	public static JSONObject getFileToJson(String filename) {
		
		JSONObject json = null;
		try {
			
			InputStream is = new FileInputStream(new ClassPathResource(filename).getFile());
            String jsonText = IOUtils.toString(is, "UTF-8");
            
            json = new JSONObject(jsonText);
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return json;
	}
}
