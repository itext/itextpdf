package com.lowagie.text.pdf.codec.postscript.commands;

import java.util.ArrayList;
import java.util.HashMap;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

/**
 *
 * <p>Title: get</p>
 *
 * <p>Description: array index get any
 * dict key get any
 * string index get int</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class get
    implements PACommand, IBinaryExecute, IPSLevel1 {
  public get() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(2);
    this.execute(context, data[0], data[1]);
  }

  public void execute(PAContext context, Object operand1, Object operand2) throws
      PainterException {
    if (! (operand1 instanceof HashMap) && ! (operand1 instanceof ArrayList)) {
      throw new PainterException("get: wrong arguments");
    }
    if (operand1 instanceof HashMap) {
      if (! (operand2 instanceof PAToken)) {
        throw new PainterException("get: wrong arguments");
      }
      PAToken patoken = (PAToken) operand2;
      if (! (patoken.type == PAToken.KEY)) {
        throw new PainterException("get: wrong arguments");
      }
      execute(context, (HashMap) operand1, patoken);
    }
    else if (operand1 instanceof ArrayList) {
      if (! (operand2 instanceof Number)) {
        throw new PainterException("get: wrong arguments");
      }
      execute(context, (ArrayList) operand1, (Number) operand2);
    }
  }

  public void execute(PAContext context, HashMap operand1, PAToken patoken) {
    context.operands.push(operand1.get(patoken.value));
  }

  public void execute(PAContext context, ArrayList operand1, Number operand2) {
    context.operands.push(operand1.get(operand2.intValue()));
  }
}
