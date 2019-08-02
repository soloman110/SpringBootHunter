package com.zinnaworks.nxpgtool.api;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.zinnaworks.nxpgtool.util.HttpClient;
import com.zinnaworks.nxpgtool.util.JsonUtil;

public class IF {
	public static final Map<String, String> IF_URLS = new HashMap<>();
	public static final String IF001 = "IF-NXPG-001";
	public static final String IF002 = "IF-NXPG-002";
	public static final String IF003 = "IF-NXPG-003";
	public static final String IF005 = "IF-NXPG-005";
	public static final String IF010 = "IF-NXPG-010";

	static {
		IF_URLS.put(IF001,
				"https://xpg-nxpg-stg.skb-doj-dev01.mybluemix.net/v5/menu/gnb?IF=IF-NXPG-001&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&menu_stb_svc_id=BTVWEBV512");
		IF_URLS.put(IF002,
				"https://xpg-nxpg-stg.skb-doj-dev01.mybluemix.net/v5/menu/all?IF=IF-NXPG-002&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&menu_stb_svc_id=BTVWEBV512");
		IF_URLS.put(IF003,
				"https://xpg-nxpg-stg.skb-doj-dev01.mybluemix.net/v5/menu/block?IF=IF-NXPG-003&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&menu_stb_svc_id=BTVWEBV512&menu_id=NM1000000400");
		IF_URLS.put(IF005,
				"https://xpg-nxpg-stg.skb-doj-dev01.mybluemix.net/v5/menu/month?IF=IF-NXPG-005&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&menu_stb_svc_id=BTVWEBV512&menu_id=NM1000000100&prd_prc_id_lst=1017470");
		IF_URLS.put(IF010,
				"https://xpg-nxpg-stg.skb-doj-dev01.mybluemix.net/v5/contents/synopsis?menu_stb_svc_id=BTVWEBV512&stb_id=%7B660D7F55-89D8-11E5-ADAE-E5AC4F814417%7D&IF=IF-NXPG-010&cmc_yn=Y&ppm_yn=Y&epsd_id=CE0001293945&epsd_rslu_id=%7B17E6CCA1-0A46-11E8-9252-FFA90356A7A9%7D&search_type=1&yn_recent=N");
	}

	public static Map<String, Object> getData(String url) throws FileNotFoundException {
		String json2 = HttpClient.doGet(url);
		// String json2 = loadData("/excel/json/2.json");
		Map<String, Object> t = JsonUtil.jsonToObjectHashMap(json2, String.class, Object.class);
		return t;
	}
}
