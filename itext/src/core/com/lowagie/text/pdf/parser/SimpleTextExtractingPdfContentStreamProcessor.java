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

/**
 * A simple text extraction processor.
 * @since	2.1.4
 */
public class SimpleTextExtractingPdfContentStreamProcessor extends PdfContentStreamProcessor {

    /** keeps track of a text matrix. */
    Matrix lastTextLineMatrix = null;
    /** keeps track of a text matrix. */
    Matrix lastEndingTextMatrix = null;

    /** The StringBuffer used to write the resulting String. */
    StringBuffer result = null;

    /**
     * Creates a new text extraction processor.
     */
    public SimpleTextExtractingPdfContentStreamProcessor() {
    }

    public void reset() {
        super.reset();
        lastTextLineMatrix = null;
        lastEndingTextMatrix = null;
        result = new StringBuffer();
    }
    
    /**
     * Returns the result so far.
     * @return	a String with the resulting text.
     */
    public String getResultantText(){
        return result.toString();
    }
    
    /**
     * Writes text to the result.
     * @param text	The text that needs to be displayed
     * @param endingTextMatrix	a text matrix
     * @see com.lowagie.text.pdf.parser.PdfContentStreamProcessor#displayText(java.lang.String, com.lowagie.text.pdf.parser.Matrix)
     */
    public void displayText(String text, Matrix endingTextMatrix){
        boolean hardReturn = false;
        if (lastTextLineMatrix != null && lastTextLineMatrix.get(Matrix.I32) != getCurrentTextLineMatrix().get(Matrix.I32)){
        //if (!textLineMatrix.equals(lastTextLineMatrix)){
            hardReturn = true;
        }

        float currentX = getCurrentTextMatrix().get(Matrix.I31);
        if (hardReturn){
            //System.out.println("<Hard Return>");
            result.append('\n');
        } else if (lastEndingTextMatrix != null){
            float lastEndX = lastEndingTextMatrix.get(Matrix.I31);
            
            //System.out.println("Displaying '" + text + "' :: lastX + lastWidth = " + lastEndX + " =?= currentX = " + currentX + " :: Delta is " + (currentX - lastEndX));
            
            float spaceGlyphWidth = gs().font.getWidth(' ')/1000f;
            float spaceWidth = (spaceGlyphWidth * gs().fontSize + gs().characterSpacing + gs().wordSpacing) * gs().horizontalScaling; // this is unscaled!!
            Matrix scaled = new Matrix(spaceWidth, 0).multiply(getCurrentTextMatrix());
            float scaledSpaceWidth = scaled.get(Matrix.I31) - getCurrentTextMatrix().get(Matrix.I31);
            
            if (currentX - lastEndX > scaledSpaceWidth/2f ){
                //System.out.println("<Implied space on text '" + text + "'> lastEndX=" + lastEndX + ", currentX=" + currentX + ", spaceWidth=" + spaceWidth);
                result.append(' ');
            }
        } else {
            //System.out.println("Displaying first string of content '" + text + "' :: currentX = " + currentX);
        }
        
        //System.out.println("After displaying '" + text + "' :: Start at " + currentX + " end at " + endingTextMatrix.get(Matrix.I31));
        
        result.append(text);

        lastTextLineMatrix = getCurrentTextLineMatrix();
        lastEndingTextMatrix = endingTextMatrix;
        
    }

}
