package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class rlineto  implements PACommand, IBinaryExecute,IPSLevel1 {
  public rlineto() {
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
    context.pencil.rlineto(operand1.doubleValue(), operand2.doubleValue());
    if(PAContext.DebugCommandleveltrace)System.out.println("rlineto("+operand1.doubleValue()+","+operand2.doubleValue()+")");
  }

}
