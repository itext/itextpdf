/*

 * @(#)PdfArray.java				0.38 2000/10/06

 *       release rugPdf0.10:		0.02 99/03/17

 *               rugPdf0.20:		0.15 99/11/30

 *               iText0.3:			0.22 2000/02/14

 *               iText0.35:         0.22 2000/08/11

 *               iText0.37			0.37 2000/10/05

 * 

 * Copyright (c) 1999, 2000 Bruno Lowagie.

 *

 * This library is free software; you can redistribute it and/or modify it

 * under the terms of the GNU Library General Public License as published

 * by the Free Software Foundation; either version 2 of the License, or any

 * later version.

 *

 * This library is distributed in the hope that it will be useful, but WITHOUT

 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS

 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more

 * details.

 *

 * You should have received a copy of the GNU Library General Public License along

 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,

 * Suite 330, Boston, MA 02111-1307 USA.

 *

 * If you didn't download this code from the following link, you should check if

 * you aren't using an obsolete version:

 * http://www.lowagie.com/iText/

 *

 * ir-arch Bruno Lowagie,

 * Adolf Baeyensstraat 121

 * 9040 Sint-Amandsberg

 * BELGIUM

 * tel. +32 (0)9 228.10.97

 * bruno@lowagie.com

 *     

 * Very special thanks to Troy Harrison, Systems Consultant

 * of CNA Life Department-Information Technology

 * Troy.Harrison@cnalife.com <mailto:Troy.Harrison@cnalife.com>

 * His input concerning the changes in version rugPdf0.20 was

 * really very important.  

 */



package com.lowagie.text.pdf;



import java.io.ByteArrayOutputStream;

import java.io.IOException;

import java.util.ArrayList;

import java.util.Iterator;

import com.lowagie.text.DocWriter;



/**

 * <CODE>PdfArray</CODE> is the PDF Array object.

 * <P>

 * An array is a sequence of PDF objects. An array may contain a mixture of object types.

 * An array is written as a left square bracket ([), followed by a sequence of objects,

 * followed by a right square bracket (]).<BR>

 * This object is described in the 'Portable Document Format Reference Manual version 1.3'

 * section 4.6 (page 40).

 *

 * @see		PdfObject

 *

 * @author  bruno@lowagie.com

 * @version 0.38 2000/10/06

 * @since   rugPdf0.10

 */



class PdfArray extends PdfObject {



// membervariables

	

	/** this is the actual array of PdfObjects */

	protected ArrayList arrayList;



// constructors



	/**

	 * Constructs an empty <CODE>PdfArray</CODE>-object.

	 *

	 * @since   rugPdf0.10

	 */



	PdfArray() {

		super(ARRAY);

		arrayList = new ArrayList();

	}



	/**

	 * Constructs an <CODE>PdfArray</CODE>-object, containing 1 <CODE>PdfObject</CODE>.

	 *

	 * @param	object		a <CODE>PdfObject</CODE> that has to be added to the array

	 *

	 * @since   rugPdf0.20

	 */



	PdfArray(PdfObject object) {

		super(ARRAY);

		arrayList = new ArrayList();

		arrayList.add(object);

	}



	/**

	 * Constructs an <CODE>PdfArray</CODE>-object, containing all the <CODE>PdfObject</CODE>s in a given <CODE>PdfArray</CODE>.

	 *

	 * @param	object		a <CODE>PdfArray</CODE> that has to be added to the array

	 *

	 * @since   rugPdf0.20

	 */



	PdfArray(PdfArray array) {

		super(ARRAY);

		arrayList = new ArrayList(array.getArrayList());

	}



// methods overriding some methods in PdfObject



	/**

     * Returns the PDF representation of this <CODE>PdfArray</CODE>.

	 *

	 * @return		an array of <CODE>byte</CODE>s

     *

	 * @since		rugPdf0.10

     */



    public byte[] toPdf() {

		try {

			ByteArrayOutputStream stream = new ByteArrayOutputStream();

			stream.write(DocWriter.getISOBytes("["));



			Iterator i = arrayList.iterator();

			PdfObject object;

			if (i.hasNext()) {

				object = (PdfObject) i.next();

				stream.write(object.toPdf());

			}

			while (i.hasNext()) {

				object = (PdfObject) i.next();

				stream.write(DocWriter.getISOBytes(" "));

				stream.write(object.toPdf());

			}

			stream.write(DocWriter.getISOBytes("]"));



			return stream.toByteArray();

		}

		catch(IOException ioe) {

			throw new RuntimeException(ioe.getMessage());

		}

    }



// methods concerning the ArrayList-membervalue 



	/**

	 * Returns an ArrayList containing <CODE>PdfObject</CODE>s.

	 *

	 * @return		an ArrayList	  

	 *

	 * @since		rugPdf0.20

	 */



	final ArrayList getArrayList() {

		 return arrayList;

	}



	/**

	 * Returns the number of entries in the array.

	 *

	 * @return		the size of the ArrayList

	 *

	 * @since		iText0.37

	 */



	public final int size() {

		return arrayList.size();

	}



	/**

	 * Adds a <CODE>PdfObject</CODE> to the <CODE>PdfArray</CODE>.

	 *

	 * @param		object			<CODE>PdfObject</CODE> to add

	 * @return		<CODE>true</CODE>

	 *

	 * @since		rugPdf0.10

	 */



	boolean add(PdfObject object) {

		return arrayList.add(object);

	}



	/**

	 * Adds a <CODE>PdfObject</CODE> to the <CODE>PdfArray</CODE>.

	 * <P>

	 * The newly added object will be the first element in the <CODE>ArrayList</CODE>.

	 *

	 * @param		object			<CODE>PdfObject</CODE> to add

	 * @return		<CODE>true</CODE>

	 *

	 * @since		rugPdf0.38

	 */



	void addFirst(PdfObject object) {

		arrayList.add(0, object);

	}



	/**

	 * Checks if the <CODE>PdfArray</CODE> allready contains a certain <CODE>PdfObject</CODE>.

	 *

	 * @param		object			<CODE>PdfObject</CODE> to check

	 * @return		<CODE>true</CODE>

	 *

	 * @since		rugPdf0.20

	 */



	final boolean contains(PdfObject object) {

		return arrayList.contains(object);

	}



// deprecated methods



	/**

	 * Returns an array containing <CODE>PdfObject</CODE>s.

	 *

	 * @return		an array	  

	 *

	 * @since		rugPdf0.10

	 * @deprecated	this method was never used

	 */



	final Object[] toArray() {

		 return arrayList.toArray();

	}

}