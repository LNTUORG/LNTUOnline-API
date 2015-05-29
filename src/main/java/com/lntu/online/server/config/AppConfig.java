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

    public static final Date firstWeekMondayAt;
    public static final String secretKey;
    public static final DbConfig db;
    public static final MailConfig mail;

    static {
        try {
            Properties properties = new Properties();
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_NAME));
            firstWeekMondayAt = new DateTime(properties.getProperty("firstWeekMondayAt", "2015-3-9")).toDate();
            secretKey = properties.getProperty("secretKey", "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
            db = new DbConfig(properties);
            mail = new MailConfig(properties);
        } catch (IOException e) {
            throw new RuntimeException(CONFIG_NAME + " load faild.", e);
        }
    }

    private AppConfig() {}

}
