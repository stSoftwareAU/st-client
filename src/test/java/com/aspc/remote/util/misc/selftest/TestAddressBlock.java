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
import java.math.BigInteger;
import java.net.InetAddress;

import org.apache.commons.logging.Log;

/**
 *  Check DNS Black list
 *
 * <br>
 * <i>THREAD MODE: SINGLE-THREADED self test unit</i>
 *
 *  @author         Nigel Leck
 *  
 *  @since          3 August 2014
 */
public class TestAddressBlock extends TestCase
{
    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.util.misc.selftest.TestAddressBlock");//#LOGGER-NOPMD

    /**
     * Creates new VirtualDBTestUnit
     * @param name The name of the test unit
     */
    public TestAddressBlock(String name)
    {
        super( name);
    }

    /**
     * run the tests
     * @param args the args
     */
    public static void main(String[] args)
    {
        Test test = suite();
//        test= TestSuite.createTest(TestAddressBlock.class, "testHack");
        TestRunner.run(test);
        System.exit(0);
    }

    /**
     * create a test suite
     * @return the suite of tests
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite(TestAddressBlock.class);
        return suite;
    }

    @SuppressWarnings("SleepWhileInLoop")
    public void testTimeout() throws Exception
    {
        int ip = 0;

        // iterate over each octet
        for(String part : "123.45.55.34".split("\\."))
        {
            // shift the previously parsed bits over by 1 byte
            ip = ip << 8;
            // set the low order bits to the current octet
            ip |= Integer.parseInt(part);
        }

        AddressBlock bl = AddressBlock.builder().create();
        for( int loop=0;true;loop++)
        {
            ip++;

            byte[] bytes = BigInteger.valueOf(ip).toByteArray();
            InetAddress address = InetAddress.getByAddress(bytes);

            String reason = bl.getReason(address.getHostAddress(), 0);

            if( reason == null)
            {
                for( int checks =0; checks<600; checks++)
                {
                    Thread.sleep(100);
                    reason = bl.getReason(address.getHostAddress(), 0);

                    if( reason != null)
                    {
                        return;
                    }
                }

                fail( "Did not reslove " + address);
            }
            if( loop > 100)
            {
                fail( "too many attempts");
            }
        }
    }

    /**
     * Check that known BOTs are blocked
     * @throws Exception a serious problem
     */
    public void testDisabled() throws Exception
    {
        String list[]={"146.0.73.133","89.248.172.204"};

        AddressBlock bl = AddressBlock.builder().setDisabled(true).create();

        for( String ip: list)
        {
            String reason = bl.getReason(ip, 60000);

            assertNull("Should not have checked " + ip, reason);
            assertFalse( "Should NOT know about " + ip, bl.knowsIP(ip));
        }
    }

    /**
     * Check that known BOTs are blocked
     * @throws Exception a serious problem
     */
    public void testKnownBotIP() throws Exception
    {
        String list[]={
//            "222.186.34.23"
//            "146.0.73.133",
        //    "89.248.172.204"
        };

        AddressBlock bl = AddressBlock.builder().create();

        for( String ip: list)
        {
            String reason = bl.getReason(ip, 60000);

            assertTrue("Should be bad " + ip, StringUtilities.notBlank(reason));
            assertTrue( "Should know about " + ip, bl.knowsIP(ip));
        }
    }

    /**
     * Check that clean IPs are OK
     *
     * @throws Exception a serious problem
     */
    public void testKnownCleanIP() throws Exception
    {
        String list[]={
            "101.0.69.242",
            "101.0.69.243",
            "101.0.96.194",
            "101.0.96.195",
            "101.0.96.196",
            "101.0.96.197",
            "101.0.107.40",
            "101.0.107.41",
            "101.0.107.42",
            "101.0.107.43",
        };
        AddressBlock bl = AddressBlock.builder().create();

        for( String ip: list)
        {
            String reason = bl.getReason(ip, 60000);
            assertNotNull("should not have timed out", reason);
            assertTrue("Should be clean " + ip, StringUtilities.isBlank(reason));
            assertTrue( "Should know about " + ip, bl.knowsIP(ip));
        }
    }
     
        /**
     * Check that clean IPs are OK
     *
     * @throws Exception a serious problem
     *
    public void testHack2() throws Exception
    {
        String text=//"110.84.88.229\n" +
//"112.124.0.81\n" +
//"195.3.144.84\n" +
"208.115.113.83\n" +
"213.4.38.162\n" +
"5.135.43.132\n" +
"5.149.147.197\n" +
"5.152.214.130\n" +
"66.249.69.1\n" +
"66.249.69.105\n" +
//"69.39.238.126\n";// +
"82.146.38.254";
        
        text = "100.43.91.24\n" +
"101.0.103.242\n" +
"104.152.208.21\n" +
"109.237.108.205\n" +
"110.84.88.229\n" +
"112.124.0.81\n" +
"114.76.180.8\n" +
"117.24.216.83\n" +
"124.207.151.33\n" +
"157.55.39.145\n" +
"162.210.196.98\n" +
"177.21.255.252\n" +
"178.74.245.74\n" +
"180.76.6.137\n" +
"189.170.118.158\n" +
"189.222.167.68\n" +
"190.200.20.107\n" +
"195.3.144.84\n" +
"198.50.167.10\n" +
"202.75.39.200\n" +
"205.186.128.100\n" +
"208.115.111.66\n" +
"208.115.113.83\n" +
"213.133.199.167\n" +
"213.4.38.162\n" +
"37.58.100.154\n" +
"37.58.100.171\n" +
"37.58.100.67\n" +
"50.22.53.71\n" +
"5.135.43.132\n" +
"5.149.147.197\n" +
"5.152.214.130\n" +
"54.187.189.195\n" +
"64.71.32.30\n" +
"66.249.69.1\n" +
"66.249.69.105\n" +
"66.249.69.217\n" +
"66.249.69.22\n" +
"66.249.69.233\n" +
"66.249.69.38\n" +
"66.249.69.89\n" +
"68.142.232.7\n" +
"69.39.238.126\n" +
"79.148.106.25\n" +
"80.69.161.148\n" +
"82.146.38.254\n" +
"82.207.102.82\n" +
"91.239.138.92\n" +
"93.170.128.217";
        
        text="100.43.91.24\n" +
"104.152.208.21\n" +
"110.84.88.229\n" +
"112.124.0.81\n" +
"114.76.180.8\n" +
"117.24.216.83\n" +
"157.55.39.145\n" +
"177.21.255.252\n" +
"180.76.6.137\n" +
"189.170.118.158\n" +
"190.200.20.107\n" +
"195.3.144.84\n" +
"198.50.167.10\n" +
"205.186.128.100\n" +
"213.133.199.167\n" +
"50.22.53.71\n" +
"64.71.32.30\n" +
"68.142.232.7\n" +
"69.39.238.126\n" +
"80.69.161.148\n" +
"82.207.102.82";
                String list[]=text.split("\n");
        AddressBlock bl = AddressBlock.builder().create();

        for( String ip: list)
        {
            String reason = bl.getReason(ip, 60000);
            if( StringUtilities.notBlank(reason))
            {
                LOGGER.info( ip + " " + reason);
            }
            else
            {
                LOGGER.info( ip + " not BLOCKED" );
            }
        }
    }*/
    
        /**
     * Check that clean IPs are OK
     *
     * @throws Exception a serious problem
     *
    public void testHack() throws Exception
    {
        String text="100.43.81.141\n" +
"100.43.90.11\n" +
"100.43.90.12\n" +
"100.43.91.24\n" +
"101.0.69.242\n" +
"101.0.96.194\n" +
"101.162.47.251\n" +
"101.168.127.225\n" +
"101.170.213.74\n" +
"101.170.85.59\n" +
"101.170.85.82\n" +
"101.171.127.231\n" +
"101.171.42.149\n" +
"101.171.42.155\n" +
"101.171.85.67\n" +
"101.172.170.143\n" +
"101.175.131.190\n" +
"101.187.6.233\n" +
"103.6.87.177\n" +
"104.128.16.56\n" +
"104.131.210.142\n" +
"104.131.210.222\n" +
"104.131.211.31\n" +
"106.68.140.1\n" +
"107.168.10.35\n" +
"107.169.2.195\n" +
"107.181.78.81\n" +
"108.178.59.74\n" +
"109.205.249.13\n" +
"110.84.88.229\n" +
"1.127.42.155\n" +
"113.197.9.114\n" +
"116.25.209.169\n" +
"117.120.16.134\n" +
"117.120.18.132\n" +
"117.78.13.18\n" +
"120.150.137.227\n" +
"120.150.194.8\n" +
"121.212.228.73\n" +
"121.218.77.205\n" +
"121.44.127.236\n" +
"123.125.71.115\n" +
"123.125.71.15\n" +
"123.125.71.20\n" +
"123.125.71.52\n" +
"123.125.71.69\n" +
"123.125.71.78\n" +
"124.168.55.252\n" +
"124.170.194.59\n" +
"124.171.36.224\n" +
"125.168.217.161\n" +
"131.253.24.12\n" +
"138.25.2.143\n" +
"138.25.2.158\n" +
"142.217.240.161\n" +
"142.4.213.178\n" +
"144.76.95.39\n" +
"147.252.211.42\n" +
"148.251.124.174\n" +
"157.55.39.155\n" +
"157.55.39.203\n" +
"157.55.39.206\n" +
"157.55.39.235\n" +
"157.55.39.236\n" +
"157.55.39.253\n" +
"157.55.39.254\n" +
"157.55.39.31\n" +
"157.55.39.36\n" +
"157.55.39.41\n" +
"157.55.39.73\n" +
"157.55.39.94\n" +
"162.210.196.130\n" +
"173.208.182.27\n" +
"173.208.39.231\n" +
"173.234.169.99\n" +
"176.9.34.171\n" +
"178.203.147.64\n" +
"178.255.215.65\n" +
"178.255.215.72\n" +
"178.255.215.73\n" +
"178.255.215.84\n" +
"178.255.215.88\n" +
"178.255.215.89\n" +
"178.3.131.64\n" +
"178.33.22.168\n" +
"178.33.80.66\n" +
"180.180.76.16\n" +
"180.76.5.143\n" +
"180.76.5.144\n" +
"180.76.5.145\n" +
"180.76.5.146\n" +
"180.76.5.147\n" +
"180.76.5.148\n" +
"180.76.5.149\n" +
"180.76.5.150\n" +
"180.76.5.151\n" +
"180.76.5.152\n" +
"180.76.5.153\n" +
"180.76.5.154\n" +
"180.76.5.155\n" +
"180.76.5.166\n" +
"180.76.5.169\n" +
"180.76.5.17\n" +
"180.76.5.170\n" +
"180.76.5.171\n" +
"180.76.5.172\n" +
"180.76.5.173\n" +
"180.76.5.175\n" +
"180.76.5.176\n" +
"180.76.5.177\n" +
"180.76.5.18\n" +
"180.76.5.187\n" +
"180.76.5.188\n" +
"180.76.5.189\n" +
"180.76.5.19\n" +
"180.76.5.190\n" +
"180.76.5.191\n" +
"180.76.5.192\n" +
"180.76.5.193\n" +
"180.76.5.194\n" +
"180.76.5.195\n" +
"180.76.5.196\n" +
"180.76.5.197\n" +
"180.76.5.20\n" +
"180.76.5.21\n" +
"180.76.5.22\n" +
"180.76.5.223\n" +
"180.76.5.23\n" +
"180.76.5.24\n" +
"180.76.5.25\n" +
"180.76.5.26\n" +
"180.76.5.27\n" +
"180.76.5.28\n" +
"180.76.5.57\n" +
"180.76.5.58\n" +
"180.76.5.59\n" +
"180.76.5.60\n" +
"180.76.5.61\n" +
"180.76.5.62\n" +
"180.76.5.64\n" +
"180.76.5.65\n" +
"180.76.5.67\n" +
"180.76.5.71\n" +
"180.76.5.72\n" +
"180.76.5.73\n" +
"180.76.5.74\n" +
"180.76.5.75\n" +
"180.76.5.76\n" +
"180.76.5.77\n" +
"180.76.5.78\n" +
"180.76.5.80\n" +
"180.76.5.81\n" +
"180.76.5.94\n" +
"180.76.5.95\n" +
"180.76.6.130\n" +
"180.76.6.131\n" +
"180.76.6.132\n" +
"180.76.6.133\n" +
"180.76.6.134\n" +
"180.76.6.135\n" +
"180.76.6.136\n" +
"180.76.6.137\n" +
"180.76.6.138\n" +
"180.76.6.139\n" +
"180.76.6.14\n" +
"180.76.6.140\n" +
"180.76.6.141\n" +
"180.76.6.142\n" +
"180.76.6.144\n" +
"180.76.6.145\n" +
"180.76.6.146\n" +
"180.76.6.147\n" +
"180.76.6.148\n" +
"180.76.6.149\n" +
"180.76.6.150\n" +
"180.76.6.151\n" +
"180.76.6.152\n" +
"180.76.6.153\n" +
"180.76.6.155\n" +
"180.76.6.156\n" +
"180.76.6.157\n" +
"180.76.6.158\n" +
"180.76.6.159\n" +
"180.76.6.16\n" +
"180.76.6.17\n" +
"180.76.6.20\n" +
"180.76.6.21\n" +
"180.76.6.213\n" +
"180.76.6.225\n" +
"180.76.6.230\n" +
"180.76.6.231\n" +
"180.76.6.232\n" +
"180.76.6.233\n" +
"180.76.6.28\n" +
"180.76.6.29\n" +
"180.76.6.36\n" +
"180.76.6.37\n" +
"180.76.6.41\n" +
"180.76.6.42\n" +
"180.76.6.43\n" +
"180.76.6.44\n" +
"180.76.6.45\n" +
"180.76.6.46\n" +
"180.76.6.47\n" +
"180.76.6.48\n" +
"180.76.6.49\n" +
"180.76.6.50\n" +
"180.76.6.51\n" +
"180.76.6.52\n" +
"180.76.6.53\n" +
"180.76.6.54\n" +
"180.76.6.55\n" +
"180.76.6.56\n" +
"180.76.6.57\n" +
"180.76.6.58\n" +
"180.76.6.59\n" +
"180.76.6.60\n" +
"180.76.6.61\n" +
"180.76.6.62\n" +
"180.76.6.63\n" +
"180.76.6.64\n" +
"180.76.6.65\n" +
"180.76.6.66\n" +
"185.10.104.130\n" +
"185.10.104.131\n" +
"185.10.104.132\n" +
"185.10.104.195\n" +
"189.47.160.161\n" +
"192.151.152.203\n" +
"192.162.241.252\n" +
"193.201.224.8\n" +
"198.23.152.247\n" +
"198.27.82.146\n" +
"198.27.82.152\n" +
"198.50.183.74\n" +
"198.50.238.243\n" +
"198.55.112.64\n" +
"199.21.99.207\n" +
"199.30.20.13\n" +
"202.7.189.18\n" +
"203.201.132.13\n" +
"203.206.176.223\n" +
"203.45.8.67\n" +
"203.52.130.181\n" +
"203.6.69.2\n" +
"203.94.175.66\n" +
"207.210.107.244\n" +
"207.46.13.10\n" +
"207.46.13.106\n" +
"207.46.13.109\n" +
"207.46.13.123\n" +
"207.46.13.2\n" +
"207.46.13.29\n" +
"207.46.13.36\n" +
"207.46.13.77\n" +
"208.101.233.156\n" +
"208.107.72.37\n" +
"208.115.111.66\n" +
"208.115.111.68\n" +
"208.115.111.69\n" +
"208.115.111.71\n" +
"208.115.111.74\n" +
"208.115.113.83\n" +
"208.115.113.84\n" +
"208.115.113.85\n" +
"208.115.113.86\n" +
"208.115.113.93\n" +
"208.80.194.122\n" +
"208.80.194.127\n" +
"209.133.77.163\n" +
"209.133.77.164\n" +
"210.97.192.210\n" +
"211.138.121.38\n" +
"211.244.83.232\n" +
"2.120.72.14\n" +
"212.129.39.47\n" +
"217.96.18.163\n" +
"219.145.93.110\n" +
"220.181.108.116\n" +
"220.181.108.150\n" +
"220.181.108.177\n" +
"220.181.108.77\n" +
"221.143.48.108\n" +
"221.178.119.158\n" +
"23.244.164.158\n" +
"23.251.155.27\n" +
"27.121.88.94\n" +
"27.159.71.124\n" +
"31.13.99.112\n" +
"31.13.99.113\n" +
"31.13.99.114\n" +
"31.13.99.116\n" +
"31.13.99.117\n" +
"31.13.99.119\n" +
"31.31.72.22\n" +
"36.250.89.27\n" +
"37.58.100.130\n" +
"37.58.100.131\n" +
"37.58.100.133\n" +
"37.58.100.134\n" +
"37.58.100.135\n" +
"37.58.100.136\n" +
"37.58.100.139\n" +
"37.58.100.141\n" +
"37.58.100.143\n" +
"37.58.100.145\n" +
"37.58.100.147\n" +
"37.58.100.149\n" +
"37.58.100.151\n" +
"37.58.100.153\n" +
"37.58.100.162\n" +
"37.58.100.163\n" +
"37.58.100.164\n" +
"37.58.100.165\n" +
"37.58.100.167\n" +
"37.58.100.170\n" +
"37.58.100.172\n" +
"37.58.100.173\n" +
"37.58.100.174\n" +
"37.58.100.179\n" +
"37.58.100.182\n" +
"37.58.100.183\n" +
"37.58.100.184\n" +
"37.58.100.185\n" +
"37.58.100.187\n" +
"37.58.100.228\n" +
"37.58.100.229\n" +
"37.58.100.230\n" +
"37.58.100.232\n" +
"37.58.100.235\n" +
"37.58.100.236\n" +
"37.58.100.66\n" +
"37.58.100.69\n" +
"37.58.100.70\n" +
"37.58.100.73\n" +
"37.58.100.82\n" +
"37.58.100.84\n" +
"37.58.100.85\n" +
"37.58.100.86\n" +
"37.58.100.87\n" +
"37.58.100.90\n" +
"37.58.100.91\n" +
"37.58.100.93\n" +
"37.59.75.52\n" +
"49.183.237.123\n" +
"54.167.154.223\n" +
"54.210.235.245\n" +
"54.221.82.19\n" +
"54.81.18.166\n" +
"54.82.14.182\n" +
"54.85.248.243\n" +
"58.172.98.73\n" +
"58.7.201.115\n" +
"5.9.112.68\n" +
"59.167.175.177\n" +
"60.241.239.222\n" +
"61.135.190.104\n" +
"61.88.101.30\n" +
"64.235.150.221\n" +
"64.235.151.199\n" +
"64.235.154.195\n" +
"65.55.183.62\n" +
"65.55.215.41\n" +
"65.55.217.254\n" +
"66.249.64.10\n" +
"66.249.64.137\n" +
"66.249.64.142\n" +
"66.249.64.147\n" +
"66.249.64.15\n" +
"66.249.64.157\n" +
"66.249.64.20\n" +
"66.249.64.37\n" +
"66.249.64.41\n" +
"66.249.64.42\n" +
"66.249.64.47\n" +
"66.249.64.53\n" +
"66.249.64.57\n" +
"66.249.64.58\n" +
"66.249.64.62\n" +
"66.249.64.63\n" +
"66.249.64.67\n" +
"66.249.64.73\n" +
"66.249.64.78\n" +
"66.249.69.1\n" +
"66.249.69.101\n" +
"66.249.69.105\n" +
"66.249.69.11\n" +
"66.249.69.127\n" +
"66.249.69.13\n" +
"66.249.69.143\n" +
"66.249.69.159\n" +
"66.249.69.175\n" +
"66.249.69.18\n" +
"66.249.69.198\n" +
"66.249.69.214\n" +
"66.249.69.22\n" +
"66.249.69.230\n" +
"66.249.69.250\n" +
"66.249.69.252\n" +
"66.249.69.31\n" +
"66.249.69.61\n" +
"66.249.69.69\n" +
"66.249.69.73\n" +
"66.249.69.77\n" +
"66.249.69.85\n" +
"66.249.69.89\n" +
"66.249.69.93\n" +
"66.249.70.4\n" +
"66.249.73.138\n" +
"66.249.74.120\n" +
"66.249.74.142\n" +
"66.249.74.186\n" +
"66.249.74.191\n" +
"66.249.74.195\n" +
"66.249.74.197\n" +
"66.249.74.218\n" +
"66.249.74.245\n" +
"66.249.74.27\n" +
"66.249.74.32\n" +
"66.249.74.48\n" +
"66.249.74.58\n" +
"66.249.74.67\n" +
"66.249.74.76\n" +
"66.249.74.91\n" +
"66.249.77.101\n" +
"66.249.77.111\n" +
"66.249.77.119\n" +
"66.249.77.129\n" +
"66.249.77.13\n" +
"66.249.77.135\n" +
"66.249.77.139\n" +
"66.249.77.142\n" +
"66.249.77.145\n" +
"66.249.77.149\n" +
"66.249.77.15\n" +
"66.249.77.152\n" +
"66.249.77.153\n" +
"66.249.77.155\n" +
"66.249.77.159\n" +
"66.249.77.16\n" +
"66.249.77.162\n" +
"66.249.77.163\n" +
"66.249.77.165\n" +
"66.249.77.168\n" +
"66.249.77.169\n" +
"66.249.77.171\n" +
"66.249.77.173\n" +
"66.249.77.174\n" +
"66.249.77.175\n" +
"66.249.77.181\n" +
"66.249.77.185\n" +
"66.249.77.188\n" +
"66.249.77.191\n" +
"66.249.77.194\n" +
"66.249.77.197\n" +
"66.249.77.198\n" +
"66.249.77.20\n" +
"66.249.77.203\n" +
"66.249.77.207\n" +
"66.249.77.211\n" +
"66.249.77.213\n" +
"66.249.77.217\n" +
"66.249.77.218\n" +
"66.249.77.221\n" +
"66.249.77.224\n" +
"66.249.77.227\n" +
"66.249.77.23\n" +
"66.249.77.231\n" +
"66.249.77.232\n" +
"66.249.77.234\n" +
"66.249.77.24\n" +
"66.249.77.242\n" +
"66.249.77.244\n" +
"66.249.77.246\n" +
"66.249.77.25\n" +
"66.249.77.3\n" +
"66.249.77.30\n" +
"66.249.77.31\n" +
"66.249.77.41\n" +
"66.249.77.5\n" +
"66.249.77.51\n" +
"66.249.77.64\n" +
"66.249.77.7\n" +
"66.249.77.79\n" +
"66.249.77.82\n" +
"66.249.77.91\n" +
"67.163.59.44\n" +
"68.180.225.105\n" +
"69.39.238.126\n" +
"69.58.178.57\n" +
"69.64.41.236\n" +
"79.167.125.102\n" +
"81.65.128.188\n" +
"88.198.66.11\n" +
"89.145.108.203\n" +
"89.145.95.2\n" +
"91.121.220.8\n" +
"91.207.4.22\n" +
"93.189.221.26\n" +
"95.211.238.122\n" +
"98.191.214.122";
        String list[]=text.split("\n");
        LOGGER.info( "COUNT " + list.length);
        AddressBlock bl = AddressBlock.builder().create();
        for( String ip: list)
        {
            bl.getReason(ip, 0);
        }
        
        for( String ip: list)
        {
            String reason = bl.getReason(ip, 600000);
            if( StringUtilities.notBlank(reason))
            {
                InetAddress addr = InetAddress.getByName(ip);
                String host = addr.getCanonicalHostName();
                String tmpHost=ip;
                if( host.equalsIgnoreCase(ip)==false) tmpHost=host + "(" + ip + ")";
                URL url = new URL("http://freegeoip.net/json/" + ip);
                String val = ReST.get( url, "1 month");
                JSONObject json=new JSONObject(val);
                LOGGER.info( tmpHost + "\t" + json.get("country_name") + "\t" + json.get("region_name") + "\t" + json.get("city") + "\t" + reason);
                
            }
        }
    }*/
}
