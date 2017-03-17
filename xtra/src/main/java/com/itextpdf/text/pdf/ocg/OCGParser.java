/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.pdf.ocg;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfContentParser;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A helper class for OCGRemover.
 */
public class OCGParser {

	/** Constant used for the default operator. */
    public static final String DEFAULTOPERATOR = "DefaultOperator";
    
	/** A map with all supported operators operators (PDF syntax). */
    protected static final Map<String, PdfOperator> operators;

	static {
		operators = new HashMap<String, PdfOperator>();
		populateOperators();
	}
    
    /** The OutputStream of this worker object. */
    protected ByteArrayOutputStream baos;
   
    /** Keeps track of BMC/EMC balance. */
    protected int mc_balance = 0;
    
    /** The names of XObjects that shouldn't be shown. */
    protected Set<PdfName> xobj;
    
    /** The OCGs that need to be removed. */
    protected Set<String> ocgs;
    
    /** The OCG properties. */
    protected PdfDictionary properties = null;
    
    /**
     * Creates an instance of the OCGParser.
     * @param ocgs	a set of String values with the names of the OCGs that need to be removed.
     */
    public OCGParser(Set<String> ocgs) {
    	this.ocgs = ocgs;
    }
    
    /**
     * Parses a stream object and removes OCGs.
     * @param stream	a stream object
     * @param resources	the resources dictionary of that object (containing info about the OCGs)
     */
    public void parse(PRStream stream, PdfDictionary resources) throws IOException {
    	baos = new ByteArrayOutputStream();
    	properties = resources.getAsDict(PdfName.PROPERTIES);
    	xobj = new HashSet<PdfName>();
    	PdfDictionary xobjects = resources.getAsDict(PdfName.XOBJECT);
    	PRStream xobject;
    	PdfDictionary oc;
    	PdfString ocname;
		if (xobjects != null) {
			// remove XObject (form or image) that belong to an OCG that needs to be removed
			for (PdfName name : xobjects.getKeys()) {
				xobject = (PRStream)xobjects.getAsStream(name);
				oc = xobject.getAsDict(PdfName.OC);
				if (oc != null) {
					ocname = oc.getAsString(PdfName.NAME);
					if (ocname != null && ocgs.contains(ocname.toString())) {
						xobj.add(name);
					}
				}
			}
			for (PdfName name : xobj) {
				xobjects.remove(name);
			}
		}
    	try {
    		// parse the content stream
    		byte[] contentBytes = PdfReader.getStreamBytes(stream);
            PRTokeniser tokeniser = new PRTokeniser(new RandomAccessFileOrArray(contentBytes));
            PdfContentParser ps = new PdfContentParser(tokeniser);
            ArrayList<PdfObject> operands = new ArrayList<PdfObject>();
            while (ps.parse(operands).size() > 0){
                PdfLiteral operator = (PdfLiteral)operands.get(operands.size() - 1);
				processOperator(this, operator, operands);
				if ("BI".equals(operator.toString())) {
					int found = 0;
					int ch;
					boolean immediateAfterBI = true;
					while ((ch = tokeniser.read()) != -1) {
						if (!immediateAfterBI || !PRTokeniser.isWhitespace(ch)) {
							baos.write(ch);
						}
						immediateAfterBI = false;
						if (found == 0 && PRTokeniser.isWhitespace(ch)){
							found++;
						} else if (found == 1 && ch == 'E'){
							found++;
						} else if (found == 1 && PRTokeniser.isWhitespace(ch)){
							// this clause is needed if we have a white space character that is part of the image data
							// followed by a whitespace character that precedes the EI operator.  In this case, we need
							// to flush the first whitespace, then treat the current whitespace as the first potential
							// character for the end of stream check. Note that we don't increment 'found' here.
						} else if (found == 2 && ch == 'I'){
							found++;
						} else if (found == 3 && PRTokeniser.isWhitespace(ch)){
							break;
						} else {
							found = 0;
						}
					}
				}
            }
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        baos.flush();
        baos.close();
        stream.setData(baos.toByteArray());
    }
    
    /**
     * Processes an operator.
     * @param parser	the parser that needs to process the operator
     * @param operator	the operator
     * @param operands	its operands
     * @throws Exception
     */
    protected static void processOperator(OCGParser parser, PdfLiteral operator, List<PdfObject> operands) throws Exception{
        PdfOperator op = operators.get(operator.toString());
        if (op == null)
            op = operators.get(DEFAULTOPERATOR);
        op.process(parser, operator, operands);
    }

    /**
     * Populates the operators variable.
     */
    protected static void populateOperators() {
    	operators.put(DEFAULTOPERATOR, new CopyContentOperator());
    	PathConstructionOrPaintingOperator opConstructionPainting = new PathConstructionOrPaintingOperator();
    	operators.put("m", opConstructionPainting);
    	operators.put("l", opConstructionPainting);
    	operators.put("c", opConstructionPainting);
    	operators.put("v", opConstructionPainting);
    	operators.put("y", opConstructionPainting);
    	operators.put("h", opConstructionPainting);
    	operators.put("re", opConstructionPainting);
    	operators.put("S", opConstructionPainting);
    	operators.put("s", opConstructionPainting);
    	operators.put("f", opConstructionPainting);
    	operators.put("F", opConstructionPainting);
    	operators.put("f*", opConstructionPainting);
    	operators.put("B", opConstructionPainting);
    	operators.put("B*", opConstructionPainting);
    	operators.put("b", opConstructionPainting);
    	operators.put("b*", opConstructionPainting);
    	operators.put("n", opConstructionPainting);
    	operators.put("W", opConstructionPainting);
    	operators.put("W*", opConstructionPainting);
    	GraphicsOperator graphics = new GraphicsOperator();
    	operators.put("q", graphics);
    	operators.put("Q", graphics);
    	operators.put("w", graphics);
    	operators.put("J", graphics);
    	operators.put("j", graphics);
    	operators.put("M", graphics);
    	operators.put("d", graphics);
    	operators.put("ri", graphics);
    	operators.put("i", graphics);
    	operators.put("gs", graphics);
    	operators.put("cm", graphics);
    	operators.put("g", graphics);
    	operators.put("G", graphics);
    	operators.put("rg", graphics);
    	operators.put("RG", graphics);
    	operators.put("k", graphics);
    	operators.put("K", graphics);
    	operators.put("cs", graphics);
    	operators.put("CS", graphics);
    	operators.put("sc", graphics);
    	operators.put("SC", graphics);
    	operators.put("scn", graphics);
    	operators.put("SCN", graphics);
    	operators.put("sh", graphics);
    	XObjectOperator	xObject = new XObjectOperator();
    	operators.put("Do", xObject);
    	InlineImageOperator inlineImage = new InlineImageOperator();
    	operators.put("BI", inlineImage);
    	operators.put("EI", inlineImage);
    	TextOperator text = new TextOperator();
    	operators.put("BT", text);
    	operators.put("ID", text);
    	operators.put("ET", text);
    	operators.put("Tc", text);
    	operators.put("Tw", text);
    	operators.put("Tz", text);
    	operators.put("TL", text);
    	operators.put("Tf", text);
    	operators.put("Tr", text);
    	operators.put("Ts", text);
    	operators.put("Td", text);
    	operators.put("TD", text);
    	operators.put("Tm", text);
    	operators.put("T*", text);
    	operators.put("Tj", text);
    	operators.put("'", text);
    	operators.put("\"", text);
    	operators.put("TJ", text);
    	MarkedContentOperator markedContent = new MarkedContentOperator();
    	operators.put("BMC", markedContent);
    	operators.put("BDC", markedContent);
    	operators.put("EMC", markedContent);
    }

    /**
     * Checks operands to find out if the corresponding operator needs to be present or not.
     * @param operands	a list of operands
     * @return	true if the operators needs to be present.
     */
    protected boolean isVisible(List<PdfObject> operands) {
    	if (operands.size() > 1 && xobj.contains(operands.get(0)))
    		return false;
    	return true;
    }
    
    /**
     * Checks if the parser is currently parsing content that needs to be ignored.
     * @return	true if the content needs to be ignored
     */
    protected boolean isToRemoved() {
    	if (mc_balance > 0) return true;
    	return false;
    }
    
    /**
     * Keeps track of the MarkedContent state.
     * @param ocref	a reference to an OCG dictionary
     */
    protected void checkMarkedContentStart(PdfName ocref) {
    	if (mc_balance > 0) {
    		mc_balance++;
    		return;
    	}
    	if (properties == null)
    		return;
    	PdfDictionary ocdict = properties.getAsDict(ocref);
    	if (ocdict == null)
    		return;
    	PdfString ocname = ocdict.getAsString(PdfName.NAME);
    	if (ocname == null)
    		return;
    	if (ocgs.contains(ocname.toString()))
    		mc_balance++;
    }

    /**
     * Keeps track of the MarkedContent state.
     */
    protected void checkMarkedContentEnd() {
    	if (mc_balance > 0)
    		mc_balance--;
    }
    
    /**
     * Processes an operator
     * @param operator	the operator
     * @param operands	its operands
     * @param removable	is the operator eligable for removal?
     * @throws IOException
     */
    protected void process(PdfLiteral operator, List<PdfObject> operands, boolean removable) throws IOException{
		if (removable && isToRemoved())
			return;
    	operands.remove(operator);
		for (PdfObject o : operands) {
			printsp(o);
		}
		println(operator);
    }
    
    /**
     * Writes a PDF object to the OutputStream, followed by a space character.
     * @param o
     * @throws IOException
     */
    protected void printsp(PdfObject o) throws IOException {
    	o.toPdf(null, baos);
    	baos.write(' ');
    }

    /**
     * Writes a PDF object to the OutputStream, followed by a newline character.
     * @param o
     * @throws IOException
     */
    protected void println(PdfObject o) throws IOException {
    	o.toPdf(null, baos);
    	baos.write('\n');
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
    	public void process(OCGParser parser, PdfLiteral operator, List<PdfObject> operands) throws IOException;
    }
    
    /**
     * Class that knows how to process path construction, path painting and path clipping operators.
     */
    private static class PathConstructionOrPaintingOperator implements PdfOperator {

		/**
		 * @see com.itextpdf.text.pdf.ocg.OCGParser.PdfOperator#process(com.itextpdf.text.pdf.ocg.OCGParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
		public void process(OCGParser parser, PdfLiteral operator,
				List<PdfObject> operands) throws IOException {
			parser.process(operator, operands, true);
		}
    	
    }
    
    /**
     * Class that knows how to process graphics state operators.
     */
    private static class GraphicsOperator implements PdfOperator {

		/**
		 * @see com.itextpdf.text.pdf.ocg.OCGParser.PdfOperator#process(com.itextpdf.text.pdf.ocg.OCGParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
    	public void process(OCGParser parser, PdfLiteral operator,
    			List<PdfObject> operands) throws IOException {
			parser.process(operator, operands, false);
    	}
    }
    
    /**
     * Class that knows how to process XObject operators.
     */
    private static class XObjectOperator implements PdfOperator {

		/**
		 * @see com.itextpdf.text.pdf.ocg.OCGParser.PdfOperator#process(com.itextpdf.text.pdf.ocg.OCGParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
    	public void process(OCGParser parser, PdfLiteral operator,
    			List<PdfObject> operands) throws IOException {
    		if (parser.isVisible(operands))
    			parser.process(operator, operands, true);
    	}
    }
    
    /**
     * Class that knows how to process inline image operators.
     */
    private static class InlineImageOperator implements PdfOperator {

		/**
		 * @see com.itextpdf.text.pdf.ocg.OCGParser.PdfOperator#process(com.itextpdf.text.pdf.ocg.OCGParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
    	public void process(OCGParser parser, PdfLiteral operator,
    			List<PdfObject> operands) throws IOException {
			parser.process(operator, operands, true);
    	}
    }

    /**
     * Class that knows how to process text state operators.
     */
    private static class TextOperator implements PdfOperator {

		/**
		 * @see com.itextpdf.text.pdf.ocg.OCGParser.PdfOperator#process(com.itextpdf.text.pdf.ocg.OCGParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
    	public void process(OCGParser parser, PdfLiteral operator,
    			List<PdfObject> operands) throws IOException {
			parser.process(operator, operands, true);
    	}
    }

    /**
     * Class that knows how to process marked content operators.
     */
    private static class MarkedContentOperator implements PdfOperator {

		/**
		 * @see com.itextpdf.text.pdf.ocg.OCGParser.PdfOperator#process(com.itextpdf.text.pdf.ocg.OCGParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
    	public void process(OCGParser parser, PdfLiteral operator,
    			List<PdfObject> operands) throws IOException {
    		if ("BDC".equals(operator.toString())) {
                    PdfName operand = null;
                    if (operands.size() > 1 && PdfName.OC.equals(operands.get(0))) {
    			operand = (PdfName) operands.get(1);
                    }
    			parser.checkMarkedContentStart(operand);
    		} else if ("BMC".equals(operator.toString())) {
    			parser.checkMarkedContentStart(null);
    		}
			parser.process(operator, operands, true);
			if ("EMC".equals(operator.toString())) {
    			parser.checkMarkedContentEnd();
    		}
    	}
    }

    /**
     * Class that processes unknown content.
     */
    private static class CopyContentOperator implements PdfOperator{
    	
		/**
		 * @see com.itextpdf.text.pdf.ocg.OCGParser.PdfOperator#process(com.itextpdf.text.pdf.ocg.OCGParser, com.itextpdf.text.pdf.PdfLiteral, java.util.List)
		 */
		public void process(OCGParser parser,
				PdfLiteral operator, List<PdfObject> operands) throws IOException {
			parser.process(operator, operands, true);
		}
    }

}
