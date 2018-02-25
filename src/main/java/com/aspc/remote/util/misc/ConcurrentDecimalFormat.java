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

import java.text.DecimalFormat;
import java.text.ParseException;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;

/**
 *  A wrapper for Decimal Format to make sure it's thread safe.
 *  <br>
 *  <i>THREAD MODE: MULTI-THREADED</i>
 *
 * @author  Nigel Leck
 * @since   5 April 2009
 */
public final class ConcurrentDecimalFormat
{
//    private static final long serialVersionUID = 42L;
    private final String pattern;
    private final ThreadLocal< DecimalFormat > df = new ThreadLocal< DecimalFormat >()
    {
        @Override
        protected DecimalFormat initialValue()
        {
             return new DecimalFormat(pattern);
        }
    };

    /**
     * create a new Concurrent decimal format
     * @param pattern the pattern
     */
    public ConcurrentDecimalFormat( final @Nonnull String pattern)
    {
        if( StringUtilities.isBlank(pattern)) throw new IllegalArgumentException("pattern is mandatory");
        this.pattern=pattern;
    }

    /**
     * format the number
     * @param number the number to format
     * @return the formated string
     */
    @Nonnull @CheckReturnValue
    public String format( final @Nonnull Object number)
    {
        if( number instanceof Number == false) throw new IllegalArgumentException("Must be a number was: " + number);
        return df.get().format(number);
    }

    /**
     * parse a number
     * @param text the text to parse
     * @return the number
     * @throws java.text.ParseException failed to parse the text
     */
    @Nonnull @CheckReturnValue
    public Number parse( final @Nonnull String text) throws ParseException
    {
        if( StringUtilities.isBlank(text)) throw new IllegalArgumentException("text is mandatory");
        return df.get().parse(text);
    }
}
