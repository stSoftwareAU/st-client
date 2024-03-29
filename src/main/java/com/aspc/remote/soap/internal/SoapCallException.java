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
package com.aspc.remote.soap.internal;

import com.aspc.remote.util.misc.StringUtilities;

/**
 *  VirtualDBTestUnit is a JUnit based test...
 *
 *  <br>
 *  <i>THREAD MODE: READONLY</i>
 *
 *  @author      Nigel Leck
 *  @since       3 December 2001, 12:02
 */
public class SoapCallException extends Exception
{
    /**
     * Soap call had an exception
     * @param code The code 
     * @param reason the reason
     */
    public SoapCallException(final String code, final String reason)
    {
        super( StringUtilities.safeMessage( reason));

        this.code = code;
       // this.reason = reason;
    }

    /**
     * return the fault code
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * returns the fault reason
     * @return the reason
     */
//    public String getReason()
//    {
//        return getMessage();
//    }

    private final String code;//,
                   //reason;
}
