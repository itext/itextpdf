package com.lowagie.text.pdf;

/**
 * a Literal
 */

class PRLiteral extends PRObject
{
    PRLiteral(int type, String text)
    {
        super(type, text);
    }
    PRLiteral(int type, byte[] b)
    {
        super(type, b);
    }
}