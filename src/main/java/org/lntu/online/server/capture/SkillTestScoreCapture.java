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

package org.lntu.online.server.capture;

import org.lntu.online.server.exception.RemoteInvokeException;
import org.lntu.online.server.exception.RemoteSecondInvokeException;
import org.lntu.online.server.exception.ResourceNotFoundException;
import org.lntu.online.server.model.SkillTestScore;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SkillTestScoreCapture {

    public static List<SkillTestScore> getSkillTestScoreList(String userId, String pwd) {
        Document doc;
        try {
            doc = Jsoup.connect(CaptureConfig.getServerUrl() + "/student/queryscore/skilltestscore.jsdo")
                    .cookie(CookieShip.name(), CookieShip.value(userId, pwd))
                    .timeout(CaptureConfig.getTimeOut())
                    .get();
        } catch (HttpStatusException e) {
            if (e.getStatusCode() == 500) { // 暂无数据
                throw new ResourceNotFoundException();
            } else {
                throw new RemoteInvokeException(e); // 远程调用失败
            }
        } catch (IOException e) { // 远程调用失败
            throw new RemoteInvokeException(e);
        }
        // 二级调用
        if (!(doc.title() + "").equals("\u7b49\u7ea7\u8003\u8bd5\u6210\u7ee9\u67e5\u8be2")) { // Unicode=等级考试成绩查询
            throw new RemoteSecondInvokeException();
        }
        // 连接成功-构建数据
        List<SkillTestScore> scoreList = new ArrayList<SkillTestScore>();
        Elements trs = doc.getElementsByTag("table").get(1).child(0).getElementsByTag("tr");
        trs.remove(0);
        Date nowTime = new Date();
        for (Element tr : trs) {
            SkillTestScore score = new SkillTestScore();
            score.setStudentId(userId);
            score.setName(tr.child(0).html().replace("&nbsp;", ""));
            score.setTime(tr.child(1).html().replace("&nbsp;", ""));
            score.setScore(tr.child(2).html().replace("&nbsp;", ""));
            scoreList.add(score);
        }
        // 返回数据
        return scoreList;
    }

}
