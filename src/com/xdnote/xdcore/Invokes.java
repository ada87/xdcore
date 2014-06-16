package com.xdnote.xdcore;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 返回对象处理类，处理返回的对象， 默认支持三种处理类型：跳转, 请求转发，JSON响应，可以通过addInvokeType进行扩展
 * @author xdnote.com
 * */
class Invokes {
	static Map<Class<?>,Method> invoke_list;
	/**
	 * 默认的三种处理方式，跳转, 请求转发，与JSON
	 * */
	public static void init() throws SecurityException, NoSuchMethodException, ClassNotFoundException{
		Class<?> c = Class.forName("com.xdnote.xdcore.Invokes");
		invoke_list=new HashMap<Class<?>,Method>();
		invoke_list.put(String.class,
					c.getMethod("handerString",new Class[] {HttpServletRequest.class,HttpServletResponse.class, String.class}));
		invoke_list.put(Json.class,
				c.getMethod("handerJson",new Class[] {HttpServletRequest.class,HttpServletResponse.class, Json.class}));
		invoke_list.put(Redirect.class,
				c.getMethod("handerRedirect",new Class[] {HttpServletRequest.class,HttpServletResponse.class, Redirect.class}));
	}
	/**
	 * 增加一个响应类型的执行
	 * @param cls 要执行的对象的类型
	 * @param fn 执行这种对象的方法
	 * 		<p>目前阶段必须是静态方法，否则无法处理！
	 * 		执行方法的类型必须是public static，参数必须是 （HttpServletRequest request,HttpServletResponse response,&lt;T&gt; obj ）,
	 * 		此处的Object为你的第一个参数cls的实体对象否则无法执行
	 * 	</p>
	 * */
	public static void addInvokeType(Class<?> cls,Method fn){
		invoke_list.put(cls, fn);
	}
	/**
	 * 执行方法，如果有这个类型的执行器，则使用此执行器执行，如果没有，则看有没有超类执行器，找到超类执行器去执行<br>
	 * PS:反射是很慢的，程序运行中，所有类型只会运行一次即放入缓存。再次执行同样的类型时不会再去找对应方法直接通过MAP取
	 * @param request 请求体
	 * @param response 响应体
	 * @param obj 需要执行的对象
	 * */
	public static void invoke(HttpServletRequest request,
			HttpServletResponse response,Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		Class<?> key = obj.getClass();
		Method fn = invoke_list.get(key);
		if(fn==null){
			Set<Class<?>> set = invoke_list.keySet();
			Iterator<Class<?>> it =set.iterator();
			while(it.hasNext()){
				Class<?> cls =it.next();
				if(cls.isAssignableFrom(obj.getClass())){
					fn = invoke_list.get(cls);
					addInvokeType(key, fn);
					fn.invoke(null , request,response,obj );
					return;
				}
			}
		}else{
			fn.invoke(null,request,response,obj);
		}
	}
	
	/**
	 * 字符串处理方法：将请求转发
	 * */
	public static void handerString(HttpServletRequest request,
			HttpServletResponse response, String str){
		try {
			request.getRequestDispatcher(str).forward(request, response);
		} catch (ServletException e) 
		{
			e.printStackTrace();
			//TODO
		} catch (IOException e) {
			//TODO
			e.printStackTrace();
		}
	}

	/**
	 * 重定向处理方法：重定向到指定链接
	 * */
	public static void handerRedirect(HttpServletRequest request,
			HttpServletResponse response, Redirect redirect){
		try {
			response.sendRedirect(redirect.getRediret());
		} catch (IOException e) {
			//TODO
		}
	}
	/**
	 * Json处理方法：响应一个json格式的页面
	 * */
	public static void handerJson(HttpServletRequest request,
			HttpServletResponse response, Json json){
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter pw=null;
		try { 
			pw= response.getWriter();
			pw.print(json.toString());
		} catch (IOException e) {
			//TODO
		}finally{
			pw.close();
		}
	}

}
