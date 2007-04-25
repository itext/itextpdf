package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;
public class gt
    implements PACommand, IBinaryExecute, IPSLevel1 {
  public gt() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(2);
    this.execute(context, data[0], data[1]);
  }

  public void execute(PAContext context, Object operand1, Object operand2) throws
      PainterException {

    if (! (operand1 instanceof Number) && ! (operand1 instanceof String)) {
      throw new PainterException("gt: wrong arguments");
    }
    if (operand1 instanceof Number) {
      if (! (operand2 instanceof Number)) {
        throw new PainterException("gt: wrong arguments");
      }
      this.execute(context, (Number) operand1, (Number) operand2);
    }
    else {
      if (! (operand2 instanceof String)) {
        throw new PainterException("gt: wrong arguments");
      }
      this.execute(context, (String) operand1, (String) operand2);
    }
  }

  public void execute(PAContext context, Number operand1, Number operand2) throws
      PainterException {
    if (operand1.doubleValue() > operand2.doubleValue()) {
      context.operands.push(Boolean.TRUE);
    }
    else {
      context.operands.push(Boolean.FALSE);
    }
  }

  public void execute(PAContext context, String operand1, String operand2) throws
      PainterException {
    if (operand1.compareTo(operand2) > 0) {
      context.operands.push(Boolean.TRUE);
    }
    else {
      context.operands.push(Boolean.FALSE);
    }
  }
}
