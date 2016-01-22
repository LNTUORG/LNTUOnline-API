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

package org.lntu.online.server.app;

import org.lntu.online.server.capture.CaptureConfig;
import org.lntu.online.server.config.AppConfig;
import org.joda.time.DateTime;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@WebListener()
public class ApplicationListener implements ServletContextListener {

    private static Timer timer = new Timer();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DBHelper.start();
        if (AppConfig.admin.enable) {
            timer.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {
                    CaptureConfig.autoFix();
                }

            }, new Date(), 1000 * 60 * 60);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DBHelper.stop();
        timer.cancel();
    }

}
