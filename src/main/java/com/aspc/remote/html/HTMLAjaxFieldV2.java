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
 *TestGWT1
 *  stSoftware
 *  Suite 223, Lifestyle Working
 *  117 Old Pittwater Rd
 *  Brookvale NSW 2100
 *  Australia.
 */

package com.aspc.remote.html;

import com.aspc.remote.util.misc.StringUtilities;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class will generate the html for the AjaxField
 *
 * <br>
 * <i>THREAD MODE: SINGLE-THREADED HTML generator component </i>
 *
 *  @author      Paul Smout
 *  @since       June 3, 2008, 7:13 PM
 */
public class HTMLAjaxFieldV2 extends HTMLComponent
{

    private static final String WAITING_GIF = "/images/waiting.gif";
    private final String path;
    private final String dbclass;
    private final String globalkey;
    private final String format;
    private boolean bold;
    private boolean includesTags;
    private int fontSize;
    
    private static final AtomicLong ID=new AtomicLong(1);
    /**
     * Module name
     */
    public static final String AJAX_FIELD_MODULE_NAME = "st-ajax";


    /**
     *
     * @param path
     * @param dbclass
     * @param globalkey
     * @param format
     */
    public HTMLAjaxFieldV2(final String path, final String dbclass, final String globalkey, final String format)
    {
        assert checkPath( path): "inalid path " + path;
        this.path = path;
        this.dbclass = dbclass;
        this.globalkey = globalkey;
        this.format = format;
    }

    private boolean checkPath( final String path)
    {
        int bracket=path.lastIndexOf("}");
        int last=path.indexOf("]", bracket);
        int start=path.indexOf("[", bracket);
        
        if( last!=-1 && start == -1)
        {
            return false;
        }
        
        return true;
    }
    
     /**
     * This is the spot to put all your generation
     * of the HTML components. Do not put it in iGenerate()
     * @param browser client browser
     */
    @Override
    protected void compile(final ClientBrowser browser)
    {
        if( StringUtilities.isBlank(getId()))
        {
            iSetId("ajax" + ID.incrementAndGet());
        }
        
        super.compile(browser);
    }
    

    @Override
    protected void iGenerate(ClientBrowser browser, StringBuilder buffer)
    {
        buffer.append("<span id=\"");
        buffer.append(getId());
        
        buffer.append("\" class=\"st-ajax");
        String className = getClassName();
        if(StringUtilities.notBlank(className))
        {
            buffer.append(" ").append(className);
        }
        buffer.append("\"");
        
        buffer.append(" data-classname=\"").append(dbclass).append("\"");
        buffer.append(" data-globalkey=\"").append(globalkey).append("\"");
        
        String tmpPath=path.trim();
        if( StringUtilities.notBlank(format))
        {
            String parms="DISPLAY_FORMAT=\""+ format.replaceAll("\"", "\\\"") + "\"";
            
            if( tmpPath.endsWith("}"))
            {
                tmpPath=tmpPath.substring(0, tmpPath.length()-2) + ", " + parms + "}";
            }
            else
            {
                tmpPath="{" + parms + "}";
            }
        }
        buffer.append(" data-path=\"").append(StringUtilities.encodeHTML(tmpPath)).append("\"");
        
        if( includesTags)
        {
            buffer.append( " data-is_html=\"true\"");
        }
        String style="";
        
        if( fontSize>0)
        {
            style="fontSize=" + fontSize +"px";            
        }
        
        if( isBold())
        {
            if( StringUtilities.notBlank(style))
            {
                style+=";";
            }
            
            style+="fontWeight=800";            
        }
        if( StringUtilities.notBlank(style))
        {
            buffer.append("style=\"" ).append(style).append("\"");
        }
        
        buffer.append(">");
        HTMLImage img  =new HTMLImage(WAITING_GIF);
        img.iGenerate(browser, buffer);
        buffer.append("</span>");
    }

    /**
     * Is bold
     * @return bold
     */
    public boolean isBold()
    {
        return bold;
    }

    /**
     * Get Font Size
     * @return size
     */
    public int getFontSize()
    {
        return fontSize;
    }

    /**
     * Set Bold
     * @param bold bold
     */
    public void setBold(boolean bold)
    {
        this.bold = bold;
    }
    /**
     * Set includes Tags
     * @param includesTags includes tags
     */
    public void setIncludesTags(boolean includesTags)
    {
        this.includesTags = includesTags;
    }
    /**
     * Font Size
     * @param fontSize fontSize
     */
    public void setFontSize(int fontSize)
    {
        this.fontSize = fontSize;
    }

}

