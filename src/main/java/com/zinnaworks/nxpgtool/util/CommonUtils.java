package com.zinnaworks.nxpgtool.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class CommonUtils {
	public static <T> void pt(T t) {
		System.out.println(t.toString());
	}
	public static String loadData(String fn) throws FileNotFoundException {
		String str1 = "";
		try (BufferedReader br = new BufferedReader(new FileReader(fn))) {
			String str = "";
			while ((str = br.readLine()) != null) {
				str1 = str1 + str;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str1;
	}
	
	public static String loadData(Path fileStorageLocation2) throws FileNotFoundException {
		return loadData(fileStorageLocation2.toFile().getAbsolutePath());
	}
	
	public static void saveJson(String fn, String json) throws IOException {
		FileOutputStream fo = new FileOutputStream(fn);
		fo.write(json.getBytes());
		fo.close();
	}
	public static void saveJson(Path fileStorageLocation2, String str) throws IOException {
		saveJson(fileStorageLocation2.toFile().getAbsolutePath(), str);
	}
	
	public static boolean strToBoolean(String text) {
		return text.equals("Y") ? true : false;
	}
}
