package com.xdnote.xdcore.util;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * 表单提交时的常用工具，包括验证邮箱地址，手机号码等等
 * 
 * @since 0.1
 * @author xdnote.com
 * @see <a href="http://www.xdnote.com">xdnote</a>
 * */
public class FormUtil {

	
	/**
	 * 验证是不是email格式
	 * 
	 * @param string
	 *            要验证的字符串
	 * @return boolean
	 * */
	public static boolean validateEmail(String string) {
		Pattern ptn = Pattern.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");
		return ptn.matcher(string).find();
	}

	/**
	 * 随机生成一个32位的字符串
	 * 
	 * @return String 随机字符串
	 * */
	public static String genKey() {
		return genKey(32);
	}

	/**
	 * 随机生成一个指定长度位数的字符串
	 * 
	 * @param length
	 *            int 指定长度
	 * @return String 随机字符串
	 * */
	public static String genKey(int length) {
		 char[] letters = new char[] {
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
			'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9'};
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			double d = Math.random();
			int index = (int) (d * 10000);
			sb.append(letters[index % 62]);
		}
		return sb.toString();
	}

	/**
	 * 判断一个文本是否是数字与字母的组合
	 * @param str 要判断的字符串
	 * @return boolean
	 */
	public static boolean isSimpleText(String str) {
		String regEx = "^[0-9A-Za-z]*$";
		Pattern p = Pattern.compile(regEx);
		if (p.matcher(str).find()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 日期转换
	 * */
	public static String dateString(String datastr){
		return datastr.substring(0,10);
	}
	
	/**
	 * 字节转换
	 * */
	public static String byteString(int b){
		String unit = "k";
		int sign = 1024;
		if(b>=sign*1024){
			unit="M";
			sign=sign*1024;
			if(b>=sign*1024){
				unit="G";
				sign = sign*1024;
			}
		}
		String o=String.valueOf(b/sign);
		String i =String.valueOf((b%sign)*100/sign);
		if(i.equals("0")){
			return o+unit;
		}
		return o+"."+i+unit;
	}
	
	public static void main(String[] ar){
//		System.out.println(3500/1024);
		System.out.println(byteString(5156352));
	}
	

}
