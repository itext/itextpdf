/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
package com.itextpdf.text.pdf.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentParser;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfString;

/**
 * Processor for a PDF content Stream.
 * @since	2.1.4
 */
public class PdfContentStreamProcessor {

	/** A map with all supported operators operators (PDF syntax). */
    private Map operators;
    /** Resources for the content stream. */
    private ResourceDictionary resources;
    /** Stack keeping track of the graphics state. */
    private Stack gsStack = new Stack();
    /** Text matrix. */
    private Matrix textMatrix;
    /** Text line matrix. */
    private Matrix textLineMatrix;
    /** Listener that will be notified of render events */
    final private RenderListener renderListener;
    /** Convenience cast of {@link RenderListener} */
    final private TextRenderListener textRenderListener;
    /** Convenience cast of {@link RenderListener} */
    final private ImageRenderListener imageRenderListener;
    
    /**
     * Creates a new PDF Content Stream Processor that will send it's output to the
     * designated render listener.
     * 
     * @param renderListener the {@link TextRenderListener} that will receive rendering notifications
     */
    public PdfContentStreamProcessor(RenderListener renderListener) {
        if (renderListener instanceof TextRenderListener)
            textRenderListener = (TextRenderListener)renderListener;
        else
            textRenderListener = null;
        
        if (renderListener instanceof ImageRenderListener)
            imageRenderListener = (ImageRenderListener)renderListener;
        else
            imageRenderListener = null;
        
        this.renderListener = renderListener;
        populateOperators();
        reset();
    }

    /**
     * Loads all the supported graphics and text state operators in a map.
     */
    private void populateOperators(){
        operators = new HashMap();
        
        registerContentOperator("q", new PushGraphicsState());
        registerContentOperator("Q", new PopGraphicsState());
        registerContentOperator("cm", new ModifyCurrentTransformationMatrix());
        registerContentOperator("gs", new ProcessGraphicsStateResource());
        
        SetTextCharacterSpacing tcOperator = new SetTextCharacterSpacing();
        registerContentOperator("Tc", tcOperator);
        SetTextWordSpacing twOperator = new SetTextWordSpacing();
        registerContentOperator("Tw", twOperator);
        registerContentOperator("Tz", new SetTextHorizontalScaling());
        SetTextLeading tlOperator = new SetTextLeading();
        registerContentOperator("TL", tlOperator);
        registerContentOperator("Tf", new SetTextFont());
        registerContentOperator("Tr", new SetTextRenderMode());
        registerContentOperator("Ts", new SetTextRise());
        
        registerContentOperator("BT", new BeginText());
        registerContentOperator("ET", new EndText());

        TextMoveStartNextLine tdOperator = new TextMoveStartNextLine();
        registerContentOperator("Td", tdOperator);
        registerContentOperator("TD", new TextMoveStartNextLineWithLeading(tdOperator, tlOperator));
        registerContentOperator("Tm", new TextSetTextMatrix());
        TextMoveNextLine tstarOperator = new TextMoveNextLine(tdOperator);
        registerContentOperator("T*", tstarOperator);

        ShowText tjOperator = new ShowText();
        registerContentOperator("Tj", new ShowText());
        MoveNextLineAndShowText tickOperator = new MoveNextLineAndShowText(tstarOperator, tjOperator);
        registerContentOperator("'", tickOperator);
        registerContentOperator("\"", new MoveNextLineAndShowTextWithSpacing(twOperator, tcOperator, tickOperator));
        registerContentOperator("TJ", new ShowTextArray());
        
        registerContentOperator("Do", new Do());
    }
    
    /**
     * Registers a content operator that will be called when the specified operator string is encountered during content processing.
     * Each operator may be registered only once (it is not legal to have multiple operators with the same operatorString)
     * @param operatorString the operator id
     * @param operator the operator that will receive notification when the operator is encountered
     * @since 2.1.7
     */
    public void registerContentOperator(String operatorString, ContentOperator operator){
        if (operators.containsKey(operatorString))
            throw new IllegalArgumentException(MessageLocalization.getComposedMessage("operator.1.already.registered", operatorString));
        operators.put(operatorString, operator);
    }
    
    /**
     * Resets the graphics state stack, matrices and resources.
     */
    public void reset(){
        gsStack.removeAllElements();
        gsStack.add(new GraphicsState());
        textMatrix = null;
        textLineMatrix = null;
        resources = new ResourceDictionary();
    }
    
    /**
     * Returns the current graphics state.
     * @return	the graphics state
     */
    private GraphicsState gs(){
        return (GraphicsState)gsStack.peek();
    }
    
    /**
     * Returns the current text matrix.
     * @return	the text matrix
     * @since 2.1.5
     */
    private Matrix getCurrentTextMatrix(){
        return textMatrix;
    }
    
    /**
     * Returns the current line matrix.
     * @return	the line matrix
     * @since 2.1.5
     */
    private Matrix getCurrentTextLineMatrix(){
        return textLineMatrix;
    }
    
    /**
     * Invokes an operator.
     * @param operator	the PDF Syntax of the operator
     * @param operands	a list with operands
     */
    private void invokeOperator(PdfLiteral operator, ArrayList operands) throws Exception{
        ContentOperator op = (ContentOperator)operators.get(operator.toString());
        if (op == null){
            //System.out.println("Skipping operator " + operator);
            return;
        }
        
        op.invoke(this, operator, operands);
    }

    /**
     * Decodes a PdfString (which will contain glyph ids encoded in the font's encoding)
     * based on the active font, and determine the unicode equivalent
     * @param in	the String that needs to be encoded
     * @return	the encoded String
     * @since 2.1.7
     */
    private String decode(PdfString in){
        byte[] bytes = in.getBytes();
        return gs().font.decode(bytes, 0, bytes.length);
    }
    
    private void beginText(){
        if (textRenderListener == null) return;
        textRenderListener.beginTextBlock();
    }
    
    private void endText(){
        if (textRenderListener == null) return;
        textRenderListener.endTextBlock();
    }
    
    /**
     * Displays text.
     * @param string	the text to display
     */
    private void displayPdfString(PdfString string){
        if (textRenderListener == null) return;
        
        String unicode = decode(string);
        
        TextRenderInfo renderInfo = new TextRenderInfo(unicode, gs(), textMatrix);

        textRenderListener.renderText(renderInfo);

        textMatrix = new Matrix(renderInfo.getUnscaledWidth(), 0).multiply(textMatrix);
    }
    
    private static class ResourceDictionary extends PdfDictionary{
        private List resourcesStack = new ArrayList();
        public ResourceDictionary() {
        }
        
        public void push(PdfDictionary resources){
            resourcesStack.add(resources);
        }
        
        public void pop(){
            resourcesStack.remove(resourcesStack.size()-1);
        }
        
        public PdfObject getDirectObject(PdfName key) {
            for (int i = resourcesStack.size() - 1; i >= 0; i--){
                PdfDictionary subResource = (PdfDictionary)resourcesStack.get(i);
                if (subResource != null){
                    PdfObject obj =  subResource.getDirectObject(key);
                    if (obj != null) return obj;
                }
            }
            return super.getDirectObject(key); // shouldn't be necessary, but just in case we've done something crazy
        }
    }
    
    /**
     * Displays an XObject
     * TODO we probably want to move the FORM XObject handling into the Do operator itself and
     * keep this method as a pure callback method
     * @param xobjectName
     */
    private void displayXObject(PdfName xobjectName) throws IOException {
        
        PdfDictionary xobjects = resources.getAsDict(PdfName.XOBJECT);
        
        PdfObject xobject = xobjects.getDirectObject(xobjectName);
        
        if (xobject.isStream()){
            PdfStream stream = (PdfStream)xobject;
            
            PdfName subType = stream.getAsName(PdfName.SUBTYPE);
            if (PdfName.FORM.equals(stream.getAsName(PdfName.SUBTYPE))){ // if this is a form xobject, process it as though it were a content stream
                final PdfDictionary resources = stream.getAsDict(PdfName.RESOURCES);
                
                byte[] contentBytes = ContentByteUtils.getContentBytesFromContentObject(stream);
                final PdfArray matrix = stream.getAsArray(PdfName.MATRIX);
                
                new PushGraphicsState().invoke(this, null, null);
                
                if (matrix != null){
                    float a = ((PdfNumber)matrix.getAsNumber(0)).floatValue();
                    float b = ((PdfNumber)matrix.getAsNumber(1)).floatValue();
                    float c = ((PdfNumber)matrix.getAsNumber(2)).floatValue();
                    float d = ((PdfNumber)matrix.getAsNumber(3)).floatValue();
                    float e = ((PdfNumber)matrix.getAsNumber(4)).floatValue();
                    float f = ((PdfNumber)matrix.getAsNumber(5)).floatValue();
                    Matrix formMatrix = new Matrix(a, b, c, d, e, f);
                    
                    gs().ctm = gs().ctm.multiply(formMatrix);
                }
                
                processContent(contentBytes, resources);
                
                new PopGraphicsState().invoke(this, null, null);
            }
            
            if (PdfName.IMAGE.equals(subType)){
                if (imageRenderListener == null) return;
                
                ImageRenderInfo renderInfo = new ImageRenderInfo(xobject, gs().ctm);
                imageRenderListener.renderImage(renderInfo);
            }
            
        }
        
    }
    
    /**
     * Adjusts the text matrix for the specified adjustment value (see TJ operator in the PDF spec for information)
     * @param tj the text adjustment
     */
    private void applyTextAdjust(float tj){
        float adjustBy = -tj/1000f * gs().fontSize * gs().horizontalScaling;
        
        textMatrix = new Matrix(adjustBy, 0).multiply(textMatrix);
    }
    
    /**
     * Processes PDF syntax
     * @param contentBytes	the bytes of a content stream
     * @param resources		the resources that come with the content stream
     */
    public void processContent(byte[] contentBytes, PdfDictionary resources){
        
        this.resources.push(resources);
        try {
            PdfContentParser ps = new PdfContentParser(new PRTokeniser(contentBytes));
            ArrayList operands = new ArrayList();
            while (ps.parse(operands).size() > 0){
                PdfLiteral operator = (PdfLiteral)operands.get(operands.size()-1);
                invokeOperator(operator, operands);
            }
            
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }    
        this.resources.pop();
        
    }
    
    /**
     * A content operator implementation (TJ).
     */
    private static class ShowTextArray implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfArray array = (PdfArray)operands.get(0);
            float tj = 0;
            for (Iterator i = array.listIterator(); i.hasNext(); ) {
            	Object entryObj = i.next();
                if (entryObj instanceof PdfString){
                    processor.displayPdfString((PdfString)entryObj);
                    tj = 0;
                } else {
                    tj = ((PdfNumber)entryObj).floatValue();
                    processor.applyTextAdjust(tj);
                }
            }

        }
    }

    /**
     * A content operator implementation (").
     */
    private static class MoveNextLineAndShowTextWithSpacing implements ContentOperator{
        private final SetTextWordSpacing setTextWordSpacing;
        private final SetTextCharacterSpacing setTextCharacterSpacing;
        private final MoveNextLineAndShowText moveNextLineAndShowText;
        
        public MoveNextLineAndShowTextWithSpacing(SetTextWordSpacing setTextWordSpacing, SetTextCharacterSpacing setTextCharacterSpacing, MoveNextLineAndShowText moveNextLineAndShowText) {
            this.setTextWordSpacing = setTextWordSpacing;
            this.setTextCharacterSpacing = setTextCharacterSpacing;
            this.moveNextLineAndShowText = moveNextLineAndShowText;
        }
        
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber aw = (PdfNumber)operands.get(0);
            PdfNumber ac = (PdfNumber)operands.get(1);
            PdfString string = (PdfString)operands.get(2);
            
            ArrayList twOperands = new ArrayList(1);
            twOperands.add(0, aw);
            setTextWordSpacing.invoke(processor, null, twOperands);

            ArrayList tcOperands = new ArrayList(1);
            tcOperands.add(0, ac);
            setTextCharacterSpacing.invoke(processor, null, tcOperands);
            
            ArrayList tickOperands = new ArrayList(1);
            tickOperands.add(0, string);
            moveNextLineAndShowText.invoke(processor, null, tickOperands);
        }
    }

    /**
     * A content operator implementation (').
     */
    private static class MoveNextLineAndShowText implements ContentOperator{
        private final TextMoveNextLine textMoveNextLine;
        private final ShowText showText;
        public MoveNextLineAndShowText(TextMoveNextLine textMoveNextLine, ShowText showText) {
            this.textMoveNextLine = textMoveNextLine;
            this.showText = showText;
        }
        
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            textMoveNextLine.invoke(processor, null, new ArrayList(0));
            showText.invoke(processor, null, operands);
        }
    }

    /**
     * A content operator implementation (Tj).
     */
    private static class ShowText implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfString string = (PdfString)operands.get(0);
            
            processor.displayPdfString(string);
        }
    }
    

    /**
     * A content operator implementation (T*).
     */
    private static class TextMoveNextLine implements ContentOperator{
        private final TextMoveStartNextLine moveStartNextLine;
        public TextMoveNextLine(TextMoveStartNextLine moveStartNextLine){
            this.moveStartNextLine = moveStartNextLine;
        }
        
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            ArrayList tdoperands = new ArrayList(2);
            tdoperands.add(0, new PdfNumber(0));
            tdoperands.add(1, new PdfNumber(-processor.gs().leading));
            moveStartNextLine.invoke(processor, null, tdoperands);
        }
    }

    /**
     * A content operator implementation (Tm).
     */
    private static class TextSetTextMatrix implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            float a = ((PdfNumber)operands.get(0)).floatValue();
            float b = ((PdfNumber)operands.get(1)).floatValue();
            float c = ((PdfNumber)operands.get(2)).floatValue();
            float d = ((PdfNumber)operands.get(3)).floatValue();
            float e = ((PdfNumber)operands.get(4)).floatValue();
            float f = ((PdfNumber)operands.get(5)).floatValue();
            
            processor.textLineMatrix = new Matrix(a, b, c, d, e, f);
            processor.textMatrix = processor.textLineMatrix;
        }
    }

    /**
     * A content operator implementation (TD).
     */
    private static class TextMoveStartNextLineWithLeading implements ContentOperator{
        private final TextMoveStartNextLine moveStartNextLine;
        private final SetTextLeading setTextLeading;
        public TextMoveStartNextLineWithLeading(TextMoveStartNextLine moveStartNextLine, SetTextLeading setTextLeading){
            this.moveStartNextLine = moveStartNextLine;
            this.setTextLeading = setTextLeading;
        }
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            float ty = ((PdfNumber)operands.get(1)).floatValue();
            
            ArrayList tlOperands = new ArrayList(1);
            tlOperands.add(0, new PdfNumber(-ty));
            setTextLeading.invoke(processor, null, tlOperands);
            moveStartNextLine.invoke(processor, null, operands);
        }
    }

    /**
     * A content operator implementation (Td).
     */
    private static class TextMoveStartNextLine implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            float tx = ((PdfNumber)operands.get(0)).floatValue();
            float ty = ((PdfNumber)operands.get(1)).floatValue();
            
            Matrix translationMatrix = new Matrix(tx, ty);
            processor.textMatrix =  translationMatrix.multiply(processor.textLineMatrix);
            processor.textLineMatrix = processor.textMatrix;
        }
    }

    /**
     * A content operator implementation (Tf).
     */
    private static class SetTextFont implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfName fontResourceName = (PdfName)operands.get(0);
            float size = ((PdfNumber)operands.get(1)).floatValue();
            
            PdfDictionary fontsDictionary = processor.resources.getAsDict(PdfName.FONT);
            CMapAwareDocumentFont font = new CMapAwareDocumentFont((PRIndirectReference)fontsDictionary.get(fontResourceName));
            
            processor.gs().font = font;
            processor.gs().fontSize = size;
            
        }
    }

    /**
     * A content operator implementation (Tr).
     */
    private static class SetTextRenderMode implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber render = (PdfNumber)operands.get(0);
            processor.gs().renderMode = render.intValue();
        }
    }

    /**
     * A content operator implementation (Ts).
     */
    private static class SetTextRise implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber rise = (PdfNumber)operands.get(0);
            processor.gs().rise = rise.floatValue();
        }
    }

    /**
     * A content operator implementation (TL).
     */
    private static class SetTextLeading implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber leading = (PdfNumber)operands.get(0);
            processor.gs().leading = leading.floatValue();
        }
    }

    /**
     * A content operator implementation (Tz).
     */
    private static class SetTextHorizontalScaling implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber scale = (PdfNumber)operands.get(0);
            processor.gs().horizontalScaling = scale.floatValue();
        }
    }

    /**
     * A content operator implementation (Tc).
     */
    private static class SetTextCharacterSpacing implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber charSpace = (PdfNumber)operands.get(0);
            processor.gs().characterSpacing = charSpace.floatValue();
        }
    }

    /**
     * A content operator implementation (Tw).
     */
    private static class SetTextWordSpacing implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber wordSpace = (PdfNumber)operands.get(0);
            processor.gs().wordSpacing = wordSpace.floatValue();
        }
    }

    /**
     * A content operator implementation (gs).
     */
    private static class ProcessGraphicsStateResource implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            
            PdfName dictionaryName = (PdfName)operands.get(0);
            PdfDictionary extGState = processor.resources.getAsDict(PdfName.EXTGSTATE);
            if (extGState == null)
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("resources.do.not.contain.extgstate.entry.unable.to.process.operator.1", operator));
            PdfDictionary gsDic = extGState.getAsDict(dictionaryName);
            if (gsDic == null)
                throw new IllegalArgumentException(MessageLocalization.getComposedMessage("1.is.an.unknown.graphics.state.dictionary", dictionaryName));
            
            // at this point, all we care about is the FONT entry in the GS dictionary
            PdfArray fontParameter = gsDic.getAsArray(PdfName.FONT);
            if (fontParameter != null){
                CMapAwareDocumentFont font = new CMapAwareDocumentFont((PRIndirectReference)fontParameter.getPdfObject(0));
                float size = fontParameter.getAsNumber(1).floatValue();
                
                processor.gs().font = font;
                processor.gs().fontSize = size;
            }            
        }
    }

    /**
     * A content operator implementation (q).
     */
    private static class PushGraphicsState implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            GraphicsState gs = (GraphicsState) processor.gsStack.peek();
            GraphicsState copy = new GraphicsState(gs);
            processor.gsStack.push(copy);
        }
    }
    
    /**
     * A content operator implementation (cm).
     */
    private static class ModifyCurrentTransformationMatrix implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            float a = ((PdfNumber)operands.get(0)).floatValue();
            float b = ((PdfNumber)operands.get(1)).floatValue();
            float c = ((PdfNumber)operands.get(2)).floatValue();
            float d = ((PdfNumber)operands.get(3)).floatValue();
            float e = ((PdfNumber)operands.get(4)).floatValue();
            float f = ((PdfNumber)operands.get(5)).floatValue();
            Matrix matrix = new Matrix(a, b, c, d, e, f);
            GraphicsState gs = (GraphicsState)processor.gsStack.peek();
            gs.ctm = gs.ctm.multiply(matrix);
        }
    }

    /**
     * A content operator implementation (Q).
     */
    private static class PopGraphicsState implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            processor.gsStack.pop();
        }
    }

    /**
     * A content operator implementation (BT).
     */
    private static class BeginText implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            processor.textMatrix = new Matrix();
            processor.textLineMatrix = processor.textMatrix;
            processor.beginText();
        }
    }

    /**
     * A content operator implementation (ET).
     */
    private static class EndText implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            processor.textMatrix = null;
            processor.textLineMatrix = null;
            processor.endText();
        }
    }
    
    /**
     * A content operator implementation (Do).
     */
    private static class Do implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) throws IOException {
            PdfName xobjectName = (PdfName)operands.get(0);
            processor.displayXObject(xobjectName);
        }
    }
}
