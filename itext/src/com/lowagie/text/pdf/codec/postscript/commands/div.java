package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

/**
 *
 * <p>Title: div</p>
 *
 * <p>Description: num1 num2 div quotient</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class div
    implements PACommand, IBinaryExecute, IPSLevel1 {
  public div() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Number data[];
    data = context.popNumberOperands(2);
    this.execute(context, data[0], data[1]);
  }

  public void execute(PAContext context, Object operand1, Object operand2) throws
      PainterException {
    this.execute(context, (Number) operand1, (Number) operand2);
  }

  public void execute(PAContext context, Number operand1, Number operand2) throws
      PainterException {
    context.operands.push(new Double(operand1.doubleValue() /
                                     operand2.doubleValue()));
    if(PAContext.DebugCommandleveltrace)System.out.println("div("+operand1.doubleValue()+","+operand2.doubleValue()+")="+operand1.doubleValue() /
                                     operand2.doubleValue());
  }
}
