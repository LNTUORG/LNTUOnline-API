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

package com.lntu.online.server.dao;

import com.lntu.online.server.config.AppConfig;
import com.lntu.online.server.util.codec.DES3;
import com.lntu.online.server.util.codec.Digest;
import com.lntu.online.server.util.gson.GsonWrapper;
import com.sun.istack.internal.Nullable;

import java.util.Date;
import java.util.UUID;

public class LoginTokenInfo {

    @Nullable
    public static LoginTokenInfo from(String loginToken) {
        try {
            return GsonWrapper.gson.fromJson(DES3.decrypt(Digest.MD5.getMessage(AppConfig.secretKey), loginToken), LoginTokenInfo.class);
        } catch (Exception e) {
            return null;
        }
    }

    public static String buildLoginToken(User user, Date expiresAt) {
        LoginTokenInfo loginTokenInfo = new LoginTokenInfo();
        loginTokenInfo.setUUID(UUID.randomUUID().toString());
        loginTokenInfo.setUserId(user.getId());
        loginTokenInfo.setPasswordMd5(user.getPasswordMd5());
        loginTokenInfo.setExpiresAt(expiresAt);
        try {
            return DES3.encrypt(Digest.MD5.getMessage(AppConfig.secretKey), GsonWrapper.gson.toJson(loginTokenInfo));
        } catch (Exception e) {
            return null;
        }
    }

    private String uuid;

    private String userId;

    private String passwordMd5;

    private Date expiresAt;

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasswordMd5() {
        return passwordMd5;
    }

    public void setPasswordMd5(String passwordMd5) {
        this.passwordMd5 = passwordMd5;
    }

    public Date getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Date expiresAt) {
        this.expiresAt = expiresAt;
    }

}
