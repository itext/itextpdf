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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.lowagie.tools.plugins.AbstractTool;

/**
 * This is a utility that allows you to use a number of iText tools.
 */
public class Toolbox extends JFrame implements ToolMenuItems, ActionListener {
	
	/** The DesktopPane of the toolbox. */
	private JDesktopPane desktop;
	/** The list of tools in the toolbox. */
	private Properties toolmap = new Properties();
	
	/**
	 * Constructs the Toolbox object.
	 */
	public Toolbox() {
		super();
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		setTitle("iText Toolbox");
		setJMenuBar(getMenubar());
		desktop = new JDesktopPane();
		setContentPane(desktop);
		setVisible(true);
	}
	
	/**
	 * Starts the Toolbox utility.
	 * @param args no arguments needed
	 */
	public static void main(String[] args) {
		Toolbox toolbox = new Toolbox();
	}
	
	/**
	 * Gets the menubar.
	 * @return a menubar
	 */
	private JMenuBar getMenubar() {
		if (toolmap.size() == 0) {
			try {
				toolmap.load(Toolbox.class.getClassLoader().getResourceAsStream("com/lowagie/tools/tools.txt"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu(FILE);
		file.setMnemonic(KeyEvent.VK_F);
		JMenuItem close = new JMenuItem(CLOSE);
		close.setMnemonic(KeyEvent.VK_C);
		close.addActionListener(this);
		file.add(close);
		JMenu tools = new JMenu(TOOLS);
		file.setMnemonic(KeyEvent.VK_T);
		JMenuItem item;
		String name;
		for (Iterator i = toolmap.keySet().iterator(); i.hasNext(); ) {
			name = (String)i.next();
			item = new JMenuItem(name);
			item.addActionListener(this);
			tools.add(item);
		}
		menubar.add(file);
		menubar.add(tools);
		return menubar;
	}
	
	/**
	 * Creates an Internal Frame.
	 * @param name the name of the app
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private void createFrame(String name) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		AbstractTool ti = (AbstractTool)Class.forName((String)toolmap.get(name)).newInstance();
		JInternalFrame f = ti.getInternalFrame();
		f.setVisible(true);
		desktop.add(f);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		if (CLOSE.equals(evt.getActionCommand())) {
			System.exit(0);
		}
		else {
			try {
				createFrame(evt.getActionCommand());
			}
			catch(Exception e) {
				// do nothing
			}
		}
	}
}