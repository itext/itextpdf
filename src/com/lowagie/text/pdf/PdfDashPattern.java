/*

 * @(#)PdfDashPattern.java				0.38 2000/10/06

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

 */



package com.lowagie.text.pdf;



import java.io.ByteArrayOutputStream;

import java.io.IOException;

import com.lowagie.text.DocWriter;



/**

 * A <CODE>PdfDashPattern</CODE> defines a dash pattern as described in

 * the PDF Reference Manual version 1.3 p 325 (section 8.4.3).

 *

 * @see		PdfArray

 *

 * @author  bruno@lowagie.com

 * @version 0.38 2000/10/06

 * @since   rugPdf0.38

 */



class PdfDashPattern extends PdfArray {



// membervariables



	/** This is the length of a dash. */

	private float dash = -1;



	/** This is the length of a gap. */

	private float gap = -1;



	/** This is the phase. */

	private float phase;



// constructors



	/**

	 * Constructs a new <CODE>PdfDashPattern</CODE>.

	 *

	 * @since		iText0.38

	 */



	PdfDashPattern() { 

		super();

	}



	/**

	 * Constructs a new <CODE>PdfDashPattern</CODE>.

	 *

	 * @since		iText0.38

	 */



	PdfDashPattern(float dash) { 

		super(new PdfNumber(dash));

		this.dash = dash;

	}



	/**

	 * Constructs a new <CODE>PdfDashPattern</CODE>.

	 *

	 * @since		iText0.38

	 */



	PdfDashPattern(float dash, float gap) { 

		super(new PdfNumber(dash));

		add(new PdfNumber(gap));

		this.dash = dash;

		this.gap = gap;

	}



	/**

	 * Constructs a new <CODE>PdfDashPattern</CODE>.

	 *

	 * @since		iText0.38

	 */



	PdfDashPattern(float dash, float gap, float phase) { 

		super(new PdfNumber(dash));

		add(new PdfNumber(gap));

		this.dash = dash;

		this.gap = gap;

		this.phase = phase;

	}



	/**

     * Returns the PDF representation of this <CODE>PdfArray</CODE>.

	 *

	 * @return		an array of <CODE>byte</CODE>s

     *

	 * @since		rugPdf0.38

     */



    final public byte[] toPdf() {

		try {

			ByteArrayOutputStream stream = new ByteArrayOutputStream();

			stream.write(DocWriter.getISOBytes("["));

			

			if (dash >= 0) {

				stream.write(new PdfNumber(dash).toPdf());

				if (gap >= 0) {

					stream.write(DocWriter.getISOBytes(" "));

					stream.write(new PdfNumber(gap).toPdf());

				}

			}

			stream.write(DocWriter.getISOBytes("]"));

			if (phase >=0) {

				stream.write(DocWriter.getISOBytes(" "));

				stream.write(new PdfNumber(phase).toPdf());

			}



			return stream.toByteArray();

		}

		catch(IOException ioe) {

			throw new RuntimeException(ioe.getMessage());

		}

    }

}