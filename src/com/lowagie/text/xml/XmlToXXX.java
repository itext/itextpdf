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

package com.lowagie.text.xml;


import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.io.InputStream;
import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.DocumentException;


/**
 * Generates an XXX file from an iText XML file.
 *
 * @version 1.0
 * @author <a href="mailto:orangeherbert@users.sourceforge.net">Matt Benson</a>
 */
public abstract class XmlToXXX
{
	
	protected Rectangle pageSize;
	
	
/**
 * Construct an <CODE>XmlToXXX</CODE> with the default page size.
 */
	public XmlToXXX()
	{
		this(PageSize.LETTER);
	}//end default constructor
	
	
/**
 * Construct an <CODE>XmlToXXX</CODE> with the specified page size.
 * @param pageSize   <CODE>String</CODE> page size name from
 * <CODE>com.lowagie.text.PageSize</CODE>.
 */
	public XmlToXXX(String pageSize)
	{
		this(getPageSize(pageSize));
	}//end constructor(String)
	
	
	private XmlToXXX(Rectangle pageSize)
	{
		this.pageSize = pageSize;
	}//end constructor(Rectangle)
	
	
/**
 * Parse the XML from the specified <CODE>InputStream</CODE>, writing to the
 * specified <CODE>OutputStream</CODE>.
 * @param in    the <CODE>InputStream</CODE> from which the XML is read.
 * @param out   the <CODE>OutputStream</CODE> to which the result is written.
 * @throws DocumentException if document errors occur.
 */
	public final void parse(InputStream in, OutputStream out)
	 throws DocumentException
	{
		Document doc = new Document(pageSize);

		addWriter(doc, out);
		XmlParser.parse(doc, in);
	}//end parse
	
	
	private static Rectangle getPageSize(String pageSize)
	{
		Rectangle result = PageSize.LETTER;
		Field fld = null;
		try
		{
			fld = PageSize.class.getDeclaredField(pageSize.toUpperCase());
			result = (fld != null
			 && Modifier.isStatic(fld.getModifiers())
			 && fld.getType().equals(Rectangle.class)) ? (Rectangle)(fld.get(null))
			 : result;
		}//end try to get field
		catch (Exception ex)
		{
			System.err.println(ex.getMessage());
		}//end catch Exception
		return result;
	}//end getPageSize
	

/**
 * Add a <CODE>DocWriter</CODE> for the specified <CODE>Document</CODE> and
 * <CODE>OutputStream</CODE>.
 * @throws DocumentException if document errors occur.
 */	
	protected abstract void addWriter(Document doc, OutputStream out)
	 throws DocumentException;
	
}//end class XmlToXXX
