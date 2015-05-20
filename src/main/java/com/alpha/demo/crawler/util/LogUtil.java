package com.alpha.demo.crawler.util;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class LogUtil{	
	/**
	 * initialize Log4J
	 */
	public static void initLog4j(){
        PropertyConfigurator.configure(Consts.LOG4J_FILE);
    }
	
    /**
     * print debug log
     * @param logger
     * @param str
     */
    public static void printDebugLog(Logger logger, String str){
    	logger.debug(str);
    }

    /**
     * print info log
     * @param llogger
     * @param str
     */
    public static void printSysLog(Logger llogger, String str) {
       llogger.info(str);
    }
    /**
     * print warn log
     * @param logger
     * @param str
     */
    public static void printWarnLog(Logger logger, String str){
        logger.warn(str);
    }
    /**
     * print error log
     * @param llogger
     * @param str
     */
    public static void printErrLog(Logger llogger, String str) {
       llogger.error(str);
    }
    
    /**
     * print error log
     * @param llogger
     * @param e
     */
    public static void printErrLog(Logger llogger, Exception e){
        {
            llogger.info("-------------------ERROR BEGIN------------------------");
            llogger.error(e.toString());
            llogger.error(e.getMessage());
            StackTraceElement[] stack = e.getStackTrace();
            for (int i = 0; i < stack.length; i++) {
                llogger.error(stack[i]);
            }
            llogger.error(e.getCause());
            llogger.info("--------------------ERROR END-------------------------");
        }
    }  
  
    
}

