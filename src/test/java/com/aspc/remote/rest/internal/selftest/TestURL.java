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
package com.aspc.remote.rest.internal.selftest;
import com.aspc.remote.rest.internal.AWSReSTAuthorization;
import com.aspc.remote.rest.internal.ReSTUtil;
import org.apache.commons.logging.Log;
import com.aspc.remote.util.misc.CLogger;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Check we can calculate the AWS authorization header.
 *  <br>
 *  <i>THREAD MODE: SINGLE-THREADED test case</i>
 *
 * @author Nigel Leck
 * @since 12 August 2017
 */
public class TestURL extends TestCase
{
    /**
     * Creates a new unit test
     * @param name the name of the unit
     */
    public TestURL( final String name )
{
        super( name );
    }

    /**
     * Creates the test suite
     * @return Test the test suite
     */
    public static Test suite()
{
        TestSuite suite = new TestSuite( TestURL.class );
        return suite;
    }

    /**
     * Entry point to run this test standalone
     * @param args the arguments to the test
     */
    public static void main( String[] args )
    {
        TestRunner.run( suite() );

        System.exit(0);
    }

    public void testGood() throws Exception
    {
        String urls[]={
            "https://2c0v5zq4w9.execute-api.ap-southeast-2.amazonaws.com/default"
        };

        for( String url: urls)
        {
            ReSTUtil.checkURL(url);
        }

    }

    public void testBad() throws Exception
    {
        String urls[]={
            "http://999.127.127.102:8080"
        };

        for( String url: urls)
        {
            try{
                ReSTUtil.checkURL(url);
                fail( url);
            }
            catch( IllegalArgumentException e)
            {
                LOGGER.info( "expected: " + e.getMessage());
            }
        }

    }

    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.rest.internal.selftest.TestURL");//#LOGGER-NOPMD
}
