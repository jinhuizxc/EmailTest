package com.example.emailtest.mail.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
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
import javax.mail.internet.MimeUtility;

import com.example.emailtest.email.WriteActivity;

import android.util.Log;

public class SendMail {

	private static SendMail instance = null;

	private SendMail() {

	}

	public static SendMail getInstance() {
		if (instance == null) {
			instance = new SendMail();
		}
		return instance;
	}

	public void send(String to[], String cs[], String ms[], String subject,
			String content, String formEmail, String fileList[],String pwd,String host) {
		try {
			Properties p = new Properties();
			p.setProperty("mail.smtp.auth", "true");
			p.setProperty("mail.transport.protocol", "smtp");
			p.setProperty("mail.smtp.host", host);
			p.setProperty("mail.smtp.starttls.enable","true");
			
			
			
			// 建立会话
			Session session = Session.getInstance(p);
			MimeMessage msg = new MimeMessage(session); // 建立信息
			BodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = new MimeMultipart();
			msg.setFrom(new InternetAddress(formEmail)); // 发件人
			msg.setSentDate(new Date()); // 发送日期
			msg.setSubject(subject); // 主题
			msg.setText(content); // 内容
			// 显示以html格式的文本内容
			messageBodyPart.setContent(content, "text/html;charset=gbk");
			multipart.addBodyPart(messageBodyPart);
			
			String toList = null;
			String toListcs = null;
			String toListms = null;

			// 发送
			if (to != null) {
				toList = getMailList(to);
				InternetAddress[] iaToList = InternetAddress.parse(toList);
				msg.setRecipients(Message.RecipientType.TO, iaToList); // 收件人
			}
//
//			// 抄送
			if (cs != null) {
				toListcs = getMailList(cs);
				InternetAddress[] iaToListcs = InternetAddress.parse(toListcs);
				msg.setRecipients(Message.RecipientType.CC, iaToListcs); // 抄送人
			}
//
//			// 密送
			if (ms != null) {
				toListms = getMailList(ms);
				InternetAddress[] iaToListms = InternetAddress.parse(toListms);
				msg.setRecipients(Message.RecipientType.BCC, iaToListms); // 密送人
			}

//
//			// 2.保存多个附件
			if (fileList != null) {
				addTach(fileList, multipart);
			}

			MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap(); 
			mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html"); 
			mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml"); 
			mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain"); 
			mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed"); 
			mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
			msg.setContent(multipart);
			msg.saveChanges();
			// 邮件服务器进行验证
			Transport tran = session.getTransport("smtp");
			tran.connect(formEmail,pwd);
			tran.sendMessage(msg, msg.getAllRecipients()); // 发送
			tran.close();
			WriteActivity.handler.sendEmptyMessage(1);
		} catch (Exception e) {
			WriteActivity.handler.sendEmptyMessage(0);
			Log.e("zcj--------", e.toString());
			e.printStackTrace();
			
		}
	}

	// 添加多个附件
	public void addTach(String fileList[], Multipart multipart)
			throws MessagingException, UnsupportedEncodingException {
		for (int index = 0; index < fileList.length; index++) {
			MimeBodyPart mailArchieve = new MimeBodyPart();
			FileDataSource fds = new FileDataSource(fileList[index]);
			mailArchieve.setDataHandler(new DataHandler(fds));
			mailArchieve.setFileName(MimeUtility.encodeText(fds.getName(),
					"GBK", "B"));
			multipart.addBodyPart(mailArchieve);
		}
	}

	private String getMailList(String[] mailArray) {

		StringBuffer toList = new StringBuffer();
		int length = mailArray.length;
		if (mailArray != null && length < 2) {
			toList.append(mailArray[0]);
		} else {
			for (int i = 0; i < length; i++) {
				toList.append(mailArray[i]);
				if (i != (length - 1)) {
					toList.append(",");
				}

			}
		}
		return toList.toString();

	}

}