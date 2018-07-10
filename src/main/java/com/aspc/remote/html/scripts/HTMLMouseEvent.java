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
import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 *  HTMLMouseEvent
 *
 * <i>THREAD MODE: SINGLE-THREADED html generator component</i>
 *  @author      Nigel Leck
 *  @since       13 August 1998
 */
public class HTMLMouseEvent extends HTMLEvent
{
    /**
     *
     */
    public static final String ON_CLICK_EVENT     = "onClick";
    /**
     *
     */
    public static final String ON_DOUBLE_CLICK_EVENT  = "onDblClick";
    /**
     *
     */
    public static final String ON_MOUSE_DOWN_EVENT = "onMouseDown";
    /**
     *
     */
    public static final String ON_DROP_EVENT      = "ondrop";
    /**
     *
     */
    public static final String ON_DRAG_OVER_EVENT  = "ondragover";
    /**
     *
     */
    public static final String ON_MOUSE_UP_EVENT   = "onMouseUp";
    /**
     *
     */
    public static final String ON_MOUSE_MOVE_EVENT = "onMouseMove";
    /**
     *
     */
    public static final String ON_MOUSE_OVER_EVENT = "onMouseOver";
    /**
     *
     */
    public static final String ON_MOUSE_OUT_EVENT  = "onMouseOut";

    /**
     * 
     * @param name 
     * @param call 
     */
    public HTMLMouseEvent(String name, String call)
    {
        super( name, call);

        if(
            name.equalsIgnoreCase("nodrop") ||
            name.equalsIgnoreCase("nodragdrop")
        )
        {
            super.name = ON_DROP_EVENT;
        }
    }

    /**
     * 
     * @param browser 
     * @return the value
     */
    @Override @CheckReturnValue @Nonnull
    public String getName(final @Nonnull ClientBrowser browser)
    {
        if( name.equals(ON_DROP_EVENT))
        {
            if( browser.isBrowserNETSCAPE())
            {
                return "ondragdrop";
            }
        }

        return name;
    }
}
