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

import com.lntu.online.server.capture.CaptureConfig;
import com.lntu.online.server.config.AppConfig;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("admin")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AdminResource {

    private static final String HTML_0 = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>";
    private static final String HTML_1 = "</body><html>";

    @GET
    @Path("auto-fix")
    @Produces(MediaType.TEXT_HTML)
    public String autoFix() {
        if (AppConfig.admin.enable) {
            return HTML_0 + CaptureConfig.autoFix() + HTML_1;
        } else {
            return HTML_0 + "系统维护未启用" + HTML_1;
        }
    }

}
