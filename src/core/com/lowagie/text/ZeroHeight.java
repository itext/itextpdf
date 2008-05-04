package com.lowagie.text;

import com.lowagie.text.pdf.PdfContentByte;

public interface ZeroHeight {
    public void draw(PdfContentByte canvas, float llx, float lly, float urx, float ury, float y);    
}