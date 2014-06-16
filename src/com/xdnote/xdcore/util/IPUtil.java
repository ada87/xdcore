package com.xdnote.xdcore.util;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取用户远程IP地址，（可防一般虚拟主机和代理访问获取不到真实IP，如果有IP鉴权功能不要使用）
 * @since 0.1
 * @author xdnote.com
 * @see <a href="http://www.xdnote.com">xdnote</a>
 * */
public class IPUtil {
	/**
	 *  获取用户远程IP地址，（可防一般虚拟主机和代理访问获取不到真实IP，如果有IP鉴权功能不要使用）
	 *  @param request HttpServletRequest 请求体 
	 *  @return String 用户 的IP地址
	 * */
	public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
