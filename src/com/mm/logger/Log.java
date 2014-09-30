/**
 * 日志输出工具 
 */
package com.mm.logger;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Log {
	
	public final static Logger logger = Logger.getLogger(Log.class);

	static {
		PropertyConfigurator.configure("log4j.properties");
	}
	
	
	public static void main(String[] args) {
		logger.info("hello");
		logger.error("error");
	}
}