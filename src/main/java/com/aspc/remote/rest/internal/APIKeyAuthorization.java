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
package com.aspc.remote.rest.internal;

import com.aspc.remote.util.misc.StringUtilities;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 * Calculate the AWS authorization header. 
 *  <br>
 *  <i>THREAD MODE: SINGLE-THREADED test case</i>
 *
 * @author Nigel Leck
 * @since 12 August 2017
 */
public final class APIKeyAuthorization implements ReSTAuthorizationInterface
{
    public final String apiKey;

    public APIKeyAuthorization(final @Nonnull String apiKey) {
        this.apiKey=apiKey;
    }

    @Override
    public ReSTAuthorizationInterface setRequestProperty(final @Nonnull URLConnection c) {
        HttpURLConnection http=(HttpURLConnection)c;
        c.setRequestProperty("x-api-key", apiKey);
        
        return this;
    }

    @Override @Nonnull @CheckReturnValue
    public String checkSumAdler32(final @Nonnull String url) {
        return StringUtilities.checkSumAdler32(url + apiKey);
    }

    @Override @Nonnull @CheckReturnValue
    public String toShortString() {
        
        if( apiKey.length()>4)
        {
            return apiKey.substring(0,4) +"****" + apiKey.substring(apiKey.length() -4);
        }
        else
        {
            return "*****";
        }
    }
}
