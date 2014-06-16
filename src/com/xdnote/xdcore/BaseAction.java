package com.xdnote.xdcore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <pre>
 * 		业务Action的基类，包含一个没有实现的index方法，和bf_Validate方法，可返回任意对象，
 * 		业务Action必须继承BaseAction，并实现index
 * 		如果实现了bf_Validate方法则将会在执行方法之前执行bf_Validate方法
 * </pre>
 * @author xdnote.com
 * 
 * */
public class BaseAction {
	/**
	 * 请求之前验证方法，在你的方法之前执行，判定用户是否符合条件<br>
	 * 如果不需要判断可以不用实现这个方法， 如果判定成功则返回null，否则返回一个框架支持的处理对象
	 * @param request 请求体
	 * @param response 响应体
	 * @param args 链接里面的请求参数
	 * @return Object 响应对象 支持字符串与框架支持的对象 可通过插件支持扩展
	 * */
	public Object bf_Validate(HttpServletRequest request,
			HttpServletResponse response, String[] args) {
		return null;
	}

	/**
	 * 默认执行的方法，没有方法名时执行index方法<br>
	 * 一般情况下请务必重写此方法，以免用户目前访问引起的404
	 * @param request  请求体
	 * @param response   响应体
	 * @param args   链接里面的请求参数
	 * @return Object  接受返回任何类型的对象，最目前能处理的只有String以及com.xdnote.framework.result下面的对象类的子类
	 *         返回String forward到返回的路径上去 返回Json
	 *         会返回一个Content-Type为application/json格式的 返回Redirect 会转向到另一个链接 返回null
	 *         不做处理，执行下一步
	 * */
	public Object index(HttpServletRequest request,
			HttpServletResponse response, String[] args) {
		return null;
	}

}
