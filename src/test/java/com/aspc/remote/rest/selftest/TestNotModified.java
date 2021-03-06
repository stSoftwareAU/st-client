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
import com.aspc.remote.rest.ReST;
import com.aspc.remote.rest.Response;
import com.aspc.remote.rest.Status;
import com.aspc.remote.rest.errors.RequestTimeoutException;
import com.aspc.remote.rest.internal.Trace;
import org.apache.commons.logging.Log;
import com.aspc.remote.util.misc.CLogger;
import java.io.File;
import java.io.FileWriter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Check the handling of status 304
 *  <br>
 *  <i>THREAD MODE: SINGLE-THREADED test case</i>
 *
 * @author luke
 * @since 16 August 2017
 */
public class TestNotModified extends TestCase
{
    /**
     * Creates a new unit test
     * @param name the name of the unit
     */
    public TestNotModified( final String name )
    {
        super( name );
    }

    /**
     * Creates the test suite
     * @return Test the test suite
     */
    public static Test suite()
{
        TestSuite suite = new TestSuite( TestNotModified.class );
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
     * check call to Google maps.
     * @throws Exception a test failure.
     */
    @SuppressWarnings("SleepWhileInLoop")
    public void testETag() throws Exception
    {
        String url="http://code.jquery.com/jquery-3.2.1.min.js";

        ReST.Builder call = ReST.builder(url);
        call.setTimeout("10 secs");
        Response rr=null;
        
        for( int loops=0;loops<30; loops++)
        {
            try{
                rr=call.getResponseAndCheck();
                break;
            }
            catch( RequestTimeoutException rte)
            {
                LOGGER.warn( call, rte);
            }
        }
        assert rr!=null;
        String data = rr.getContentAsString();
        String mimeType = rr.mimeType;
        assertTrue( "check mime: " + mimeType, mimeType.matches("application/javascript.*"));

        boolean found304=false;
        for( int loops=0;loops<3; loops++)
        {
            Response rr2=call.getResponseAndCheck();

            String data2=rr2.getContentAsString();
            assertEquals( "data match", data, data2);
            assertEquals( "mime type", mimeType, rr2.mimeType);

            if( rr2.trace == Trace.CACHED)
            {
                found304=true;
            }
            else if( Status.C304_NOT_MODIFIED.equals( rr2.status))
            {
                found304=true;
            }
            else
            {
                LOGGER.info( "status: " + rr2.status);
            }
            Thread.sleep((long) (5000 * Math.random()));
        }

        assertTrue( "should have found 304", found304);
        rr.getContentAsFile().delete();

        Response rr3=null;
        for( int attempts=0;attempts<3;attempts++)
        {
            rr3=call.getResponse();
            
            if( rr3.status == Status.C200_SUCCESS_OK) break;
            
            Thread.sleep((long) (60000 * Math.random() + 1));
        }

        assert rr3!=null;
        assertEquals( "OK", Status.C200_SUCCESS_OK, rr3.status);
        assertEquals( "data match", data, rr3.getContentAsString());
        assertEquals( "mime type", mimeType, rr3.mimeType);

        File file3 = rr3.getContentAsFile();
        file3.setWritable(true);
        try(FileWriter fw=new FileWriter( file3)){
            fw.write("Hello world");
        }

        Response rr4=call.getResponse();

        if( rr4.status.isError())
        {
            rr4=call.getResponse();
        }
        assertEquals( "data match", data, rr4.getContentAsString());
        assertEquals( "mime type", mimeType, rr4.mimeType);
        assertEquals( "status", Status.C200_SUCCESS_OK, rr4.status);


    }


    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.rest.selftest.TestNotModified");//#LOGGER-NOPMD
}
