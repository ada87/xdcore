package com.xdnote.xdcore.plugin;

import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.xdnote.xdcore.util.CacheUtil;
/**
 * 邮件发送类，目前可通过MailUtil发送HTML文件或文本文件
 * 
 * @author xdnote.com
 * @since 0.1
 * 
 * <h4>邮件发送类需要在xPlugin里面配置smtp信息，如下：</h4>
 * <pre>
mail.smtp.host=smtp.163.com
mail.smtp.port=25
mail.smtp.auth=true
mail.smtp.user=88excel
mail.smtp.pass=password
mail.smtp.form=88excel@163.com
</pre>
 * 
 * */
public class MailUtil {

	/**
	 * 发送HTML格式的邮件
	 * @param  tomail 收件人地址
	 * @param  subject 邮件标题
	 * @param  context 邮件内容
	 * @throws MessagingException 发送邮件时异常
	 * <p>ep: MailUtil.send("xxxx@163.com","系统邮件","&lt;a href=\"http://www.xdnote.com/\" style=\"color:#fff000;\"&gt;xdnote&lt;/a&gt;")</p>
	 * 
	 * */
	public static void sendHtmlMail(String tomail,String subject,String context) throws MessagingException{
		Properties props = new Properties();
		props.put("mail.smtp.host", PluginUtil.getConfigValue("mail.smtp.host"));
		props.put("mail.smtp.port", PluginUtil.getConfigValue("mail.smtp.port"));
		props.put("mail.smtp.auth",PluginUtil.getConfigValue("mail.smtp.auth"));
		MailAuthenticator auth = new MailAuthenticator( PluginUtil.getConfigValue("mail.smtp.user"), PluginUtil.getConfigValue("mail.smtp.pass"));
		Session session= Session.getDefaultInstance(props,auth);
		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(CacheUtil.getConfig("mail.smtp.form")));
			message.setRecipient(Message.RecipientType.TO,new InternetAddress(tomail));
			message.setSubject(subject);
			Multipart contentHtml = new MimeMultipart();    
			BodyPart html = new MimeBodyPart();
			html.setContent(context, "text/html; charset=utf-8");
			contentHtml.addBodyPart(html); 
			message.setContent(contentHtml);
			Transport.send(message);
		} catch (MessagingException e) {
			throw e;
		}
	}
	
//	/**
//	 * 发送文本格式的邮件
//	 * @param  tomail 收件人地址
//	 * @param  subject 邮件标题
//	 * @param  context 邮件内容
//	 * @throws MessagingException 发送邮件时异常
//	 * */
//	public static void sendTextMail(String tomail,String subject,String context) throws MessagingException{
//		Properties props = new Properties();
//		props.put("mail.smtp.host", PluginUtil.getConfigValue("mail.smtp.host"));
//		props.put("mail.smtp.port", PluginUtil.getConfigValue("mail.smtp.port"));
//		props.put("mail.smtp.auth",PluginUtil.getConfigValue("mail.smtp.auth"));
//		MailAuthenticator auth = new MailAuthenticator( PluginUtil.getConfigValue("mail.smtp.user"), PluginUtil.getConfigValue("mail.smtp.pass"));
//		Session session= Session.getDefaultInstance(props,auth);
//		Message message = new MimeMessage(session);
//		try {
//			message.setFrom(new InternetAddress(CacheUtil.getConfig("mail.smtp.form")));
//			message.setRecipient(Message.RecipientType.TO,new InternetAddress(tomail));
//			message.setSubject(subject);
//			message.setContent(arg0);
//			message.setContent(context);
//			Transport.send(message);
//		} catch (MessagingException e) {
//			throw e;
//		}
//	}
}
