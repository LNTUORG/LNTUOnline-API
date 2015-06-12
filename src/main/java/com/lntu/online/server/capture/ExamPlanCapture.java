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

import com.lntu.online.server.model.ExamPlan;
import org.joda.time.DateTime;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExamPlanCapture {

    public static List<ExamPlan> getExamPlanList(String userId, String pwd) {
        Document doc = DocumentShip.document(CaptureConfig.getServerUrl() + "/student/exam/index.jsdo", "\u5b66\u751f\u8003\u8bd5\u5b89\u6392", userId, pwd); // Unicode=学生考试安排
        List<ExamPlan> examList = new ArrayList<ExamPlan>();

        // 构建数据
        Elements trs = doc.getElementsByTag("table").get(1).child(0).getElementsByTag("tr");
        trs.remove(0);
        Date nowTime = new Date();
        for (Element tr : trs) {
            ExamPlan exam = new ExamPlan();
            exam.setStudentId(userId);
            exam.setCourse(tr.child(0).html());
            exam.setLocation(tr.child(2).html().replace(" &nbsp;", ""));

            String[] arr = tr.child(1).html().split(" ");
            String strDate = arr[0];
            String[] arr1 = arr[1].split("--");
            String strStart = arr1[0];
            String strEnd = arr1[1];
            exam.setStartTime(new DateTime(strDate + "T" + strStart + "+08:00").toDate());
            exam.setEndTime(new DateTime(strDate + "T" + strEnd + "+08:00").toDate());

            examList.add(exam);
        }

        // 返回数据
        return examList;
    }

}
