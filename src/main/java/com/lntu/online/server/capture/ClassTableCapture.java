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

import com.lntu.online.server.config.AppConfig;
import com.lntu.online.server.exception.ArgsErrorException;
import com.lntu.online.server.model.ClassTable;
import com.lntu.online.server.model.DayInWeek;
import com.lntu.online.server.util.TextUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class ClassTableCapture {

    public static ClassTable getClassTable(String userId, String pwd, int year, String term) {
        int termInt;
        if ("\u6625".equals(term)) { // Unicode=春
            termInt = 1;
        }
        else if ("\u79cb".equals(term)) { // Unicode=秋
            termInt = 2;
        } else {
            throw new ArgsErrorException("Term is not '\u6625' or '\u79cb'."); // Unicode=春 # 秋
        }
        Document doc = DocumentShip.document(CaptureConfig.getServerUrl() + "/student/currcourse/currcourse.jsdo?year=" + (year - 1980) + "&term=" + termInt, "\u672c\u5b66\u671f\u8bfe\u7a0b\u5b89\u6392", userId, pwd); // Unicode=本学期课程安排
        ClassTable classTable = new ClassTable();
        classTable.setStudentId(userId);
        classTable.setYear(year);
        classTable.setTerm(term);
        classTable.setFirstWeekMondayAt(AppConfig.firstWeekMondayAt);

        List<ClassTable.Course> courseList = new ArrayList<ClassTable.Course>();

        // 开始解析
        Elements elements = doc.getElementsByTag("table").get(3).child(0).children();
        elements.remove(0);
        for (Element element : elements) {
            ClassTable.Course course = new ClassTable.Course();

            course.setNum(element.child(0).text());
            course.setSerialNum(Integer.parseInt(element.child(1).text()));
            course.setName(element.child(2).child(0).text());
            course.setTeacher(element.child(3).text());
            course.setCredit(Float.parseFloat(element.child(4).text()));
            course.setSelectType(element.child(5).text());
            course.setTestMode(element.child(6).text());
            course.setExamType(element.child(7).html().replace("&nbsp;", "").replace("<font color=\"#FF0000\">\u672a\u4ea4\u8d39</font>", "")); // 这里包含重修未交费信息 Unicode=未交费
            // child(8) 是否缓考，忽略

            // 时间位置列表
            List<ClassTable.TimeAndPlace> timeAndPlaceList = new ArrayList<ClassTable.TimeAndPlace>();
            for (Element tr : element.child(9).child(0).child(0).children()) {
                // 基本参数
                int startWeek;
                int endWeek;
                String room;
                DayInWeek dayInWeek;
                // 始终周数
                String weekSE = tr.child(0).text();
                if ("-".equals(weekSE) || TextUtils.isEmpty(weekSE)) { // 地点未定
                    continue;
                }
                String[] weeks = weekSE.split("-");
                if (weeks.length == 2) { // 包含始终
                    startWeek = Integer.parseInt(weeks[0]);
                    endWeek = Integer.parseInt(weeks[1]);
                } else { // 只有一个数字
                    startWeek = Integer.parseInt(weeks[0]);
                    endWeek = Integer.parseInt(weeks[0]);
                }
                // 星期几
                String strDayInWeek = tr.child(1).text();
                if ("\u5468\u4e00".equals(strDayInWeek)) { // Unicode=周一
                    dayInWeek = DayInWeek.Monday;
                } else if ("\u5468\u4e8c".equals(strDayInWeek)) { // Unicode=周二
                    dayInWeek = DayInWeek.Tuesday;
                } else if ("\u5468\u4e09".equals(strDayInWeek)) { // Unicode=周三
                    dayInWeek = DayInWeek.Wednesday;
                } else if ("\u5468\u56db".equals(strDayInWeek)) { // Unicode=周四
                    dayInWeek = DayInWeek.Thursday;
                } else if ("\u5468\u4e94".equals(strDayInWeek)) { // Unicode=周五
                    dayInWeek = DayInWeek.Friday;
                } else if ("\u5468\u516d".equals(strDayInWeek)) { // Unicode=周六
                    dayInWeek = DayInWeek.Saturday;
                } else
                    dayInWeek = DayInWeek.Sunday;
                // 地点
                room = tr.child(3).text();
                // 大节课程
                String strStage = tr.child(2).text();
                if (strStage.contains("\u4e00")) { // Unicode=一
                    ClassTable.TimeAndPlace timeAndPlace = new ClassTable.TimeAndPlace();
                    timeAndPlace.setStartWeek(startWeek);
                    timeAndPlace.setEndWeek(endWeek);
                    timeAndPlace.setDayInWeek(dayInWeek);
                    timeAndPlace.setRoom(room);
                    timeAndPlace.setStage(1);
                    timeAndPlaceList.add(timeAndPlace);
                }
                if (strStage.contains("\u4e8c")) { // Unicode=二
                    ClassTable.TimeAndPlace timeAndPlace = new ClassTable.TimeAndPlace();
                    timeAndPlace.setStartWeek(startWeek);
                    timeAndPlace.setEndWeek(endWeek);
                    timeAndPlace.setDayInWeek(dayInWeek);
                    timeAndPlace.setRoom(room);
                    timeAndPlace.setStage(2);
                    timeAndPlaceList.add(timeAndPlace);
                }
                if (strStage.contains("\u4e09")) { // Unicode=三
                    ClassTable.TimeAndPlace timeAndPlace = new ClassTable.TimeAndPlace();
                    timeAndPlace.setStartWeek(startWeek);
                    timeAndPlace.setEndWeek(endWeek);
                    timeAndPlace.setDayInWeek(dayInWeek);
                    timeAndPlace.setRoom(room);
                    timeAndPlace.setStage(3);
                    timeAndPlaceList.add(timeAndPlace);
                }
                if (strStage.contains("\u56db")) { // Unicode=四
                    ClassTable.TimeAndPlace timeAndPlace = new ClassTable.TimeAndPlace();
                    timeAndPlace.setStartWeek(startWeek);
                    timeAndPlace.setEndWeek(endWeek);
                    timeAndPlace.setDayInWeek(dayInWeek);
                    timeAndPlace.setRoom(room);
                    timeAndPlace.setStage(4);
                    timeAndPlaceList.add(timeAndPlace);
                }
                if (strStage.contains("\u4e94")) { // Unicode=五
                    ClassTable.TimeAndPlace timeAndPlace = new ClassTable.TimeAndPlace();
                    timeAndPlace.setStartWeek(startWeek);
                    timeAndPlace.setEndWeek(endWeek);
                    timeAndPlace.setDayInWeek(dayInWeek);
                    timeAndPlace.setRoom(room);
                    timeAndPlace.setStage(5);
                    timeAndPlaceList.add(timeAndPlace);
                }
            }
            course.setTimesAndPlaces(timeAndPlaceList);

            courseList.add(course);
        }

        classTable.setCourses(courseList);

        return classTable;
    }

}
