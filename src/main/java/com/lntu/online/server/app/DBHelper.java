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

package com.lntu.online.server.app;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.lntu.online.server.config.AppConfig;
import com.lntu.online.server.dao.Admin;
import com.lntu.online.server.dao.User;

public final class DBHelper {

    private DBHelper() {}

    private static C3p0Plugin c3p0Plugin;
    private static ActiveRecordPlugin activeRecordPlugin;

    public static void start() {
        c3p0Plugin = new C3p0Plugin(AppConfig.db.jdbcUrl, AppConfig.db.username, AppConfig.db.password);
        c3p0Plugin.setDriverClass(AppConfig.db.driverClass);
        c3p0Plugin.start();
        activeRecordPlugin = new ActiveRecordPlugin(c3p0Plugin);
        activeRecordPlugin.setDialect(new MysqlDialect());
        activeRecordPlugin.addMapping("user", User.class);
        activeRecordPlugin.addMapping("admin", Admin.class);
        activeRecordPlugin.start();
    }

    public static void stop() {
        // activeRecordPlugin.stop();
        // c3p0Plugin.stop();
    }

}
