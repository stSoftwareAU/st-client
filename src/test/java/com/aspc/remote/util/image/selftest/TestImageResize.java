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
package com.aspc.remote.util.image.selftest;

import com.aspc.remote.rest.ReST;
import com.aspc.remote.rest.Response;
import com.aspc.remote.util.image.ImageResize;
import com.aspc.remote.util.image.ImageUtil;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.aspc.remote.util.misc.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLConnection;
import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;

/**
 *  Check image resize
 *
 * <br>
 * <i>THREAD MODE: SINGLE-THREADED self test unit</i>
 *
 *  @author         Nigel Leck
 *  @since          1 Feb 2014
 */
public class TestImageResize extends TestCase
{
    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.util.image.selftest.TestImageResize");//#LOGGER-NOPMD

    private static boolean firstRun=true;

    /**
     * Creates new VirtualDBTestUnit
     * @param name The name of the test unit
     */
    public TestImageResize(String name)
    {
        super( name);
    }

    /**
     * run the tests
     * @param args the args
     */
    public static void main(String[] args)
    {
        Test test=suite();
//        test=TestSuite.createTest(TestImageResize.class, "testXIcon");
        TestRunner.run(test);
    }

    /**
     * create a test suite
     * @return the suite of tests
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite(TestImageResize.class);
        return suite;
    }
    
    public void testScaleSVG()throws Exception 
    {
        File srcFile=new File( System.getProperty("SRC_DIR"), "/com/aspc/remote/util/image/selftest/play.svg");
           
        ImageResize ir = new ImageResize(srcFile);
        ir.setMaxWidth(200);
        File scaledImage = ir.process();
        assertTrue("don't scale SVG", scaledImage.exists());
    }    
    
    public void testXIcon() throws Exception 
    {
        //image/x-icon
        File srcFile=load( "favicon", "http://www.sydneyshardrockstory.com/docs/web/shr/favicon.ico");
           
        ImageResize ir = new ImageResize(srcFile);
        ir.setMaxWidth(200);
        File scaledImage = ir.process();

        LOGGER.info( scaledImage);
        assertTrue("should exist", scaledImage.exists());
    }

    public void testScaleJPG()throws Exception 
    {
        File srcFile=load( "scaleJPG", "https://aspc.jobtrack.com.au/docs/web/images/avatar/Tina_2010%5B1%5D.jpg?height=100");
           
        BufferedImage targetImage ;
                
        targetImage = ImageIO.read(srcFile);
        
        int h=targetImage.getHeight();
        
        assertEquals( "rounding up of scaled height", 100, h);
    }

    public void testRaster()throws Exception 
    {
        File srcFile=load( "smeg-raster", "https://crm.smegateway.com.au/docs/web/images%20from%20Gaye/Dee-Logos/Hifrasergroup_Spot.jpg");
        
        //ImageIO.read(srcFile);
        process( srcFile, 50, 50,"", -1, -1,-1, -1);
    }

    /**
     * Checks we handle Bit maps
     * @throws IOException if an IO exception occurs.
     */
    public void testBMP()throws Exception
    {
        File srcFile=load( 
            "srl-Brochure", 
            "http://www.ece.rice.edu/~wakin/images/lena512.bmp"
//            "http://shawreynolds.com/docs/web/images/MODE11429_IWP_Brochure_S5A.pdf%20-%20Adobe%20Reader.bmp"
        );
        if( srcFile.getName().endsWith(".bmp")==false)
        {
            fail( "wrong format transferred " + srcFile.getName());
        }
        process( srcFile, -1, -1,"png", -1, -1,-1, 600);
        process( srcFile, 555, 320,"", -1, -1,-1, -1);
        process( srcFile, 555, 320,"", -1, -1,-1, 300);
        process( srcFile, -1, -1,"jpg", -1, -1,-1, -1);
    }

    /**
     * Check we handle inconsistent meta data.
     * @throws IOException if an IO exception occurs.
     */
    public void testHandleInconsistent2()throws Exception
    {
        File srcFile=load( "smeg-bs", "http://crm.smegateway.com.au/docs/web/smeg/capabilities/BUSINESS%20SUPPORT.jpeg");
        process( srcFile, 150, 150,"", -1, -1,-1, -1);
    }
    
    /**
     * Checks large image to small
     * @throws IOException if an IO exception occurs.
     */
    public void testGlyphIcons()throws Exception
    {
        File logo=load( "glyphicons", "http://aspc.jobtrack.com.au/docs/web/images/glyphicons-halflings.png");
        process( logo, 48, 48,"", -1, -1,-1, -1);
    }
    
    public void testMime() throws Exception
    {
        File srcFile=load( "404.jpg", "http://stSoftware.com.au/images/404.jpg");

        ImageResize ir=new ImageResize(srcFile);

        ir.setFormat("");
        ir.setHeight( -1);
        ir.setWidth(-1);
        ir.setMaxHeight( -1);
        ir.setMaxWidth(-1);
        ir.setQuality( 100);

        File rawFile=ir.process();
        boolean doesContentMatch = FileUtil.doesContentMatch(srcFile, rawFile);
        assertTrue( "content match", doesContentMatch);
        
        String mime=ir.getMimeType();
        
        assertEquals( "Mime should not change", "image/jpg", mime);
        
        ir.setFormat("png");
        
        mime=ir.getMimeType();
        
        assertEquals( "Mime should not change", "image/png", mime);   
    }
    
     public void testNoChange() throws Exception
    {
        File srcFile=load( "design", "http://aspc.jobtrack.com.au/docs/web/st/page-design.jpg");

        ImageResize ir=new ImageResize(srcFile);

        ir.setMaxWidth(10000);
        
        File targetFile = ir.process();
        
        boolean doesContentMatch = FileUtil.doesContentMatch(srcFile, targetFile);
        
        assertTrue( "no need to re-generate if no change", doesContentMatch);
    }    
     
    public void testDPI() throws Exception
    {
        File srcFile=load( "landscape", "http://aspc.jobtrack.com.au/docs/web/st/help/resize/landscape.jpg");
        //ImageUtil.dump(srcFile);
        
        int dpi = ImageUtil.getDPI(srcFile);
        assertEquals( "check DPI", 300, dpi);
        
        process( srcFile, 100, -1,"", -1, -1,-1, -1);
        process( srcFile, -1, -1,"", -1, -1,-1, 72);
        process( srcFile, -1, -1,"", -1, -1,-1, 96);
        process( srcFile, -1, -1,"", -1, -1,-1, 300);
        process( srcFile, -1, -1,"", -1, -1,-1, 600);
    }    
      
    public void testDPIChange() throws Exception
    {
        File srcFile=load( "design", "http://aspc.jobtrack.com.au/docs/web/st/page-design.jpg");
        process( srcFile, 100, -1,"", -1, -1,-1, 96);
        process( srcFile, 149, -1,"", -1, -1,-1, 96);
        process( srcFile, 149, 35,"", -1, -1,-1, 96);
        process( srcFile, -1, 30,"", -1, -1,-1, 96);
        process( srcFile, -1, 70,"", -1, -1,-1, 96);
        process( srcFile, 200, 100,"", -1, -1,-1, 96);
        process( srcFile, -1, -1,"jpg", -1, -1,-1, 96);
        process( srcFile, -1, -1,"png", -1, -1,-1, 96);
        process( srcFile, -1, -1,"gif", -1, -1,-1, 96);
        process( srcFile, -1, -1,"", 1000, 1000,-1, 96);
        process( srcFile, -1, -1,"", 100, 1000,-1, 96);
        process( srcFile, -1, -1,"", -1, 30,-1, 96);
    }    
    
    public void testGifDPI() throws Exception
    {
        File srcFile=load( "logo2", "http://aspc.jobtrack.com.au/docs/web/logo.gif");
        ImageUtil.dump(srcFile);
                
        int dpi = ImageUtil.getDPI(srcFile);
        assertEquals( "check DPI", 300, dpi);
        ImageResize ir=new ImageResize(srcFile);

        ir.setWidth(100);
        File smallFile = ir.process();

        ImageUtil.dump(smallFile);
        
        dpi = ImageUtil.getDPI(smallFile);
        assertEquals( "check DPI", 300, dpi);
    }
    
    /**
     * Checks what happens if not an image.
     * 
     * @throws IOException if an IO exception occurs.
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void testNonImage()throws Exception, Exception
    {
        File tmpFile=File.createTempFile("non-image", ".txt",FileUtil.makeQuarantineDirectory());
        try{
            try (FileWriter fw = new FileWriter( tmpFile)) {
                fw.write("Hello World");
            }
            try
            {
                new ImageResize( tmpFile);
                fail( "should have failed");
            }
            catch( IOException io)
            {
                // good
            }
        }
        finally{
            tmpFile.delete();
        }
    }
    
    /**
     * Checks what happens if non-existing
     * 
     * @throws IOException if an IO exception occurs.
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void testNonExisting()throws Exception, Exception
    {
        File tmpFile=File.createTempFile("image", ".png",FileUtil.makeQuarantineDirectory());
     
        try
        {
            new ImageResize( tmpFile);
            fail( "did not detect missing file");
        }
        catch( IllegalArgumentException iae)
        {
            //good
        }
        finally{
            tmpFile.delete();
        }
    }
    
    /**
     * Checks 24->48
     * @throws IOException if an IO exception occurs.
     */
    public void testClose24by24()throws Exception
    {
        File logo=load( "close", "http://aspc.jobtrack.com.au/docs/web/images/close_24x24.png");
        process( logo, 48, 48,"", -1, -1,-1,-1);
    }
    
    /**
     * Checks 32->48 & 32->1
     * @throws IOException if an IO exception occurs.
     */
    public void testRedo32by32()throws Exception
    {
        File image=load( "redo", "http://aspc.jobtrack.com.au/docs/web/images/redo-disabled.png");
        process( image, 48, 48,"", -1, -1,-1, -1);
        process( image, 48, 48,"", -1, -1,0.01f, -1);
        process( image, 1, -1,"", -1, -1,-1, -1);
        process( image, -1, 1,"", -1, -1,-1, -1);
        process( image, -1, -1,"", 1, -1,-1, -1);
        process( image, -1, -1,"", -1, 1,-1, -1);
        process( image, -1, -1,"", -1, -1, 1, -1);
    }
    
    public void testTranslate()
    {
        double pixelMM = ImageUtil.convertDPI2PixelMM(300);
        
        int dpi = ImageUtil.convertDPI(pixelMM);
        
        assertEquals("DPI", 300, dpi);
    }
    
    public void testConvert() throws Exception
    {
        File logo=load( "logo3", "http://aspc.jobtrack.com.au/docs/web/logo.gif");
        
        process( logo, -1, -1,"jpg", -1, -1,-1, -1);
    }
    
    public void testConvert2() throws Exception
    {
        File logo=load( "logo4", "http://aspc.jobtrack.com.au/docs/web/logo.gif");
        
        process( logo, -1, -1,"gif", -1, -1,-1, -1);
    }
    /**
     * Check we can process images.
     * @throws Exception a serious problem
     */
    public void testProcess() throws Exception
    {
        File logo=load( "logo5", "http://aspc.jobtrack.com.au/docs/web/logo.gif");

        process( logo, 100, -1,"", -1, -1,-1, -1);
        process( logo, 149, -1,"", -1, -1,-1, -1);
        process( logo, 149, 35,"", -1, -1,-1, -1);
        process( logo, -1, 30,"", -1, -1,-1, -1);
        process( logo, -1, 70,"", -1, -1,-1, -1);
        process( logo, 200, 100,"", -1, -1,-1, -1);
        process( logo, -1, -1,"jpg", -1, -1,-1, -1);
        process( logo, -1, -1,"png", -1, -1,-1, -1);
        process( logo, -1, -1,"gif", -1, -1,-1, -1);
        process( logo, -1, -1,"", 1000, 1000,-1, -1);
        process( logo, -1, -1,"", 100, 1000,-1, -1);
        process( logo, -1, -1,"", -1, 30,-1, -1);

        File email=load( "email", "http://aspc.jobtrack.com.au/explorer/transfer/docs/web/st/email-marketing_1698x1131.jpg");
        process( email, -1, -1,"",   300, -1,-1, -1);
        process( email, -1, -1,"jpg", -1, -1,-1, -1);
        process( email, -1, -1,"png", -1, -1,-1, -1);
        process( email, -1, -1,"png", -1, 300,-1, -1);
        process( email, 200, 500,"png", -1, 300,-1, -1);
        process( email, -1, 500,"png", 200, 200, -1, -1);
        process( email, -1, -1,"gif", -1, -1,-1, -1);

        process( email, -1, -1,"", -1, -1,0.1f, -1);
        process( email, -1, -1,"jpg", -1, -1, 0.3f, -1);
        process( email, -1, -1,"png", -1, -1, 0.5f, -1);
        process( email, -1, -1,"gif", -1, -1, 0.2f, -1);
    }

    @Override
    /**
     * Set up the data universe ready for the test. A test unit maybe stopped half way through the processing so
     * we can not rely on the tear down process to set up the data for the next run.
     * 
     * @throws java.lang.Exception A serious problem
     */
    public void setUp()throws Exception
    {
        if( firstRun)
        {
            firstRun=false;
            File dir= new File( FileUtil.getCachePath() + "/test/resize");
            if( dir.exists())
            {
                for( File f: dir.listFiles())
                {
                    f.delete();
                }
            }
        }
    }

    @SuppressWarnings("SleepWhileInLoop")
    private File load( final String name, final String src) throws Exception
    {                
        File dir= new File( FileUtil.getCachePath() + "/test/resize");
        dir.mkdirs();
        Response r=null;
        for( int attempt=0;attempt<3;attempt++)
        {
            r = ReST.builder(src).setMinCachePeriod("7 days").getResponse();
            
            if( r.status.isError()==false) break;
        }
        assert r!=null;
        r.checkStatus();
        
        String redirect=r.redirection;
        if( redirect!=null && StringUtilities.notBlank(redirect))
        {
            r=ReST.builder(redirect).setMinCachePeriod("7 days").getResponseAndCheck();
        }

        String format;
        if(r.mimeType.endsWith("png"))
        {
            format="png";
        }
        else if(r.mimeType.endsWith("jpeg") || r.mimeType.endsWith("jpg") )
        {
            format="jpg";
        }
        else if(r.mimeType.endsWith("gif"))
        {
            format="gif";
        }
        else if(r.mimeType.endsWith("bmp"))
        {
            format="bmp";
        }
        else if(
            r.mimeType.endsWith("image/vnd.microsoft.icon")||
            r.mimeType.endsWith("image/x-icon")
        )
        {
            format="ico";
        }
        else
        {
            throw new IOException( "unknown type " + r.mimeType);
        }

        File orginal = new File( dir, name + "." + format);

        FileUtil.copy(r.getContentAsFile(), orginal);
        
        return orginal;
    }

    private void process(
        final File srcFile,
        final int w,
        final int h,
        final String requiredFormat,
        final int mw,
        final int mh,
        final float quality,
        final int dpi
    ) throws Exception
    {
        ImageResize ir = new ImageResize( srcFile);
        ir.setWidth(w);
        ir.setHeight(h);
        ir.setDPI(dpi);
        if(quality>0 )
        {
            ir.setQuality((int)(quality * 100));
        }
        ir.setMaxWidth(mw);
        ir.setMaxHeight(mh);
        ir.setFormat(requiredFormat);

        File process = ir.process();

        String mimeType = ir.getMimeType();

        String format;
        if(mimeType.endsWith("png"))
        {
            format="png";
        }
        else if(mimeType.endsWith("jpg")||mimeType.endsWith("jpeg"))
        {
            format="jpg";
        }
        else if(mimeType.endsWith("gif"))
        {
            format="gif";
        }
        else if(mimeType.endsWith("bmp"))
        {
            format="bmp";
        }
        else if(mimeType.endsWith("ico"))
        {
            format="ico";
        }
        else
        {
            throw new IOException( "unknown type " + mimeType);
        }
        String name = srcFile.getName();
        int pos = name.lastIndexOf(".");
        String fn = name.substring(0, pos) + "(";

        boolean start=false;
        if( w >=0)
        {
            fn += "width=" + w;
            start=true;
        }

        if( h >= 0)
        {
            if( start) fn += "&";
            start=true;

            fn += "height=" + h;
        }
        if( StringUtilities.notBlank(requiredFormat))
        {
            if( start) fn += "&";
            start=true;

            fn += "format=" + requiredFormat;
        }
        if( mw >= 0)
        {
            if( start) fn += "&";
            start=true;

            fn += "max-width=" + mw;
        }
        if( mh >= 0)
        {
            if( start) fn += "&";
            start=true;

            fn += "max-height=" + mh;
        }
        if( dpi >= 0)
        {
            if( start) fn += "&";
            start=true;

            fn += "dpi=" + dpi;
        }
        if( quality>0)
        {
            if( start) fn += "&";
         //   start=true;

            fn += "quality=" + (int)(quality * 100);
        }

        fn += ")." + format;

        File targetFile=new File( srcFile.getParentFile(), fn);

        FileUtil.copy(process, targetFile);
        BufferedImage orginalImage=null;
        try
        {
            orginalImage = ImageIO.read(srcFile);
        }
        catch( IllegalArgumentException e)
        {
            LOGGER.info( "could not read orginal file", e);
        }
        BufferedImage targetImage ;
                
        targetImage = ImageIO.read(targetFile);
        
        assertNotNull(targetFile.toString(), targetImage);
        
        if( w >0 )
        {
            assertEquals( "check width", w, targetImage.getWidth());
        }

        if( h >0 )
        {
            assertEquals( "check height", h, targetImage.getHeight());
        }

        if( h<=0 && w <= 0 && mh > 0 && mh < targetImage.getHeight())
        {
            fail( targetImage.getHeight() + " greater than max height " + mh);
        }

        if( mw > 0 && mw < targetImage.getWidth())
        {
            fail( targetImage.getWidth() + " greater than max width " + mw);
        }

        if(orginalImage!=null && mh >0 && mw <= 0 && w <=0 && mh < orginalImage.getHeight())
        {
            if( orginalImage.getWidth() <= targetImage.getWidth())
            {
                fail( "should have scaled down image when filtered by max-height");
            }
        }

        if(orginalImage!=null && mw >0 && mh <= 0 && h <=0 && mw < orginalImage.getWidth())
        {
            if( orginalImage.getHeight()<= targetImage.getHeight())
            {
                fail( "should have scaled down image when filtered by max-width");
            }
        }
                
        int srcDPI = ImageUtil.getDPI(srcFile);
        int checkDPI;
        if( dpi > 0)
        {
            checkDPI=dpi;
        }
        else
        {
            checkDPI=srcDPI;
        }
        int targetDPI = ImageUtil.getDPI(targetFile);
        if( format.equals( "gif") == false &&format.equals( "bmp") == false && checkDPI != targetDPI)
        {
            fail( "Should not have changed the DPI " + checkDPI + "->" + targetDPI);
        }
        
        if( format.equals( "bmp") == false)
        {
            try
            (BufferedInputStream is = new BufferedInputStream(new FileInputStream(targetFile))) 
            {
                String guessMimeType = URLConnection.guessContentTypeFromStream(is);
                if(guessMimeType == null) 
                {
                    fail( "could not read file " + targetFile);
                }
            }          
        }
    }
}
