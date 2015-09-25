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

package com.lntu.online.server.resource;

import com.lntu.online.server.app.Authorization;
import com.lntu.online.server.capture.StudentCapture;
import com.lntu.online.server.dao.Advice;
import com.lntu.online.server.dao.CrashLog;
import com.lntu.online.server.dao.User;
import com.lntu.online.server.model.Student;
import com.lntu.online.server.model.UserType;
import com.lntu.online.server.util.ThreadUtils;
import com.lntu.online.server.util.mail.MailSender;
import org.joda.time.DateTime;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.UUID;

@Path("feedback")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class FeedbackResource {

    @POST
    @Path("crash-log")
    public void crashLog(
            @HeaderParam("User-Agent") String userAgent,
            @FormParam("userId") String userId,
            @FormParam("content") String content
    ) {
        CrashLog crashLog = new CrashLog();
        crashLog.setId(UUID.randomUUID().toString());
        crashLog.setUserId(userId);
        crashLog.setUserAgent(userAgent);
        crashLog.setContent(content);
        crashLog.setCreateAt(new Date());
        crashLog.save();

        String mail =
                "时间：" + new DateTime().toString() + "<br><br>" +
                "尾巴：" + userAgent + "<br><br>" +
                "学号：" + userId + "<br><br>" +
                content.replace("\n", "<br>");

        MailSender.sendToAdmin(MailSender.LEVEL_CRASH, mail);
    }

    @POST
    @Path("advice")
    @Authorization
    public void advice(
            @Context ContainerRequestContext requestContext,
            @HeaderParam("User-Agent") final String userAgent,
            @FormParam("content") final String content
    ) {
        final User user = (User) requestContext.getProperty("user");

        Advice advice = new Advice();
        advice.setId(UUID.randomUUID().toString());
        advice.setUserId(user.getId());
        advice.setUserAgent(userAgent);
        advice.setContent(content);
        advice.setCreateAt(new Date());
        advice.save();

        if (user.getType() == UserType.STUDENT) {
            ThreadUtils.execute(new Runnable() {

                @Override
                public void run() {
                    Student student = StudentCapture.getStudent(user.getId(), user.getPassword());

                    String mail = "" +
                            "时间：" + new DateTime().toString() + "<br><br>" +
                            "尾巴：" + userAgent + "<br><br>" +
                            "学号：" + student.getId() + "<br><br>" +
                            "姓名：" + student.getName() + "<br><br>" +
                            "学院：" + student.getCollege() + "<br><br>" +
                            "班级：" + student.getClassInfo() + "<br><br>" +
                            content.replace("\n", "<br>");

                    MailSender.sendToAdmin(MailSender.LEVEL_ADVICE, mail);
                }

            });
        }
    }

}