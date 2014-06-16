package com.xdnote.xdcore.plugin;


import net.spy.memcached.MemcachedClient;


/**
 * MC操作类
 */
public class MemcacheUtil {
	
	private static int checkNumTime = Integer.parseInt(PluginUtil.getConfigValue("memcached.defaultExpireTime"));
	
	
	
	//设置保存memcached值（字符串类型的值）
	public static boolean setData(String key,Object value){
		return setData(key,checkNumTime,value);
	}
	
	//获取memcached值（字符串类型的值）
	public static <T>T getData(String key){	
		MemcachedClient m =MemCacheFactory.getMemCachedClient();
		if(null != m){
			Object obj = m.get(key);
			if(obj!=null){
				return(T)obj;
			}
		}
		return null;
	}	
	

	//设置保存memcached值
	public static <T> boolean setData(String key,Integer validTime,T value){
		MemcachedClient m =MemCacheFactory.getMemCachedClient();
		if(null != m){	
			m.set(key,validTime,value);
			return true;
		}
		return false;

	}
	
	//单纯清除某个key值的memcache对象
	public static void deleteData(String key){
		MemcachedClient m =MemCacheFactory.getMemCachedClient();
		if(null != m){	
			m.delete(key);
		}
	}
	
}

