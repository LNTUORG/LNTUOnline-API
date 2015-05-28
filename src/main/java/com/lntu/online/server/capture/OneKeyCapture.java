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

import com.lntu.online.server.exception.CourseEvaKeyException;
import com.lntu.online.server.exception.RemoteInvokeException;
import com.lntu.online.server.exception.RemoteSecondInvokeException;
import com.lntu.online.server.model.CourseEvaInfo;
import com.lntu.online.server.util.TextUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OneKeyCapture {

    public static List<CourseEvaInfo> getCourseEvaInfoList(String userId, String pwd) {
        Document doc = DocumentShip.document(CaptureConfig.getServerUrl() + "/eva/index/resultlist.jsdo", "\u6559\u5e08\u6559\u5b66\u8bc4\u4ef7", userId, pwd); // Unicode=教师教学评价
        List<CourseEvaInfo> infoList = new ArrayList<CourseEvaInfo>();

        // 连接成功-获取评课列表
        Elements trs = doc.getElementsByTag("table").get(1).child(0).getElementsByTag("tr");
        trs.remove(0);
        for (Element tr : trs) {
            CourseEvaInfo info = new CourseEvaInfo();
            info.setStudentId(userId);
            info.setTeacher(tr.child(0).text());
            String[] courseInfos = tr.child(1).text().replace("(", "\n").replace(")", "").split("\n");
            info.setName(courseInfos[0]);
            info.setNum(courseInfos[1]);
            info.setDone(tr.child(2).text().equals("\u5df2\u8bc4\u4f30")); // Unicode=已评估
            if (!info.isDone()) {
                info.setUrl(tr.child(3).child(0).absUrl("href"));
            }
            infoList.add(info);
        }

        // 返回数据
        return infoList;
    }

    public static void doEva(String userId, String pwd, String url) {
        if (TextUtils.isEmpty(url)) {
            throw new CourseEvaKeyException();
        }

        String cookieValue = CookieShip.value(userId, pwd);

        // 获取评课页面
        Document doc = DocumentShip.document(url, cookieValue);
        if (!(doc.title() + "").equals("\u6559\u5e08\u6559\u5b66\u8bc4\u4ef7")) { // Unicode=教师教学评价
            throw new CourseEvaKeyException();
        }

        // 连接成功-进行评课
        Connection conn = Jsoup.connect(CaptureConfig.getServerUrl() + "/eva/index/putresult.jsdo")
                .cookie(CookieShip.name(), cookieValue)
                .timeout(CaptureConfig.getTimeOut());
        Elements inputs = doc.getElementsByTag("form").get(0).getElementsByTag("input");
        for (int n = 0; n < 8; n++) {
            Element input = inputs.get(n);
            conn.data(input.attr("name"), input.attr("value"));
        }
        for (int n = 8; n < inputs.size() - 2; n+=7) {
            Element input0 = inputs.get(n + 0);
            Element input1 = inputs.get(n + 1);
            Element input2 = inputs.get(n + 2);
            Element input3 = inputs.get(n + 3);
            Element input4 = inputs.get(n + 4);
            conn.data(input0.attr("name"), input0.attr("value"));
            conn.data(input1.attr("name"), input1.attr("value"));
            conn.data(input2.attr("name"), input2.attr("value"));
            conn.data(input3.attr("name"), input3.attr("value"));
            conn.data(input4.attr("name"), input4.attr("value"));
        }
        Document doc2;
        try {
            doc2 = conn.post();
        } catch (IOException e) {
            throw new RemoteInvokeException(e);
        }
        // 二级调用失败
        if (!(doc2.title() + "").equals("\u6559\u5e08\u6559\u5b66\u8bc4\u4ef7")) { // Unicode=教师教学评价
            throw new RemoteSecondInvokeException();
        }
    }

}
