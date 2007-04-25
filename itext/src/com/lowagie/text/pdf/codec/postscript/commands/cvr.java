package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

/**
 *
 * <p>Title: cvr (convert to real)</p>
 *
 * <p>Description: num cvr real
 *                 string cvr real</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class cvr
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public cvr() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    if (! (operand1 instanceof Number) && ! (operand1 instanceof String)) {
      throw new PainterException("cvr: wrong arguments");
    }
    if (operand1 instanceof Number) {
      this.execute(context, (Number) operand1);
    }
    else {
      execute(context, (String) operand1);
    }
  }

  public void execute(PAContext context, String operand1) throws
      NumberFormatException {
    context.operands.push(new Double(operand1));
  }

  public void execute(PAContext context, Number operand1) throws
      PainterException {
    context.operands.push(new Double(operand1.intValue()));
  }
}
