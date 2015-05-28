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

package com.lntu.online.server.capture;

import com.lntu.online.server.exception.RemoteAuthorizedException;
import com.lntu.online.server.exception.RemoteInvokeException;
import com.lntu.online.server.util.TextUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;

public class CookieShip {

    private static final String JSESSIONID = "JSESSIONID";

    public static String name() {
        return JSESSIONID;
    }

    public static String value(String userId, String password) {
        return value(CaptureConfig.getServerUrl(), userId, password);
    }

    protected static String value(String baseUrl, String userId, String password) throws WebApplicationException {
        // 连接远程服务器
        Connection.Response res;
        try {
            res = Jsoup.connect(baseUrl + "/j_acegi_security_check")
                    .data("j_username", userId)
                    .data("j_password", password)
                    .method(Connection.Method.POST)
                    .timeout(CaptureConfig.getTimeOut())
                    .execute();
        } catch (IOException e) {
            throw new RemoteInvokeException(e);
        }
        // 解析DOM
        Document doc;
        try {
            doc = res.parse();
        } catch (IOException e) {
            throw new RemoteInvokeException(e);
        }
        // 判断登录状态
        if (!(doc.title()+"").equals("\u672c\u79d1\u751f\u6559\u52a1\u7ba1\u7406\u7cfb\u7edf")) { // unicode=本科生教务管理系统
            throw new RemoteAuthorizedException();
        }
        // cookie空指针异常
        String cookieValue = res.cookie(name());
        if (TextUtils.isEmpty(cookieValue)) {
            throw new RemoteInvokeException("Remote cookie value is null.");
        }
        // 登录成功
        return cookieValue;
    }

}
