package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

public class truncate
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public truncate() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    this.execute(context, (Number) operand1);
  }

  public void execute(PAContext context, Number operand1) throws
      PainterException {
    double truncated;
    if (operand1.doubleValue() < 0) {
      truncated = Math.ceil(operand1.doubleValue());
    }
    else {
      truncated = Math.floor(operand1.doubleValue());
    }
    context.operands.push(new Double(truncated));
  }
}
