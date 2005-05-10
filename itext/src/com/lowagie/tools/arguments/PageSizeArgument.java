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
package com.lowagie.tools.arguments;

import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.tools.plugins.AbstractTool;

/**
 * Argument that can be one of several options.
 */
public class PageSizeArgument extends OptionArgument {

	private TreeMap options = new TreeMap();

	/**
	 * Constructs an OptionArgument.
	 * 
	 * @param tool
	 *            the tool that needs this argument
	 * @param name
	 *            the name of the argument
	 * @param description
	 *            the description of the argument
	 */
	public PageSizeArgument(AbstractTool tool, String name, String description) {
		super(tool, name, description);
		Class ps = PageSize.class;
		Field[] sizes = ps.getDeclaredFields();
		try {
			for (int i = 0; i < sizes.length; i++) {
				addOption(sizes[i].getName(), sizes[i].get(null));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds an Option.
	 * @param description the description of the option
	 * @param value the value of the option
	 */
	public void addOption(Object description, Object value) {
		options.put(description, value);
	}
	
	/**
	 * Gets the options.
	 * @return Returns the options.
	 */
	public TreeMap getOptions() {
		return options;
	}
	
	/**
	 * Gets the argument as an object.
	 * @return an object
	 * @throws InstantiationException
	 */
	public Object getArgument() throws InstantiationException {
		if (value == null) return null;
		try {
			return ((Rectangle)options.get(value));
		} catch (Exception e) {
			throw new InstantiationException(e.getMessage());
		}
	}
	
	/**
	 * @see com.lowagie.tools.arguments.ToolArgument#getUsage()
	 */
	public String getUsage() {
		StringBuffer buf = new StringBuffer("  ");
		buf.append(name);
		buf.append(" -  ");
		buf.append(description);
		buf.append("\n");
		buf.append("    possible options:\n");
		String s;
		for (Iterator i = options.keySet().iterator(); i.hasNext(); ) {
			s  = (String)i.next();
			buf.append("    - ");
			buf.append(s);
			buf.append("\n");
		}
		return buf.toString();
	}
	
	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		Object[] message = new Object[2];
		message[0] = "Choose one of the following pagesizes:";
		JComboBox cb = new JComboBox();
		for(Iterator i = options.keySet().iterator(); i.hasNext(); ) {
			cb.addItem(i.next());
		}
		message[1] = cb;
		int result = JOptionPane.showOptionDialog( 
	 		    tool.getInternalFrame(),
	 		    message, 
	 		    description,
	 		    JOptionPane.OK_CANCEL_OPTION, 
	 		    JOptionPane.QUESTION_MESSAGE,
	 		    null,
	 		    null,
				null
	 		);
		if (result == 0) {
			setValue((String)cb.getSelectedItem());
		}
	}
}