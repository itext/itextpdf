/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Carsten Hammer.
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

import java.text.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import com.lowagie.text.*;
import com.lowagie.tools.arguments.*;
import com.lowagie.tools.plugins.*;

/**
 * JFrame that shows the versions of all the plugins.
 */
public class Versions extends JFrame {
	private static final long serialVersionUID = 2925242862240301106L;

	/**
	 * Constructs a JFrame.
	 */
	public Versions() {
		super("Plugins and their version");
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Main method (test purposes only)
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Versions version = new Versions();
		version.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		version.setVisible(true);
	}

	public TableModel getVersionTableModel(final ArrayList versionsarray) {
		return new AbstractTableModel() {

			private static final long serialVersionUID = 5105003782164682777L;

			public int getColumnCount() {
				return 3;
			}

			public int getRowCount() {
				return versionsarray.size();
			}

			public Object getValueAt(int rowIndex, int columnIndex) {
				String dummy;
				switch (columnIndex) {
				case 0:
					dummy = versionsarray.get(rowIndex).toString();
					return dummy.split(".java")[0];
				case 1:
					dummy = versionsarray.get(rowIndex).toString();
					return dummy.split(" ")[1];
				case 2:
					DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					dummy = versionsarray.get(rowIndex).toString();
					try {
						return df.parse(dummy.split(" ")[2] + " "
								+ dummy.split(" ")[3]);
					} catch (ParseException ex) {
						return null;
					}
				}
				return versionsarray;
			}

			public String getColumnName(int column) {
				switch (column) {
				case 0:
					return "Name";
				case 1:
					return "Version";
				case 2:
					return "Changed";
				default:
					return "";
				}
			}

			public Class getColumnClass(int column) {
				switch (column) {
				case 0:
					return String.class;
				case 1:
					return String.class;
				case 2:
					return java.util.Date.class;
				default:
					return null;
				}
			}
		};
	}

	private void jbInit() throws Exception {
		this.getContentPane().setLayout(borderLayout1);
		StringBuffer sb = new StringBuffer();
		sb.append("<html>");

		Iterator it = new TreeSet(AbstractTool.versionsarray).iterator();

		while (it.hasNext()) {
			sb.append("<p>");
			sb.append((String) it.next());
			sb.append("</p>");
		}

		sb.append("</html>");
		jScrollPane2.setViewportView(jTable1);

		this.getContentPane().add(jLabel2, java.awt.BorderLayout.NORTH);
		this.getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

		Properties properties = System.getProperties();
		Runtime runtime = Runtime.getRuntime();
		sb = new StringBuffer();
		sb.append("<html>");
		sb.append("<p>iText version: " + Document.getVersion() + "</p>");
		sb.append("<p>java.version: " + properties.getProperty("java.version")
				+ "</p>");
		sb.append("<p>java.vendor: " + properties.getProperty("java.vendor")
				+ "</p>");
		sb.append("<p>java.home: " + properties.getProperty("java.home")
				+ "</p>");
		sb.append("<p>java.freeMemory: " + runtime.freeMemory() + " bytes"
				+ "</p>");
		sb.append("<p>java.totalMemory: " + runtime.totalMemory() + " bytes"
				+ "</p>");
		sb.append("<p>user.home: " + properties.getProperty("user.home")
				+ "</p>");
		sb.append("<p>os.name: " + properties.getProperty("os.name") + "</p>");
		sb.append("<p>os.arch: " + properties.getProperty("os.arch") + "</p>");
		sb.append("<p>os.version: " + properties.getProperty("os.version")
				+ "</p>");
		sb.append("</html>");
		jLabel2.setText(sb.toString());
		TableSorter sorter = new TableSorter(
				getVersionTableModel(AbstractTool.versionsarray));
		jTable1.setModel(sorter);
		sorter.addMouseListenerToHeaderInTable(jTable1);
		pack();
	}

	BorderLayout borderLayout1 = new BorderLayout();

	JLabel jLabel2 = new JLabel();

	JScrollPane jScrollPane2 = new JScrollPane();

	JTable jTable1 = new JTable();
}
