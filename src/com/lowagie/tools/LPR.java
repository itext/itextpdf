/*
 * $Id$
 * $Name$
 *
 * Copyright 2002 by Mario Muller / Carsten Hammer
 * Class based on freeware by Mario Muller: http://www.hemasoft.de/dev/lprj/
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999-2006 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2006 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

/**
 * Modified!
 * 
 * This class provides methods for the Line Printer Daemon Protocol <BR>
 * <A HREF="http://www.hemasoft.de/dev/lprj/">more info about LPD/LPR </A>
 * 
 * based on code by Mario M&uuml;ller (supermario@gmx.net)
 * @version 1.0 (12/98)
 */
public class LPR {
	public static final int UNKNOWN = 0;

	private String host;

	private int port = 515;

	private String user = System.getProperty("user.name");

	private Vector jobs = new Vector();

	private String hostname = null;

	private String jobname = "";

	private String cfAlen;

	private String cfA;

	private int copies = 1;

	private int timeout = 60000;

	private boolean cfA_formatted = false; // f

	private boolean cfA_postscript = false; // o

	private boolean cfA_banner = false; // L

	private boolean cfA_pr = false;

	/**
	 * default constructor without parameters, standard port is <B>515 </B>
	 */
	public LPR() {
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (Exception e2) {
			System.out.println("can't resolve hostname");
			hostname = null;
		}
	}

	/**
	 * constuctor with host and username standard port is <B>515 </B>
	 */
	public LPR(String host, String user) {
		this();
		setHost(host);
		setUser(user);
	}

	/**
	 * constuctor with host, port and username
	 */
	public LPR(String host, int port, String user) {
		this();
		setHost(host);
		setUser(user);
		setPort(port);
	}

	/**
	 * set LPD host
	 */
	public void setHost(String value) {
		host = value;
	}

	/**
	 * get LPD host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * set LPD port
	 */
	public void setPort(int value) {
		port = value;
	}

	/**
	 * get LPD port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * set username
	 */
	public void setUser(String value) {
		user = value;
	}

	/**
	 * get username
	 */
	public String getUser() {
		return user;
	}

	/**
	 * set the timeout for any commands
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * get the timeout for any commands
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * get if file printed as postscript file
	 */
	public boolean getCfA_postscript() {
		return cfA_postscript;
	}

	/**
	 * set if file printed as postscript file
	 * <P>
	 * <I>o option in control file </I>
	 * </P>
	 * <P>
	 * This command prints the data file to be printed, treating the data as
	 * standard Postscript input.
	 * </P>
	 */
	public void setCfA_postscript(boolean value) {
		cfA_postscript = value;

		if (value) {
			cfA_formatted = false;
		}
	}

	/**
	 * get the job name
	 */
	public String getCfA_jobname() {
		return jobname;
	}

	/**
	 * set the job name
	 * <P>
	 * <I>J option in control file </I>
	 * </P>
	 * <P>
	 * This command sets the job name to be printed on the banner page. The name
	 * of the job must be 99 or fewer octets. It can be omitted. The job name is
	 * conventionally used to display the name of the file or files which were
	 * "printed". It will be ignored unless the print banner command ('L') is
	 * also used.
	 * </P>
	 */
	public void setCfA_jobname(String value) {
		jobname = value;
		cfA_banner = true;
	}

	/**
	 * get if print file with 'pr' format
	 */
	public boolean getCfA_pr() {
		return cfA_pr;
	}

	/**
	 * <P>
	 * <I>p - Print file with 'pr' format </I>
	 * </P>
	 * <P>
	 * This command causes the data file to be printed with a heading, page
	 * numbers, and pagination. The heading should include the date and time
	 * that printing was started, the title, and a page number identifier
	 * followed by the page number. The title is the name of file as specified
	 * by the 'N' command, unless the 'T' command (title) has been given. After
	 * a page of text has been printed, a new page is started with a new page
	 * number. (There is no way to specify the length of the page.)
	 * </P>
	 */
	public void setCfA_pr(boolean value) {
		cfA_pr = value;
	}

	/**
	 * get if job printed with banner page
	 */
	public boolean getCfA_banner() {
		return cfA_banner;
	}

	/**
	 * set if job printed with banner page
	 * <P>
	 * <I>L option in control file </I>
	 * </P>
	 * <P>
	 * This command causes the banner page to be printed. The user name can be
	 * omitted. The class name for banner page and job name for banner page
	 * commands must precede this command in the control file to be effective.
	 * </P>
	 */
	public void setCfA_banner(boolean value) {
		cfA_banner = value;
	}

	/**
	 * get the host name for this computer
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * set the host name for this computer
	 */
	public void setHostname(String value) {
		hostname = value;
	}

	/**
	 * set Copies
	 */
	public void setCopies(int value) {
		copies = value;
	}

	/**
	 * get Copies
	 */
	public int getCopies() {
		return copies;
	}

	/**
	 * Print any waiting jobs
	 * <P>
	 * This command starts the printing process if it not already running.
	 * </P>
	 */
	public boolean printWaitingJobs(String queue) {
		Socket printer = connect();

		if (printer != null) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						printer.getInputStream()));
				DataOutputStream out = new DataOutputStream(printer
						.getOutputStream());

//-------------------------- start -------------------------------
				out.write(01);
				out.writeBytes(queue + "\n");

				if (in.read() != 0) {
					System.err.println("Error while start print jobs on queue " + queue);

					return false;
				}

				close(printer, in, out);

				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * Remove job
	 * <P>
	 * This command deletes the print jobs from the specified queue which are
	 * listed as the other operands. If only the agent is given, the command is
	 * to delete the currently active job. Unless the agent is "root", it is not
	 * possible to delete a job which is not owned by the user. This is also the
	 * case for specifying user names instead of numbers. That is, agent "root"
	 * can delete jobs by user name but no other agents can.
	 * </P>
	 */
	public boolean removeJob(String queue, String user, String jobid) {
		Socket printer = connect();

		if (printer != null) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						printer.getInputStream()));
				DataOutputStream out = new DataOutputStream(printer
						.getOutputStream());

//-------------------------- start -------------------------------
				out.write(05);
				out.writeBytes(queue + " " + user + " " + jobid + "\n");

				if (in.read() != 0) {
					System.err.println("Error while remove print job " + jobid + " on queue " + queue);
					return false;
				}

				close(printer, in, out);

				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * gets the state and description of the printer queue in short or long
	 * format
	 */
	public String getQueueState(String queue, boolean shortstate) {
		Socket printer = connect();

		if (printer != null) {
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						printer.getInputStream()));

				DataOutputStream out = new DataOutputStream(printer
						.getOutputStream());

				if (shortstate) {
					out.write(03);
				} else {
					out.write(04);
				}

				out.writeBytes(queue + " \n");

				new LineNumberReader(in);
				String line = in.readLine();
				close(printer, in, out);

				return line;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * print a byte array with the document name as parameter
	 * 
	 * @see LPR#print(String queue, String dfA, String document) {
	 */
	public String print(String queue, byte[] dfA, String document) {
		PrintJob job = new PrintJob(queue, dfA, document);
		job.start();

		return job.getJobId();
	}

	/**
	 * print a String with the document name as parameter
	 * 
	 * @see LPR#print(String queue, String dfA, String document) {
	 */
	public String print(String queue, String dfA, String document) {
		PrintJob job = new PrintJob(queue, dfA.toCharArray(), document);
		job.start();

		return job.getJobId();
	}

	/**
	 * print a char array with the document name as parameter
	 * 
	 * @see LPR#print(String queue, String dfA, String document) {
	 */
	public String print(String queue, char[] dfA, String document) {
		PrintJob job = new PrintJob(queue, dfA, document);
		job.start();

		return job.getJobId();
	}

	/**
	 * print a file with the document name as parameter
	 * 
	 * @see LPR#print(String queue, String dfA, String document) {
	 */
	public String print(String queue, File file, String document) {
		if (file.exists()) {
			try {
				FileInputStream in = new FileInputStream(file);
				byte[] data = new byte[(int) file.length()];
				int count = 0;
				int size = (int) file.length();

				while (count < size) {
					count += in.read(data, count, size - count);
				}

				in.close();

				PrintJob job = new PrintJob(queue, data, document);
				job.start();
				waitFor(job.getJobId());

				return job.getJobId();
			} catch (Exception e) {
				System.out.println("error while printig file " + file.getName()
						+ "\n" + e.getMessage());
			}
		} else {
			System.out.println("file " + file.getName() + " not found");
		}

		return null;
	}

	/**
	 * wait until job is printed
	 */
	public void waitFor(String jobid) {
		try {
			while (jobs.contains(jobid)) {
				Thread.sleep(500);
			}
		} catch (Exception e) {
		}
	}

	/**
	 * internal funktion for printing
	 */
	private boolean print(String queue, String document, byte[] dfA1,
			char[] dfA2, int mode, String jobid) {
		Socket printer = connect();

		if (printer != null) {
			boolean rc = true;
			makecfA(document, jobid);

			String dfAlen = String.valueOf(((mode == 1) ? dfA1.length
					: dfA2.length));

			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						printer.getInputStream()));
				DataOutputStream out = new DataOutputStream(printer
						.getOutputStream());
				System.out.println("jobid: " + jobid + " queue: " + queue);

//-------------------------- start -------------------------------
				out.write(02);
				out.writeBytes(queue + "\n");
				out.flush();

				if (in.read() != 0) {
					rc = false;
					System.out.println("Error while start printing on queue "
							+ queue);
				}

//----------------------- write control file ----------------------------
				out.write(02);
				out.writeBytes(cfAlen);
				out.writeBytes(" ");
				out.writeBytes("cfA" + jobid + user + "\n");
				out.flush();

				if (in.read() != 0) {
					rc = false;
					System.out
							.println("Error while start sending control file");
				}

				out.writeBytes(cfA);
				out.writeByte(0);
				out.flush();

				if (in.read() != 0) {
					rc = false;
					System.out.println("Error while sending control file");
				}

//----------------------- write data file ----------------------------
				out.write(03);

				/**
				 * Länge des Datenfiles
				 */
				out.writeBytes(dfAlen);
				out.writeBytes(" ");
				out.writeBytes("dfA" + jobid + user + "\n");
				out.flush();

				if (in.read() != 0) {
					rc = false;
					System.err.println("Error while start sending data file");
				}

				if (mode == 1) {
					out.write(dfA1);
				} else {
					out.writeBytes(new String(dfA2));
				}

				out.writeByte(0);
				out.flush();

				if (in.read() != 0) {
					rc = false;
					System.out.println("Error while sending data file");
				}
				out.flush();
				close(printer, in, out);
			} catch (Exception e) {
				rc = false;
				e.printStackTrace();
			}

			return rc;
		}

		return false;
	}

	/**
	 * connect to the LPD Server
	 */
	private Socket connect() {
		try {
			System.out.println("Connect with " + host);

			Socket socket = new Socket(InetAddress.getByName(host), port);
			socket.setSoTimeout(timeout);

			return socket;
		} catch (Exception e1) {
			System.out
					.println("Error while connecting to " + host + ":" + port);
			System.out.println(e1.getMessage());
		}

		return null;
	}

	/**
	 * closed connection to the LPD
	 */
	private void close(Socket socket, BufferedReader in, DataOutputStream out) {
		try {
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			System.out.println("Errror while closing printerport");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * internal methode to create the control file
	 */
	private void makecfA(String document, String jobid) {
		cfA = "";

		if (hostname != null) {
			cfA += ("H" + hostname + "\n");
		}

		cfA += ("P" + user + "\n");
		cfA += ("J" + document + "\n");
		cfA += ("L" + user + "\n");

		if (cfA_formatted) {
			cfA += ("f" + document + "\n");
		}

		if (cfA_postscript) {
			cfA += ("o" + document + "\n");
		}

		if (cfA_banner) {
			cfA += ("L" + jobname + "\n");
		}

		cfA += ("UdfA" + jobid + hostname + "\n");

		for (int i = 0; i < copies; i++) {
			cfA += ("ldfA" + jobid + user + "\n");
		}

		cfA += ("N" + document + "\n");
		cfAlen = String.valueOf(cfA.length());

		System.out.println(cfA);

	}

	private String getNewJobId() {
		return fillLeft(String.valueOf((int) Math.floor(Math.random() * 999)),
				3, "0");
	}

	private String fillLeft(String data, int size, String filler) {
		while (data.length() < size) {
			data = filler + data;
		}

		return data;
	}

	/** ******************* Job ************************ */
	private class PrintJob extends Thread {
		String queue;

		String document;

		String id;

		int mode;

		private byte[] dfA1 = null;

		private char[] dfA2 = null;

		public PrintJob(String queue, byte[] data, String document) {
			super();
			this.queue = queue;
			this.dfA1 = data;
			this.document = document;
			this.mode = 1;
			this.id = getNewJobId();
			jobs.addElement(id);
			System.out.println("Printjob (byte) " + id + " queue=" + queue + " document=" + document);
		}

		public PrintJob(String queue, char[] data, String document) {
			super();
			this.queue = queue;
			this.dfA2 = data;
			this.document = document;
			this.mode = 2;
			this.id = getNewJobId();
			jobs.addElement(id);
			System.out.println("Printjob (char) " + id + " queue=" + queue + " document=" + document);
		}

		public void run() {
			boolean rc = false;
			int i = 0;
			System.out.println("Printjob (char) " + id + " queue=" + queue + " document=" + document);

			while (!rc) {
				try {
					i++;
					rc = print(queue, document, dfA1, dfA2, mode, id);
				} catch (Exception e) {
					System.err.println(e.getMessage());
				}

				if (!rc && (i > 5)) {
					break;
				}

				if (!rc) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						break;
					}
				}
			}

			jobs.removeElement(id);

			if (rc) {
				System.out.println("Job " + id + " allready printed and removed from queue (" + i + " trys)");
			} else {
				System.out.println("Job " + id + " not printed and removed from queue (" + i + " trys)");
			}
		}

		public String getJobId() {
			return this.id;
		}
	}
}