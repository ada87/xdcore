package com.xdnote.xdcore.plugin;


import com.xdnote.xdcore.util.CacheUtil;

/**
 * 扩展插件管理器，允许插件在Fiter或Action里面执行并影响响应结果
 * */
class PluginUtil {

	private static final String PLUGIN_FILE = "xPlugin.properties";
	private static final String PLUGIN_GROUP = "XDCORE_PLUGIN_GROUP";
	
	private static boolean PLUGIN_CONFIG_LOAD = false;
	
	public static String getConfigValue(String key){
		if(!PLUGIN_CONFIG_LOAD){
			CacheUtil.loadFileToGroup(PLUGIN_FILE, PLUGIN_GROUP);
			PLUGIN_CONFIG_LOAD=true;
		}
		return (String) CacheUtil.getValue(PLUGIN_GROUP,key);
	}
}
