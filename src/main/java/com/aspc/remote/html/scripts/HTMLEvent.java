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
package com.aspc.remote.html.scripts;

import com.aspc.remote.html.*;
import com.aspc.remote.util.misc.StringUtilities;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *  HTMLEvent
 *
 * <i>THREAD MODE: SINGLE-THREADED html generator component</i>
 *  @author      Nigel Leck
 *  @since       12 August 1998
 */
public class HTMLEvent
{
    /**
     * Create a new HTML Event
     *
     * @param name the name of the event 
     * @param call the script to call
     */
    public HTMLEvent(final @Nonnull String name, final @Nonnull String call)
    {
        if( StringUtilities.isBlank(name)) throw new IllegalArgumentException("name is mandatory");
        /* blank is OK */
        if( call==null) throw new IllegalArgumentException("call is mandatory");
        assert validCall( call):"Invalid javascript call: " + call;
        this.name = name.trim();
        this.call = call;
        priority = 50;
    }

    public static boolean validCall( final @Nonnull String call)
    {
        char lastQuote='\0';
        boolean lastEscape=false;
        for( char c:call.toCharArray())
        {
            if( lastEscape)
            {
                lastEscape=false;
            }
            else
            {
                if( c == '\\')
                {
                    lastEscape=true;
                }
                else if( c == lastQuote)
                {
                    lastQuote='\0';
                }
                else if( c == '\'' || c == '"')
                {
                    if( lastQuote=='\0')
                    {
                        lastQuote=c;
                    }
                }
            }
        }
        
        return lastEscape==false && lastQuote=='\0';
    }
    
    /**
     * The name of this event.
     *
     * @param browser The browser for which we are generating the event 
     * @return The name
     */
    @Nonnull @CheckReturnValue
    public String getName(final @Nonnull ClientBrowser browser)
    {
        return name;
    }

    /**
     * The call.
     *
     * @return the url. 
     */
    @Nonnull @CheckReturnValue
    public String getCall()
    {
        return call;
    }

    /**
     * The priority of this event ( the order in which they are called)
     *
     * @return priority normally 1-100 default to 50 
     */
    @CheckReturnValue
    public int getPriority( )
    {
        return priority;
    }
    
    /**
     * The priority of this event ( the order in which they are called)
     *
     * @param priority normally 1-100 default to 50 
     */
    public void setPriority( final @Nonnegative int priority)
    {
        assert priority>=0: "priority must be non-negative was: " + priority;
        this.priority = priority;
    }
    
    /** The priority of this event ( the order in which they are called)*/
    protected int priority;
    
    /** the name of the event */
    protected String name;

    /** the call */
    protected String call;
}
