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

package com.lntu.online.server.app;

import com.lntu.online.server.util.digest.SHA256;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

public final class AppConfig {

    private volatile static AppConfig singleton;

    private static AppConfig getInstance() {
        if (singleton == null) {
            synchronized (AppConfig.class) {
                if (singleton == null) {
                    singleton = new AppConfig();
                }
            }
        }
        return singleton;
    }

    private static final String CONFIG_NAME = "config.properties";

    private Properties properties;

    private AppConfig() {
        try {
            properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(CONFIG_NAME));
        } catch (IOException e) {
            throw new RuntimeException(CONFIG_NAME + " load faild.", e);
        }
    }

    public static String getJdbcUrl() {
        return getInstance().properties.getProperty("jdbcUrl");
    }

    public static String getUsername() {
        return getInstance().properties.getProperty("username");
    }

    public static String getPassword() {
        return getInstance().properties.getProperty("password");
    }

    public static String getDriverClass() {
        return getInstance().properties.getProperty("driverClass");
    }

    public static String getSecretKey() {
        return SHA256.getMessageDigest(getInstance().properties.getProperty("secretKey"));
    }

    public static Date getFirstWeekMondayAt() {
        return new DateTime(getInstance().properties.getProperty("firstWeekMondayAt", "2000-1-1")).toDate();

    }

}
