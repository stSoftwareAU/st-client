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
package com.aspc.remote.html;

import com.aspc.developer.ThreadCop;
import com.aspc.remote.util.misc.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.*;
import org.apache.commons.logging.Log;

/**
 *  HTMLPanel
 *
 * <i>THREAD MODE: SINGLE-THREADED HTML generator component</i>
 *  @author      Nigel Leck
 *  @since       14 June 1998
 */
public class HTMLPanel extends HTMLContainer
{
    private final ReentrantLock generateLock = new ReentrantLock();

    @Override
    public void setParent( final HTMLComponent parent)
    {
        super.setParent(parent);
    }
    
    /**
     *
     * @return the value
     * @throws Exception a serious problem.
     */
    @Nonnull @CheckReturnValue
    public String generateText() throws Exception
    {
        int count = getComponentCount();
        if( count == 0 ) return "";

        return StringUtilities.convertHtmlToText(generate());
    }

    /**
     *
     * @return the value
     * @throws IOException a problem writing to the file occurred.
     */
    @Nonnull @CheckReturnValue
    public File generateHTMLFile( ) throws IOException
    {
        String html = generate();

        return writeToFile( html);
    }

    @Nonnull @CheckReturnValue
    private File writeToFile( final @Nonnull String html ) throws IOException
    {
        String title = "page";
        if( this instanceof HTMLPage)
        {
            title = ((HTMLPage)this).getTitle();
        }

        return writeToFile( title, html, "html");
    }

    @Nonnull @CheckReturnValue
    public static File writeToFile( final @Nonnull String title, final @Nonnull String data, final @Nonnull String extension ) throws IOException
    {
        Date now = new Date();
        String dirName = CProperties.getProperty("LOG_DIR") + "/pages/" + TimeUtil.format("yyyy/MMM/dd/HH/mm", now, TimeZone.getDefault());
        FileUtil.mkdirs(dirName);
        String tmpTitle = title.replace( " ", "_");
        tmpTitle = tmpTitle.replace( ":", "_");
        tmpTitle = StringUtilities.encode(tmpTitle);
        while( tmpTitle.contains("__"))
        {
            tmpTitle = tmpTitle.replace( "__", "_");
        }

        if( StringUtilities.isBlank(tmpTitle) || tmpTitle.length() < 3)
        {
            tmpTitle="unknown";
        }
        else if( tmpTitle.length() > 80)
        {
            tmpTitle=tmpTitle.substring(0, 80);
        }
        File dir=new File( dirName);
        File tmpFile = File.createTempFile(tmpTitle, ".html", dir);

        try
        (PrintWriter out = new PrintWriter( new FileWriter( tmpFile))) {
            out.println(data);
        }

        File file = null;

        for( int v =1; true;v++)
        {
            String fileName = tmpTitle + ( v > 1 ? "_" + v : "") + "." + extension;

            file=new File( dirName, fileName);

            if( tmpFile.renameTo(file)) break;

            // This should never happen but just in case.
            if( v > 1000 ) return tmpFile;
        }
        assert file!=null;
        return file;
    }


    /**
     *
     * @return the value
     */
    @CheckReturnValue @Nonnull
    public String generate( )
    {
        return generate( new ClientBrowser());
    }

    /**
     *
     * @param component
     */
    public void registerPostCompileCallBack( final @Nonnull HTMLComponent component)
    {
        if( postCompileList == null) postCompileList = new ArrayList();//MT WARN: Inconsistent synchronization

        postCompileList.add( component);
    }

    /**
     *
     * @param browser
     * @return the value
     */
    @SuppressWarnings({"AssertWithSideEffects", "ResultOfMethodCallIgnored"})
    @CheckReturnValue
    public @Nonnull String generate( @Nonnull final ClientBrowser browser)
    {
        if( browser == null){
            throw new IllegalArgumentException("browser is mandatory");
        }
        generateLock.lock();
        try
        {
            assert ThreadCop.pushMonitor(this, ThreadCop.MODE.EXTERNAL_SYNCHRONIZED);

            if( isCompiled() == false)
            {
                compile(browser);

                if( postCompileList != null)
                {
                    for (Object postCompileList1 : postCompileList) {
                        HTMLComponent component = (HTMLComponent) postCompileList1;
                        component.postCompile(browser);
                    }
                }
            }

            StringBuilder buffer = new StringBuilder( 50000);
            try
            {
                assert ThreadCop.pushMonitor(this, ThreadCop.MODE.READONLY);
                iGenerate( browser, buffer);
            }
            finally
            {
                assert ThreadCop.popMonitor(this);
            }

            String html = buffer.toString();

            if( LOGGER.isDebugEnabled())
            {
                try
                {
                    writeToFile( html);
                }
                catch( IOException e)
                {
                    LOGGER.warn( "could not write generated html", e);
                }
            }

            return html;
        }
        finally
        {
            assert ThreadCop.popMonitor(this);
            generateLock.unlock();
        }
    }

    private ArrayList postCompileList;

    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.html.HTMLPanel");//#LOGGER-NOPMD
}
