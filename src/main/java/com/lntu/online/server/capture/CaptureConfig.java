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

package com.lntu.online.server.capture;

import com.lntu.online.server.config.AppConfig;
import com.lntu.online.server.util.mail.MailSender;
import org.joda.time.DateTime;

public final class CaptureConfig {

    private static final String[] SERVER_RULS = {
            "http://60.18.131.131:11080/academic",
            "http://60.18.131.131:11081/academic",
            "http://60.18.131.131:11180/academic",    //*
            "http://60.18.131.131:11181/academic",
            "http://60.18.131.131:11080/newacademic", //*
            "http://60.18.131.131:11081/newacademic",
            "http://60.18.131.133:11180/newacademic", //*
            "http://60.18.131.133:11181/newacademic", //*
            // 检测两次
            "http://60.18.131.131:11080/academic",
            "http://60.18.131.131:11081/academic",
            "http://60.18.131.131:11180/academic",    //*
            "http://60.18.131.131:11181/academic",
            "http://60.18.131.131:11080/newacademic", //*
            "http://60.18.131.131:11081/newacademic",
            "http://60.18.131.133:11180/newacademic", //*
            "http://60.18.131.133:11181/newacademic"  //*
    };

    private static int currentUrlIndex = 4;

    private static final int TIME_OUT = 20000;

    public static String getServerUrl() {
        return SERVER_RULS[currentUrlIndex];
    }

    public static int getTimeOut() {
        return TIME_OUT;
    }

    /**
     * TODO 需要重构
     */
    public static String autoFix() {
        System.out.println("-----------------------");
        System.out.println("Capture Config Auto Fix");
        System.out.println("-----------------------");
        StringBuilder sbLog = new StringBuilder();
        int index = 0;
        long minResponseTime = Long.MAX_VALUE;
        for (int n = 0; n < SERVER_RULS.length; n++) {
            String testUrl = SERVER_RULS[n];
            System.out.println("test : " + testUrl);
            sbLog.append("test : ").append(testUrl).append("<br>");
            try {
                long startTime = System.currentTimeMillis();
                CookieShip.value(testUrl, AppConfig.admin.userId, AppConfig.admin.password);
                long endTime = System.currentTimeMillis();
                long responseTime = endTime - startTime;
                System.out.println("response time : " + responseTime + " (ms)");
                sbLog.append("response time : ").append(responseTime).append(" (ms)").append("<br>");
                if (responseTime < minResponseTime) {
                    minResponseTime = responseTime;
                    index = n;
                }
            } catch(Exception e) {
                System.out.println("error : " + e.getLocalizedMessage());
                sbLog.append("error : ").append(e.getLocalizedMessage()).append("<br>");
            }
            System.out.print("\n");
            sbLog.append("<br>");
        }
        System.out.println("---------------");
        if (minResponseTime == Long.MAX_VALUE) {
            System.out.println("Url search failed, please manual check.");
            String content = "维护时间：" + new DateTime().toString() + "<br><br>" + "抓取服务器所有远程地址均不可用，请手动检查。或者1个小时之后系统再次自检";
            MailSender.sendToAdmin(MailSender.LEVEL_LOG, content);
            return content.replace("<br>", "\n");
        }
        else if (currentUrlIndex == index) {
            System.out.println("Do not change url : " + getServerUrl());
            return (sbLog.toString() + "<br><br>" + "Do not change url : " + getServerUrl()).replace("<br>", "\n");
        } else {
            System.out.println("Change url to index " + index + " : " + SERVER_RULS[index]);
            currentUrlIndex = index;
            sbLog.append("<br><br>").append("Change url to index ").append(index).append(" : ").append(getServerUrl());
            String content = "维护时间：" + new DateTime().toString() + "<br><br>" + sbLog.toString();
            MailSender.sendToAdmin(MailSender.LEVEL_LOG, content);
            return content.replace("<br>", "\n");
        }
    }

}
