package com.lowagie.text.pdf.codec.postscript.commands;

import java.util.ArrayList;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class makefont
    implements PACommand, IBinaryExecute,IPSLevel1 {
  public makefont() {
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
      throw new PainterException("makefont: wrong arguments");
    }
    if (! (operand2 instanceof ArrayList)) {
      throw new PainterException("makefont: wrong arguments");
    }
    this.execute(context, (java.awt.Font) operand1, (ArrayList) operand2);
  }

  public void execute(PAContext context, java.awt.Font operand1,
                      ArrayList operand2) throws
      PainterException {
    // @TODO implement!!!
    context.operands.push(operand1);
  }
}
