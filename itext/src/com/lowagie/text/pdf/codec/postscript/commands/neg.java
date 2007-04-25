package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class neg
    implements PACommand, IUnaryExecute,IPSLevel1 {
  public neg() {
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
    context.operands.push(new Double( -operand1.doubleValue()));
    if(PAContext.DebugCommandleveltrace)System.out.println("neg("+operand1.doubleValue()+")="+-operand1.doubleValue());
  }

}
