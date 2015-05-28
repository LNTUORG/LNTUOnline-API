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

package com.lntu.online.server.model;

import com.lntu.online.server.util.coder.Base64;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CourseEvaInfo {

    private String studentId;

    private String teacher;

    private String name;

    private String num;

    private boolean done;

    private String evaKey;

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getEvaKey() {
        return evaKey;
    }

    public void setEvaKey(String evaKey) {
        this.evaKey = evaKey;
    }

    public String getUrl() {
        try {
            return Base64.decode(getEvaKey());
        } catch (Exception e) {
            return "";
        }
    }

    public void setUrl(String url) {
        try {
            setEvaKey(Base64.encode(url));
        } catch (Exception e) {
            setEvaKey("");
        }
    }

    public static String getUrlFormKey(String key) {
        try {
            return Base64.decode(key);
        } catch (Exception e) {
            return "";
        }
    }

}
