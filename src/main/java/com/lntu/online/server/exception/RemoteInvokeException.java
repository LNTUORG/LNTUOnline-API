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

package com.lntu.online.server.exception;

import com.lntu.online.server.model.ErrorCode;
import com.lntu.online.server.model.ErrorInfo;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;

public class RemoteInvokeException extends ServerErrorException {

    public RemoteInvokeException(String message) {
        super(Response.status(500).entity(new ErrorInfo(ErrorCode.REMOTE_INVOKE_ERROR, message)).build());
    }

    public RemoteInvokeException(Throwable throwable) {
        super(Response.status(500).entity(new ErrorInfo(ErrorCode.REMOTE_INVOKE_ERROR, throwable.getLocalizedMessage())).build());
    }

}
