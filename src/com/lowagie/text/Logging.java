/* 
 * $Id$
 * $Name$
 *
 * Copyright 2001 by Geert Poels  - Geert.Poels@skynet.be.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text;

import org.apache.log4j.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;


/*
*	This class will act like a singleton, holding the levels of logging for each class/package
*	@author Geert Poels
*/

public final class Logging
{
	private static Logging theLoggingSingleTon = null;
	
	private boolean LogLogClass = false; // :-)  got it ?
	
	/*
	*	Using  'if (false)' will cause the compiler to not include this statement
	*	in the compiled/binary classes.  This way conditional compilation can be simulated.
	*	Even if tests done by the Log4j team (), didn't reveal a significant slowdown;
	*	any speedimprovement may be welcome.
	*/
	final public static boolean INCLUDE_LOG_IN_BUILD = true;
	
	final public String DIR_LOG_OUTPUT	= "log";
	final public String FILE_LOG_CONFIG	= "log.ini";
	
	final public String FILE_LOG_OUTPUT			= "log.txt";
	final public String HTML_FILE_LOG_OUTPUT	= "log.html";

	final public static Priority DEFAULT_PRIORITY = Priority.DEBUG;
	
	boolean OverWriteLog	= true;
	boolean LogToFile		= true;
	boolean LogToConsole	= false;	// normally screen will be pumped with output so off by default
	
	static Category c;
	
	private Logging()
	{
		if (LogLogClass && (!INCLUDE_LOG_IN_BUILD) )	System.out.println("WARNING : log in the Logging-class was enabled but was disabled for all other classes.\nSet INCLUDE_LOG_IN_BUILD to true to do otherwise.");

		if (LogLogClass)	System.out.println("Start Newspickerlog in " + DIR_LOG_OUTPUT);

		try
		{
			// create outputdir
			File lDummy = new File("");
			File lOutputDir = new File(lDummy.getAbsolutePath() + File.separator + DIR_LOG_OUTPUT);
			if ( !lOutputDir.exists() )
			{
				lOutputDir.mkdir();
				if (LogLogClass)	System.out.println("Created Log outputdir : " + lDummy.getAbsolutePath() + File.separator + DIR_LOG_OUTPUT);
			}
			if (LogLogClass)	System.out.println("Log output to : " + lOutputDir);

			// parse configfile
			Enumeration	lEnum = null;
			String lKey = null, lValue = null;
			Properties lProperties = null;
			String lLogConfigPath	= lDummy.getAbsolutePath() + File.separator + FILE_LOG_CONFIG;
			File lLogConfigFile		= new File(lLogConfigPath);

			if ( lLogConfigFile.exists() )
			{
				if (LogLogClass) System.out.println("Start parsing logconfig file : " + lLogConfigPath);
				BufferedInputStream bi = new BufferedInputStream(new FileInputStream(lLogConfigFile));
				lProperties = new Properties();
				lProperties.load(bi);
				if (LogLogClass) {lProperties.list(System.out);	System.out.println("--------------------");}
				
				// some options may alter the logging behaviour so handle them first
				lEnum = lProperties.propertyNames();
				while ( lEnum.hasMoreElements() )
				{
					lKey = (String) lEnum.nextElement();
					lValue = lProperties.getProperty(lKey);

					if ( lKey.equals("overwrite") )
					{
						OverWriteLog = new Boolean(lValue).booleanValue();
						if (lProperties.remove(lKey) == null)
						{
							System.err.println("Error on removing properties key : " + lKey);
						}
					}
					else if ( lKey.equals("logtofile") )
					{
						LogToFile = new Boolean(lValue).booleanValue();
						if (lProperties.remove(lKey) == null)
						{
							System.err.println("Error on removing properties key : " + lKey);
						}

					}
					else if ( lKey.equals("logtoconsole") )
					{
						LogToConsole = new Boolean(lValue).booleanValue();
						if (lProperties.remove(lKey) == null)
						{
							System.err.println("Error on removing properties key : " + lKey);
						}
					}
					// add other options here
				}
			}
			else
			{
				if (LogLogClass) System.err.println("No log-config found !\n\t Assuming default priority of " + DEFAULT_PRIORITY.toString());
			}

			// set Logfile (erase)
			File lLog = new File(lDummy.getAbsolutePath() + File.separator + DIR_LOG_OUTPUT + File.separator + FILE_LOG_OUTPUT);
			if (OverWriteLog == true)
			{
				if (lLog.delete() == false)
				{
					System.err.println("Error on deleting previous logfile : " + lLog.getAbsolutePath() + ".  Will append");
				}
				else
				{
					if (LogLogClass) System.out.println("Previous log was deleted.");
				}
			}

			// create Logmechanism
			c = Category.getRoot();
			c.setPriority(DEFAULT_PRIORITY);
			
			if (LogToFile)
			{
				if (LogLogClass)	System.out.println(" Creating logfile at :" + lLog.getAbsolutePath());
				FileAppender fa = new FileAppender(new PatternLayout("%d{HH:mm:ss,SSS} %-5p [%t,%c]: %m%n"),lLog.getAbsolutePath());
				c.addAppender(fa);
			}
			if (LogToConsole)
			{
				if (LogLogClass)	System.out.println(" Forward log to console");
				ConsoleAppender ca = new ConsoleAppender(new PatternLayout("%d{HH:mm:ss,SSS} %-5p [%t,%c]: %m%n"));
				c.addAppender(ca);
			}

			if ( c.getAllAppenders().hasMoreElements() == false )
			{
				// oops, no appenders
				System.out.println("Fatal error : no appenders found for logging, check/create ini-file.");
				System.exit(1);						
			}

			// Set loglevels
			if ( lProperties != null )	// no properties were loaded from file
			{
				lEnum = lProperties.propertyNames();
				// Priorities created with a wrong name are set to DEBUG by default by Log4j 
				// we'll warn the user of this behaviour
				Priority[] p = Priority.getAllPossiblePriorities();
				ArrayList al = new ArrayList(p.length);
				for (int i=0; i < p.length;i++)
				{
					al.add(p[i].toString());
				}
	
				while ( lEnum.hasMoreElements() )
				{
					lKey = (String) lEnum.nextElement();
					System.out.println("***" + lKey);
					lValue = lProperties.getProperty(lKey).toUpperCase();
					if ( !al.contains(lValue) )	// non-existant priority
					{
						if (LogLogClass)	c.getRoot().debug("Priority " + lValue + " of " + lKey + " doesn't exist.  DEBUG is assumed");
					}
					c = c.getInstance(lKey);
					c.setPriority(Priority.toPriority(lValue));
					if (LogLogClass)	c.getRoot().debug("Set priority of " + c.getName() + " to "  + c.getPriority().toString());
				}
			}
			if (c == null)
			{
				System.err.println("Fatal error : Log was not intialised");
				System.exit(1);						
			}
			else if (LogLogClass)
			{
				// print all categories
				Category lDummyCat = null;
				c.getRoot().debug("--= Overview of all priorities =--");
				c.getRoot().debug("Root = " + c.getRoot().getName() + " : " + c.getRoot().getPriority());
				lEnum = c.getCurrentCategories();
				while ( lEnum.hasMoreElements() )
				{
					lDummyCat = (Category) lEnum.nextElement();
					c.getRoot().debug(lDummyCat.getName() + " : "  + lDummyCat.getPriority().toString());
				}
				c.getRoot().debug("--=   End logoverview  =--");
			}
			
		}
		catch(Exception e)
		{
			System.out.println("Fatal error : " + e.toString());
			System.exit(1);
		}
	}
	
	public static Logging getLogInstance()
	{
		if (theLoggingSingleTon == null)
		{
			theLoggingSingleTon = new Logging();
			Assert.assert( c != null,"Category was not instanciated in the logging class.");
		}

		return theLoggingSingleTon;
	}

	public Category getLogger(Class aClass)
	{
		return getLogger(aClass.getName());
	}
	
	public Category getLogger(String aClassName)
	{
		Category lCat = c.exists(aClassName);
		if (lCat == null)
		{
			lCat = c.getInstance(aClassName);
		}
		if (LogLogClass)	{	Priority lDummyPrior = lCat.getPriority();
								c.getRoot().debug(" getting Category of " + aClassName + " -> priority :" + lCat.getPriority());
								if ( lDummyPrior == null) c.getRoot().debug("\t\t no fixed priority defined, getChainedPrior. : " + lCat.getChainedPriority());
							}
		return lCat;
	}	
}
