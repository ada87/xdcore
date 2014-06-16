package com.xdnote.xdcore.plugin;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import net.spy.memcached.MemcachedClient;

class MemCacheFactory {

	private static MemcachedClient client=null;
	//获得关键客户端对象，方便后续的set,get方法
	
	public static MemcachedClient getMemCachedClient()
    {
		if(client==null){
			List<InetSocketAddress> addrs = new ArrayList<InetSocketAddress>();
			String hostAddr = PluginUtil.getConfigValue("memcached.host");
			String portstr = PluginUtil.getConfigValue("memcached.port");
			String[] ports=portstr.split(",");
			for(String port:ports){
				addrs.add(new InetSocketAddress(hostAddr,Integer.parseInt(port)));
			}			
			try {	 
				client = new MemcachedClient(addrs);
			} catch (IOException e) {
			}
		}			
	return client;
    }
	
}
