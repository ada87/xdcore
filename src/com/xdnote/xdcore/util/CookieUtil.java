package com.xdnote.xdcore.util;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie管理，基本的Cookie操作，增加，删除等
 * @since 0.1
 * @author xdnote.com
 * @see <a href="http://www.xdnote.com">xdnote</a>
 * */
public class CookieUtil {

	public static Cookie getCookie(HttpServletRequest request, String name) {
		Cookie cookies[] = request.getCookies();
		if (cookies == null || name == null || name.length() == 0) {
			return null;
		}
		for (int i = 0; i < cookies.length; i++) {
			if (name.equals(cookies[i].getName())
					&& request.getServerName().equals(cookies[i].getDomain())) {
				return cookies[i];
			}
		}
		return null;
	}

	public static void setCookie(HttpServletResponse response, String name,
			String value) {
		Cookie cookie = new Cookie(name, value == null ? "" : value);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public static void setCookie(HttpServletResponse response, String name,
			String value, int maxAge) {
		Cookie cookie = new Cookie(name, value == null ? "" : value);
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

}
