package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

public class where
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public where() {
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
      throw new PainterException("known: wrong arguments");
    }
    this.execute(context, (PAToken) operand1);
  }

  public void execute(PAContext context, PAToken patoken) throws
      PainterException {
    Object foundObject;
    if (! (patoken.type == PAToken.KEY)) {
      throw new PainterException("where: wrong arguments");
    }
    foundObject = context.findDictionary(patoken.value);
    if (foundObject != null) {
      context.operands.push(foundObject);
      context.operands.push(Boolean.TRUE);
    }
    else {
      context.operands.push(Boolean.FALSE);
    }
  }
}
