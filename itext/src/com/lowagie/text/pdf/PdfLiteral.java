/*
 * PdfLiteral.java
 *
 * Created on March 28, 2001, 8:34 PM
 */
package com.lowagie.text.pdf;
/**
 *
 * @author  Administrator
 * @version 
 */
class PdfLiteral extends PdfObject
{
    PdfLiteral(String text)
    {
        super(0, text);
    }
    PdfLiteral(byte b[])
    {
        super(0, b);
    }
}
