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
 *
 */

package com.lntu.online.server.config;

import java.util.Properties;

public final class MailConfig {

    public final boolean enable;
    public final String smtp;
    public final String from;
    public final String username;
    public final String password;

    protected MailConfig(Properties properties) {
        enable = Boolean.parseBoolean(properties.getProperty("mail.enable", "false"));
        smtp = properties.getProperty("mail.smtp", "SMTP.lntu.org");
        from = properties.getProperty("mail.from", "online@lntu.org");
        username = properties.getProperty("mail.username", "online@lntu.org");
        password = properties.getProperty("mail.password", "123");
    }

}
