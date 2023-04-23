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
package com.aspc.remote.rest.selftest;
import com.aspc.remote.database.selftest.DBTestUnit;
import com.aspc.remote.rest.ReST;
import com.aspc.remote.rest.Response;
import com.aspc.remote.rest.Status;
import org.apache.commons.logging.Log;
import com.aspc.remote.util.misc.CLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Check that the status references are valid. 
 *  <br>
 *  <i>THREAD MODE: SINGLE-THREADED test case</i>
 *
 * @author luke
 * @since July 29, 2013
 */
public class TestStatusReference extends TestCase
{
    /**
     * Creates a new unit test
     * @param name the name of the unit
     */
    public TestStatusReference( final String name )
    {
        super( name );
    }

    /**
     * Creates the test suite
     * @return Test the test suite
     */
    public static Test suite()
{
        TestSuite suite = new TestSuite( TestStatusReference.class );
        return suite;
    }

    /**
     * Entry point to run this test standalone
     * @param args the arguments to the test
     */
    public static void main( String[] args )
    {
        Test test=suite();
//        test = TestSuite.createTest(TestReSTBuilder.class, "testWrongPassword");
        TestRunner.run( test);

        System.exit(0);
    }

    /**
     * Check all the references are valid.
     * @throws Exception a test failure.
     */
    public void testCheckReferenceIsValid() throws Exception
    {
        HashSet<String> testedURL = new HashSet<>();
        Status[] array = Status.values();
        ArrayList<Status> statuses = new ArrayList<>(Arrays.asList(array));
        while(statuses.size() > 0)
        {
            int i = (int)(statuses.size() * Math.random());
            Status status = statuses.get(i);
            statuses.remove(i);
            if(status == Status.C420_ENHANCE_YOUR_CALM && DBTestUnit.hideKnownErrors())
            {
                //420 reference page is 404 now... https://developer.twitter.com/en/docs/response-codes
                continue;
            }
            String url=status.reference;
            if( url!=null)
            {
                int pos = url.indexOf("#");
                if( pos != -1)
                {
                    url=url.substring(0, pos);
                }
                if("https://support.cloudflare.com/hc/en-us/articles/115003011431".equals(url))
                {
                    //cloudflare return 403 for his page if the cookie is not enabled
                    if(DBTestUnit.hideKnownErrors())
                    {
                        continue;
                    }
                }
                if(testedURL.contains(url))
                {
                    LOGGER.info("status " + status.code + " URL " + url + " has been tested successfully, skip it now");
                    continue;
                }
                LOGGER.info("testing status " + status.code + " URL: " + url);
//                Response r = ReST.builder(url).setMinCachePeriod("31 days").setAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36").getResponse();
                Response r = ReST.builder(url)
                        .setAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 13_3_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36") //chrome 112 on macOS
                        .getResponse();
                int attempt = 0;
                while(r.status.code == 403 || r.status.code == 429)
                {
                    if(attempt > 10)
                    {
                        break;
                    }
                    attempt++;
                    LOGGER.info("Status " + status.code + ": " + url + " status: " + r.status + ", retrying " + attempt + "...");
                    long sleep = 1000;
                    if(r.status.code == 429)
                    {
                        sleep += attempt * 1000;
                    }
                    Thread.sleep(sleep);
                    r = ReST.builder(url).getResponse();
                }
                if(r.status.isError())
                {
                    if(r.status.code != 429)
                    {
                        fail( "Status " + status.code + ": " + url + " status: " + r.status);
                    }
                    else
                    {
                        //skip the check for Too Many Requests error
                        LOGGER.info("Skip Status " + status.code + ": " + url + " status: " + r.status + " because of too many requests error");
                    }
                }
                testedURL.add(url);
            }
        }

    }

    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.rest.selftest.TestStatusReference");//#LOGGER-NOPMD
}
