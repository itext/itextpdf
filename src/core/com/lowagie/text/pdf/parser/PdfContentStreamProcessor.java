/*
 * Copyright 2008 by Kevin Day.
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
 * the Initial Developer are Copyright (C) 1999-2008 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000-2008 by Paulo Soares. All Rights Reserved.
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
package com.lowagie.text.pdf.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.pdf.CMapAwareDocumentFont;
import com.lowagie.text.pdf.DocumentFont;
import com.lowagie.text.pdf.PRIndirectReference;
import com.lowagie.text.pdf.PRTokeniser;
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfContentParser;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfLiteral;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfNumber;
import com.lowagie.text.pdf.PdfString;

/**
 * @since	2.1.4
 */
public abstract class PdfContentStreamProcessor {

    private Map operators;
    
    PdfDictionary resources;
    Stack gsStack = new Stack();
    Matrix textMatrix;
    Matrix textLineMatrix;
    
    public PdfContentStreamProcessor() {
        populateOperators();
        reset();
    }

    private void populateOperators(){
        operators = new HashMap();
        
        operators.put("q", new PushGraphicsState());
        operators.put("Q", new PopGraphicsState());
        operators.put("cm", new ModifyCurrentTransformationMatrix());
        operators.put("gs", new ProcessGraphicsStateResource());
        
        operators.put("Tc", new SetTextCharacterSpacing());
        operators.put("Tw", new SetTextWordSpacing());
        operators.put("Tz", new SetTextHorizontalScaling());
        operators.put("TL", new SetTextLeading());
        operators.put("Tf", new SetTextFont());
        operators.put("Tr", new SetTextRenderMode());
        operators.put("Ts", new SetTextRise());
        
        operators.put("BT", new BeginText());
        operators.put("ET", new EndText());

        operators.put("Td", new TextMoveStartNextLine());
        operators.put("TD", new TextMoveStartNextLineWithLeading());
        operators.put("Tm", new TextSetTextMatrix());
        operators.put("T*", new TextMoveNextLine());
        
        operators.put("Tj", new ShowText());
        operators.put("'", new MoveNextLineAndShowText());
        operators.put("\"", new MoveNextLineAndShowTextWithSpacing());
        operators.put("TJ", new ShowTextArray());
    }
    
    public void reset(){
        gsStack.removeAllElements();
        gsStack.add(new GraphicsState());
        textMatrix = null;
        textLineMatrix = null;
        resources = null;
    }
    
    public GraphicsState gs(){
        return (GraphicsState)gsStack.peek();
    }
    
    public void invokeOperator(PdfLiteral operator, ArrayList operands){
        ContentOperator op = (ContentOperator)operators.get(operator.toString());
        if (op == null){
            //System.out.println("Skipping operator " + operator);
            return;
        }
        
        op.invoke(this, operator, operands);
    }

    private String encode(String in){
        byte[] bytes = in.getBytes();
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i < bytes.length; i++){
            String rslt = gs().font.encode(bytes, i, 1);
            if (rslt == null){
                rslt = gs().font.encode(bytes, i, 2);
                i++;
            }
            sb.append(rslt);
        }
        
        return sb.toString();
    }
    
    abstract public void displayText(String text, Matrix nextTextMatrix);
    
    public float getStringWidth(String string, float tj){
        DocumentFont font = gs().font;
        char[] chars = string.toCharArray();
        float totalWidth = 0;
        for (int i = 0; i < chars.length; i++) {
            float w = font.getWidth(chars[i]) / 1000.0f;
            float wordSpacing = chars[i] == 32 ? gs().wordSpacing : 0f;
            totalWidth += ((w - tj/1000f) * gs().fontSize + gs().characterSpacing + wordSpacing) * gs().horizontalScaling;
        }
        
        return totalWidth;
    }
    
    public void displayPdfString(PdfString string, float tj){
        String unicode = encode(string.toUnicodeString());
        
        float width = getStringWidth(unicode, tj); // this is width in unscaled units - we have to normalize by the Tm scaling

        Matrix nextTextMatrix = new Matrix(width, 0).multiply(textMatrix);

        displayText(unicode, nextTextMatrix);

        textMatrix = nextTextMatrix;
    }
    
    public void processContent(byte[] contentBytes, PdfDictionary resources){
        
        reset();
        this.resources = resources;
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
        
    }
    

    private static class TemplateOperation implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            
        }
    }

    private static class ShowTextArray implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfArray array = (PdfArray)operands.get(0);
            ArrayList entries = array.getArrayList();
            float tj = 0;
            for (Iterator i = entries.iterator(); i.hasNext(); ) {
            	Object entryObj = i.next();
                if (entryObj instanceof PdfString){
                    processor.displayPdfString((PdfString)entryObj, tj);
                    tj = 0;
                } else {
                    tj = ((PdfNumber)entryObj).floatValue();
                }
            }

        }
    }
    
    private static class MoveNextLineAndShowTextWithSpacing implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber aw = (PdfNumber)operands.get(0);
            PdfNumber ac = (PdfNumber)operands.get(1);
            PdfString string = (PdfString)operands.get(2);
            
            ArrayList twOperands = new ArrayList(1);
            twOperands.add(0, aw);
            processor.invokeOperator(new PdfLiteral("Tw"), twOperands);

            ArrayList tcOperands = new ArrayList(1);
            tcOperands.add(0, ac);
            processor.invokeOperator(new PdfLiteral("Tc"), tcOperands);
            
            ArrayList tickOperands = new ArrayList(1);
            tickOperands.add(0, string);
            processor.invokeOperator(new PdfLiteral("'"), tickOperands);
        }
    }
    
    private static class MoveNextLineAndShowText implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            processor.invokeOperator(new PdfLiteral("T*"), new ArrayList(0));
            processor.invokeOperator(new PdfLiteral("Tj"), operands);
        }
    }
    
    private static class ShowText implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfString string = (PdfString)operands.get(0);
            
            processor.displayPdfString(string, 0);
        }
    }
    
    
    private static class TextMoveNextLine implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            ArrayList tdoperands = new ArrayList(2);
            tdoperands.add(0, new PdfNumber(0));
            tdoperands.add(1, new PdfNumber(processor.gs().leading));
            processor.invokeOperator(new PdfLiteral("Td"), tdoperands);
        }
    }
    
    
    private static class TextSetTextMatrix implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            float a = ((PdfNumber)operands.get(0)).floatValue();
            float b = ((PdfNumber)operands.get(1)).floatValue();
            float c = ((PdfNumber)operands.get(2)).floatValue();
            float d = ((PdfNumber)operands.get(3)).floatValue();
            float e = ((PdfNumber)operands.get(4)).floatValue();
            float f = ((PdfNumber)operands.get(5)).floatValue();
            Matrix matrix = new Matrix(a, b, c, d, e, f);
            
            processor.textLineMatrix = new Matrix(a, b, c, d, e, f);
            processor.textMatrix = processor.textLineMatrix;
        }
    }
    

    private static class TextMoveStartNextLineWithLeading implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            float tx = ((PdfNumber)operands.get(0)).floatValue();
            float ty = ((PdfNumber)operands.get(1)).floatValue();
            
            ArrayList tlOperands = new ArrayList(1);
            tlOperands.add(0, new PdfNumber(-ty));
            processor.invokeOperator(new PdfLiteral("TL"), tlOperands);
            processor.invokeOperator(new PdfLiteral("Td"), operands);
        }
    }
    
    private static class TextMoveStartNextLine implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            float tx = ((PdfNumber)operands.get(0)).floatValue();
            float ty = ((PdfNumber)operands.get(1)).floatValue();
            
            Matrix translationMatrix = new Matrix(tx, ty);
            processor.textMatrix =  translationMatrix.multiply(processor.textLineMatrix);
            processor.textLineMatrix = processor.textMatrix;
        }
    }
    
    
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
    
    private static class SetTextRenderMode implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber render = (PdfNumber)operands.get(0);
            processor.gs().renderMode = render.intValue();
        }
    }
    
    private static class SetTextRise implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber rise = (PdfNumber)operands.get(0);
            processor.gs().rise = rise.floatValue();
        }
    }
    
    private static class SetTextLeading implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber leading = (PdfNumber)operands.get(0);
            processor.gs().leading = leading.floatValue();
        }
    }
    
    private static class SetTextHorizontalScaling implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber scale = (PdfNumber)operands.get(0);
            processor.gs().horizontalScaling = scale.floatValue();
        }
    }

    private static class SetTextCharacterSpacing implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber charSpace = (PdfNumber)operands.get(0);
            processor.gs().characterSpacing = charSpace.floatValue();
        }
    }

    private static class SetTextWordSpacing implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfNumber wordSpace = (PdfNumber)operands.get(0);
            processor.gs().wordSpacing = wordSpace.floatValue();
        }
    }
    
    private static class ProcessGraphicsStateResource implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            PdfName dictionaryName = (PdfName)operands.get(0);
            PdfDictionary extGState = processor.resources.getAsDict(PdfName.EXTGSTATE);
            if (extGState == null)
                throw new IllegalArgumentException("Resources do not contain ExtGState entry. Unable to process operator " + operator);
            PdfDictionary gsDic = extGState.getAsDict(dictionaryName);
            if (gsDic == null)
                throw new IllegalArgumentException(dictionaryName + " is an unknown graphics state dictionary");
            
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
    
    private static class PushGraphicsState implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            GraphicsState gs = (GraphicsState) processor.gsStack.peek();
            GraphicsState copy = new GraphicsState(gs);
            processor.gsStack.push(copy);
        }
    }
    
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
    
    
    
    private static class PopGraphicsState implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            GraphicsState gs = (GraphicsState) processor.gsStack.pop();
        }
    }

    private static class BeginText implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            processor.textMatrix = new Matrix();
            processor.textLineMatrix = processor.textMatrix;
        }
    }

    private static class EndText implements ContentOperator{
        public void invoke(PdfContentStreamProcessor processor, PdfLiteral operator, ArrayList operands) {
            processor.textMatrix = null;
            processor.textLineMatrix = null;
        }
    }
    
}
