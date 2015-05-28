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

package com.lntu.online.server.dao;

import com.jfinal.plugin.activerecord.Model;
import com.lntu.online.server.app.AppConfig;
import com.lntu.online.server.model.UserType;
import com.lntu.online.server.util.TextUtils;
import com.lntu.online.server.util.crypto.DES3;
import com.lntu.online.server.util.digest.MD5;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class User extends Model<User> {

    public static final User dao = new User();

    public User findByLoginToken(String loginToken) {
        if (TextUtils.isEmpty(loginToken)) {
            return null;
        } else {
            return dao.findFirst("select * from user where login_token = ?", MD5.getMessageDigest(loginToken));
        }
    }

    public User findByIdWithUserTypeAdmin(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return null;
        } else {
            return dao.findFirst("select * from user where id = ? and type = ?", userId, UserType.ADMINISTRATOR.name());
        }
    }

    public String getId() {
        return getStr("id");
    }

    public void setId(String id) {
        set("id", id);
    }

    public String getLoginTokenMD5() {
        return getStr("login_token");
    }

    public void setLoginToken(String loginToken) {
        set("login_token", MD5.getMessageDigest(loginToken));
    }

    public String getPassword() {
        try {
            return DES3.decrypt(AppConfig.getSecretKey(), getStr("password"));
        } catch (Exception e) {
            return "";
        }
    }

    public void setPassword(String password) {
        try {
            set("password", DES3.encrypt(AppConfig.getSecretKey(), password));
        } catch (Exception e) {
            set("password", "");
        }
    }

    public boolean verifyPassword(String password) {
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(getPassword())) {
            return false;
        } else {
            return getPassword().equals(password);
        }
    }

    public UserType getType() {
        return UserType.valueOf(getStr("type"));
    }

    public void setType(UserType type) {
        set("type", type.name());
    }

    public Date getCreateAt() {
        return getDate("create_at");
    }

    public void setCreateAt(Date time) {
        set("create_at", time);
    }

    public Date getUpdateAt() {
        return getDate("update_at");
    }

    public void setUpdateAt(Date time) {
        set("update_at", time);
    }

    public Date getExpiresAt() {
        return getDate("expires_at");
    }

    public void setExpiresAt(Date time) {
        set("expires_at", time);
    }

    public boolean isExpires() {
        return new Date().after(getExpiresAt());
    }

    public String renewLoginToken() {
        String loginToken = UUID.randomUUID().toString();
        setLoginToken(loginToken);
        Calendar time = Calendar.getInstance();
        setUpdateAt(time.getTime());
        time.add(Calendar.MONTH, 1);
        setExpiresAt(time.getTime());
        return loginToken;
    }

}
