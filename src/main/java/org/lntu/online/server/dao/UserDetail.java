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
import org.lntu.online.server.model.UserType;

import java.util.Date;

public class UserDetail extends Model<UserDetail> {

    public static final UserDetail dao = new UserDetail();

    public String getId() {
        return getStr("id");
    }

    public void setId(String id) {
        set("id", id);
    }

    public String getName() {
        return getStr("name");
    }

    public void setName(String name) {
        set("name", name);
    }

    public String getClassInfo() {
        return getStr("class_info");
    }

    public void setClassInfo(String classInfo) {
        set("class_info", classInfo);
    }

    public String getSex() {
        return getStr("sex");
    }

    public void setSex(String sex) {
        set("sex", sex);
    }

    public String getCollege() {
        return getStr("college");
    }

    public void setCollege(String college) {
        set("college", college);
    }

    public UserType getType() {
        return UserType.valueOf(getStr("type"));
    }

    public void setType(UserType type) {
        set("type", type.name());
    }

    public Date getUpdateAt() {
        return getDate("update_at");
    }

    public void setUpdateAt(Date time) {
        set("update_at", time);
    }

}