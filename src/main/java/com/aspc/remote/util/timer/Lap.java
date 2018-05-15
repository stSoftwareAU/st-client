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
package com.aspc.remote.util.timer;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;

/**
 *  Stopwatch
 *
 *  <br>
 *  <i>THREAD MODE: Single thread</i>
 *
 *  @author      Nigel Leck
 *  @since       3 June 2015
 */
public final class Lap
{
    private final long nanoStart=System.nanoTime();
    private long nanoEnd;
    
    public Lap()
    {
        assert nanoStart>0: "start must be positive was: " + nanoStart;
    }
    
    /**
     * When was the lap started ? 
     * @return the nano time. 
     */
    @CheckReturnValue @Nonnegative
    public long start()
    {
        return nanoStart;
    }
    
    /**
     * When was the lap ended ? 
     * @return the end time. 
     */    
    @Nonnegative
    public long end()
    {
        if( nanoEnd ==0)
        {
            nanoEnd=System.nanoTime();
        }
        assert nanoEnd>0: "end must be positive was: " + nanoEnd;
        
        assert nanoEnd>=nanoStart: "Should end: " + nanoEnd +" after started: " + nanoStart;
        return nanoEnd;
    }

    /**
     * The duration in Nanoseconds. 
     * @return the difference.
     */
    @CheckReturnValue @Nonnegative
    public long duration()
    {
        assert nanoStart>0: "Must have a positive start time: " + nanoStart;
        long tmpEnd=nanoEnd;
        if( tmpEnd < nanoStart)
        {
            assert tmpEnd==0: "End: " + tmpEnd + " should be greater than equal to start: " + nanoStart;
            tmpEnd=System.nanoTime();
        }
        
        long tmpDuration= tmpEnd-nanoStart;
        assert tmpDuration>=0: "Must be non negative was: " + tmpDuration;
        
        return tmpDuration;
    }   
    
    /**
     * The duration in MILLISECONDS.
     * @return the difference.
     */
    @CheckReturnValue @Nonnegative
    public long durationMS()
    {        
        long tmpDurationMS=  duration()/1000L/1000L;
        
        assert tmpDurationMS>=0: "Must be non negative was: " + tmpDurationMS;
        return tmpDurationMS;
    }  
}
