package com.xdnote.xdcore;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * Json结果类，在执行Action返回一个Json的子类，会直接返回一个Json格式的响应。
 * @since 0.1
 * @author xdnote.com
 * */
public class Json {
	/**
	 * 全部小写
	 * */
	public final static int ALL_LOWERCASE = 0;
	/**
	 * 全部大写
	 * */
	public final static int ALL_UPCASE = 1;
	/**
	 * 首字母小写（默认）
	 * */
	public final static int FRIST_LOWERCASE = 2;
	/**
	 * 首字母大写
	 * */
	public final static int FRIST_UPCASE = 3;
	
	private int mode = FRIST_LOWERCASE;
	/**
	 * 设置输出Json时，key的模式
	 * */
	public void setDefaultMode(int mode){
		this.mode = mode;
	}
	@Override
	public String toString() {
		return toString(this.mode);
	}
	/**
	 * 重写toString方法,返回一个json格式字符串
	 * */
	private String toString(int mode) {
		StringBuffer sb = new StringBuffer();
		sb.append("{");
		Method[] methods = this.getClass().getMethods();
		for(int i = 0, j = methods.length; i<j ;i++){
			Method fn = methods[i];
			String fnName = fn.getName();
			if(fnName.startsWith("get")&&!fnName.endsWith("Class")){
				String field = this.convertString(fnName, mode);
				String value = "";
				try {
					Object returnObj = fn.invoke(this);
					value = this.convertObj(returnObj, mode);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				sb.append("\""+field+"\":"+value);
				sb.append(",");
			}
		}
		sb.append("}");
		return sb.toString().replace(",}", "}");
		
	}

	/**
	 * 转换字段名称，按规则转换，默认首字母小写
	 * */
	private String convertString(String fnName, int mode){
		String field = fnName.substring(3);
		switch(mode){
			case Json.ALL_LOWERCASE:
				field = field.toLowerCase();
			break;
			case Json.ALL_UPCASE:
				field = field.toUpperCase();
			break;
			case Json.FRIST_LOWERCASE:
				field = field.substring(0, 1).toLowerCase()+field.substring(1);
			break;
			case Json.FRIST_UPCASE:
				field = field.substring(0, 1).toUpperCase()+field.substring(1);
			break;
		}
		return field;
	}
	/**
	 * 转换值，根据对象的具体类型转换
	 * @param obj 
	 * 		要转换的对象，以目前支持Java基本类型，String,Map,List已经继承了Json类的对象
	 * @param mode 
	 * 		类型
	 * */
	private String convertObj(Object obj, int mode){
		if(obj ==  null){
			return "\"\"";
		}else if(obj instanceof String){
			return "\""+obj.toString().replace("\"", "\\\"").replace("\n", "")+"\"";
		}else if(obj instanceof Double || obj instanceof Float){
			return "\""+obj.toString()+"\"";
		}else if(obj instanceof Number){
			return obj.toString();
		}else if(obj instanceof Boolean){
			return obj.toString();
		}else if(obj instanceof Json){
			Json json = (Json) obj;
			return json.toString(mode);
		}else if(obj instanceof Object[]){
			Object[] objs = (Object[]) obj;
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for(int i=0,j=objs.length;i<j;i++){
				Object value = objs[i];
				sb.append(this.convertObj(value, mode));
				if((i+1)<j){
					sb.append(",");
				}
			}
			sb.append("]");
			return sb.toString();
		}else if(obj instanceof int[]){
			int[] objs = (int[]) obj;
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for(int i=0,j=objs.length;i<j;i++){
				int value = objs[i];
				sb.append(value);
				if((i+1)<j){
					sb.append(",");
				}
			}
			sb.append("]");
			return sb.toString();
		}else if(obj instanceof double[]){
			double[] objs = (double[]) obj;
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for(int i=0,j=objs.length;i<j;i++){
				double value = objs[i];
				sb.append(value);
				if((i+1)<j){
					sb.append(",");
				}
			}
			sb.append("]");
			return sb.toString();
		}else if(obj instanceof Map){
			Map map = (Map) obj;
			Set set = map.keySet();
			StringBuffer sb = new StringBuffer();
			sb.append("{");
			Iterator it = set.iterator();
			if(it.hasNext()){
				Object key = it.next();
				Object value = map.get(key);
				sb.append(key.toString()).append(":").append(this.convertObj(value, mode)).append(",");
			}
			sb.append("}");
			return sb.toString();
		}else if(obj instanceof List){
			List list = (List) obj;
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for(int i=0,j=list.size();i<j;i++){
				Object rtnObj = list.get(i);
				sb.append(this.convertObj(rtnObj, mode));
				if((i+1)<j){
					sb.append(",");
				}
			}
			sb.append("]");
			return sb.toString();
		}
		return "\"\"";
	}
	
}
