package com.xdnote.xdcore;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * MVC的控制器，配置xMap后，可以根据请求与xMap里面的的key进行匹配，匹配到则按匹配到的处理Action执行请求
 * @author xdnote.com
 * */
public class BaseFilter implements Filter {

	/**
	 * TODO 当initParam传入
	 * */
	private boolean debugMode = true;
	
	private void throwErrorPage(HttpServletResponse response,Exception e,String resion){
		if(debugMode){
			try {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.setCharacterEncoding("UTF-8");
				PrintWriter pw = response.getWriter();
				pw.write("<h1>"+resion+"</h1>");
				e.printStackTrace();
				pw.close();
			} catch (IOException e1) {
				System.out.println("系统已经挂了？");
			}
		}else{
			
		}
	}
	/**
	 * 控制器，如果想扩展一下，可以自己写一个Filter继承本Filter，实现的被捕super.doFilter一下，再加扩展代码
	 * */
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain fc) throws IOException, ServletException {
		// 转换数据
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		// 请求控制器－获取请求头
		String requestURI = request.getServletPath();
		// 请求控制器－根据请求头找到对应执行action（所有的请求信息存MAP缓存，查找一次后不再查找）
		ExcBean exc = Configs.getAction(requestURI);
		
		Object returnObj = null;
		try {
			if (exc != null) {
					returnObj = this.run(request, response, exc);
			} else {
				returnObj = this.run(request, response, requestURI);
			}
		} catch (InstantiationException e) {
			throwErrorPage(response,e,"实例化Action时出错" + exc.getClassName()+":"+exc.getMethodName());
			return ;
		} catch (IllegalAccessException e) {
			throwErrorPage(response,e,"访问Action时出错" + exc.getClassName()+":"+exc.getMethodName());
			return ;
		} catch (ClassNotFoundException e) {
			throwErrorPage(response,e,"没有找到类" + exc.getClassName()+":"+exc.getMethodName());
			return ;
		} catch (InvocationTargetException e) {
			throwErrorPage(response,e,"执行方法时出错" +exc.getClassName() +":"+exc.getMethodName());
			return ;
		}
		if (returnObj != null) {
			this.handderReturn(request, response, returnObj);
		} else {
			fc.doFilter(req, resp);
		}
	}

	/**
	 * 将请求链接转化为请求方法，再执行
	 * 
	 * @param request
	 *            上下文-请求Request
	 * @param response
	 *            上下文-响应Response
	 * @param requestURI
	 *            当前请求的URI
	 * @return obj 返回方法执行后得到的结果
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InvocationTargetException 
	 * */
	private Object run(HttpServletRequest request,
			HttpServletResponse response, String requestURI) throws InstantiationException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
		String[] uris = requestURI.replaceFirst("/", "").split("/");
		String className = "";
		String methodName = "index", temp = "index";
		String[] params = {};
		for (int i = uris.length, j = 0; i > j; i--) {
			String umatch = "";
			for (int m = 0; m < i; m++) {
				umatch += "/" + uris[m];
			}
			className = Configs.getValue(umatch);
			if (!className.equals("")) {
				if (i < uris.length) {
					temp = uris[i];
				}
				BaseAction act = (BaseAction) Class.forName(className)
						.newInstance();
				Method fn = this.getMethod(act, temp);
				if (fn != null) {
					methodName = temp;
				}
				String param = requestURI.replace(umatch, "").replace("/"+methodName, "");
				param = param.startsWith("/") ? param.substring(1).trim()
						: param.trim();
				if (!param.equals("")) {
					params = param.split("/");
				}
				ExcBean exc = new ExcBean(className, methodName, params);
				Configs.setAction(requestURI, exc);
				return this.run(request, response, exc);
	
			}
		}
		return null;
	}

	/**
	 * 根据请求方法执行
	 * 
	 * @param request
	 *            上下文-请求Request
	 * @param response
	 *            上下文-响应Response
	 * @param exc
	 *            封装需要执行的java类，方法，执行方法的参数
	 * @return obj 返回方法执行后得到的结果
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws ClassNotFoundException 
	 * @throws InvocationTargetException 
	 * */
	private Object run(HttpServletRequest request,
			HttpServletResponse response, ExcBean exc) throws InstantiationException, IllegalAccessException, ClassNotFoundException, InvocationTargetException {
		BaseAction act;
		act = (BaseAction) Class.forName(exc.getClassName()).newInstance();
		Object obj = act.bf_Validate(request, response, exc.getParams());
		if(obj!=null){
			return obj;
		}
		Method fn = this.getMethod(act, exc.getMethodName());
		return fn.invoke(act, request, response, exc.getParams());

	}

	@Override
	public void init(FilterConfig config) throws ServletException{
		String mapFile="xMap.properties";
		try {
			Invokes.init();
			Configs.init(mapFile);
		} catch (SecurityException e) {
			System.err.println("启动错误：请检查Invokes:init");
			e.printStackTrace();
			throw e;
		} catch (NoSuchMethodException e) {
			System.err.println("启动错误：请检查Invokes:init");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("启动错误：请检查Invokes:init");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("启动错误：请检查xMap文件");
			e.printStackTrace();
		}
	}

	
	
	@Override
	public void destroy() {

	}

	/**
	 * 获取对象里面的方法
	 * 
	 * @param act
	 *            Action对象，可以是BaseAction的子类
	 * @param methodName
	 *            需要执行的方法
	 * @return method 需要执行的方法的method形态
	 * */
	private Method getMethod(BaseAction act, String methodName) {
		Method[] methods = act.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equalsIgnoreCase(methodName)) {
				return methods[i];
			}
		}
		return null;
	}
	/**
	 * 处理返回结果
	 * @throws ServletException 
	 * @throws IOException 
	 * */
	private void handderReturn(HttpServletRequest request,
			HttpServletResponse response, Object obj)  {
		try {
			Invokes.invoke(request, response, obj);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

	}


}
