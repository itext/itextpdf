package com.lowagie.text.pdf.codec.postscript.interfaces;

import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;

public interface IUnaryExecute {
  public void execute(PAContext context, Object operand1) throws
      PainterException;
}
