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
package com.aspc.remote.tail;

import org.apache.commons.logging.Log;

import com.aspc.remote.util.misc.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *  Queue of lines.
 *
 * <br>
 * <i>THREAD MODE: MULTI-THREADED</i>
 *
 *  @author      Nigel
 *  @since       31 August 2012
 */
public class TailQueue
{
    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.tail.TailQueue");//#LOGGER-NOPMD
    private final BlockingQueue<TailLine> queue=new LinkedBlockingQueue<>();
    private boolean closed;
    
    /**
     * create a line queue
     */
    public TailQueue()
    {
    }

    public boolean isClosed()
    {
        return closed;        
    }
    
    public void close()
    {
        closed=true;
    }
    public boolean isEmpty()
    {
        return queue.isEmpty();
    }

    public void addLine( final TailLine line)
    {
        queue.add(line);
    }
    public TailLine nextLine() throws InterruptedException
    {
        return queue.take();
    }
}
