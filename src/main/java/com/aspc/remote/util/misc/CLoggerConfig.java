/**
 *  STS Remote library
 *
 *  Copyright (C) 2006  stSoftware Pty Ltd
 *
 *  stSoftware.com.au
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Bug fixes, suggestions and comments should be sent to:
 *
 *  info AT stsoftware.com.au
 *
 *  or by snail mail to:
 *
 *  stSoftware
 *  Suite 223, Lifestyle Working
 *  117 Old Pittwater Rd
 *  Brookvale NSW 2100
 *  Australia.
 */
package com.aspc.remote.util.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This is the static configuration class for logger
 *
 * <br>
 * <i>THREAD MODE: MULTI-THREADED</i>
 *
 * @author alex
 * @since 29 September 2006
 */
public final class CLoggerConfig {

    private static final String LOGGING_CONFIG_FILE = "java.util.logging.config.file";

    private CLoggerConfig() {

    }

    public static void flush() {
        final LogManager lm = LogManager.getLogManager();
        
        Enumeration<String> loggerNames = lm.getLoggerNames();
        while (loggerNames.hasMoreElements()) {
            String name = loggerNames.nextElement();        
            Logger logger = lm.getLogger(name);    
            if( logger !=null){
                for (Handler handler : logger.getHandlers()) {        
                    handler.flush();
                }
            }
        }
    }

    /**
     *
     */
    public static void configure() {
        LogManager lm = LogManager.getLogManager();

        String configFile = System.getProperty(LOGGING_CONFIG_FILE);

        if (configFile == null ) {

            if (configFile == null) {
                String docRoot = System.getProperty("DOC_ROOT");
                if (docRoot != null) {
                    File tmp = new File(docRoot + "/WEB-INF/logging.properties");
                    if (tmp.canRead()) {
                        configFile = tmp.getAbsolutePath();
                    }
                }
            }

            if (configFile != null) {
                System.setProperty(LOGGING_CONFIG_FILE, configFile);

                lm.reset();
                try {
                    InputStream in = new FileInputStream(configFile);

                    lm.readConfiguration(in);
                } catch (IOException e) {
                    System.out.println("Could not read configuration: " + e);
                }
            }
        }
    }
}
