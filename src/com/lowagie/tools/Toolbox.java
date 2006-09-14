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
package com.lowagie.tools;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import com.lowagie.tools.plugins.AbstractTool;

/**
 * This is a utility that allows you to use a number of iText tools.
 */
public class Toolbox extends JFrame implements ToolMenuItems, ActionListener {

	private static final long serialVersionUID = -3766198389452935073L;
	/** The DesktopPane of the toolbox. */
	private JDesktopPane desktop;
	/** The ConsolePane of the toolbox. */
	private JScrollPane console;

	/** The list of tools in the toolbox. */
	private Properties toolmap = new Properties();

	/** x-coordinate of the location of a new internal frame. */
	private int locationX = 0;

	/** y-coordinate of the location of a new internal frame. */
	private int locationY = 0;

	/**
	 * Constructs the Toolbox object.
	 */
	public Toolbox() {
		super();
		setSize(600, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setTitle("iText Toolbox");
		setJMenuBar(getMenubar());
		desktop = new JDesktopPane();
		Console c;
		try {
			c = new Console();
			console = new JScrollPane(c.textArea);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, desktop, console);
        splitPane.setContinuousLayout(true);
     	splitPane.setOneTouchExpandable(true);
     	splitPane.setDividerLocation(300);
		setContentPane(splitPane);
		centerFrame(this);
		setVisible(true);
	}

	/**
	 * Starts the Toolbox utility.
	 *
	 * @param args
	 *            no arguments needed
	 */
	public static void main(String[] args) {
          Toolbox toolbox = new Toolbox();
          if(args.length==0){

          }else{
            try {
              AbstractTool tool = toolbox.createFrame(args[0]);
              String[] nargs=new String[args.length-1];
              System.arraycopy(args,1,nargs,0,args.length-1);
              tool.setArguments(nargs);
              tool.execute();
            }
            catch (PropertyVetoException ex) {
            }
            catch (ClassNotFoundException ex) {
            }
            catch (IllegalAccessException ex) {
            }
            catch (InstantiationException ex) {
            }
          }
	}

	/**
	 * Gets the menubar.
	 *
	 * @return a menubar
	 */
	private JMenuBar getMenubar() {
		Properties p = new Properties();
		try {
			p.load(Toolbox.class.getClassLoader().getResourceAsStream(
					"com/lowagie/tools/plugins/tools.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		toolmap = new Properties();
		TreeMap tmp = new TreeMap();
		tmp.putAll(p);
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu(FILE);
		JMenuItem close = new JMenuItem(CLOSE);
		close.setMnemonic(KeyEvent.VK_C);
		close.addActionListener(this);
		file.add(close);
		JMenu tools = new JMenu(TOOLS);
		file.setMnemonic(KeyEvent.VK_T);
		String name, tool;
		JMenu current = null;
		JMenuItem item;
		for (Iterator i = tmp.keySet().iterator(); i.hasNext();) {
			name = (String) i.next();
			if (current == null || !name.startsWith(current.getText())) {
				current = new JMenu(name.substring(0, name.indexOf(".")));
				tools.add(current);
			}
			item = new JMenuItem(name.substring(current.getText().length() + 1));
			item.addActionListener(this);
			tool = (String) tmp.get(name);
			try {
				Class.forName(tool);
				toolmap.put(item.getText(), tool);
				current.add(item);
			} catch (ClassNotFoundException e) {
				System.err.println("Plugin " + name
						+ " was not found in your CLASSPATH.");
			}
		}
		JMenu help = new JMenu(HELP);
		JMenuItem about = new JMenuItem(ABOUT);
		about.setMnemonic(KeyEvent.VK_A);
		about.addActionListener(this);
		help.add(about);
		JMenuItem versions = new JMenuItem(VERSION);
		versions.addActionListener(this);
		help.add(versions);
		menubar.add(file);
		menubar.add(tools);
		menubar.add(Box.createGlue());
		menubar.add(help);
		return menubar;
	}

	/**
	 * Creates an Internal Frame.
	 *
	 * @param name
	 *            the name of the app
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws PropertyVetoException
	 */
	public AbstractTool createFrame(String name) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException,
			PropertyVetoException {
		AbstractTool ti = (AbstractTool) Class.forName(
				(String) toolmap.get(name)).newInstance();
		JInternalFrame f = ti.getInternalFrame();
		f.setLocation(locationX, locationY);
		locationX += 25;
		if (locationX > this.getWidth() + 50)
			locationX = 0;
		locationY += 25;
		if (locationY > this.getHeight() + 50)
			locationY = 0;
		f.setVisible(true);
		desktop.add(f);
		f.setSelected(true);
                return ti;
	}

	public static void centerFrame(JFrame f) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = f.getSize();
		if (frameSize.height > screenSize.height) {
			frameSize.height = screenSize.height;
		}
		if (frameSize.width > screenSize.width) {
			frameSize.width = screenSize.width;
		}
		f.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		if (CLOSE.equals(evt.getActionCommand())) {
			System.out.println("The Toolbox is closed.");
			System.exit(0);
		} else if (ABOUT.equals(evt.getActionCommand())) {
			System.out
					.println("The iText Toolbox is part of iText, a Free Java-PDF Library.\nVisit http://www.lowagie.com/iText/toolbox.html for more info.");
			try {
				Executable
						.launchBrowser("http://www.lowagie.com/iText/toolbox.html");
			} catch (IOException ioe) {
				JOptionPane
						.showMessageDialog(
								this,
								"The iText Toolbox is part of iText, a Free Java-PDF Library.\nVisit http://www.lowagie.com/iText/toolbox.html for more info.");
			}
		} else if (VERSION.equals(evt.getActionCommand())) {
			JFrame f = new Versions();
			centerFrame(f);
			f.setVisible(true);
		} else {
			try {
				createFrame(evt.getActionCommand());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
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
        public Console() throws IOException {
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

                while (true) {
                	try {
                        final int len = pi.read(buf);
                        if (len == -1) {
                            break;
                        }
                        textArea.append(new String(buf, 0, len));
                        textArea.setCaretPosition(textArea.getDocument().getLength());

                	} catch (IOException e) {
                	}
                }
            }
        }
    }

}
