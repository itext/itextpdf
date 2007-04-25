package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGraphics2D;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class setflat
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public setflat() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    this.execute(context, (Number) operand1);
  }

  public void execute(PAContext context, Number operand1) throws
      PainterException {
    PdfGraphics2D pdfg2d = (PdfGraphics2D) context.pencil.graphics;
    PdfContentByte cb = pdfg2d.getContent();
//        cb.setFlatness( ( (float) operand1));
  }
}
