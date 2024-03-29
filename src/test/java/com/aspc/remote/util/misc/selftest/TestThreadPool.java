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
package com.aspc.remote.util.misc.selftest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.aspc.remote.util.misc.*;

import org.apache.commons.logging.Log;

/**
 *  check thread pool
 *
 * <br>
 * <i>THREAD MODE: SINGLE-THREADED self test unit</i>
 *
 *  @author         Nigel Leck
 *  
 *  @since          24 Dec 2009
 */
public class TestThreadPool extends TestCase
{
    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.util.misc.selftest.TestThreadPool");//#LOGGER-NOPMD
    
    /**
     * Creates new VirtualDBTestUnit
     * @param name The name of the test unit
     */
    public TestThreadPool(String name)
    {
        super( name);
    }

    /**
     * run the tests
     * @param args the args
     */
    public static void main(String[] args)
    {
        TestRunner.run(suite());
    }

    /**
     * create a test suite
     * @return the suite of tests
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite(TestThreadPool.class);
        return suite;
    }

    /**
     * Check the wait for timeout
     *
     * @throws Exception a serious problem
     */
    public void testWaitForTimeout() throws Exception
    {

        Runnable r1 = new Runnable( )
        {

            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(60000);
                }
                catch( Exception e )
                {
                    LOGGER.warn( "did not sleep", e);
                }
            }
        };

        ThreadPool.schedule(r1);

        long startTS = System.currentTimeMillis();
        ThreadPool.waitFor(r1, 1000);
        long endTS=System.currentTimeMillis();

        if( endTS-startTS > 10000)
        {
            fail( "didn't timeout " + TimeUtil.getDiff(startTS, endTS));
        }

    }
}
