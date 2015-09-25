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

package com.lntu.online.server.util.mail;

import com.lntu.online.server.config.AppConfig;
import com.lntu.online.server.dao.Admin;
import com.lntu.online.server.util.ThreadUtils;

import java.util.List;

public class MailSender {

    public static final String LEVEL_RUNNING = "【教务在线2.0】运行日志";
    public static final String LEVEL_LOG = "【教务在线2.0】维护日志";
    public static final String LEVEL_ALARM = "【教务在线2.0】系统警报";
    public static final String LEVEL_CRASH = "【教务在线2.0】客户端异常";

    public static void send(String to, String subject, String content) {
        if (AppConfig.mail.enable) {
            Mail.sendAndCc(AppConfig.mail.smtp, AppConfig.mail.from, to, "", subject, content, AppConfig.mail.username, AppConfig.mail.password);
        }
    }

    public static void sendToAdmin(final String level, final String content) {
        if (AppConfig.mail.enable) {
            ThreadUtils.execute(new Runnable() {

                @Override
                public void run() {
                    List<Admin> adminList = Admin.dao.findBySubscribe();
                    for (Admin admin : adminList) {
                        send(admin.getEmail(), level, content);
                    }
                }

            });
        }
    }

}
