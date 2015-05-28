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

import com.lntu.online.server.model.UnpassCourse;
import com.lntu.online.server.util.TextUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class UnpassCourseCapture {

    public static List<UnpassCourse> getUnpassCourseList(String userId, String pwd) {
        Document doc = DocumentShip.documentForGrades(CaptureConfig.getServerUrl() + "/student/unpasscourse/unpasscourse.jsdo", "\u672a\u901a\u8fc7\u8bfe\u7a0b\u67e5\u8be2", userId, pwd); // Unicode=未通过课程查询
        List<UnpassCourse> unpassCourseList = new ArrayList<UnpassCourse>();

        // 构建数据
        Elements trs = doc.getElementsByTag("table").get(2).child(0).getElementsByTag("tr");
        trs.remove(0);
        for (Element tr : trs) {
            if ("\u6b63\u5e38\u8003\u8bd5".equals(tr.child(7).html().replace("&nbsp;", ""))) { // Unicode=正常考试
                UnpassCourse course = new UnpassCourse();
                course.setStudentId(userId);
                course.setNum(tr.child(0).html().replace("&nbsp;", ""));
                course.setName(tr.child(1).html().replace("&nbsp;", ""));
                course.setSerialNum(Integer.valueOf(tr.child(2).html().replace("&nbsp;", "")));
                course.setCredit(Float.valueOf(tr.child(4).html().replace("&nbsp;", "")));
                course.setSelectType(tr.child(8).html().replace("&nbsp;", ""));

                List<UnpassCourse.Record> recordList = new ArrayList<UnpassCourse.Record>();
                for (Element tr2 : trs) { // 二次循环
                    if (course.getNum().equals(tr2.child(0).html().replace("&nbsp;", ""))) {
                        UnpassCourse.Record record = new UnpassCourse.Record();

                        record.setScore(tr2.child(3).html().replace("&nbsp;", ""));
                        if (TextUtils.isEmpty(record.getScore())) {
                            record.setScore("\u65e0\u6210\u7ee9"); // Unicode=无成绩
                        }
                        record.setRemarks(tr2.child(6).html().replace("&nbsp;", ""));
                        record.setExamType(tr2.child(7).html().replace("&nbsp;", ""));
                        String semester = tr2.child(9).html().replace("&nbsp;", "");
                        record.setYear(Integer.parseInt(semester.substring(0, 4)));
                        record.setTerm(semester.contains("\u6625") ? "\u6625" : "\u79cb"); // Unicode=春 # 春 # 秋

                        recordList.add(record);
                    }
                }
                course.setRecords(recordList);

                unpassCourseList.add(course);
            }
        }

        return unpassCourseList;
    }

}
