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


import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.html.HtmlWriter;


/**
 * HTML-specific subclass of <code>XmlToXXX</code>.
 *
 * @version 1.0
 * @author <a href="mailto:orangeherbert@users.sourceforge.net">Matt Benson</a>
 */
public class XmlToHtml
 extends XmlToXXX
{
	
/**
 * Construct an <CODE>XmlToHtml</CODE> with the default page size.
 */
	public XmlToHtml()
	{
		super();
	}//end default constructor
	
	
/**
 * Construct an <CODE>XmlToHtml</CODE> with the specified page size.
 * @param pageSize   <CODE>String</CODE> page size name from
 * <CODE>com.lowagie.text.PageSize</CODE>.
 */
	public XmlToHtml(String pageSize)
	{
		super(pageSize);
	}//end constructor(String)
	
	
/**
 * Add a <CODE>DocWriter</CODE> for the specified <CODE>Document</CODE> and
 * <CODE>OutputStream</CODE>.
 * @throws DocumentException if document errors occur.
 */	
	protected final void addWriter(Document doc, OutputStream out)
	 throws DocumentException
	{
		HtmlWriter.getInstance(doc, out);
	}//end addWriter
	
	
/**
 * Main method of the <CODE>XmlToHtml</CODE> class.
 * @param args   <CODE>String[]</CODE> of command-line arguments.
 */
	public static void main(String[] args)
	{
		int code = 0;
		
		if (args.length > 1)
		{
			try
			{
				XmlToHtml x;
				if (args.length > 2)
				{
					x = new XmlToHtml(args[2]);
				}//end if at least 3 args
				else
				{
					x = new XmlToHtml();
				}//end else, only 2 args
				
				x.parse(new FileInputStream(args[0]), new FileOutputStream(args[1]));
			}//end try to do everything
			catch (Exception ex)
			{
				code = 2;
				ex.printStackTrace(System.err);
			}//end catch Exception
		}//end if at least 2 args
		else
		{
			code = 1;
			System.err.println(
			 "Usage:  XmlToHtml [XML file in] [PDF file out] [optional page size]");
		}//end else, not enough arguments
		
		System.exit(code);
	}//end main
	
}//end class XmlToHtml
