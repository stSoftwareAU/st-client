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
package com.aspc.remote.database;

/**
 *  NullValueException is thrown when the cell is blank
 *
 *  <br>
 *  <i>THREAD MODE: READONLY</i>
 *
 *  @author      Nigel Leck
 *  @since       12 November 1998
 */
public class NullValueException extends Exception
{
    private static final long serialVersionUID = 42L;
    
    /**
     * 
     * @param text the text
     * @param cause the cause
     */
    public NullValueException( final String text, final Throwable cause)
    {
        super( text, cause);
    }

    /**
     * 
     * @param text the text
     */
    public NullValueException( final String text)
    {
        super( text);
    }
}
