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

package com.lntu.online.server.capture;

import com.lntu.online.server.exception.NotEvaluateException;
import com.lntu.online.server.exception.RemoteInvokeException;
import com.lntu.online.server.exception.RemoteSecondInvokeException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DocumentShip {

    public static Document document(String url, String cookieValue) {
        try {
            return Jsoup.connect(url)
                    .cookie(CookieShip.name(), cookieValue)
                    .timeout(CaptureConfig.getTimeOut())
                    .get();
        } catch (IOException e) {
            throw new RemoteInvokeException(e);
        }
    }

    public static Document document(String url, String userId, String pwd) {
        return document(url, CookieShip.value(userId, pwd));
    }

    public static Document document(String url, String title, String userId, String pwd) {
        Document doc = document(url, userId, pwd);
        if (!(doc.title() + "").equals(title)) {
            throw new RemoteSecondInvokeException();
        }
        return doc;
    }

    public static Document documentForGrades(String url, String cookieValue) {
        try {
            return Jsoup.connect(url)
                    .cookie(CookieShip.name(), cookieValue)
                    .timeout(CaptureConfig.getTimeOut())
                    .get();
        } catch (IOException e) {
            throw new NotEvaluateException();
        }
    }

    public static Document documentForGrades(String url, String userId, String pwd) {
        return documentForGrades(url, CookieShip.value(userId, pwd));
    }

    public static Document documentForGrades(String url, String title, String userId, String pwd) {
        Document doc = documentForGrades(url, userId, pwd);
        if (!(doc.title() + "").equals(title)) {
            throw new RemoteSecondInvokeException();
        }
        return doc;
    }

}
