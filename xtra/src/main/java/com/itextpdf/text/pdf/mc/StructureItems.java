/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.mc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfNumberTree;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Creates a list of meaningful StructureItem objects extracted from the
 * Structure Tree of a PDF document.
 */
public class StructureItems extends ArrayList<StructureItem> {
	
	/** The StructTreeRoot dictionary */
	protected PdfDictionary structTreeRoot;
	
	/** The StructParents number tree values. */
	protected HashMap<Integer, PdfObject> parentTree;
	
	/**
	 * Creates a list of StructuredItem objects.
	 * @param reader the reader holding the PDF to examine
	 */
	public StructureItems(PdfReader reader)
		throws DocumentException {
		super();
		PdfDictionary catalog = reader.getCatalog();
		structTreeRoot = catalog.getAsDict(PdfName.STRUCTTREEROOT);
		if (structTreeRoot == null)
			throw new DocumentException(MessageLocalization.getComposedMessage("can.t.read.document.structure"));
		parentTree = PdfNumberTree.readTree(structTreeRoot.getAsDict(PdfName.PARENTTREE));
		structTreeRoot.remove(PdfName.STRUCTPARENTS);
		inspectKids(structTreeRoot);
	}
	
	/**
	 * Inspects the value of the K entry of a structure element
	 * and stores all meaningful StructureItem objects that are encountered.
	 * @param structElem a structure element
	 */
	protected void inspectKids(PdfDictionary structElem) {
		if (structElem == null)
			return;
		PdfObject object = structElem.getDirectObject(PdfName.K);
		if (object == null)
			return;
		switch(object.type()) {
		case PdfObject.DICTIONARY:
			addStructureItem((PdfDictionary)object, structElem.getAsIndirectObject(PdfName.K));
			break;
		case PdfObject.ARRAY:
			PdfArray array = (PdfArray) object;
			for (int i = 0; i < array.size(); i++) {
				addStructureItem(array.getAsDict(i), array.getAsIndirectObject(i));
			}
			break;
		}
	}
	
	/**
	 * Looks at a kid of a structure item, adds it as a
	 * structure item (if necessary) and inspects its kids
	 * (if any).
	 * @param dict	the dictionary that needs to be examined
	 * @param ref	the reference to this dictionary
	 * @throws DocumentException
	 */
	protected void addStructureItem(PdfDictionary dict, PdfIndirectReference ref) {
		if (dict == null)
			return;
		StructureItem item = new StructureItem(dict, ref);
		inspectKids(dict);
		if (item.isRealContent())
			add(item);
	}
	
	/**
	 * Removes a StructParent from the parent tree.
	 * @param	PdfNumber	the number to remove
	 */
	public void removeFromParentTree(PdfNumber structParent) {
		parentTree.remove(structParent.intValue());
	}

	/**
	 * Creates a new MCID in the parent tree of the page
	 * and returns that new MCID so that it can be used
	 * in the content stream
	 * @param structParents	the StructParents entry in the page dictionary
	 * @param item	the item for which we need a new MCID
	 * @return	a new MCID
	 * @throws DocumentException
	 */
	public int processMCID(PdfNumber structParents, StructureItem item)
			throws DocumentException {
		PdfIndirectReference ref = item.getRef();
		if (ref == null)
			throw new DocumentException(MessageLocalization.getComposedMessage("can.t.read.document.structure"));
		PdfObject object = parentTree.get(structParents.intValue());
		PdfArray array = (PdfArray)PdfReader.getPdfObject(object);
		// We could try reusing null values in the array
		array.add(item.getRef());
		return array.size() - 1;
	}
	
	/**
	 * Writes the altered parent tree to a PdfWriter and updates the StructTreeRoot entry.
	 * @param writer	The writer to which the StructParents have to be written
	 * @throws IOException 
	 */
	public void writeParentTree(PdfWriter writer) throws IOException {
		if (structTreeRoot == null)
			return;
        Integer numbers[] = new Integer[parentTree.size()];
        numbers = parentTree.keySet().toArray(numbers);
        Arrays.sort(numbers);
        structTreeRoot.put(PdfName.PARENTTREENEXTKEY, new PdfNumber(numbers[numbers.length - 1] + 1));
		structTreeRoot.put(PdfName.PARENTTREE, PdfNumberTree.writeTree(parentTree, writer));
	}
	
	/** Serial version UID */
	private static final long serialVersionUID = -8247348202717165564L;
}
