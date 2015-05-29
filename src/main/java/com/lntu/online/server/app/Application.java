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
import com.lntu.online.server.dao.User;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

@ApplicationPath("/*")
public class Application extends ResourceConfig {

    private static final String ROOT_PACKAGES = "com.lntu.online.server";

    public Application() {
        packages(ROOT_PACKAGES);
        register(LoggingFilter.class);

        C3p0Plugin c3p0Plugin = new C3p0Plugin(AppConfig.db.jdbcUrl, AppConfig.db.username, AppConfig.db.password);
        c3p0Plugin.setDriverClass(AppConfig.db.driverClass);
        c3p0Plugin.start();
        ActiveRecordPlugin activeRecordPlugin = new ActiveRecordPlugin(c3p0Plugin);
        activeRecordPlugin.setDialect(new MysqlDialect());
        activeRecordPlugin.addMapping("user", User.class);
        activeRecordPlugin.start();
    }

}
