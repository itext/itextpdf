/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Bruno Lowagie.
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
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
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
package com.lowagie.tools.plugins;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.lowagie.tools.Executable;
import com.lowagie.tools.ToolMenuItems;
import com.lowagie.tools.arguments.ToolArgument;

/**
 * Every iText tool has to implement this interface.
 */
public abstract class AbstractTool implements ToolMenuItems, ActionListener {
	 
	/** An array with the versions of the tool. */
	public static ArrayList versionsarray = new ArrayList(); 
	
    /**
     * A Class that redirects output to System.out and System.err.
     */
    public class Console {
        PipedInputStream piOut;
        PipedInputStream piErr;
        PipedOutputStream poOut;
        PipedOutputStream poErr;
        JTextArea textArea = new JTextArea();
    
        /**
         * Creates a new Console object.
         * @param columns
         * @param rows
         * @throws IOException
         */
        public Console(int columns, int rows) throws IOException {
            // Set up System.out
            piOut = new PipedInputStream();
            poOut = new PipedOutputStream(piOut);
            System.setOut(new PrintStream(poOut, true));
    
            // Set up System.err
            piErr = new PipedInputStream();
            poErr = new PipedOutputStream(piErr);
            System.setErr(new PrintStream(poErr, true));
    
            // Add a scrolling text area
            textArea.setEditable(false);
            textArea.setRows(rows);
            textArea.setColumns(columns);
    
            // Create reader threads
            new ReaderThread(piOut).start();
            new ReaderThread(piErr).start();
        }
    
        class ReaderThread extends Thread {
            PipedInputStream pi;
    
            ReaderThread(PipedInputStream pi) {
                this.pi = pi;
            }
    
            /**
             * @see java.lang.Thread#run()
             */
            public void run() {
                final byte[] buf = new byte[1024];
                try {
                    while (true) {
                        final int len = pi.read(buf);
                        if (len == -1) {
                            break;
                        }
                        textArea.append(new String(buf, 0, len));
                        textArea.setCaretPosition(textArea.getDocument().getLength());
                    }
                } catch (IOException e) {
                }
            }
        }
    }
	
	
	/** The internal frame of the tool. */
	protected JInternalFrame internalFrame = null;
	/** The list of arguments needed by the tool. */
	protected ArrayList arguments = new ArrayList();
	/** Execute menu options */
	protected int menuoptions = MENU_EXECUTE;
	/** a menu option */
	public static final int MENU_EXECUTE = 1;
	/** a menu option */
	public static final int MENU_EXECUTE_SHOW = 2;
	/** a menu option */
	public static final int MENU_EXECUTE_PRINT = 4;
	/** a menu option */
	public static final int MENU_EXECUTE_PRINT_SILENT = 8;
	
	/**
	 * Sets the arguments.
	 * @param arguments The arguments to set.
	 */
	public void setArguments(ArrayList arguments) {
		this.arguments = arguments;
	}
	
	/**
	 * Sets the arguments.
	 * @param args the arguments as String-array.
	 */
	public void setArguments(String[] args) {
    	int counter = 0;
    	ToolArgument argument;
        for (Iterator i = arguments.iterator(); i.hasNext(); ) {
        	argument = (ToolArgument) i.next();
        	if (args.length > counter) {
        		argument.setValue(args[counter]);
        	}
        	else {
        		break;
        	}
        	counter++;
        }
	}
	
	/**
	 * Gets the arguments.
	 * @return Returns the arguments.
	 */
	public ArrayList getArguments() {
		return arguments;
	}
	
	/**
	 * Gets the value of a given argument.
	 * @param name the name of the argument
	 * @return the value of an argument as an Object.
	 * @throws InstantiationException
	 */
	public Object getValue(String name) throws InstantiationException {
		ToolArgument argument;
		for (Iterator i = arguments.iterator(); i.hasNext(); ) {
			argument = (ToolArgument) i.next();
			if (name.equals(argument.getName())) {
				return argument.getArgument();
			}
		}
		return null;
	}

	/**
	 * Sets the internal frame.
	 * @param internalFrame The internalFrame to set.
	 */
	public void setInternalFrame(JInternalFrame internalFrame) {
		this.internalFrame = internalFrame;
	}
	
	/**
	 * Returns the internal frame. Creates one if it's null.
	 * @return Returns the internalFrame.
	 */
	public JInternalFrame getInternalFrame() {
		if (internalFrame == null) {
			createFrame();
		}
		return internalFrame;
	}
	
	/**
	 * Gets the menubar.
	 * @return a menubar for this tool
	 */
	public JMenuBar getMenubar() {
		JMenuBar menubar = new JMenuBar();
		JMenu tool = new JMenu(TOOL);
		tool.setMnemonic(KeyEvent.VK_F);
		JMenuItem usage = new JMenuItem(USAGE);
		usage.setMnemonic(KeyEvent.VK_U);
		usage.addActionListener(this);
		tool.add(usage);
		JMenuItem args = new JMenuItem(ARGUMENTS);
		args.setMnemonic(KeyEvent.VK_A);
		args.addActionListener(this);
		tool.add(args);
		if ((menuoptions & MENU_EXECUTE) > 0) {
			JMenuItem execute = new JMenuItem(EXECUTE);
			execute.setMnemonic(KeyEvent.VK_E);
			execute.addActionListener(this);
			tool.add(execute);
		}
		if ((menuoptions & MENU_EXECUTE_SHOW) > 0) {
			JMenuItem execute = new JMenuItem(EXECUTESHOW);
			execute.addActionListener(this);
			tool.add(execute);
		}
		if ((menuoptions & MENU_EXECUTE_PRINT) > 0) {
			JMenuItem execute = new JMenuItem(EXECUTEPRINT);
			execute.addActionListener(this);
			tool.add(execute);
		}
		if ((menuoptions & MENU_EXECUTE_PRINT_SILENT) > 0) {
			JMenuItem execute = new JMenuItem(EXECUTEPRINTSILENT);
			execute.addActionListener(this);
			tool.add(execute);
		}
		JMenuItem close = new JMenuItem(CLOSE);
		close.setMnemonic(KeyEvent.VK_C);
		close.addActionListener(this);
		tool.add(close);
		menubar.add(tool);
		if (arguments.size() > 0) {
			JMenu params = new JMenu(ARGUMENTS);
			tool.setMnemonic(KeyEvent.VK_T);
			JMenuItem item;
			ToolArgument argument;
			for (Iterator i = arguments.iterator(); i.hasNext(); ) {
				argument = (ToolArgument)i.next();
				item = new JMenuItem(argument.getName());
				item.setToolTipText(argument.getDescription());
				item.addActionListener(argument);
				params.add(item);
			}
			menubar.add(params);
		}
		return menubar;
	}
	
	/**
	 * Gets a console JScrollPanel that listens to the System.err and System.out.
	 * @param columns a number of columns for the console
	 * @param rows	 a number of rows for the console
	 * @return a JScrollPane with a Console that shows everything that was written to System.out or System.err
	 */
	public JScrollPane getConsole(int columns, int rows) {
		try {
			Console console = new Console(columns, rows);
			return new JScrollPane(console.textArea);
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Gets the usage of the tool.
	 * @return a String describing how to use the tool.
	 */
	public String getUsage() {
		StringBuffer buf = new StringBuffer("java ");
		buf.append(getClass().getName());
		ToolArgument argument;
		for (Iterator i = arguments.iterator(); i.hasNext(); ) {
			argument = (ToolArgument) i.next();
			buf.append(" ");
			buf.append(argument.getName());
		}
		buf.append("\n");
		for (Iterator i = arguments.iterator(); i.hasNext(); ) {
			argument = (ToolArgument) i.next();
			buf.append(argument.getUsage());
		}
		return buf.toString();
	}
	
	/**
	 * Gets the current arguments of the tool.
	 * @return a String with the list of arguments and their values.
	 */
	public String getArgs() {
		StringBuffer buf = new StringBuffer("Current arguments:\n");
		ToolArgument argument;
		for (Iterator i = arguments.iterator(); i.hasNext(); ) {
			argument = (ToolArgument) i.next();
			buf.append("  ");
			buf.append(argument.getName());
			if (argument.getValue() == null) {
				buf.append(" = null\n");
			}
			else {
				buf.append(" = '");
				buf.append(argument.getValue());
				buf.append("'\n");
			}
		}
		return buf.toString();
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		if (CLOSE.equals(evt.getActionCommand())) {
			internalFrame.dispose();
		}
		if (USAGE.equals(evt.getActionCommand())) {
			JOptionPane.showMessageDialog(internalFrame, getUsage());
		}
		if (ARGUMENTS.equals(evt.getActionCommand())) {
			JOptionPane.showMessageDialog(internalFrame, getArgs());
		}
		if (EXECUTE.equals(evt.getActionCommand())) {
			this.execute();
		}
		if (EXECUTESHOW.equals(evt.getActionCommand())) {
			this.execute();
			try {
				Executable.openDocument(getDestPathPDF());
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		if (EXECUTEPRINT.equals(evt.getActionCommand())) {
			this.execute();
			try {
				Executable.printDocument(getDestPathPDF());
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
		if (EXECUTEPRINTSILENT.equals(evt.getActionCommand())) {
			this.execute();
			try {
				Executable.printDocumentSilent(getDestPathPDF());
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Gets the PDF file that should be generated (or null if the output isn't a PDF file).
	 * @return the PDF file that should be generated 
	 * @throws InstantiationException
	 */
	protected abstract File getDestPathPDF() throws InstantiationException;
	
	/**
	 * Creates the internal frame.
	 */
	protected abstract void createFrame();

	/**
	 * Executes the tool (in most cases this generates a PDF file).
	 */
	public abstract void execute();
	
	/**
	 * Indicates that the value of an argument has changed.
	 * @param arg the argument that has changed
	 */
	public abstract void valueHasChanged(ToolArgument arg);
}