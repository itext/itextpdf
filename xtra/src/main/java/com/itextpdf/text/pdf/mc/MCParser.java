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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.log.Level;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentParser;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

/**
 * This class will parse page content streams and add Do operators
 * in a marked-content sequence for every field that needs to be
 * flattened.
 */
public class MCParser {
	
	// static final constants
	
	/** The Logger instance */
	protected final static Logger LOGGER = LoggerFactory.getLogger(MCParser.class);

	/** Factory that will help us build a RandomAccessSource. */
	protected final static RandomAccessSourceFactory RASFACTORY = new RandomAccessSourceFactory();
	
	/** Constant used for the default operator. */
    public static final String DEFAULTOPERATOR = "DefaultOperator";
    
    /** A new line operator */
    public static final PdfLiteral TSTAR = new PdfLiteral("T*");
    
    // variables needed when parsing
    
	/** A map with all supported operators operators (PDF syntax). */
    protected Map<String, PdfOperator> operators = null;

    /** The list with structure items. */
    protected StructureItems items;
    
    // properties of the page that is being processed
    
    /** The contents of the new content stream of the page. */
    protected ByteArrayOutputStream baos;
    
    /** The page dictionary */
    protected PdfDictionary page;
    
    /** The reference to the page dictionary */
    protected PdfIndirectReference pageref;
    
    /** the annotations of the page that is being processed. */
    protected PdfArray annots;
    
    /** the StructParents of the page that is being processed. */
    protected PdfNumber structParents;
    
    /** the XObject dictionary of the page that is being processed. */
    protected PdfDictionary xobjects;
    
    // Keeping track of text state
    
    /** Did we postpone writing a BT operator? */
    protected boolean btWrite = false;
    
    /** Did we postpone writing a BT operator? */
    protected boolean etExtra = false;
    
    /** Are we inside a BT/ET sequence? */
    protected boolean inText = false;
    
    /** A buffer containing text state. */
    protected StringBuffer text;
    
    /**
     * Creates an MCParser object.
     * @param items a list of StructureItem objects
     */
    public MCParser(StructureItems items) {
    	populateOperators();
    	this.items = items;
    }
    
    /**
     * Populates the operators variable.
     */
    protected void populateOperators() {
    	if (operators != null)
    		return;
    	operators = new HashMap<String, PdfOperator>();
    	operators.put(DEFAULTOPERATOR, new CopyContentOperator());
    	PdfOperator markedContent = new BeginMarkedContentDictionaryOperator();
    	operators.put("BDC", markedContent);
    	PdfOperator doOperator = new DoOperator();
    	operators.put("Do", doOperator);
    	PdfOperator beginText = new BeginTextOperator();
    	operators.put("BT", beginText);
    	PdfOperator endText = new EndTextOperator();
    	operators.put("ET", endText);
    	PdfOperator textPos = new TextPositioningOperator();
    	operators.put("Td", textPos);
    	operators.put("TD", textPos);
    	operators.put("Tm", textPos);
    	operators.put("T*", textPos);
    	PdfOperator textState = new TextStateOperator();
    	operators.put("Tc", textState);
    	operators.put("Tw", textState);
    	operators.put("Tz", textState);
    	operators.put("TL", textState);
    	operators.put("Tf", textState);
    	operators.put("Tr", textState);
    	operators.put("Ts", textState);
    	PdfOperator textNL = new TextNewLineOperator();
    	operators.put("'", textNL);
    	operators.put("\"", textNL);
    }
    
    /**
     * Parses the content of a page, inserting the normal (/N) appearances (/AP)
     * of annotations into the content stream as Form XObjects.
     * @param page		a page dictionary
     * @param pageref	the reference to the page dictionary
     * @param finalPage	indicates whether the page being processed is the final page in the document
     * @throws IOException
     * @throws DocumentException 
     */
    public void parse(PdfDictionary page, PdfIndirectReference pageref) throws IOException, DocumentException {
    	LOGGER.info("Parsing page with reference " + pageref);
    	// initializing member variables
    	baos = new ByteArrayOutputStream();
    	this.page = page;
    	this.pageref = pageref;
    	structParents = page.getAsNumber(PdfName.STRUCTPARENTS);
    	if (structParents == null)
    		throw new DocumentException(MessageLocalization.getComposedMessage("can.t.read.document.structure"));
    	annots = page.getAsArray(PdfName.ANNOTS);
    	if (annots == null)
    		annots = new PdfArray();
    	PdfDictionary resources = page.getAsDict(PdfName.RESOURCES);
    	xobjects = resources.getAsDict(PdfName.XOBJECT);
    	if (xobjects == null) {
    		xobjects = new PdfDictionary();
    		resources.put(PdfName.XOBJECT, xobjects);
    	}
    	// parsing the content stream of the page
		PRStream stream = (PRStream)page.getAsStream(PdfName.CONTENTS);
		byte[] contentBytes = PdfReader.getStreamBytes(stream);
        PRTokeniser tokeniser = new PRTokeniser(new RandomAccessFileOrArray(RASFACTORY.createSource(contentBytes)));
        PdfContentParser ps = new PdfContentParser(tokeniser);
        ArrayList<PdfObject> operands = new ArrayList<PdfObject>();
        while (ps.parse(operands).size() > 0){
            PdfLiteral operator = (PdfLiteral)operands.get(operands.size() - 1);
            processOperator(operator, operands);
        }
        // dealing with orphans
        while (items.size() > 0 && items.get(0).getPageref() == pageref.getNumber()) {
        	StructureItem item = items.get(0);
        	if (item instanceof StructureObject) {
        		convertToXObject((StructureObject)item);
        		items.remove(0);
        	}
        }
        if (annots.size() == 0) {
        	page.remove(PdfName.ANNOTS);
        }
        else {
        	PdfDictionary annot;
        	for (int i = 0; i < annots.size(); i++) {
        		annot = annots.getAsDict(i);
        		if (annot.getAsNumber(PdfName.STRUCTPARENT) == null)
        			throw new DocumentException(MessageLocalization.getComposedMessage("could.not.flatten.file.untagged.annotations.found"));
        	}
        }
        // replacing the content stream
        baos.flush();
        baos.close();
        stream.setData(baos.toByteArray());
        // showing how many items are left
		if (LOGGER.isLogging(Level.INFO)) {
			LOGGER.info(String.format("There are %d items left for processing", items.size()));
		}
	}
    
    /**
     * When an XObject with a StructParent is encountered,
     * we want to remove it from the stack.
     * @param xobj	the name of an XObject
     */
    protected void dealWithXObj(PdfName xobj) {
    	PdfDictionary dict = xobjects.getAsStream(xobj);
    	PdfNumber structParent = dict.getAsNumber(PdfName.STRUCTPARENT);
    	if (LOGGER.isLogging(Level.INFO)) {
			LOGGER.info(String.format("Encountered StructParent %s in content", structParent));
		}
		if (structParent == null)
    		return;
    	StructureItem item = items.get(0);
    	if (item.checkStructParent(pageref.getNumber(), structParent.intValue()) == 1)
    		items.remove(0);
    }
    
    /**
     * When an MCID is encountered, the parser will check the list
     * structure items and turn an annotation into an XObject if
     * necessary.
     * @param mcid	the MCID that was encountered in the content stream
     * @throws IOException
     * @throws DocumentException 
     */
    protected void dealWithMcid(PdfNumber mcid) throws IOException, DocumentException {
    	if (mcid == null)
    		return;
    	StructureItem item = items.get(0);
    	if (LOGGER.isLogging(Level.INFO)) {
			LOGGER.info(String.format("Encountered MCID %s in content, comparing with %s", mcid, item));
		}
		switch (item.checkMCID(pageref.getNumber(), mcid.intValue())) {
    	case 0 :
    		StructureObject obj = (StructureObject)item;
    		convertToXObject(obj);
    		LOGGER.info("Removed structure item from stack.");
    		items.remove(0);
			dealWithMcid(mcid);
    		return;
    	case 1 :
    		LOGGER.info("Removed structure item from stack.");
    		items.remove(0);
    		return;
    	default:
    		LOGGER.warn("MCID not found! There's probably an error in your form!");
    		// hack to deal with MCIDs that are added in the wrong order
    		int check;
    		for (int i = 1; i < items.size(); i++) {
    			item = items.get(i);
    			check = item.checkMCID(pageref.getNumber(), mcid.intValue());
    			switch (check) {
    			case 1:
    	    		LOGGER.info("Removed structure item from stack.");
    				items.remove(i);
    				return;
    			case 0:
    				break;
    			}
    		}
    		throw new DocumentException(MessageLocalization.getComposedMessage("can.t.read.document.structure"));
    	}
    }

    /**
     * Converts an annotation structure item to a Form XObject annotation.
     * @param item	the structure item
     * @throws IOException
     * @throws DocumentException 
     */
    protected void convertToXObject(StructureObject item) throws IOException, DocumentException {
    	PdfDictionary structElem = item.getStructElem();
    	if (structElem == null)
    		return;
    	PdfDictionary dict = item.getObjAsDict();
    	if (dict == null || !dict.checkType(PdfName.ANNOT))
    		return;
    	PdfDictionary ap = dict.getAsDict(PdfName.AP);
    	if (ap == null)
    		return;
    	PdfNumber structParent = dict.getAsNumber(PdfName.STRUCTPARENT);
    	if (structParent == null)
    		return;
    	PdfStream stream = ap.getAsStream(PdfName.N);
    	if (stream == null)
    		return;
    	PdfIndirectReference xobjr = ap.getAsIndirectObject(PdfName.N);
    	if (xobjr == null)
    		return;
    	// remove the annotation from the page
    	for (int i = 0; i < annots.size(); i++) {
    		PdfIndirectReference annotref = annots.getAsIndirectObject(i);
    		if (item.getObjRef().getNumber() == annotref.getNumber()) {
    			annots.remove(i);
    			break;
    		}
    	}
    	// replace the existing attributes by a PrintField attribute
    	PdfDictionary attribute = new PdfDictionary();
    	attribute.put(PdfName.O, PdfName.PRINTFIELD);
    	PdfString description = dict.getAsString(PdfName.TU);
    	if (description == null)
    		description = dict.getAsString(PdfName.T);
    	if (PdfName.BTN.equals(dict.get(PdfName.FT))) {
    		PdfNumber fflags = dict.getAsNumber(PdfName.FF);
    		if (fflags != null) {
    			int ff = fflags.intValue();
                if ((ff & PdfFormField.FF_PUSHBUTTON) != 0)
                    attribute.put(PdfName.ROLE, PdfName.PB);
                // I don't think the condition below will ever be true
                if ((ff & PdfFormField.FF_RADIO) != 0)
                    attribute.put(PdfName.ROLE, PdfName.rb);
                else
                    attribute.put(PdfName.ROLE, PdfName.CB);
    		}
    	}
    	else {
    		attribute.put(PdfName.ROLE, PdfName.TV);
    	}
    	attribute.put(PdfName.DESC, description);
    	// Updating the values of the StructElem dictionary
    	PdfString t = structElem.getAsString(PdfName.T);
    	if (t == null || t.toString().trim().length() == 0)
    		structElem.put(PdfName.T, dict.getAsString(PdfName.T));
    	structElem.put(PdfName.A, attribute);
    	structElem.put(PdfName.S, PdfName.P);
    	structElem.put(PdfName.PG, pageref);
    	// Defining a new MCID
    	int mcid = items.processMCID(structParents, item.getRef());
		LOGGER.info("Using MCID " + mcid);
    	structElem.put(PdfName.K, new PdfNumber(mcid));
    	// removing the annotation from the parent tree
    	items.removeFromParentTree(structParent);
    	// Adding the XObject to the page
    	PdfName xobj = new PdfName("XObj" + structParent.intValue());
    	LOGGER.info("Creating XObject with name " + xobj);
    	xobjects.put(xobj, xobjr);
    	PdfArray array = dict.getAsArray(PdfName.RECT);
    	// Getting the position of the annotation
    	Rectangle rect = new Rectangle(
    			array.getAsNumber(0).floatValue(), array.getAsNumber(1).floatValue(),
    			array.getAsNumber(2).floatValue(), array.getAsNumber(3).floatValue());
    	rect.normalize();
    	// A Do operator is forbidden inside a text block
    	if (inText && !btWrite) {
    		LOGGER.debug("Introducing extra ET");
    		baos.write("ET\n".getBytes());
    		etExtra = true;
    	}
    	// Writing the marked-content sequence with the Do operator
    	// Note that the position assumes that the CTM wasn't changed in the graphics state
    	// TODO: do the math if the CTM did change!
    	ByteBuffer buf = new ByteBuffer();
    	buf.append("/P <</MCID ");
    	buf.append(mcid);
    	buf.append(">> BDC\n");
    	buf.append("q 1 0 0 1 ");
    	buf.append(rect.getLeft());
    	buf.append(" ");
    	buf.append(rect.getBottom());
    	buf.append(" cm ");
    	buf.append(xobj.getBytes());
    	buf.append(" Do Q\n");
    	buf.append("EMC\n");
    	buf.flush();
    	buf.writeTo(baos);
    	// if we were inside a text block, we've introduced an ET, so we'll need to write a BT
    	if (inText)
    		btWrite = true;
    }
    
    // Operator actions
    
    /**
     * Processes an operator, for instance: write the operator and its operands to baos.
     * @param operator	the operator
     * @param operands	the operator's operands
     * @throws IOException
     * @throws DocumentException 
     */
    protected void processOperator(PdfLiteral operator, List<PdfObject> operands) throws IOException, DocumentException {
        PdfOperator op = operators.get(operator.toString());
        if (op == null)
            op = operators.get(DEFAULTOPERATOR);
        op.process(this, operator, operands);
    }
    
    /**
     * Adds an operator and its operands (if any) to baos.
     * @param operator	the operator
     * @param operands	its operands
     * @throws IOException
     */
    protected void printOperator(PdfLiteral operator, List<PdfObject> operands) throws IOException{
    	operands.remove(operator);
		for (PdfObject o : operands) {
			printsp(o);
		}
		println(operator);
    }
    
    /**
     * Adds an operator and its operands (if any) to baos, keeping track of the text state.
     * @param operator	the operator
     * @param operands	its operands
     * @throws IOException
     */
    protected void printTextOperator(PdfLiteral operator, List<PdfObject> operands) throws IOException{
    	for (PdfObject obj : operands)
    		text.append(obj).append(" ");
    	text.append("\n");
    	printOperator(operator, operands);
    }
    
    /**
     * Writes a PDF object to the OutputStream, followed by a space character.
     * @param o a PdfObject
     * @throws IOException
     */
    protected void printsp(PdfObject o) throws IOException {
    	checkBT();
    	o.toPdf(null, baos);
    	baos.write(' ');
    }

    /**
     * Writes a PDF object to the OutputStream, followed by a newline character.
     * @param o a PdfObject
     * @throws IOException
     */
    protected void println(PdfObject o) throws IOException {
    	checkBT();
    	o.toPdf(null, baos);
    	baos.write('\n');
    }

    /**
     * Checks if a BT operator is waiting to be added.
     */
    protected void checkBT() throws IOException {
    	if (btWrite) {
    		baos.write("BT ".getBytes());
    		if (etExtra) {
    			baos.write(text.toString().getBytes());
    			etExtra = false;
    			text = new StringBuffer();
    		}
    		LOGGER.debug("BT written");
    	}
    	btWrite = false;
    }
    
    /**
     * Informs the parser that we're inside or outside a text object.
     * Also sets a parameter indicating that BT needs to be written.
     * @param inText	true if we're inside.
     */
    protected void setInText(boolean inText) {
    	if (inText) {
    		text = new StringBuffer();
    		btWrite = true;
    	}
    	else {
    		etExtra = false;
    	}
    	this.inText = inText;
    }
    
    // Operator interface and implementing classes
    
    /**
     * PDF Operator interface.
     */
    public interface PdfOperator {
    	/**
    	 * Methods that processes an operator
    	 * @param parser	the parser
    	 * @param operator	the operator
    	 * @param operands	its operands
    	 * @throws IOException
    	 */
    	public void process(MCParser parser, PdfLiteral operator, List<PdfObject> operands) throws DocumentException, IOException;
    }


    /**
     * Class that processes content by just printing the operator and its operands.
     */
    private static class CopyContentOperator implements PdfOperator{
    	
		/**
		 * @see com.itextpdf.text.pdf.mc.MCParser.PdfOperator#process(com.itextpdf.text.pdf.mc.MCParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
		public void process(MCParser parser,
				PdfLiteral operator, List<PdfObject> operands) throws IOException {
			parser.printOperator(operator, operands);
		}
    }

    /**
     * Class that knows how to process marked content operators.
     */
    private static class BeginMarkedContentDictionaryOperator implements PdfOperator {

    	/**
    	 * @see com.itextpdf.text.pdf.mc.MCParser.PdfOperator#process(com.itextpdf.text.pdf.mc.MCParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
    	 */
    	public void process(MCParser parser, PdfLiteral operator,
    			List<PdfObject> operands) throws IOException, DocumentException {
    		if (operands.get(1).isDictionary()) {
    			PdfDictionary dict = (PdfDictionary)operands.get(1);
    			parser.dealWithMcid(dict.getAsNumber(PdfName.MCID));
    		}
			parser.printOperator(operator, operands);
    	}
    }
    
    /**
     * Class that knows how to process Do operators.
     */
    private static class DoOperator implements PdfOperator {

		/**
		 * @see com.itextpdf.text.pdf.mc.MCParser.PdfOperator#process(com.itextpdf.text.pdf.mc.MCParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
		public void process(MCParser parser, PdfLiteral operator,
				List<PdfObject> operands) throws IOException {
			if (operands.get(0).isName())
				parser.dealWithXObj((PdfName)operands.get(0));
			parser.printOperator(operator, operands);
		}
    	
    }
    
    /**
     * Class that knows how to process the BT operator.
     */
    private static class BeginTextOperator implements PdfOperator {

		/**
		 * @see com.itextpdf.text.pdf.mc.MCParser.PdfOperator#process(com.itextpdf.text.pdf.mc.MCParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
		public void process(MCParser parser, PdfLiteral operator,
				List<PdfObject> operands) throws IOException {
			LOGGER.debug("BT: begin text on hold");
			parser.setInText(true);
		}
    	
    }
    
    /**
     * Class that knows how to the ET operators.
     */
    private static class EndTextOperator implements PdfOperator {

		/**
		 * @see com.itextpdf.text.pdf.mc.MCParser.PdfOperator#process(com.itextpdf.text.pdf.mc.MCParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
		public void process(MCParser parser, PdfLiteral operator,
				List<PdfObject> operands) throws IOException {
			LOGGER.debug("ET: end text block");
			parser.setInText(false);
			parser.printOperator(operator, operands);
		}
    	
    }
    
    /**
     * Class that knows how to the ET operators.
     */
    private static class TextPositioningOperator implements PdfOperator {

		/**
		 * @see com.itextpdf.text.pdf.mc.MCParser.PdfOperator#process(com.itextpdf.text.pdf.mc.MCParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
		public void process(MCParser parser, PdfLiteral operator,
				List<PdfObject> operands) throws IOException {
			parser.printTextOperator(operator, operands);
		}
    	
    }
    
    /**
     * Class that knows how to the text state operators.
     */
    private static class TextStateOperator implements PdfOperator {

		/**
		 * @see com.itextpdf.text.pdf.mc.MCParser.PdfOperator#process(com.itextpdf.text.pdf.mc.MCParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
		public void process(MCParser parser, PdfLiteral operator,
				List<PdfObject> operands) throws IOException {
			parser.printTextOperator(operator, operands);
		}
    	
    }
    
    /**
     * Class that knows how to the text state operators that result in a newline.
     */
    private static class TextNewLineOperator implements PdfOperator {

		/**
		 * @see com.itextpdf.text.pdf.mc.MCParser.PdfOperator#process(com.itextpdf.text.pdf.mc.MCParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
		public void process(MCParser parser, PdfLiteral operator,
				List<PdfObject> operands) throws IOException {
			List<PdfObject> list = new ArrayList<PdfObject>();
			list.add(TSTAR);
			parser.printTextOperator(MCParser.TSTAR, list);
		}
    	
    }
}
