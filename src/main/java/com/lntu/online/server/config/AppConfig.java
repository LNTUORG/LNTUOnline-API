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

package com.lntu.online.server.config;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public final class AppConfig {

    private static final String CONFIG_NAME = "config.properties";

    private static final Properties properties;

    public static final String secretKey;
    public static final DbConfig db;
    public static final MailConfig mail;
    public static final AdminConfig admin;

    static {
        try {
            properties = new Properties();
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_NAME));
            secretKey = properties.getProperty("secretKey", "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
            db = new DbConfig(properties);
            mail = new MailConfig(properties);
            admin = new AdminConfig(properties);
        } catch (IOException e) {
            throw new RuntimeException(CONFIG_NAME + " load faild.", e);
        }
    }

    private AppConfig() {}

    public static Date getFirstWeekMonday(int year, String term) {
        return new DateTime(properties.getProperty("firstWeekMonday." + year + "_" + ("春".equals(term) ? 1 : 2), "2000-01-01T00:00:00.000+08:00")).toDate();
    }

}
