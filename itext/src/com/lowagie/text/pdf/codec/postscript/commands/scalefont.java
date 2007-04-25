package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class scalefont
    implements PACommand, IBinaryExecute, IPSLevel1 {
  public scalefont() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(2);
    this.execute(context, data[0], data[1]);
  }

  public void execute(PAContext context, Object operand1, Object operand2) throws
      PainterException {
    if (! (operand1 instanceof java.awt.Font)) {
      throw new PainterException("scalefont: wrong arguments");
    }
    if (! (operand2 instanceof Number)) {
      throw new PainterException("scalefont: wrong arguments");
    }
    this.execute(context, (java.awt.Font) operand1, (Number) operand2);
  }

  public void execute(PAContext context, java.awt.Font operand1,
                      Number operand2) throws
      PainterException {
    java.awt.Font fn = operand1.deriveFont(
        operand2.floatValue());
    if (PAContext.DebugExecution) {
      System.out.println("Fonthoehe:" + fn.getSize2D());
    }
    context.operands.push(fn);
    if(PAContext.DebugCommandleveltrace)System.out.println("scalefont("+operand1.getFontName()+","+operand2.doubleValue()+")");
  }
}
