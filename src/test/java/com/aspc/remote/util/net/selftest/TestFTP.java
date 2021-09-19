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
package com.aspc.remote.util.net.selftest;
import com.aspc.remote.rest.ReST;
import com.aspc.remote.rest.Response;
import org.apache.commons.logging.Log;
import com.aspc.remote.util.misc.CLogger;
import com.aspc.remote.util.misc.CProperties;
import com.aspc.remote.util.net.NetClient;
import com.aspc.remote.util.net.NetUtil;
import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Check that we can connect to an FTP server.
 *  <br>
 *  <i>THREAD MODE: SINGLE-THREADED test case</i>
 *
 * @author Nigel Leck
 */
public class TestFTP extends TestCase
{
    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.util.net.selftest.TestFTP");//#LOGGER-NOPMD

    /**
     * Check a sample FTP server.
     * @throws Exception a test failure.
     */
    @SuppressWarnings("SleepWhileInLoop")
    public void testConnect() throws Exception
    {
        String testFtpURL = CProperties.findProperty("TEST_FTP_URL", "ftp://anonymous:@speedtest.tele2.net/");
        LOGGER.info("TEST FTP URL: " + testFtpURL);

        if(testFtpURL.contains("speedtest.tele2.net"))
        {
            Response r = ReST.builder("http://speedtest.tele2.net/").getResponse();

            if( r.status.isError())
            {
                LOGGER.warn("Could not connect:" + r.status);

                return;
            }
        }

        for( int attempts=0;true;attempts++)
        {
            try
            {

                NetClient client = null;
                client = NetUtil.borrowClient(testFtpURL);
                File temp = null;
                try
                {
                    temp = File.createTempFile("test", "test");
                    try (FileWriter fw = new FileWriter(temp)) {
                        fw.write("t");
                    }
                    String remotePath = "test-" + UUID.randomUUID() + ".txt";
                    client.send(temp, remotePath);

                    String[] files = client.retrieveFileList();
                    LOGGER.info("Number of FTP files: " + files.length);
                    for (String fileName : files)
                    {
                        LOGGER.info("file -> " + fileName);
                    }

                    client.fetch(remotePath, temp);

                    client.remove(remotePath);
                }
                finally
                {
                    if(temp != null)
                    {
                        temp.delete();
                    }
                    NetUtil.returnClient(client);
                }
                break;
                
            }
            catch (Exception e)
            {
                if (attempts > 5)
                {
                    LOGGER.fatal("could not FTP after " + attempts + " attempts", e);
                    throw e;
                }
                else
                {
                    LOGGER.warn(attempts + " retry FTP", e);
                    try
                    {
                        Thread.sleep((long) (10000 * Math.random() + 1000));
                    }
                    catch (InterruptedException ex)
                    {
                        LOGGER.warn("retrying FTP", ex);
                    }
                }
            }
        }
    }

    /**
     * Creates a new unit test
     * @param name the name of the unit
     */
    public TestFTP( final String name )
    {
        super( name );
    }

    /**
     * Creates the test suite
     * @return Test the test suite
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite( TestFTP.class );
        return suite;
    }

    /**
     * Entry point to run this test standalone
     * @param args the arguments to the test
     */
    public static void main( String[] args )
    {
        TestRunner.run( suite() );
    }
}
