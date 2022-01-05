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

import java.io.PrintStream;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Standard out or standard err. 
 */
public final class ConsoleHandler extends Handler {

    
    @Override
    public void publish(final LogRecord record) {
        if (getFormatter() == null) {
            setFormatter(new SimpleFormatter());
        }

        String message = getFormatter().format(record);
        PrintStream ps = System.out;

        if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
            ps = System.err;
        }

        ps.print(message);
        Throwable thrown = record.getThrown();

        if (thrown != null) {
            thrown.printStackTrace(ps);
        }
    }

    @Override
    public void close() throws SecurityException {
        flush();
    }

    @Override
    public void flush() {
        System.err.flush();
        System.out.flush();
    }
}
