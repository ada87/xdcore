package com.xdnote.xdcore.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;


/**
 * 缓存管理，基本的缓存操作，设置，取出等，使用上类似Jcs，以GROUP编组，以KEY-VALUE对为缓存,<span style="color:red">项目内小型数据缓存专用，不会过期，慎放大型数据</span>
 * @author xdnote.com
 * @since 0.1
 * */
public class CacheUtil {
	
	private static Map<String, Map<Object,Object>> cacheMap = new HashMap<String, Map<Object,Object>>();
	
	private static String DEFAULT ="XDCORE_DEFAULT_GROUP";
	
	private static String DEFAULT_CONFIG = "XDCORE_DEFAULT_CONFIG";
	
	
	/**
	 * 获取默认缓存
	 * @param key
	 * key
	 * @return obj
	 * 缓存对象
	 * */
	public static Object getValue(Object key){
		return getValue(DEFAULT,key);
	};
	/**
	 * 获取指定Group的缓存
	 * @param groupName
	 *    指定groupName
	 * @param key
	 *    key
	 * @return obj
	 * 缓存对象
	 * */
	public static Object getValue(String groupName,Object key){
		if(cacheMap.containsKey(groupName)){
			Map<Object,Object> groupCache = cacheMap.get(groupName);
			if(groupCache.containsKey(key)){
				return groupCache.get(key);
			}
		}
		return null;
	};
	/**
	 * @param key
	 *     存入的KEY
	 * @param value
	 *     存入的value
	 * */
	public static void setValue(Object key, Object value){
		setValue(DEFAULT ,key, value);
	}
	/**
	 * 设置缓存
	 * @param groupName
	 *    指定groupName
	 * @param key  
	 *     存入的KEY
	 * @param value
	 *     存入的value
	 * */
	public static void setValue(String groupName,Object key, Object value){
		if(!cacheMap.containsKey(groupName)){
			cacheMap.put(groupName, new HashMap<Object, Object>());
		}
		Map<Object, Object> groupCache = cacheMap.get(groupName);
		groupCache.put(key, value);
	}
	
	
	public static void loadFileToGroup(String filePath,String groupName){
		Properties prop = new Properties();
		InputStream in = null;
		try { 
			in= CacheUtil.class.getClassLoader().getResourceAsStream(filePath);
			prop.load(in);
			Iterator<Entry<Object, Object>> it = prop.entrySet().iterator();
			while(it.hasNext()){
				Entry<Object, Object> et = it.next();
				CacheUtil.setValue(groupName, et.getKey(), et.getValue());
			}
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getConfig(String key){
		if(!cacheMap.containsKey(DEFAULT_CONFIG)){
			loadFileToGroup("xConfig.properties",DEFAULT_CONFIG);
		}
		Object obj = getValue(DEFAULT_CONFIG,key);
		if(obj!=null){
			return obj.toString();
		}
		return "";
	}
	
}
