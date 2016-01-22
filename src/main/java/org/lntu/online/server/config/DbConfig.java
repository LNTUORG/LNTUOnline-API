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

package org.lntu.online.server.config;

import java.util.Properties;

public final class DbConfig {

    public final String jdbcUrl;
    public final String driverClass;
    public final String username;
    public final String password;

    protected DbConfig(Properties properties) {
        jdbcUrl = properties.getProperty("db.jdbcUrl", "jdbc:mysql://localhost:3306/lntuonline");
        driverClass = properties.getProperty("db.driverClass", "com.mysql.jdbc.Driver");
        username = properties.getProperty("db.username", "root");
        password = properties.getProperty("db.password", "123");
    }

}
