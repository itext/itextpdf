package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import java.util.ArrayList;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import java.util.HashMap;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: getinterval</p>
 *
 * <p>Description: array index count getinterval subarray
 * string index count getinterval substring</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class getinterval
    implements PACommand, ITernaryExecute, IPSLevel1 {
  public getinterval() {
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
    if (! (operand1 instanceof HashMap) && ! (operand1 instanceof ArrayList)) {
      throw new PainterException("getinterval: wrong arguments");
    }
    if (operand1 instanceof HashMap) {
      if (! (operand2 instanceof PAToken)) {
        throw new PainterException("getinterval: wrong arguments");
      }
      PAToken patoken = (PAToken) operand2;
      if (! (patoken.type == PAToken.KEY)) {
        throw new PainterException("getinterval: wrong arguments");
      }
      if (! (operand3 instanceof Number)) {
        throw new PainterException("getinterval: wrong arguments");
      }
      execute(context, (HashMap) operand1, patoken);
    }
    else if (operand1 instanceof ArrayList) {
      if (! (operand2 instanceof Number)) {
        throw new PainterException("getinterval: wrong arguments");
      }
      if (! (operand3 instanceof Number)) {
        throw new PainterException("getinterval: wrong arguments");
      }
      execute(context, (Number) operand2, (Number) operand3,
              (ArrayList) operand1);
    }
  }

  public void execute(PAContext context, Number index, Number count,
                      ArrayList source) {
    int from = index.intValue();
    int to = from + count.intValue();
    ArrayList target = new ArrayList(source.subList(from, to));
    context.operands.push(target);
  }

  public void execute(PAContext context, HashMap operand1, PAToken patoken) {
    context.operands.push(operand1.get(patoken.value));
  }
}
