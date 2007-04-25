package com.lowagie.text.pdf.codec.postscript.interfaces;

import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;

public interface IBinaryExecute {
  public void execute(PAContext context, Object operand1, Object operand2) throws
      PainterException;
}
