/*
 * $Id: PdfContentStreamProcessor.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Kevin Day, Bruno Lowagie, Paulo Soares, et al.
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
package com.itextpdf.text.pdf.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.CMapAwareDocumentFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PRIndirectReference;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfContentParser;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfLiteral;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNumber;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

/**
 * Processor for a PDF content Stream.
 * @since	2.1.4
 */
public class PdfContentStreamProcessor {
	/**
	 * Default operator
	 * @since 5.0.1
	 */
    public static final String DEFAULTOPERATOR = "DefaultOperator";

	/** A map with all supported operators operators (PDF syntax). */
    final private Map<String, ContentOperator> operators;
    /** Resources for the content stream. */
    private ResourceDictionary resources;
    /** Stack keeping track of the graphics state. */
    private final Stack<GraphicsState> gsStack = new Stack<GraphicsState>();
    /** Text matrix. */
    private Matrix textMatrix;
    /** Text line matrix. */
    private Matrix textLineMatrix;
    /** Listener that will be notified of render events */
    final private RenderListener renderListener;
    /** A map with all supported XObject handlers */
    final private Map<PdfName, XObjectDoHandler> xobjectDoHandlers;
    /**
     * The font cache.
     * @since 5.0.6
     */
    /**  */
    final private Map<Integer,CMapAwareDocumentFont> cachedFonts = new HashMap<Integer, CMapAwareDocumentFont>();
    /**
     * A stack containing marked content info.
     * @since 5.0.2
     */
    private final Stack<MarkedContentInfo> markedContentStack = new Stack<MarkedContentInfo>();

    /**
     * Creates a new PDF Content Stream Processor that will send it's output to the
     * designated render listener.
     *
     * @param renderListener the {@link RenderListener} that will receive rendering notifications
     */
    public PdfContentStreamProcessor(RenderListener renderListener) {
        this.renderListener = renderListener;
        operators = new HashMap<String, ContentOperator>();
        populateOperators();
        xobjectDoHandlers = new HashMap<PdfName, XObjectDoHandler>();
        populateXObjectDoHandlers();
        reset();
    }

    private void populateXObjectDoHandlers(){
        registerXObjectDoHandler(PdfName.DEFAULT, new IgnoreXObjectDoHandler());
        registerXObjectDoHandler(PdfName.FORM, new FormXObjectDoHandler());
        registerXObjectDoHandler(PdfName.IMAGE, new ImageXObjectDoHandler());
    }

    /**
     * Registers a Do handler that will be called when Do for the provided XObject subtype is encountered during content processing.
     * <br>
     * If you register a handler, it is a very good idea to pass the call on to the existing registered handler (returned by this call), otherwise you
     * may inadvertently change the internal behavior of the processor.
     * @param xobjectSubType the XObject subtype this handler will process, or PdfName.DEFAULT for a catch-all handler
     * @param handler the handler that will receive notification when the Do operator for the specified subtype is encountered
     * @return the existing registered handler, if any
     * @since 5.0.1
     */
    public XObjectDoHandler registerXObjectDoHandler(PdfName xobjectSubType, XObjectDoHandler handler){
        return xobjectDoHandlers.put(xobjectSubType, handler);
    }

    /**
     * Gets the font pointed to by the indirect reference. The font may have been cached.
     * @param ind the indirect reference ponting to the font
     * @return the font
     * @since 5.0.6
     */
    private CMapAwareDocumentFont getFont(PRIndirectReference ind) {
        Integer n = Integer.valueOf(ind.getNumber());
        CMapAwareDocumentFont font = cachedFonts.get(n);
        if (font == null) {
            font = new CMapAwareDocumentFont(ind);
            cachedFonts.put(n, font);
        }
        return font;
    }

    private CMapAwareDocumentFont getFont(PdfDictionary fontResource) {
        return new CMapAwareDocumentFont(fontResource);
    }

    /**
     * Loads all the supported graphics and text state operators in a map.
     */
    private void populateOperators(){

        registerContentOperator(DEFAULTOPERATOR, new IgnoreOperatorContentOperator());

        registerContentOperator("q", new PushGraphicsState());
        registerContentOperator("Q", new PopGraphicsState());
        registerContentOperator("g", new SetGrayFill());
        registerContentOperator("G", new SetGrayStroke());
        registerContentOperator("rg", new SetRGBFill());
        registerContentOperator("RG", new SetRGBStroke());
        registerContentOperator("k", new SetCMYKFill());
        registerContentOperator("K", new SetCMYKStroke());
        registerContentOperator("cs", new SetColorSpaceFill());
        registerContentOperator("CS", new SetColorSpaceStroke());
        registerContentOperator("sc", new SetColorFill());
        registerContentOperator("SC", new SetColorStroke());
        registerContentOperator("scn", new SetColorFill());
        registerContentOperator("SCN", new SetColorStroke());
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
        registerContentOperator("BMC", new BeginMarkedContent());
        registerContentOperator("BDC", new BeginMarkedContentDictionary());
        registerContentOperator("EMC", new EndMarkedContent());

        TextMoveStartNextLine tdOperator = new TextMoveStartNextLine();
        registerContentOperator("Td", tdOperator);
        registerContentOperator("TD", new TextMoveStartNextLineWithLeading(tdOperator, tlOperator));
        registerContentOperator("Tm", new TextSetTextMatrix());
        TextMoveNextLine tstarOperator = new TextMoveNextLine(tdOperator);
        registerContentOperator("T*", tstarOperator);

        ShowText tjOperator = new ShowText();
        registerContentOperator("Tj", tjOperator);
        MoveNextLineAndShowText tickOperator = new MoveNextLineAndShowText(tstarOperator, tjOperator);
        registerContentOperator("'", tickOperator);
        registerContentOperator("\"", new MoveNextLineAndShowTextWithSpacing(twOperator, tcOperator, tickOperator));
        registerContentOperator("TJ", new ShowTextArray());

        registerContentOperator("Do", new Do());
    }

    /**
     * Registers a content operator that will be called when the specified operator string is encountered during content processing.
     * <br>
     * If you register an operator, it is a very good idea to pass the call on to the existing registered operator (returned by this call), otherwise you
     * may inadvertently change the internal behavior of the processor.
     * @param operatorString the operator id, or DEFAULTOPERATOR for a catch-all operator
     * @param operator the operator that will receive notification when the operator is encountered
     * @return the existing registered operator, if any
     * @since 2.1.7
     */
    public ContentOperator registerContentOperator(String operatorString, ContentOperator operator){
        return operators.put(operatorString, operator);
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
        return gsStack.peek();
    }

    /**
     * Invokes an operator.
     * @param operator	the PDF Syntax of the operator
     * @param operands	a list with operands
     */
    private void invokeOperator(PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception{
        ContentOperator op = operators.get(operator.toString());
        if (op == null)
            op = operators.get(DEFAULTOPERATOR);
        op.invoke(this, operator, operands);
    }

    /**
     * Add to the marked content stack
     * @param tag the tag of the marked content
     * @param dict the PdfDictionary associated with the marked content
     * @since 5.0.2
     */
    private void beginMarkedContent(PdfName tag, PdfDictionary dict) {
    	markedContentStack.push(new MarkedContentInfo(tag, dict));
    }

    /**
     * Remove the latest marked content from the stack.  Keeps track of the BMC, BDC and EMC operators.
     * @since 5.0.2
     */
    private void endMarkedContent() {
    	markedContentStack.pop();
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

    /**
     * Used to trigger beginTextBlock on the renderListener
     */
    private void beginText(){
        renderListener.beginTextBlock();
    }

    /**
     * Used to trigger endTextBlock on the renderListener
     */
    private void endText(){
        renderListener.endTextBlock();
    }

    /**
     * Displays text.
     * @param string	the text to display
     */
    private void displayPdfString(PdfString string){

        String unicode = decode(string);

        TextRenderInfo renderInfo = new TextRenderInfo(unicode, gs(), textMatrix, markedContentStack);

        renderListener.renderText(renderInfo);

        textMatrix = new Matrix(renderInfo.getUnscaledWidth(), 0).multiply(textMatrix);
    }





    /**
     * Displays an XObject using the registered handler for this XObject's subtype
     * @param xobjectName the name of the XObject to retrieve from the resource dictionary
     */
    private void displayXObject(PdfName xobjectName) throws IOException {
        PdfDictionary xobjects = resources.getAsDict(PdfName.XOBJECT);
        PdfObject xobject = xobjects.getDirectObject(xobjectName);
        PdfStream xobjectStream = (PdfStream)xobject;

        PdfName subType = xobjectStream.getAsName(PdfName.SUBTYPE);
        if (xobject.isStream()){
            XObjectDoHandler handler = xobjectDoHandlers.get(subType);
            if (handler == null)
                handler = xobjectDoHandlers.get(PdfName.DEFAULT);
            handler.handleXObject(this, xobjectStream, xobjects.getAsIndirectObject(xobjectName));
        } else {
            throw new IllegalStateException(MessageLocalization.getComposedMessage("XObject.1.is.not.a.stream", xobjectName));
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
     * Processes PDF syntax.
     * <b>Note:</b> If you re-use a given {@link PdfContentStreamProcessor}, you must call {@link PdfContentStreamProcessor#reset()}
     * @param contentBytes	the bytes of a content stream
     * @param resources		the resources that come with the content stream
     */
    public void processContent(byte[] contentBytes, PdfDictionary resources){
        this.resources.push(resources);
        try {
            PRTokeniser tokeniser = new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(contentBytes)));
            PdfContentParser ps = new PdfContentParser(tokeniser);
            ArrayList<PdfObject> operands = new ArrayList<PdfObject>();
            while (ps.parse(operands).size() > 0){
                PdfLiteral operator = (PdfLiteral)operands.get(operands.size()-1);
                if ("BI".equals(operator.toString())){
                    // we don't call invokeOperator for embedded images - this is one area of the PDF spec that is particularly nasty and inconsistent
                    PdfDictionary colorSpaceDic = resources != null ? resources.getAsDict(PdfName.COLORSPACE) : null;
                    handleInlineImage(InlineImageUtils.parseInlineImage(ps, colorSpaceDic), colorSpaceDic);
                } else {
                    invokeOperator(operator, operands);
                }
            }

        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
        this.resources.pop();

    }

    /**
     * Callback when an inline image is found.  This requires special handling because inline images don't follow the standard operator syntax
     * @param info the inline image
     * @param colorSpaceDic the color space for the inline immage
     */
    protected void handleInlineImage(InlineImageInfo info, PdfDictionary colorSpaceDic){
        ImageRenderInfo renderInfo = ImageRenderInfo.createForEmbeddedImage(gs().ctm, info, colorSpaceDic);
        renderListener.renderImage(renderInfo);
    }

    /**
     * A resource dictionary that allows stack-like behavior to support resource dictionary inheritance
     */
    private static class ResourceDictionary extends PdfDictionary{
        private final List<PdfDictionary> resourcesStack = new ArrayList<PdfDictionary>();
        public ResourceDictionary() {
        }

        public void push(PdfDictionary resources){
            resourcesStack.add(resources);
        }

        public void pop(){
            resourcesStack.remove(resourcesStack.size()-1);
        }

        @Override
        public PdfObject getDirectObject(PdfName key) {
            for (int i = resourcesStack.size() - 1; i >= 0; i--){
                PdfDictionary subResource = resourcesStack.get(i);
                if (subResource != null){
                    PdfObject obj =  subResource.getDirectObject(key);
                    if (obj != null) return obj;
                }
            }
            return super.getDirectObject(key); // shouldn't be necessary, but just in case we've done something crazy
        }
    }

    /**
     * A content operator implementation (unregistered).
     */
    private static class IgnoreOperatorContentOperator implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands){
            // ignore the operator
        }
    }

    /**
     * A content operator implementation (TJ).
     */
    private static class ShowTextArray implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfArray array = (PdfArray)operands.get(0);
            float tj = 0;
            for (Iterator<PdfObject> i = array.listIterator(); i.hasNext(); ) {
                PdfObject entryObj = i.next();
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

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber aw = (PdfNumber)operands.get(0);
            PdfNumber ac = (PdfNumber)operands.get(1);
            PdfString string = (PdfString)operands.get(2);

            ArrayList<PdfObject> twOperands = new ArrayList<PdfObject>(1);
            twOperands.add(0, aw);
            setTextWordSpacing.invoke(processor, null, twOperands);

            ArrayList<PdfObject> tcOperands = new ArrayList<PdfObject>(1);
            tcOperands.add(0, ac);
            setTextCharacterSpacing.invoke(processor, null, tcOperands);

            ArrayList<PdfObject> tickOperands = new ArrayList<PdfObject>(1);
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

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            textMoveNextLine.invoke(processor, null, new ArrayList<PdfObject>(0));
            showText.invoke(processor, null, operands);
        }
    }

    /**
     * A content operator implementation (Tj).
     */
    private static class ShowText implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
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

        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            ArrayList<PdfObject> tdoperands = new ArrayList<PdfObject>(2);
            tdoperands.add(0, new PdfNumber(0));
            tdoperands.add(1, new PdfNumber(-processor.gs().leading));
            moveStartNextLine.invoke(processor, null, tdoperands);
        }
    }

    /**
     * A content operator implementation (Tm).
     */
    private static class TextSetTextMatrix implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
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
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            float ty = ((PdfNumber)operands.get(1)).floatValue();

            ArrayList<PdfObject> tlOperands = new ArrayList<PdfObject>(1);
            tlOperands.add(0, new PdfNumber(-ty));
            setTextLeading.invoke(processor, null, tlOperands);
            moveStartNextLine.invoke(processor, null, operands);
        }
    }

    /**
     * A content operator implementation (Td).
     */
    private static class TextMoveStartNextLine implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
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
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfName fontResourceName = (PdfName)operands.get(0);
            float size = ((PdfNumber)operands.get(1)).floatValue();

            PdfDictionary fontsDictionary = processor.resources.getAsDict(PdfName.FONT);
            CMapAwareDocumentFont font;
            PdfObject fontObject = fontsDictionary.get(fontResourceName);
            if (fontObject instanceof PdfDictionary)
                font = processor.getFont((PdfDictionary)fontObject);
            else
                font = processor.getFont((PRIndirectReference)fontObject);

            processor.gs().font = font;
            processor.gs().fontSize = size;

        }
    }

    /**
     * A content operator implementation (Tr).
     */
    private static class SetTextRenderMode implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber render = (PdfNumber)operands.get(0);
            processor.gs().renderMode = render.intValue();
        }
    }

    /**
     * A content operator implementation (Ts).
     */
    private static class SetTextRise implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber rise = (PdfNumber)operands.get(0);
            processor.gs().rise = rise.floatValue();
        }
    }

    /**
     * A content operator implementation (TL).
     */
    private static class SetTextLeading implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber leading = (PdfNumber)operands.get(0);
            processor.gs().leading = leading.floatValue();
        }
    }

    /**
     * A content operator implementation (Tz).
     */
    private static class SetTextHorizontalScaling implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber scale = (PdfNumber)operands.get(0);
            processor.gs().horizontalScaling = scale.floatValue()/100f;
        }
    }

    /**
     * A content operator implementation (Tc).
     */
    private static class SetTextCharacterSpacing implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber charSpace = (PdfNumber)operands.get(0);
            processor.gs().characterSpacing = charSpace.floatValue();
        }
    }

    /**
     * A content operator implementation (Tw).
     */
    private static class SetTextWordSpacing implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            PdfNumber wordSpace = (PdfNumber)operands.get(0);
            processor.gs().wordSpacing = wordSpace.floatValue();
        }
    }

    /**
     * A content operator implementation (gs).
     */
    private static class ProcessGraphicsStateResource implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {

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
                CMapAwareDocumentFont font = processor.getFont((PRIndirectReference)fontParameter.getPdfObject(0));
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
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            GraphicsState gs = processor.gsStack.peek();
            GraphicsState copy = new GraphicsState(gs);
            processor.gsStack.push(copy);
        }
    }

    /**
     * A content operator implementation (cm).
     */
    private static class ModifyCurrentTransformationMatrix implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            float a = ((PdfNumber)operands.get(0)).floatValue();
            float b = ((PdfNumber)operands.get(1)).floatValue();
            float c = ((PdfNumber)operands.get(2)).floatValue();
            float d = ((PdfNumber)operands.get(3)).floatValue();
            float e = ((PdfNumber)operands.get(4)).floatValue();
            float f = ((PdfNumber)operands.get(5)).floatValue();
            Matrix matrix = new Matrix(a, b, c, d, e, f);
            GraphicsState gs = processor.gsStack.peek();
            gs.ctm = matrix.multiply(gs.ctm);
        }
    }
    
    /**
     * Gets a color based on a list of operands.
     */
    private static BaseColor getColor(PdfName colorSpace, List<PdfObject> operands) {
    	if (PdfName.DEVICEGRAY.equals(colorSpace)) {
    		return getColor(1, operands);
    	}
    	if (PdfName.DEVICERGB.equals(colorSpace)) {
    		return getColor(3, operands);
    	}
    	if (PdfName.DEVICECMYK.equals(colorSpace)) {
    		return getColor(4, operands);
    	}
    	return null;
    }
    
    /**
     * Gets a color based on a list of operands.
     */
    private static BaseColor getColor(int nOperands, List<PdfObject> operands) {
    	float[] c = new float[nOperands];
    	for (int i = 0; i < nOperands; i++) {
    		c[i] = ((PdfNumber)operands.get(i)).floatValue();
    	}
    	switch (nOperands) {
    	case 1:
    		return new GrayColor(c[0]);
    	case 3:
    		return new BaseColor(c[0], c[1], c[2]);
    	case 4:
    		return new CMYKColor(c[0], c[1], c[2], c[3]);
    	}
    	return null;
    }
    
    /**
     * A content operator implementation (g).
     */
    private static class SetGrayFill implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().fillColor = getColor(1, operands);
        }
    }
    
    /**
     * A content operator implementation (G).
     */
    private static class SetGrayStroke implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().strokeColor = getColor(1, operands);
        }
    }
    
    /**
     * A content operator implementation (rg).
     */
    private static class SetRGBFill implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().fillColor = getColor(3, operands);
        }
    }
    
    /**
     * A content operator implementation (RG).
     */
    private static class SetRGBStroke implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().strokeColor = getColor(3, operands);
        }
    }
    
    /**
     * A content operator implementation (rg).
     */
    private static class SetCMYKFill implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().fillColor = getColor(4, operands);
        }
    }
    
    /**
     * A content operator implementation (RG).
     */
    private static class SetCMYKStroke implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().strokeColor = getColor(4, operands);
        }
    }

    /**
     * A content operator implementation (CS).
     */
    private static class SetColorSpaceFill implements ContentOperator{
		public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
			processor.gs().colorSpaceFill = (PdfName)operands.get(0);		
		}
    }

    /**
     * A content operator implementation (cs).
     */
    private static class SetColorSpaceStroke implements ContentOperator{
		public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
			processor.gs().colorSpaceStroke = (PdfName)operands.get(0);		
		}
    }
    
    /**
     * A content operator implementation (sc / scn).
     */
    private static class SetColorFill implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().fillColor = getColor(processor.gs().colorSpaceFill, operands);
        }
    }
    
    /**
     * A content operator implementation (SC / SCN).
     */
    private static class SetColorStroke implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gs().strokeColor = getColor(processor.gs().colorSpaceStroke, operands);
        }
    }

    /**
     * A content operator implementation (Q).
     */
    private static class PopGraphicsState implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.gsStack.pop();
        }
    }

    /**
     * A content operator implementation (BT).
     */
    private static class BeginText implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.textMatrix = new Matrix();
            processor.textLineMatrix = processor.textMatrix;
            processor.beginText();
        }
    }

    /**
     * A content operator implementation (ET).
     */
    private static class EndText implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) {
            processor.textMatrix = null;
            processor.textLineMatrix = null;
            processor.endText();
        }
    }

    /**
     * A content operator implementation (BMC).
     * @since 5.0.2
     */
    private static class BeginMarkedContent implements ContentOperator{

		public void invoke(PdfContentStreamProcessor processor,
				PdfLiteral operator, ArrayList<PdfObject> operands)
				throws Exception {
			processor.beginMarkedContent((PdfName)operands.get(0), new PdfDictionary());
		}

    }

    /**
     * A content operator implementation (BDC).
     * @since 5.0.2
     */
    private static class BeginMarkedContentDictionary implements ContentOperator{

		public void invoke(PdfContentStreamProcessor processor,
				PdfLiteral operator, ArrayList<PdfObject> operands)
				throws Exception {

		    PdfObject properties = operands.get(1);

			processor.beginMarkedContent((PdfName)operands.get(0), getPropertiesDictionary(properties, processor.resources));
		}

		private PdfDictionary getPropertiesDictionary(PdfObject operand1, ResourceDictionary resources){
            if (operand1.isDictionary())
                return (PdfDictionary)operand1;

            PdfName dictionaryName = ((PdfName)operand1);
            return resources.getAsDict(dictionaryName);
		}
    }

    /**
     * A content operator implementation (BMC).
     * @since 5.0.2
     */
    private static class EndMarkedContent implements ContentOperator{
		public void invoke(PdfContentStreamProcessor processor,
				PdfLiteral operator, ArrayList<PdfObject> operands)
				throws Exception {
			processor.endMarkedContent();
		}
    }

    /**
     * A content operator implementation (Do).
     */
    private static class Do implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList<PdfObject> operands) throws IOException {
            PdfName xobjectName = (PdfName)operands.get(0);
            processor.displayXObject(xobjectName);
        }
    }

    /**
     * An XObject subtype handler for FORM
     */
    private static class FormXObjectDoHandler implements XObjectDoHandler{

        public void handleXObject(PdfContentStreamProcessor processor, PdfStream stream, PdfIndirectReference ref) {

            final PdfDictionary resources = stream.getAsDict(PdfName.RESOURCES);

            // we read the content bytes up here so if it fails we don't leave the graphics state stack corrupted
            // this is probably not necessary (if we fail on this, probably the entire content stream processing
            // operation should be rejected
            byte[] contentBytes;
            try {
                contentBytes = ContentByteUtils.getContentBytesFromContentObject(stream);
            } catch (IOException e1) {
                throw new ExceptionConverter(e1);
            }
            final PdfArray matrix = stream.getAsArray(PdfName.MATRIX);

            new PushGraphicsState().invoke(processor, null, null);

            if (matrix != null){
                float a = matrix.getAsNumber(0).floatValue();
                float b = matrix.getAsNumber(1).floatValue();
                float c = matrix.getAsNumber(2).floatValue();
                float d = matrix.getAsNumber(3).floatValue();
                float e = matrix.getAsNumber(4).floatValue();
                float f = matrix.getAsNumber(5).floatValue();
                Matrix formMatrix = new Matrix(a, b, c, d, e, f);

                processor.gs().ctm = formMatrix.multiply(processor.gs().ctm);
            }

            processor.processContent(contentBytes, resources);

            new PopGraphicsState().invoke(processor, null, null);

        }

    }

    /**
     * An XObject subtype handler for IMAGE
     */
    private static class ImageXObjectDoHandler implements XObjectDoHandler{

        public void handleXObject(PdfContentStreamProcessor processor, PdfStream xobjectStream, PdfIndirectReference ref) {
            PdfDictionary colorSpaceDic = processor.resources.getAsDict(PdfName.COLORSPACE);
            ImageRenderInfo renderInfo = ImageRenderInfo.createForXObject(processor.gs().ctm, ref, colorSpaceDic);
            processor.renderListener.renderImage(renderInfo);
        }
    }

    /**
     * An XObject subtype handler that does nothing
     */
    private static class IgnoreXObjectDoHandler implements XObjectDoHandler{
        public void handleXObject(PdfContentStreamProcessor processor, PdfStream xobjectStream, PdfIndirectReference ref) {
            // ignore XObject subtype
        }
    }
}
