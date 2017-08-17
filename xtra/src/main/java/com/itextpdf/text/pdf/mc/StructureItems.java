/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
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
import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
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
 * Creates a list of StructureItem objects extracted from the
 * Structure Tree of a PDF document.
 */
public class StructureItems extends ArrayList<StructureItem> {

	/** The Logger instance */
	protected final static Logger LOGGER = LoggerFactory.getLogger(StructureItems.class);
	
	/** The StructTreeRoot dictionary */
	protected PdfDictionary structTreeRoot;
	
	/** The StructParents number tree values. */
	protected HashMap<Integer, PdfObject> parentTree;
	
	/**
	 * Creates a list of StructuredItem objects.
	 * @param reader the reader holding the PDF to examine
	 */
	public StructureItems(PdfReader reader)
			throws DocumentException, InvalidPdfException {
		super();
		PdfDictionary catalog = reader.getCatalog();
		structTreeRoot = catalog.getAsDict(PdfName.STRUCTTREEROOT);
		if (structTreeRoot == null)
			throw new DocumentException(MessageLocalization.getComposedMessage("can.t.read.document.structure"));
		// Storing the parent tree
		parentTree = PdfNumberTree.readTree(structTreeRoot.getAsDict(PdfName.PARENTTREE));
		structTreeRoot.remove(PdfName.STRUCTPARENTS);
		// Examining the StructTreeRoot
		PdfObject object = structTreeRoot.getDirectObject(PdfName.K);
		if (object == null)
			return;
		switch(object.type()) {
		case PdfObject.DICTIONARY:
			LOGGER.info("StructTreeRoot refers to dictionary");
			processStructElems((PdfDictionary)object, structTreeRoot.getAsIndirectObject(PdfName.K));
			break;
		case PdfObject.ARRAY:
			LOGGER.info("StructTreeRoot refers to array");
			PdfArray array = (PdfArray) object;
			for (int i = 0; i < array.size(); i++) {
				processStructElems(array.getAsDict(i), array.getAsIndirectObject(i));
			}
			break;
		}
	}
	
	/**
	 * Looks at a StructElem dictionary, and processes it.
	 * @param structElem the StructElem dictionary that needs to be examined
	 * @param ref	the reference to the StructElem dictionary
	 * @throws DocumentException
	 */
	protected void processStructElems(PdfDictionary structElem, PdfIndirectReference ref) throws InvalidPdfException {
		if (LOGGER.isLogging(Level.INFO)) {
			LOGGER.info(String.format("addStructureItems(%s, %s)", structElem, ref));
		}
		if (structElem == null)
			return;
		processStructElemKids(structElem, ref, structElem.getDirectObject(PdfName.K));
	}
	
	/**
	 * Processes the kids object of a StructElem dictionary.
	 * This kids object can be a number (MCID), another StructElem dictionary,
	 * an MCR dictionary, an OBJR dictionary, or an array of the above.
	 * @param structElem	the StructElem dictionary
	 * @param ref			the reference to the StructElem dictionary
	 * @param object		the kids object
	 */
	protected void processStructElemKids(PdfDictionary structElem, PdfIndirectReference ref, PdfObject object) throws InvalidPdfException {
		if (LOGGER.isLogging(Level.INFO)) {
			LOGGER.info(String.format("addStructureItem(%s, %s, %s)", structElem, ref, object));
		}
		if (object == null)
			return;
		StructureItem item;
		switch(object.type()) {
		case PdfObject.NUMBER:
			item = new StructureMCID(structElem.getAsIndirectObject(PdfName.PG), (PdfNumber) object);
			add(item);
			LOGGER.info("Added " + item);
			break;
		case PdfObject.ARRAY:
			PdfArray array = (PdfArray)object;
			for (int i = 0; i < array.size(); i++) {
				processStructElemKids(structElem, array.getAsIndirectObject(i), array.getDirectObject(i));
			}
			break;
		case PdfObject.DICTIONARY:
			PdfDictionary dict = (PdfDictionary)object;
			if (dict.checkType(PdfName.MCR)) {
				item = new StructureMCID(dict);
				add(item);
				LOGGER.info("Added " + item);
			} else if (dict.checkType(PdfName.OBJR)) {
				item = new StructureObject(structElem, ref, dict);
				add(item);
				LOGGER.info("Added " + item);
			}
			else {
				processStructElems(dict, ref);
			}
            break;
		}
	}
	
	/**
	 * Removes a StructParent from the parent tree.
	 * @param	structParent	the number to remove
	 */
	public void removeFromParentTree(PdfNumber structParent) {
		parentTree.remove(structParent.intValue());
	}

	/**
	 * Creates a new MCID in the parent tree of the page
	 * and returns that new MCID so that it can be used
	 * in the content stream
	 * @param structParents	the StructParents entry in the page dictionary
	 * @param ref the item for which we need a new MCID
	 * @return	a new MCID
	 * @throws DocumentException
	 */
	public int processMCID(PdfNumber structParents, PdfIndirectReference ref)
			throws DocumentException {
		if (ref == null)
			throw new DocumentException(MessageLocalization.getComposedMessage("can.t.read.document.structure"));
                PdfObject object = parentTree.get(structParents.intValue());
                PdfArray array = (PdfArray)PdfReader.getPdfObject(object);
                int i = getNextMCID(structParents);
                if (i < array.size()) {
                        array.set(i, ref);
                        return i;
                }
		array.add(ref);
		return array.size() - 1;
	}
        
        /**
	 * Finds the next available MCID, which is either the lowest empty ID in
         * the existing range, or the first available higher number.
	 * @param structParents	the StructParents entry in the page dictionary
	 * @return	the first available MCID
	 */
        public int getNextMCID(PdfNumber structParents) {
            PdfObject object = parentTree.get(structParents.intValue());
            PdfArray array = (PdfArray)PdfReader.getPdfObject(object);
            for (int i = 0; i < array.size(); i++) {
                if (array.getAsIndirectObject(i) == null) {
                    return i;
                }
            }
            return array.size();
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
