package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class initclip
    implements PACommand, IPSLevel1 {
  public initclip() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
//       PdfGraphics2D pdfg2d = (PdfGraphics2D) context.pencil.graphics;
//       PdfContentByte cb = pdfg2d.getContent();
    context.pencil.clippath();
//       pdfg2d.setClip(context.);
//    if(!PAContext.IgnoreUnknownCommands)
//    throw new UnsupportedOperationException("initclip");
  }
}
