package com.example.emailtest.mail.util;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import android.util.Log;

public class MailReceiver implements Serializable {

    private static final long serialVersionUID = 1L;
    private MimeMessage mimeMessage = null;
    private StringBuffer mailContent = new StringBuffer();// 邮件内容
    private String dataFormat = "yyyy年MM月dd日 HH:mm";
    private String charset;
    private String flag; //0 text 1 html
    private ArrayList<String> attachments = new ArrayList<String>();
    private ArrayList<InputStream> attachmentsInputStreams = new ArrayList<InputStream>();
    private boolean alternative;
    public MailReceiver(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
        try {
            charset = parseCharset(mimeMessage.getContentType());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得送信人的姓名和邮件地址
     * 
     * @throws Exception
     */
    public String getFrom() throws Exception {
        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
        String addr = address[0].getAddress();
        String name = address[0].getPersonal();
        if (addr == null) {
            addr = "";
        }
        if (name == null) {
            name = "";
        } else if (charset == null) {
            name = TranCharsetUtil.TranEncodeTOGB(name);
        }
        String nameAddr = name + "<" + addr + ">";
        return nameAddr;
    }
    
    /**
     * 根据类型，获取邮件地址 "TO"--收件人地址 "CC"--抄送人地址 "BCC"--密送人地址
     * 
     * @throws Exception
     */
    public String getMailAddress(String type) throws Exception {
        String mailAddr = "";
        String addType = type.toUpperCase(Locale.CHINA);
        InternetAddress[] address = null;
        if (addType.equals("TO")) {
            address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);
        } else if (addType.equals("CC")) {
            address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);
        } else if (addType.equals("BCC")) {
            address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);
        } else {
            System.out.println("error type!");
            throw new Exception("Error emailaddr type!");
        }
        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                String mailaddress = address[i].getAddress();
                if (mailaddress != null) {
                    mailaddress = MimeUtility.decodeText(mailaddress);
                } else {
                    mailaddress = "";
                }
                String name = address[i].getPersonal();
                if (name != null) {
                    name = MimeUtility.decodeText(name);
                } else {
                    name = "";
                }
                mailAddr = name + "<" + mailaddress + ">";
            }
        }
        return mailAddr;
    }

    /**
     * 取得邮件标题
     * 
     * @return String
     */
    public String getSubject() {
        String subject = "";
        try {
            subject = mimeMessage.getSubject();
            if (subject.indexOf("=?gb18030?") != -1) {
                subject = subject.replace("gb18030", "gb2312");
            }
            subject = MimeUtility.decodeText(subject);
            if (charset == null) {
                subject = TranCharsetUtil.TranEncodeTOGB(subject);
            }
        } catch (Exception e) {
        }
        return subject;
    }

    /**
     * 取得邮件日期
     * 
     * @throws MessagingException
     */
    public String getSentData() throws MessagingException {
        Date sentdata = mimeMessage.getSentDate();
        if (sentdata != null) {
            SimpleDateFormat format = new SimpleDateFormat(dataFormat, Locale.CHINA);
            return format.format(sentdata);
        } else {
            return "未知";
        }
    }

    /**
     * 取得邮件内容
     * 
     * @throws Exception
     */
    public String getMailContent() throws Exception {
    	resolveMail(mimeMessage);
    	String content = mailContent.toString();
    	mailContent.setLength(0);
        return content;
    }

    public void setMailContent(StringBuffer mailContent) {
        this.mailContent = mailContent;
    }

    /**
     * 是否有回执
     * 
     * @throws MessagingException
     */
    public boolean getReplySign() throws MessagingException {
        boolean replySign = false;
        String needreply[] = mimeMessage.getHeader("Disposition-Notification-To");
        if (needreply != null) {
            replySign = true;
        }
        return replySign;
    }
    
    /**
     * 取得「message-ID」
     * 
     * @throws MessagingException
     */
    public String getMessageID() throws MessagingException {
        return mimeMessage.getMessageID();
    }
    
    /**
     * 是否新邮件
     * 
     * @throws MessagingException
     */
    public boolean isNew() throws MessagingException {
        boolean isnew = false;
        Flags flags = ((Message) mimeMessage).getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == Flags.Flag.SEEN) {
                isnew = true;
                break;
            }
        }
        return isnew;
    }
    
    public String getCharset() {
        return charset;
    }

    public ArrayList<String> getAttachments() {
        return attachments;
    }

    public String isHtml() {
        return flag;
    }

    public ArrayList<InputStream> getAttachmentsInputStreams() {
        return attachmentsInputStreams;
    }


    /**
     * 解析字符集编码
     * 
     * @param contentType
     * @return
     */
    private String parseCharset(String contentType) {
        if (contentType.contains("gbk") || contentType.contains("GBK")) {
            return "gbk";
        } else if (contentType.contains("gb2312") || contentType.contains("GB2312")) {
            return "gb2312";
        } else if(contentType.contains("gb18030") || contentType.contains("GB18030")){
        	return "gb2312";
        }else{
            return "utf-8";
        }
    }

    /**
     * 解析流格式
     * 
     * @param is
     * @param contentType
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    private String parseInputStream(InputStream is) throws IOException, MessagingException {
        StringBuffer str = new StringBuffer();
        byte[] readByte = new byte[1024];
        int count;
        try {
            while ((count = is.read(readByte)) != -1) {
                if (charset == null) {
                    str.append(new String(readByte, 0, count, "utf-8"));
                } else {
                    str.append(new String(readByte, 0, count, charset));
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return str.toString();
    }
    
    public void resolveMail(Part mimeMessage) throws MessagingException, IOException{
		if(mimeMessage.isMimeType("text/html")){
			flag = "1";
			String content = (String) mimeMessage.getContent();
			mailContent.append(content);
		}else if(mimeMessage.isMimeType("text/plain")){
			flag = "0";
			String content = (String) mimeMessage.getContent();
			mailContent.append(content);
		}else if(mimeMessage.isMimeType("multipart/alternative")){
			
			Multipart mp = (Multipart) mimeMessage.getContent();
			int num = mp.getCount();
			for(int j = 0; j < num; j++){
				if(mp.getBodyPart(j).isMimeType("text/html")){
					String content = (String) mp.getBodyPart(j).getContent();
					mailContent.append(content);
				}
			}
		}else if(mimeMessage.isMimeType("multipart/related")){
			flag = "3";
		}else if(mimeMessage.getDisposition() != null){
			String filename = mimeMessage.getFileName();
			if(filename.startsWith("=?")){
				filename = MimeUtility.decodeText(filename);
			}
			attachments.add(filename);
            attachmentsInputStreams.add(mimeMessage.getInputStream());
		}else if(mimeMessage.isMimeType("multipart/mixed")){
			Multipart mp = (Multipart) mimeMessage.getContent();
			int num = mp.getCount();
			for(int j = 0; j < num; j++){
				BodyPart bp = mp.getBodyPart(j);
				resolveMail(bp);
			}
		}
	}

}
