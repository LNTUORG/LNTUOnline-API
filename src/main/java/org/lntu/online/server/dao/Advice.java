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

import java.util.Date;

public class Advice extends Model<Advice> {

    public static final Advice dao = new Advice();

    public String getId() {
        return getStr("id");
    }

    public void setId(String id) {
        set("id", id);
    }

    public String getUserId() {
        return getStr("user_id");
    }

    public void setUserId(String userId) {
        set("user_id", userId);
    }

    public Date getCreateAt() {
        return getDate("create_at");
    }

    public void setCreateAt(Date time) {
        set("create_at", time);
    }

    public String getUserAgent() {
        return getStr("user_agent");
    }

    public void setUserAgent(String userAgent) {
        set("user_agent", userAgent);
    }

    public String getContent() {
        return getStr("content");
    }

    public void setContent(String content) {
        set("content", content);
    }

}
