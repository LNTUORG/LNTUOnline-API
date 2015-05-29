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

import com.lntu.online.server.model.Student;
import com.lntu.online.server.util.TimeUtils;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class StudentCapture {

    public static Student getStudent(String userId, String pwd) {
        Document doc = DocumentShip.document(CaptureConfig.getServerUrl() + "/student/studentinfo/studentinfo.jsdo", "\u5b66\u7c4d\u7ba1\u7406", userId, pwd); // Unicode=学籍管理
        Student student = new Student();

        // 开始解析
        Elements tables = doc.getElementsByTag("table");

        // #基本信息#
        Elements trs1 = tables.get(1).child(0).getElementsByTag("tr");

        student.setId(trs1.get(0).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setNationality(trs1.get(0).child(3).html().replace("&nbsp;", "").replace(" ", ""));
        student.setPhotoUrl(trs1.get(0).child(4).getElementsByTag("img").get(0).absUrl("src").replace(" ", ""));

        student.setName(trs1.get(1).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setBirthplace(trs1.get(1).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setEnglishName(trs1.get(2).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setBirthday(TimeUtils.getTime(trs1.get(2).child(3).html().replace("&nbsp;", "").replace(" ", "")));

        student.setIdCardType(trs1.get(3).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setPoliticalAffiliation(trs1.get(3).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setIdCardNum(trs1.get(4).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setTravelRange(trs1.get(4).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setSex(trs1.get(5).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setNation(trs1.get(5).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setCollege(trs1.get(6).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setMajor(trs1.get(6).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setClassInfo(trs1.get(7).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setStudentType(trs1.get(7).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setEntranceExamArea(trs1.get(8).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setEntranceExamScore(trs1.get(8).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setEntranceExamNum(trs1.get(9).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setGraduateSchool(trs1.get(9).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setForeignLanguage(trs1.get(10).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setAdmissionNum(trs1.get(10).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setAdmissionTime(TimeUtils.getTime(trs1.get(11).child(1).html().replace("&nbsp;", "").replace(" ", "")));
        student.setAdmissionType(trs1.get(11).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setGraduationTime(TimeUtils.getTime(trs1.get(12).child(1).html().replace("&nbsp;", "").replace(" ", "")));
        student.setEducationType(trs1.get(12).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setHomeAddress(trs1.get(13).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setZipCode(trs1.get(13).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setTel(trs1.get(14).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setEmail(trs1.get(14).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setStudentInfoTableNum(trs1.get(15).child(1).html().replace("&nbsp;", "").replace(" ", ""));
        student.setSourceOfStudent(trs1.get(15).child(3).html().replace("&nbsp;", "").replace(" ", ""));

        student.setWhereaboutsAftergraduation(trs1.get(16).child(1).html().replace("&nbsp;", "").replace(" ", ""));

        student.setRemarks(trs1.get(17).child(1).child(0).html());

        // #高考科目#
        Elements trs2 = tables.get(3).child(0).getElementsByTag("tr");
        trs2.remove(0);
        List<Student.EntranceExam> exams = new ArrayList<Student.EntranceExam>();
        for (int n = 0; n < trs2.size(); n++) {
            Student.EntranceExam exam = new Student.EntranceExam();
            exam.setName(trs2.get(n).child(0).html().replace("&nbsp;", "").replace(" ", ""));
            exam.setScore(trs2.get(n).child(1).html().replace("&nbsp;", "").replace(" ", ""));
            exams.add(exam);
        }
        student.setEntranceExams(exams);

        // #教育经历#
        Elements trs3 = tables.get(5).child(0).getElementsByTag("tr");
        trs3.remove(0);
        List<Student.EducationExperience> expers = new ArrayList<Student.EducationExperience>();
        for (int n = 0; n < trs3.size(); n++) {
            Student.EducationExperience exper = new Student.EducationExperience();
            exper.setStartTime(TimeUtils.getTime(trs3.get(n).child(0).html().replace("&nbsp;", "").replace(" ", "")));
            exper.setEndTime(TimeUtils.getTime(trs3.get(n).child(1).html().replace("&nbsp;", "").replace(" ", "")));
            exper.setSchoolInfo(trs3.get(n).child(2).html().replace("&nbsp;", "").replace(" ", ""));
            exper.setWitness(trs3.get(n).child(3).html().replace("&nbsp;", "").replace(" ", ""));
            expers.add(exper);
        }
        student.setEducationExperiences(expers);

        // #家庭情况#
        Elements trs4 = tables.get(7).child(0).getElementsByTag("tr");
        trs4.remove(0);
        List<Student.Family> familys = new ArrayList<Student.Family>();
        for (int n = 0; n < trs4.size(); n++) {
            Student.Family family = new Student.Family();
            family.setName(trs4.get(n).child(0).html().replace("&nbsp;", "").replace(" ", ""));
            family.setRelationship(trs4.get(n).child(1).html().replace("&nbsp;", "").replace(" ", ""));
            family.setPoliticalAffiliation(trs4.get(n).child(2).html().replace("&nbsp;", "").replace(" ", ""));
            family.setJob(trs4.get(n).child(3).html().replace("&nbsp;", "").replace(" ", ""));
            family.setPost(trs4.get(n).child(4).html().replace("&nbsp;", "").replace(" ", ""));
            family.setWorkLocation(trs4.get(n).child(5).html().replace("&nbsp;", "").replace(" ", ""));
            family.setTel(trs4.get(n).child(6).html().replace("&nbsp;", "").replace(" ", ""));
            familys.add(family);
        }
        student.setFamilys(familys);

        // #处罚情况 - 这个不一定有，要判断#
        if (tables.size() >= 10) {
            Elements trs5 = tables.get(9).child(0).getElementsByTag("tr");
            trs5.remove(0);
            List<Student.DisciplinaryAction> actions = new ArrayList<Student.DisciplinaryAction>();
            for (int n = 0; n < trs5.size(); n++) {
                Student.DisciplinaryAction action = new Student.DisciplinaryAction();
                action.setLevel(trs5.get(n).child(0).html().replace("&nbsp;", "").replace(" ", ""));
                action.setCreateTime(TimeUtils.getTime(trs5.get(n).child(1).html().replace("&nbsp;", "").replace(" ", "")));
                action.setCreateReason(trs5.get(n).child(2).html().replace("&nbsp;", "").replace(" ", ""));
                action.setCancelTime(TimeUtils.getTime(trs5.get(n).child(3).html().replace("&nbsp;", "").replace(" ", "")));
                action.setCancelReason(trs5.get(n).child(4).html().replace("&nbsp;", "").replace(" ", ""));
                action.setState(trs5.get(n).child(5).html().replace("&nbsp;", "").replace(" ", ""));
                action.setRemarks(trs5.get(n).child(6).html().replace("&nbsp;", "").replace(" ", ""));
                actions.add(action);
            }
            student.setDisciplinaryActions(actions);
        }

        // 返回数据
        return student;
    }

}
