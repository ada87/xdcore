package com.xdnote.xdcore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

class Configs {
	
	private static Map<String,String> xMap;
	private static Map<String,ExcBean> xExc;
	
	protected static void init(String configPath) throws IOException{
		xMap = new HashMap<String,String>();
		xExc = new HashMap<String,ExcBean>();
		Properties prop = new Properties();
		InputStream in = null;
		try { 
			in= Configs.class.getClassLoader().getResourceAsStream(configPath);
			prop.load(in);
			Iterator<Entry<Object, Object>> it = prop.entrySet().iterator();
			while(it.hasNext()){
				Entry<Object, Object> et = it.next();
				xMap.put(et.getKey().toString(), et.getValue().toString());
			}
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}finally{
			in.close();
		}
	
	}
	
	protected static String getValue(String key){
		String value = xMap.get(key);
		if(value!=null){
			return value;
		}
		return "";
	}
	
	protected static ExcBean getAction(String uri){
		ExcBean obj = xExc.get(uri);
		if(obj!=null){
			return obj;
		}
		return null;
	}
	
	protected static void setAction(String uri, ExcBean excBean){
		xExc.put(uri, excBean);
	}
}
