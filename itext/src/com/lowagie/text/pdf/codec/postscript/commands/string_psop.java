package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class string_psop
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public string_psop() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    if (! (operand1 instanceof Number)) {
      throw new PainterException("string: wrong arguments");
    }
    this.execute(context, (Number) operand1);
  }

  public void execute(PAContext context, Number operand1) throws
      PainterException {
    int d;
    d = operand1.intValue();
    StringBuffer sb = new StringBuffer(d);
    sb.setLength(d);
    context.operands.push(sb);
  }
}
