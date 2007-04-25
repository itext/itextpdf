package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.PdfGraphics2D;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class restore
    implements PACommand, IPSLevel1 {
  public restore() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    PdfGraphics2D pdfg2d = (PdfGraphics2D) context.pencil.graphics;
    PdfContentByte cb = pdfg2d.getContent();
    cb.restoreState();
    Object data[];
    data = context.popOperands(1);
  }
}
