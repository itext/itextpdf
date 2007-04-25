package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

public class bind
    implements PACommand, IUnaryExecute,IPSLevel1 {
  public bind() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    if (! (operand1 instanceof PAToken)) {
      throw new PainterException("bind: wrong arguments, not PAToken");
    }
    this.execute(context, (PAToken) operand1);
  }

  public void execute(PAContext context, PAToken operand1) throws
      PainterException {
    if (! (operand1.type == PAToken.PROCEDURE)) {
      throw new PainterException("bind: wrong arguments, not Procedure " +
                                 operand1.value);
    }
    context.engine.bindProcedure(operand1);
    context.operands.push(operand1);
  }
}
