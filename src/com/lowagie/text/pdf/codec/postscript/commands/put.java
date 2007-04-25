package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import java.util.ArrayList;
import java.util.HashMap;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class put
    implements PACommand, ITernaryExecute, IPSLevel1 {
  public put() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(3);
    this.execute(context, data[0], data[1], data[2]);
  }

  public void execute(PAContext context, Object operand1, Object operand2,
                      Object operand3) throws
      PainterException {
    if ( (operand1 instanceof HashMap) && (operand2 instanceof PAToken)) {
      this.execute(context, (HashMap)operand1, (PAToken)operand2, operand3);
    }
    else
    if ( (operand1 instanceof ArrayList) && (operand2 instanceof Number)) {
      this.execute(context, (ArrayList)operand1, (Number)operand2, operand3);
    }
    else
    if ( (operand1 instanceof StringBuffer) && (operand2 instanceof Number) &&
        (operand3 instanceof Number)) {
      this.execute(context, (StringBuffer)operand1, (Number)operand2, operand3);
    }
//    else if ( (operand1 instanceof String) && (operand2 instanceof Number) &&
//        (operand3 instanceof Number)) {
//      this.execute(context, new StringBuffer((String)operand1), operand2, operand3);
//    }
    else
{
      throw new PainterException("put: wrong arguments");
    }
  }

  public void execute(PAContext context, HashMap operand1, PAToken operand2,
                      Object operand3) throws
      PainterException {
    if (! (operand2.type == PAToken.KEY)) {
      throw new PainterException("put: wrong arguments");
    }
    operand1.put(operand2.value, operand3);
  }

  public void execute(PAContext context, ArrayList operand1, Number operand2,
                      Object operand3) throws
      PainterException {
    ArrayList ar = (ArrayList) operand1;
    Number nr = (Number) operand2;
    ar.set(nr.intValue(), operand3);
  }

  public void execute(PAContext context, StringBuffer operand1, Number operand2,
                      Object operand3) throws
      PainterException {
    Number nr = (Number) operand2;
    Number ch = (Number) operand3;
    operand1.setCharAt(nr.intValue(), (char) (ch.intValue()));
  }

}
