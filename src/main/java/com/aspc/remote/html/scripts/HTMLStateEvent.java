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

/**
 *  HTMLStateEvent
 *
 * <i>THREAD MODE: SINGLE-THREADED html generator component</i>
 *  @author      Nigel Leck
 *  @since       12 August 1998
 */
public class HTMLStateEvent extends HTMLEvent
{
    /**
     *
     */
    public static final String onReadyStateChangeEvent  = "onReadyStateChange";
    /**
     *
     */
    public static final String onLoadEvent              = "onLoad";
    /**
     *
     */
    public static final String onUnloadEvent            = "onUnload";
    /**
     *
     */
    public static final String onResizeEvent            = "onResize";
    /**
     *
     */
    public static final String onAbortEvent             = "onAbort";

    /**
     * 
     * @param name 
     * @param call 
     */
    public HTMLStateEvent( String name, String call)
    {
        super(name, call);
    }
}
