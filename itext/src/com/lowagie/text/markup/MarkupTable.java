/*
 * Copyright 2002 by Matt Benson.
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

package com.lowagie.text.markup;


import java.util.Set;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Collections;

import com.lowagie.text.Row;
import com.lowagie.text.Table;
import com.lowagie.text.BadElementException;


/**
 * A <CODE>Table</CODE> that implements <CODE>MarkupAttributes</CODE>.
 *
 * @author <a href="mailto:orangeherbert@users.sourceforge.net">Matt Benson</a>
 */
public class MarkupTable
 extends Table
 implements MarkupAttributes
{
	
	// to be lazily instantiated
	private Properties markupAttributes;
	private Hashtable alternatingRowAttributes;
	private byte rowSwitch = 0;

	
/**
 * @see com.lowagie.text.Table#Table(int)
 */
	public MarkupTable(int columns)
	 throws BadElementException
	{
		super(columns);
	}//end constructor(int)


/**
 * @see com.lowagie.text.Table#Table(int, int)
 */
	public MarkupTable(int columns, int rows)
	 throws BadElementException
	{
		super(columns, rows);
	}//end constructor(int, int)


/**
 * @see com.lowagie.text.Table#Table(java.util.Properties)
 */
	public MarkupTable(Properties attributes)
	{
		super(attributes);
	}//end constructor(Properties)


/**
 * @see com.lowagie.text.markup.MarkupAttributes#getMarkupAttributeNames()
 */
  public Set getMarkupAttributeNames()
	{
		return (markupAttributes == null) ?
		 Collections.EMPTY_SET : markupAttributes.keySet();
	}//end getMarkupAttributeNames
	
	
/**
 * @see com.lowagie.text.markup.MarkupAttributes#setMarkupAttribute(java.lang.String, java.lang.String)
 */
  public void setMarkupAttribute(String name, String value)
	{
		markupAttributes = (markupAttributes == null) ?
		 new Properties() : markupAttributes;
		markupAttributes.put(name, value);
	}//end setMarkupAttribute
	
	
/**
 * @see com.lowagie.text.markup.MarkupAttributes#getMarkupAttribute(java.lang.String)
 */
  public String getMarkupAttribute(String name)
	{
		return (markupAttributes == null) ?
		 null : String.valueOf(markupAttributes.get(name));
	}//end getMarkupAttribute
	

/**
 * Allows clients to set up alternating attributes for each Row in the Table.
 */
  public void setAlternatingRowAttribute(String name, String value0, String value1)
	{
		if (value0 == null || value1 == null)
		{
			throw new NullPointerException(
			 "MarkupTable#setAlternatingRowAttribute(): null values are not permitted.");
		}//end if either of the specified values is null, Hashtable will handle null name
		alternatingRowAttributes = (alternatingRowAttributes == null) ?
		 new Hashtable() : alternatingRowAttributes;
		 
		// we could always use new Arrays but this is big enough
		String[] value = (String[])(alternatingRowAttributes.get(name));
		value = (value == null) ? new String[2] : value;
		value[0] = value0;
		value[1] = value1;
		alternatingRowAttributes.put(name, value);
	}//end setAlternatingRowAttribute
	
	
/**
 * @see com.lowagie.text.Table#addColumns(int)
 */
	public void addColumns(int aColumns)
	{
		rowSwitch = 0;
		super.addColumns(aColumns);
	}//end addColumns

	
/**
 * @see com.lowagie.text.Table#createRow(int)
 */
	protected Row createRow(int columns)
	{
		Row result;
		if (alternatingRowAttributes != null)
		{
			result = new MarkupRow(columns);
			Iterator iterator = alternatingRowAttributes.keySet().iterator();
			while (iterator.hasNext())
			{
				String name = String.valueOf(iterator.next());
				((MarkupAttributes)(result)).setMarkupAttribute(
				 name, ((String[])(alternatingRowAttributes.get(name)))[rowSwitch]);
			}//end while iterator has next
	//flip rowSwitch
			rowSwitch ^= 1;
		}//end if there are any alternating attributes
		else
		{
			result = super.createRow(columns);
		}//end else, no row attributes, so why use a MarkupRow?
		return result;
	}//end createRow
		
		
/**
 * @see com.lowagie.text.Table#checkIllegalRowspan()
 */
	protected void checkIllegalRowspan()
	 throws BadElementException
	{
		byte tempRowSwitch = rowSwitch;
		rowSwitch = 0;
		super.checkIllegalRowspan();
		rowSwitch = tempRowSwitch;
	}//end checkIllegalRowspan

}//end class MarkupTable
