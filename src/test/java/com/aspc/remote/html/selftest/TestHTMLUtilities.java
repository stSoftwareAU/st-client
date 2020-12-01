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
package com.aspc.remote.html.selftest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import com.aspc.remote.html.*;
import com.aspc.remote.util.misc.CLogger;
import java.net.URL;
import static junit.framework.TestCase.assertEquals;
import org.apache.commons.logging.Log;

/**
 *  check the GWT modules.
 *
 * <br>
 * <i>THREAD MODE: SINGLE-THREADED self test unit</i>
 *
 *  @author         Paul Smout
 *  
 *  @since          June 5 2008
 */
public class TestHTMLUtilities extends TestCase
{
    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.html.selftest.TestHTMLUtilities");//#LOGGER-NOPMD

    /**
     * Creates new VirtualDBTestUnit
     * @param name The name of the test unit
     */
    public TestHTMLUtilities(String name)
    {
        super( name);
    }

    /**
     * @param args The command line arguments
     */
    public static void main(String[] args)
    {
        TestRunner.run(suite());
    }

    /**
     * @return the value
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite(TestHTMLUtilities.class);
        return suite;
    }

    /**
     * Upgrade to SSL
     *
     * @throws Exception a serious problem
     */
    public void testHTTPS() throws Exception
    {
        checkHTTPS();
        checkHTTPS();
    }

    public void testSafeHTML()
    {
        String checks[][]={
            {
                "<UL> <LI>30 great rides throughout New Zealand,  <LI>Route descriptions, map and cue sheets for each ride</UL>",

                "<ul> <li>30 great rides throughout New Zealand, </li><li>Route descriptions, map and cue sheets for each ride</li></ul>",
            },
            
            {
                "<p><span style=\"line-height: 1.45em;\">A poignant story of a remarkable relationship between Frank Stevens, an Australian soldier sent to the "+
                "Vietnamese Highlands to recruit and train the local hill tribes during the Vietnam War, and his Vietnamese translator, Minh."+
                "</span><br></p><p>Nearly fifty years later, Minh, now living in Australia and seriously ill, remembers the experiences that he shared with Frank,"+
                " and discovers that even amongst his traumatic memories, there is consolation and joy.</p><p>Reviewed here on "+
                "<a href=\"https://anzlitlovers.com/2016/04/19/seeing-the-elephant-by-portland-jones/\" target=\"_blank\">ANZ Litlovers blog</a></p>",
                
                "<p>A poignant story of a remarkable relationship between Frank Stevens, an Australian soldier sent to the "+
                "Vietnamese Highlands to recruit and train the local hill tribes during the Vietnam War, and his Vietnamese translator, Minh."+
                "<br></p><p>Nearly fifty years later, Minh, now living in Australia and seriously ill, remembers the experiences that he shared with Frank,"+
                " and discovers that even amongst his traumatic memories, there is consolation and joy.</p><p>Reviewed here on "+
                "<a href=\"https://anzlitlovers.com/2016/04/19/seeing-the-elephant-by-portland-jones/\">ANZ Litlovers blog</a></p>",
            },
            
            {
                "<div>Here's a wonderful review of&nbsp;<i>Collecting Ladies&nbsp;</i>in&nbsp;The Journal of Melbourne Royal Botanic Gardens,&nbsp;<i><a href=\"http://www.rbg.vic.gov.au/documents/MuelleriaVol_32_-_p173_Thurlow_review.pdf\">Muelleria</a></i><br></div>",
                "Here's a wonderful review of&nbsp;<i>Collecting Ladies&nbsp;</i>in&nbsp;The Journal of Melbourne Royal Botanic Gardens,&nbsp;<i><a href=\"http://www.rbg.vic.gov.au/documents/MuelleriaVol_32_-_p173_Thurlow_review.pdf\">Muelleria</a></i><br>"                
            },
            {"<a href='http://apple.com'>apple</a>", "<a href=\"http://apple.com\">apple</a>"},
            {"hello <b>world</b>","hello <b>world</b>"},
            {"hacker <javascript>window.alert('hacked');</javascript>", "hacker"},
            {"<a href='javascript:window.alert(\'HACK\')'>hack</a>", "hack"},           
            {
                "<p><a href=\"https://www.dropbox.com/s/he1b792nj2qka13/Mad%20Magpie%20Teacher%20Notes.pdf?dl=0\" target=\"_blank\">Teachers Notes</a> available.</p>",
                "<a href=\"https://www.dropbox.com/s/he1b792nj2qka13/Mad%20Magpie%20Teacher%20Notes.pdf?dl=0\">Teachers Notes</a> available."
            },
            {
                "<em>World Cruising Routes</em>",
                "<em>World Cruising Routes</em>"
            },
            {
                "<strong>World Cruising Routes</strong>",
                "<strong>World Cruising Routes</strong>"
            },
            {
                "<em>Emphasized text</em><br><strong>Strong text</strong><br><code>A piece of computer code</code><br><samp>Sample output from a computer program</samp><br><kbd>Keyboard input</kbd><br><var>Variable</var>",
                "<em>Emphasized text</em><br><strong>Strong text</strong><br><code>A piece of computer code</code><br><samp>Sample output from a computer program</samp><br><kbd>Keyboard input</kbd><br><var>Variable</var>",
            },
            {
                "<p>This text contains <sup>superscript</sup> text.</p>",
                "This text contains <sup>superscript</sup> text."
            },
            {
                "<u>Ignore underline allow <b>bold</b> text.</u>",
                "Ignore underline allow <b>bold</b> text."
            },
            {
                "<ol reversed type=\"A\" start=\"5\">\n" +
                "   <li>Cats</li>\n" +
                "   <li>Dogs</li>\n" +
                "</ol>",
                "<ol reversed type=\"A\" start=\"5\">" +
                " <li>Cats</li>" +
                " <li>Dogs</li> " +
                "</ol>"                
            },
            
            {
                "<div>duopress labs&#160;<p>SmartFlash</p>&trade;&#1;and <p>TummyTime&trade;</p></div>",
                "duopress labs&nbsp;<p>SmartFlash</p>™and <p>TummyTime™</p>"
            },
            {
                "Mauricio Vela&#769;zquez de Leo&#769;n, developing dozens of books&#160;<I>100 Pablo Picassos</I>;",
                "Mauricio Velázquez de León, developing dozens of books&nbsp;<i>100 Pablo Picassos</i>;"
            },
            {
                "<div>&#33;</div>",
                "!"
            },
            {
                "<p>&#9825</p>",
                "♡"
            },
            {
                "<p>&#129472</p>",
                "🧀"
            },
            {
                "<p>&#11001;</p>",
                "⫹"
            },
            {
                "<div><p>&#x2AF9;</p><p>\u0416</p></div>",
                "<p>⫹</p><p>Ж</p>"
            },  
            {
                "<p>&#17555;apple&#17555;</p>",
                "䒓apple䒓"
            },
            {
                "<p>&#129530;&#x1f9fa;0x0001F9FA</p>",
                "🧺🧺0x0001F9FA"
            },
            {
                "<p>&#2579;&#xa13;\u0A13&#2579;</p>",
                "ਓਓਓਓ"
            },
            {
                "<p>\uD87E\uDDF4&#x2f9f4;&#195060;</p>",
                "嶲嶲嶲"
            }
        };
        
        for( String check[]:checks)
        {
            String html=check[0];
            String safeHTML=HTMLUtilities.makeSafeSegment(html).replace("\n", "");
            
            String expectedHTML=check[1];
            if( expectedHTML.equals(safeHTML)==false)
            {
                LOGGER.warn( "Invalid: " + safeHTML);
            }
            assertEquals( html, expectedHTML, safeHTML);
        }
    }
        
    private void checkHTTPS() throws Exception
    {
        String hosts[]={
            "www.stsoftware.com.au",
            "www.stsoftware.net.au", 
            "stsoftware.com.au",
            "www.stSoftware.com.au",
            "www.jobtrack.com.au",
            "demo.jobtrack.com.au", 
            "jobtrack.com.au",
            "aspc.jobtrack.com.au"
        };
        for( String host: hosts)
        {
            checkHTTPS( host);
        }
    }
    private void checkHTTPS(final String host) throws Exception
    {
        URL url = HTMLUtilities.bestURL("http://" + host + "/", true);

        assertEquals("should have upgraded to SSL for " + host, "https", url.getProtocol());

        assertEquals("Change to default port https", "https://"+ host.toLowerCase() + "/", url.toString());
        
        url = HTMLUtilities.bestURL("http://"+ host, true);

        assertEquals("should have upgraded to SSL", "https", url.getProtocol());
        assertEquals("Change to default port https", "https://"+ host.toLowerCase(), url.toString());

        url = HTMLUtilities.bestURL("http://" + host + ":8080/siteST", true);

        assertEquals("should have upgraded to SSL", "https", url.getProtocol());
        assertEquals("Change to default port https", "https://" + host.toLowerCase() + "/siteST", url.toString());

        url = HTMLUtilities.bestURL("http://" + host + ":80/siteST", true);

        assertEquals("should have upgraded to SSL", "https", url.getProtocol());

        url = HTMLUtilities.bestURL("https://" + host, true);

        assertEquals("Already SSL no need to change", "https", url.getProtocol());
        
        url = HTMLUtilities.bestURL("https://" + host, false);

        assertEquals("Already SSL no need to change", "https", url.getProtocol());
    }
    
    /**
     * Just Check HTTP
     *
     * @throws Exception a serious problem
     */
    public void testHTTP() throws Exception
    {
        checkHTTP();
        checkHTTP();
    }
    
    private void checkHTTP() throws Exception
    {
        URL url = HTMLUtilities.bestURL("http://stSoftware.com.au/site/ST?X=Y", false);

        assertEquals("Don't upgrade if not asked", "http", url.getProtocol());

        assertEquals("Don't change", "http://stsoftware.com.au/site/st?x=y", url.toString().toLowerCase());

        url = HTMLUtilities.bestURL("http://stSoftware.com.au:80/site/ST?X=Y", false);

        assertEquals("Don't upgrade if not asked", "http", url.getProtocol());

        assertEquals("Don't change", "http://stsoftware.com.au/site/st?x=y", url.toString().toLowerCase());
        
        url = HTMLUtilities.bestURL("http://60.241.239.222:8080", true);

        assertEquals("devserver has no SSL", "http", url.getProtocol());

  //      url = HTMLUtilities.bestURL("http://60.241.239.222/site", false);
        
   //     assertEquals("devserver has no SSL", "http", url.getProtocol());
       // assertEquals("Change to 8080", "http://60.241.239.222:8080/site", url.toString());
                
        url = HTMLUtilities.bestURL("http://60.241.239.223/site", false);
        
        assertEquals("devserver has no SSL", "http", url.getProtocol());
        assertEquals("Leave as is as we can't connect", "http://60.241.239.223/site", url.toString());
        /*
        try
        {
            URL testURL = new URL("http://localhost");
            URLConnection c = testURL.openConnection();
           //NetUrl.relaxSSLConnection(c);

            c.connect();
        }
        catch(ConnectException e)
        {
            try
            {
                URL testURL = new URL("http://localhost:8080");
                URLConnection c = testURL.openConnection();
               //NetUrl.relaxSSLConnection(c);

                c.connect();
                
                //only test when the port 80 is not available
                url = HTMLUtilities.bestURL("http://localhost/site/ST?X=Y", false);
                assertEquals("1) should use port 8080", 8080, url.getPort());
                url = HTMLUtilities.bestURL("http://localhost/site/ST?X=Y", false);
                assertEquals("2) should use port 8080", 8080, url.getPort());
            }
            catch(ConnectException e2)
            {
                
            }
        }*/
    }
}
