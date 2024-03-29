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

package com.aspc.remote.util.net;

/**
 * Client listner
 * 
 * WARN: All messages need to come from the DBMessage table. They also need to be Multi Lingual use resource bunbles.
 * WARN: The message bundle job that Mei did should be used/improved to do resource bundles. ( standard way from jdk)
 *
 * @author Nigel Leck
 * @since 29 September 2006
 *  <br>
 *  <i>THREAD MODE: MULTI-THREADED</i>
 *
 */
public interface IClientListener
{
    
    /**
     * Handles messages.
     * @param messsage the message
     */
    void clientStatusActionPerformed(String messsage);
    /**
     * Handles messages and progess bar.
     * @param progress The percentage of the file received
     */
    void clientStatusActionPerformed(int progress);
    
    
    
}
