package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

public class stopped
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public stopped() {
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
      throw new PainterException("stopped: wrong arguments");
    }
    this.execute(context, (PAToken) operand1);
  }

  public void execute(PAContext context, PAToken patoken) throws
      PainterException {
    // PENDING(uweh): we only support procedure right now and always push false on the stack
    // stopped
    if (! (patoken.type == PAToken.PROCEDURE)) {
      throw new PainterException("stopped: wrong arguments");
    }
    context.engine.process(patoken);
    context.operands.push(Boolean.FALSE);
  }
}
