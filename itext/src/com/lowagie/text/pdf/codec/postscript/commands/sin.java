package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

public class sin extends sqrt
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public sin() {
    super();
  }

  public void execute(PAContext context, Number operand1) throws
      PainterException {
    context.operands.push(new Double(Math.sin(operand1.doubleValue() * Math.PI /
                                              180.0)));
  }
}
