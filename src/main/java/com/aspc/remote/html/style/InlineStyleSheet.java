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
package com.aspc.remote.html.style;
import com.aspc.remote.html.ClientBrowser;
import com.aspc.remote.html.HTMLComponent;

/**
 *  HTMLStyleSheet
 *
 * <i>THREAD MODE: SINGLE-THREADED html generator component</i>
 *  @author      Nigel Leck
 *  @since       30 August 1998
 */
public class InlineStyleSheet extends InternalStyleSheet
{
    /**
     * generate the raw HTML for this component.
     *
     * @param browser The target browser
     * @param buffer The generate HTML
     */
    @Override
    public void iGenerate( final ClientBrowser browser, final StringBuilder buffer)
    {
        if( items == null) return;

        buffer.append("style=\"");

        StringBuilder inlineStyle=new StringBuilder();
        
        boolean started=false;
        for( String key: items.keySet())
        {
            if( started)
            {
                inlineStyle.append("; ");
            }
            started=true;
            inlineStyle.append(key).append(": ");
            inlineStyle.append(items.get(key));
        }
        if( started)
        {
            inlineStyle.append(";");
        }
        
        assert inlineStyle.toString().matches( HTMLComponent.VALID_STYLE_REGEX) : "invalid style '" + inlineStyle + "'";
        
        buffer.append(inlineStyle);
        buffer.append("\"");
    }
}
