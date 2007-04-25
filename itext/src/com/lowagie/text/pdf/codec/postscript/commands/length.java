package com.lowagie.text.pdf.codec.postscript.commands;

import java.util.ArrayList;
import java.util.HashMap;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

public class length
    implements PACommand, IUnaryExecute,IPSLevel1 {
  public length() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(1);
    execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {

    if (operand1 instanceof PAToken) {
      execute(context, (PAToken) operand1);
    }
    else if (operand1 instanceof HashMap) {
      execute(context, (HashMap) operand1);
    }
    else if (operand1 instanceof ArrayList) {
      execute(context, (ArrayList) operand1);
    }
    else if (operand1 instanceof String) {
      execute(context, (String) operand1);
    }
    else {
      throw new PainterException("length: wrong arguments");
    }

  }

  public void execute(PAContext context, HashMap operand1) throws
      PainterException {
    context.operands.push(new Integer(operand1.size()));
  }

  public void execute(PAContext context, ArrayList operand1) throws
      PainterException {
    context.operands.push(new Integer(operand1.size()));
  }

  public void execute(PAContext context, String operand1) throws
      PainterException {
    context.operands.push(new Integer(operand1.length()));
  }

  public void execute(PAContext context, PAToken operand1) throws
      PainterException {
    int size = 0;
    if (! (operand1.type == PAToken.KEY)) {
      throw new PainterException("length: wrong arguments");
    }
    size = ( (String) operand1.value).length();
    context.operands.push(new Integer(size));
  }
}
