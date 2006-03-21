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
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.lowagie.tools.plugins.AbstractTool;

/**
 * ToolArgument class if the argument is a java.io.File.
 */
public class FileArgument extends ToolArgument {
	/** a filter to put on the FileChooser. */
	private FileFilter filter;
	/** indicates if the argument has to point to a new or an existing file. */
	private boolean newFile;
	/** the label */
	LabelAccessory label = null;
        final static String PROPERTYFILENAME="inputfilename";
	/**
	 * Constructs a FileArgument.
	 * @param tool	the tool that needs this argument
	 * @param name	the name of the argument
	 * @param description	the description of the argument
	 * @param newFile		makes the difference between an Open or Save dialog
	 * @param filter
	 */
	public FileArgument(AbstractTool tool, String name, String description, boolean newFile, FileFilter filter) {
		super(tool, name, description, File.class.getName());
		this.newFile = newFile;
		this.filter = filter;
	}
	/**
	 * Constructs a FileArgument.
	 * @param tool	the tool that needs this argument
	 * @param name	the name of the argument
	 * @param description	the description of the argument
	 * @param newFile		makes the difference between an Open or Save dialog
	 */
	public FileArgument(AbstractTool tool, String name, String description, boolean newFile) {
		this(tool, name, description, newFile, null);
	}

	/**
	 * Gets the argument as an object.
	 * @return an object
	 * @throws InstantiationException
	 */
	public Object getArgument() throws InstantiationException {
		if (value == null) return null;
		try {
			return new File(value);
		} catch (Exception e) {
			throw new InstantiationException(e.getMessage());
		}
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();

		if (filter != null) {
			fc.setFileFilter(filter);
			if (filter instanceof DirFilter)
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		}
		if (label != null) {
			fc.setAccessory(label);
			fc.addPropertyChangeListener(
				JFileChooser.SELECTED_FILE_CHANGED_PROPERTY, label);
		}
		if (newFile) {
			fc.showSaveDialog(tool.getInternalFrame());
		}
		else {
			fc.showOpenDialog(tool.getInternalFrame());
		}
		try {
			setValue(fc.getSelectedFile().getAbsolutePath(), PROPERTYFILENAME);
		}
		catch(NullPointerException npe) {
		}
	}

	/**
	 * @return Returns the filter.
	 */
	public FileFilter getFilter() {
		return filter;
	}
	/**
	 * @param filter The filter to set.
	 */
	public void setFilter(FileFilter filter) {
		this.filter = filter;
	}
	/**
	 * @return Returns the label.
	 */
	public LabelAccessory getLabel() {
		return label;
	}
	/**
	 * @param label The label to set.
	 */
	public void setLabel(LabelAccessory label) {
		this.label = label;
	}



}
