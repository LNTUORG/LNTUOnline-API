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

package com.lntu.online.server.resource;

import com.lntu.online.server.app.Authorization;
import com.lntu.online.server.capture.OneKeyCapture;
import com.lntu.online.server.dao.User;
import com.lntu.online.server.exception.NoRightsException;
import com.lntu.online.server.exception.ResourceNotFoundException;
import com.lntu.online.server.model.CourseEvaInfo;
import com.lntu.online.server.model.UserType;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("course-eva-info")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class CourseEvaInfoResource {

    @GET
    @Path("{studentId}")
    @Authorization
    public List<CourseEvaInfo> getCourseEvaInfoList(
            @Context ContainerRequestContext requestContext,
            @PathParam("studentId") String studentId
    ) {
        User user = (User) requestContext.getProperty("user");
        if (user.getType() == UserType.ADMINISTRATOR) { // 管理员用户
            try {
                User targetUser = User.dao.findById(studentId);
                return OneKeyCapture.getCourseEvaInfoList(targetUser.getId(), targetUser.getPassword());
            } catch (Exception e) {
                throw new ResourceNotFoundException();
            }
        }
        else if (user.getType() == UserType.STUDENT) { // 如果是学生用户
            if ("~self".equals(studentId) || user.getId().equals(studentId)) {
                return OneKeyCapture.getCourseEvaInfoList(user.getId(), user.getPassword());
            } else {
                throw new NoRightsException();
            }
        }
        else if (user.getType() == UserType.TEACHER) { // 教师用户
            throw new NoRightsException();
        } else { // 其他用户没权限
            throw new NoRightsException();
        }
    }

    @POST
    @Path("{studentId}/do:eva")
    @Authorization
    public void doEva(
            @Context ContainerRequestContext requestContext,
            @PathParam("studentId") String studentId,
            @FormParam("evaKey") String evaKey
    ) {
        User user = (User) requestContext.getProperty("user");
        if (user.getType() == UserType.STUDENT) { // 如果是学生用户
            if ("~self".equals(studentId) || user.getId().equals(studentId)) {
                OneKeyCapture.doEva(user.getId(), user.getPassword(), CourseEvaInfo.getUrlFormKey(evaKey));
            } else {
                throw new NoRightsException();
            }
        } else { // 其他用户没权限
            throw new NoRightsException();
        }
    }

}
