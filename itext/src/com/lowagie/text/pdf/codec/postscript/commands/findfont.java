package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

/**
 *
 * <p>Title: findfont</p>
 *
 * <p>Description: key findfont font
 *                 key findfont cidfont</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class findfont
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public findfont() {
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
      throw new PainterException("findfont: wrong arguments");
    }
    if (! ( ( (PAToken) (operand1)).type == PAToken.KEY)) {
      throw new PainterException("findfont: wrong arguments");
    }
    this.execute(context, (PAToken) operand1);
  }

  public void execute(PAContext context, PAToken operand1) throws
      PainterException {
    context.operands.push(context.pencil.findFont( (String) operand1.value));
    if(PAContext.DebugCommandleveltrace)System.out.println("findfont("+(String) operand1.value+")");
  }
}
