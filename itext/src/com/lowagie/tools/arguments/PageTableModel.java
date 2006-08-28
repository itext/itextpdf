/*
 * $Id$
 * $Name$
 *
 * Copyright 2005 by Carsten Hammer
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

import javax.swing.table.*;
import com.lowagie.text.pdf.PdfReader;
import java.io.*;
import com.lowagie.text.Rectangle;
import java.text.DecimalFormat;

/**
 * A table that shows info about the pages in a PDF document.
 */
public class PageTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -3954578372030475127L;

	int numberOfPages;

	PdfReader reader;

	DecimalFormat myFormatter = new DecimalFormat("00000");

	public PageTableModel(String filename) {
		super();
		try {
			reader = new PdfReader(filename);
			numberOfPages = reader.getNumberOfPages();
		} catch (IOException ex) {
			throw new RuntimeException("File " + filename
					+ " can't be read!");
		}
	}

	/**
	 * Returns the number of columns in the model.
	 * 
	 * @return the number of columns in the model
	 * @todo Implement this javax.swing.table.TableModel method
	 */
	public int getColumnCount() {
		return 4;
	}

	/**
	 * Returns the number of rows in the model.
	 * 
	 * @return the number of rows in the model
	 * @todo Implement this javax.swing.table.TableModel method
	 */
	public int getRowCount() {
		return numberOfPages;
	}

	/**
	 * Returns the value for the cell at <code>columnIndex</code> and
	 * <code>rowIndex</code>.
	 * 
	 * @param rowIndex
	 *            the row whose value is to be queried
	 * @param columnIndex
	 *            the column whose value is to be queried
	 * @return the value Object at the specified cell
	 * @todo Implement this javax.swing.table.TableModel method
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		Rectangle rec = reader.getPageSizeWithRotation(rowIndex + 1);
		switch (columnIndex) {
		case 0:
			return myFormatter.format(rowIndex + 1);
		case 1:
			return new Float(rec.width());
		case 2:
			return new Float(rec.height());
		case 3:
			return new Float(rec.getRotation());
		}
		return null;
	}

	public String getColumnName(int column) {
		String name = new Integer(column + 1).toString();
		switch (column) {
		case 0:
			name = "<html>Pagenr<p>" + name + "</html>";
			break;
		case 1:
			name = "<html>Width<p>" + name + "</html>";
			break;
		case 2:
			name = "<html>Height<p>" + name + "</html>";
			break;
		case 3:
			name = "<html>Rotation<p>" + name + "</html>";
			break;

		default:
			name = "<html>-<p>" + name + "</html>";
			break;
		}
		return name;
	}

}
