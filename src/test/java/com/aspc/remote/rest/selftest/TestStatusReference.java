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
        for( Status status:Status.values())
        {
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
//                Response r = ReST.builder(url).setMinCachePeriod("31 days").setAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36").getResponse();
                Response r = ReST.builder(url).setMinCachePeriod("31 days").getResponse();
                if( r.status.isError())
                {
                    fail( url + " status: " + r.status);
                }

            }
        }

    }

    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.rest.selftest.TestStatusReference");//#LOGGER-NOPMD
}
