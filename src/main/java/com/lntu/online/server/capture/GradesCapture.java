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

import com.lntu.online.server.model.Grades;
import com.lntu.online.server.util.TextUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class GradesCapture {

    public static Grades getGrades(String userId, String pwd) {
        Document doc = DocumentShip.documentForGrades(CaptureConfig.getServerUrl() + "/student/queryscore/queryscore.jsdo", "\u8bfe\u7a0b\u6210\u7ee9\u67e5\u8be2", userId, pwd); // Unicode=课程成绩查询
        Grades grades = new Grades();
        grades.setStudentId(userId);

        // 平均学分绩
        {
            Grades.AverageCredit averageCredit = new Grades.AverageCredit();
            averageCredit.setStudentId(userId);

            Element ele = doc.getElementsByTag("table").get(1).child(0).child(0).child(2);
            ele.children().remove();
            String strValue = ele.html().replace("&nbsp;", "")
                    .replace("\u4f60\u83b7\u5f97\u7684\u5e73\u5747\u5b66\u5206\u7ee9\u662f", "") // Unicode=你获得的平均学分绩是
                    .replace("\uff0c\u7edf\u8ba1\u65f6\u95f4\u4e3a\u6bcf\u5b66\u671f\u7b2c\u0034\u5468\u3002", ""); // Unicode=，统计时间为每学期第4周。
            if (strValue == null || strValue.equals("")) {
                averageCredit.setValue(0);
                averageCredit.setSummary("\u6682\u65f6\u65e0\u5e73\u5747\u5b66\u5206\u7ee9\u4fe1\u606f"); // Unicode=暂时无平均学分绩信息
            } else {
                averageCredit.setValue(Float.parseFloat(strValue));
                averageCredit.setSummary("\u60a8\u5f53\u524d\u7684\u5e73\u5747\u5b66\u5206\u7ee9\u4e3a\uff1a" + strValue); // Unicode=您当前的平均学分绩为：
            }
            grades.setAverageCredit(averageCredit);
        }

        // 成绩列表
        {
            List<Grades.CourseScore> scoreList = new ArrayList<Grades.CourseScore>();

            Elements trs = doc.getElementsByTag("table").get(2).child(0).getElementsByTag("tr");
            trs.remove(0);
            Date nowTime = new Date();
            Set<String> keySet = new HashSet<String>();
            for (Element tr : trs) {

                // 校验重复
                String semester = tr.child(9).html().replace("&nbsp;", "");
                int year = Integer.parseInt(semester.substring(0, 4));
                String term = semester.substring(4, 5);
                String num = tr.child(0).html().replace("&nbsp;", "");
                String examType = tr.child(8).html().replace("&nbsp;", "");

                String key = num + year + term + examType;
                if (keySet.contains(key)) {
                    continue;
                }
                keySet.add(key);

                // 构建
                Grades.CourseScore score = new Grades.CourseScore();

                score.setStudentId(userId);
                score.setNum(num);
                score.setYear(year);
                score.setTerm(term);
                score.setExamType(examType);

                score.setName(tr.child(1).html().replace("&nbsp;", ""));
                score.setSerialNum(Integer.valueOf(tr.child(2).html().replace("&nbsp;", "")));

                // 检测没有得分的情况
                String strScore = tr.child(3).html().replace("<font color=\"#CC0000\"> ", "").replace(" </font>", "").replace("&nbsp;", "");
                score.setScore(TextUtils.isEmpty(strScore) ? "\u65e0\u6210\u7ee9" : strScore); // Unicode=无成绩

                score.setCredit(Float.valueOf(tr.child(4).html().replace("&nbsp;", "")));
                score.setTestMode(tr.child(5).html().replace("&nbsp;", ""));
                score.setSelectType(tr.child(6).html().replace("&nbsp;", ""));
                score.setRemarks(tr.child(7).html().replace("&nbsp;", ""));

                // 分数等级判断
                try { // 分数是得分类
                    float s = Float.parseFloat(score.getScore());
                    if (s >= 90) {
                        score.setLevel(Grades.Level.GREAT);
                    } else if (s >= 60) {
                        score.setLevel(Grades.Level.NORMAL);
                    } else {
                        score.setLevel(Grades.Level.UNPASS);
                    }
                } catch (Exception e) { // 分数是文字类
                    if ("\u4f18".equals(score.getScore()) || "\u4f18\u79c0".equals(score.getScore()) // Unicode=优 # 优秀
                            || "\u4e0a".equals(score.getScore()) || "\u597d".equals(score.getScore())) { // Unicode=上 # 好
                        score.setLevel(Grades.Level.GREAT);
                    } else if ("\u5dee".equals(score.getScore()) || "\u4e0b".equals(score.getScore()) // Unicode=差 # 下
                            || "\u4e0d\u53ca\u683c".equals(score.getScore()) || "\u4e0d\u5408\u683c".equals(score.getScore()) // Unicode=不及格 # 不合格
                            || "\u65e0\u6210\u7ee9".equals(score.getScore()) || TextUtils.isEmpty(score.getScore())) { // Unicode=无成绩
                        score.setLevel(Grades.Level.UNPASS);
                    } else {
                        score.setLevel(Grades.Level.NORMAL);
                    }
                }

                scoreList.add(score);
            }

            grades.setCourseScores(scoreList);
        }

        return grades;
    }

}
