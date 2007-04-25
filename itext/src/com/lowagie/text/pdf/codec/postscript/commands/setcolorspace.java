package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGraphics2D;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

public class setcolorspace
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public setcolorspace() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    if (operand1 instanceof PAToken) {
      this.execute(context, (PAToken) operand1);
    }
  }

  public void execute(PAContext context, PAToken operand1) throws
      PainterException {
    PdfGraphics2D pdfg2d = (PdfGraphics2D) context.pencil.graphics;
    PdfContentByte cb = pdfg2d.getContent();
    String colorspace = ( (String) operand1.value);
    cb.setDefaultColorspace(PdfName.COLORSPACE, PdfName.DEVICERGB);
  }
}
