package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class round implements PACommand, IUnaryExecute,IPSLevel1 {
  public round() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Number data[];
    data = context.popNumberOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    this.execute(context, (Number) operand1);
  }

  public void execute(PAContext context, Number operand1) throws
      PainterException {
    context.operands.push(new Long(Math.round(operand1.doubleValue())));
  }

}
