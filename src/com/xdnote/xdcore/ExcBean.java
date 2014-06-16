package com.xdnote.xdcore;

class ExcBean {
	//执行的类名称
	private String className;
	//执行的类方法
	private String methodName = "index";
	//方法带入的参数
	private String[] params;

	public ExcBean(String className, String methodName, String[] params) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.params = params;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String[] getParams() {
		return params;
	}
	public void setParams(String[] params) {
		this.params = params;
	}

}
