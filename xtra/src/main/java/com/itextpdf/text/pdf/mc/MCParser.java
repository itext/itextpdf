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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.pdf.ByteBuffer;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentParser;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

public class MCParser {
	/** The Logger instance */
	protected final static Logger LOGGER = LoggerFactory.getLogger(MCParser.class);

	/** Factory that will help us build a RandomAccessSource. */
	protected RandomAccessSourceFactory factory = new RandomAccessSourceFactory();
    
	/** A map with all supported operators operators (PDF syntax). */
    protected static Map<String, PdfOperator> operators = null;
	
	/** Constant used for the default operator. */
    public static final String DEFAULTOPERATOR = "DefaultOperator";
    
    /** A new line operator */
    public static final PdfLiteral TSTAR = new PdfLiteral("T*");
    
    /** The OutputStream of this worker object. */
    protected static ByteArrayOutputStream baos;

    /** The list with structure items. */
    protected StructureItems items;
    
    // properties of the page that is being processed
    
    /** The reference to the page dictionary */
    protected PdfIndirectReference pageref;
    
    /** the XObject dictionary of the page that is being processed. */
    protected PdfDictionary xobjects;
    
    /** the StructParents of the page that is being processed. */
    protected PdfNumber structParents;
    
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
     * Parses the content of a page, replacing appearances of annotations
     * with Form XObjects.
     * @param page a page dictionary
     * @throws IOException
     */
    public void parse(PdfDictionary page, PdfIndirectReference pageref, boolean finalPage) throws IOException {
    	this.pageref = pageref;
    	baos = new ByteArrayOutputStream();
    	structParents = page.getAsNumber(PdfName.STRUCTPARENTS);
    	PdfDictionary resources = page.getAsDict(PdfName.RESOURCES);
    	xobjects = resources.getAsDict(PdfName.XOBJECT);
    	if (xobjects == null) {
    		xobjects = new PdfDictionary();
    		resources.put(PdfName.XOBJECT, xobjects);
    	}
		PRStream stream = (PRStream)page.getAsStream(PdfName.CONTENTS);
		byte[] contentBytes = PdfReader.getStreamBytes(stream);
        PRTokeniser tokeniser = new PRTokeniser(new RandomAccessFileOrArray(factory.createSource(contentBytes)));
        PdfContentParser ps = new PdfContentParser(tokeniser);
        ArrayList<PdfObject> operands = new ArrayList<PdfObject>();
        while (ps.parse(operands).size() > 0){
            PdfLiteral operator = (PdfLiteral)operands.get(operands.size() - 1);
            processOperator(operator, operands);
        }
        if (finalPage) {
        	LOGGER.info(String.format("There are %d items left for processing", items.size()));
        	for (StructureItem item : items) {
        		convertToXObject(item);
        	}
        }
        baos.flush();
        baos.close();
        stream.setData(baos.toByteArray());
    }
    
    /**
     * Processes an operator, for instance: write the operator and its operands to baos.
     * @param operator	the operator
     * @param operands	the operator's operands
     * @throws IOException
     */
    protected void processOperator(PdfLiteral operator, List<PdfObject> operands) throws IOException {
        PdfOperator op = operators.get(operator.toString());
        if (op == null)
            op = operators.get(DEFAULTOPERATOR);
        op.process(this, operator, operands);
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
    	else
    		etExtra = false;
    	this.inText = inText;
    }
    
    /**
     * When an MCID is encountered, the parser will check the list
     * structure items and turn an annotation into an XObject if
     * necessary.
     * @param mcid	the MCID that was encountered in the content stream
     * @throws IOException
     */
    protected void dealWithMcid(PdfNumber mcid) throws IOException {
    	if (mcid == null)
    		return;
    	LOGGER.info(String.format("Encountered MCID %s in content", mcid));
    	StructureItem item = items.get(0);
    	switch (item.process(mcid.intValue())) {
    	case 0 :
    		items.remove(0);
    		LOGGER.info(String.format("Discovered %s as an object reference", item.getObj()));
    		convertToXObject(item);
    		dealWithMcid(mcid);
    		return;
    	case 1 :
    		LOGGER.info("Removed structure item from stack.");
    		items.remove(0);
    		return;
    	case 2:
    		LOGGER.info("Removed MCID from structure item.");
    		return;
    	default:
    		LOGGER.warn("MCID not found!");
    	}
    }

    /**
     * Converts an annotation structure item to a Form XObject annotation.
     * @param item the structure item
     * @throws IOException
     */
    protected void convertToXObject(StructureItem item) throws IOException {
    	PdfDictionary dict = item.getObj();
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
    	stream.put(PdfName.STRUCTPARENT, structParent);
    	PdfIndirectReference xobjr = ap.getAsIndirectObject(PdfName.N);
    	if (xobjr == null)
    		return;
    	int mcid = items.processMCID(structParents, item);
		LOGGER.info("Using MCID " + mcid);
    	item.getStructElem().put(PdfName.S, PdfName.P);
    	item.getStructElem().put(PdfName.PG, pageref);
    	item.getStructElem().put(PdfName.K, new PdfNumber(mcid));
    	item.getObjr().put(PdfName.OBJ, xobjr);
    	items.removeFromParentTree(structParent);
    	PdfName xobj = new PdfName("XObj" + structParent.intValue());
    	LOGGER.info("Creating XObject with name " + xobj);
    	xobjects.put(xobj, xobjr);
    	PdfArray array = dict.getAsArray(PdfName.RECT);
    	Rectangle rect = new Rectangle(
    			array.getAsNumber(0).floatValue(), array.getAsNumber(1).floatValue(),
    			array.getAsNumber(2).floatValue(), array.getAsNumber(3).floatValue());
    	rect.normalize();
    	if (inText && !btWrite) {
    		LOGGER.debug("Introducing extra ET");
    		baos.write("ET\n".getBytes());
    		etExtra = true;
    	}
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
    	if (inText)
    		btWrite = true;
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
     * Adds an operator and its operands (if any) to baos.
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
    		LOGGER.debug("BT written");
    		baos.write("BT ".getBytes());
    		if (etExtra) {
    			baos.write(text.toString().getBytes());
    			etExtra = false;
    			text = new StringBuffer();
    		}
    	}
    	btWrite = false;
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
    	public void process(MCParser parser, PdfLiteral operator, List<PdfObject> operands) throws IOException;
    }


    /**
     * Class that processes content by just printing the operator and its operands.
     */
    private static class CopyContentOperator implements PdfOperator{
    	
		/**
		 * @see com.itextpdf.text.pdf.ocg.OCGParser.PdfOperator#process(com.itextpdf.text.pdf.ocg.OCGParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
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
		 * @see com.itextpdf.text.pdf.ocg.OCGParser.PdfOperator#process(com.itextpdf.text.pdf.ocg.OCGParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
    	public void process(MCParser parser, PdfLiteral operator,
    			List<PdfObject> operands) throws IOException {
    		if ("BDC".equals(operator.toString())) {
    			if (operands.get(1).isDictionary()) {
    				PdfDictionary dict = (PdfDictionary)operands.get(1);
    				parser.dealWithMcid(dict.getAsNumber(PdfName.MCID));
    			}
    		}
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
     * Class that knows how to the ET operators.
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
     * Class that knows how to the ET operators.
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