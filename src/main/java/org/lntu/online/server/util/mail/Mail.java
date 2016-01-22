/*
 * Copyright (C) 2015-2016 LNTU.ORG (http://lntu.org)
 * Copyright (C) 2014-2015 TakWolf <takwolf@foxmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.lntu.online.server.util.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class Mail {

    private MimeMessage mimeMsg;
    private Session session;
    private Properties props;
    private String username;
    private String password;
    private Multipart mp;

    public Mail(String smtp) {
        setSmtpHost(smtp);
        createMimeMessage();
    }

    public void setSmtpHost(String hostName) {
        System.out.println("设置系统属性：mail.smtp.host=" + hostName);
        if (props == null) {
            props = System.getProperties();
        }
        props.put("mail.smtp.host", hostName);
    }

    public boolean createMimeMessage() {
        try {
            System.out.println("准备获取邮件会话对象！");
            session = Session.getDefaultInstance(props, null);
        } catch (Exception e) {
            System.out.println("获取邮件会话错误！" + e);
            return false;
        }
        System.out.println("准备创建MIME邮件对象！");
        try {
            mimeMsg = new MimeMessage(session);
            mp = new MimeMultipart();

            return true;
        } catch (Exception e) {
            System.out.println("创建MIME邮件对象失败！" + e);
            return false;
        }
    }

    /*定义SMTP是否需要验证*/
    public void setNeedAuth(boolean need) {
        System.out.println("设置smtp身份认证：mail.smtp.auth = " + need);
        if (props == null)
            props = System.getProperties();
        if (need) {
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.smtp.auth", "false");
        }
    }

    public void setNamePass(String name, String pass) {
        username = name;
        password = pass;
    }

    /*定义邮件主题*/
    public boolean setSubject(String mailSubject) {
        System.out.println("定义邮件主题！");
        try {
            mimeMsg.setSubject(mailSubject); // TODO 这里有编码问题
            return true;
        } catch (Exception e) {
            System.err.println("定义邮件主题发生错误！");
            return false;
        }
    }

    /*定义邮件正文*/
    public boolean setBody(String mailBody) {
        try {
            BodyPart bp = new MimeBodyPart();
            bp.setContent("" + mailBody, "text/html;charset=GBK"); // TODO 这里有编码问题
            mp.addBodyPart(bp);
            return true;
        } catch (Exception e) {
            System.err.println("定义邮件正文时发生错误！" + e);
            return false;
        }
    }

    /*设置发信人*/
    public boolean setFrom(String from) {
        System.out.println("设置发信人！");
        try {
            mimeMsg.setFrom(new InternetAddress(from)); //发信人
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*定义收信人*/
    public boolean setTo(String to) {
        if (to == null)
            return false;
        System.out.println("定义收信人！");
        try {
            mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*定义抄送人*/
    public boolean setCopyTo(String copyto) {
        if (copyto == null)
            return false;
        try {
            mimeMsg.setRecipients(Message.RecipientType.CC, (Address[]) InternetAddress
                    .parse(copyto));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*发送邮件模块*/
    public boolean sendOut() {
        try {
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            System.out.println("邮件发送中....");
            Session mailSession = Session.getInstance(props, null);
            Transport transport = mailSession.getTransport("smtp");
            transport.connect((String) props.get("mail.smtp.host"), username, password);
            transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
            System.out.println("发送成功！");
            transport.close();
            return true;
        } catch (Exception e) {
            System.err.println("邮件失败！" + e);
            return false;
        }
    }

    /*调用sendOut方法完成发送*/
    public static boolean sendAndCc(String smtp, String from, String to, String copyto,
                                    String subject, String content, String username, String password) throws UnsupportedEncodingException {
        Mail theMail = new Mail(smtp);
        theMail.setNeedAuth(true); // 验证
        if (!theMail.setSubject(MimeUtility.encodeText(subject,MimeUtility.mimeCharset("gb2312"),null)))
            return false;
        if (!theMail.setBody(content))
            return false;
        if (!theMail.setTo(to))
            return false;
        if (!theMail.setCopyTo(copyto))
            return false;
        if (!theMail.setFrom(from))
            return false;
        theMail.setNamePass(username, password);
        if (!theMail.sendOut())
            return false;
        return true;
    }

}
