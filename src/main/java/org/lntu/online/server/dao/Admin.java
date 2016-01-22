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

package org.lntu.online.server.dao;

import com.jfinal.plugin.activerecord.Model;

import java.util.List;

public class Admin extends Model<Admin> {

    public static final Admin dao = new Admin();

    public Admin findByUserId(String userId) {
        return dao.findById(userId, "user_id");
    }

    public List<Admin> findBySubscribe() {
        return dao.find("select * from admin where subscribe = ?", 1);
    }

    public String getUserId() {
        return getStr("user_id");
    }

    public void setUserId(String userId) {
        set("user_id", userId);
    }

    public String getEmail() {
        return getStr("email");
    }

    public void setEmail(String email) {
        set("email", email);
    }

    public boolean isSubscribe() {
        return getBoolean("subscribe");
    }

    public void setSubscribe(boolean b) {
        set("subscribe", b);
    }

}
