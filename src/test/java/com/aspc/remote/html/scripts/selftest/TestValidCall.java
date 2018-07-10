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
package com.aspc.remote.html.scripts.selftest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import com.aspc.remote.html.*;
import com.aspc.remote.html.scripts.HTMLEvent;
import com.aspc.remote.util.misc.CLogger;
import com.aspc.remote.util.misc.StringUtilities;
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
public class TestValidCall extends TestCase
{
    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.html.scripts.selftest.TestValidCall");//#LOGGER-NOPMD

    /**
     * Creates new VirtualDBTestUnit
     * @param name The name of the test unit
     */
    public TestValidCall(String name)
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
        TestSuite suite = new TestSuite(TestValidCall.class);
        return suite;
    }

    /**
     * Check Valid call
     *
     * @throws Exception a serious problem
     */
    public void testValid() throws Exception
    {

        String checks[]={
            "status='hello'"
        };

        for( String call:checks)
        {
            if( HTMLEvent.validCall(call)==false)
            {
                fail( "Should be valid " + call);
            }
        }
    }

    /**
     * Check Valid call
     *
     * @throws Exception a serious problem
     */
    public void testInvalid() throws Exception
    {

        String checks[]={
            "status='O'Niel'"
        };

        for( String call:checks)
        {
            if( HTMLEvent.validCall(call))
            {
                fail( "Should be invalid " + call);
            }
        }
    }

}
