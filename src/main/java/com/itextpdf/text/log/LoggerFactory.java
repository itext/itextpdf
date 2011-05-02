/**
 * 
 */
package com.itextpdf.text.log;

/**
 * @author Balder Van Camp
 * 
 */
public class LoggerFactory {

	static {
		myself = new LoggerFactory();
	}
	
	private static LoggerFactory myself;

	public static Logger getLogger() {
		return myself.logger;
	}

	public static LoggerFactory getInstance() {
		return myself;
	}
	
	private Logger logger = null;

	private LoggerFactory() {
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public Logger logger() {
		return logger;
	}
}
