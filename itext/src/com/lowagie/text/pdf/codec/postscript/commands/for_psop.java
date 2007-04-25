package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IQuaternaryExecute;

/**
 *
 * <p>Title: for</p>
 *
 * <p>Description: initial increment limit proc for -</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class for_psop
    implements PACommand, IQuaternaryExecute, IPSLevel1 {
  public for_psop() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(4);
    this.execute(context, data[0], data[1], data[2], data[3]);
  }

  public void execute(PAContext context, Object initial, Object increment,
                      Object limit, Object proc) throws
      PainterException {
    if (! (proc instanceof PAToken)) {
      throw new PainterException("for: wrong arguments");
    }
    if (! (initial instanceof Number)) {
      throw new PainterException("for: wrong arguments");
    }
    if (! (increment instanceof Number)) {
      throw new PainterException("for: wrong arguments");
    }
    if (! (limit instanceof Number)) {
      throw new PainterException("for: wrong arguments");
    }
    this.execute(context, (Number) initial, (Number) increment,
                 (Number) limit, (PAToken) proc);
  }

  public void execute(PAContext context, Number initial, Number increment,
                      Number limit, PAToken patoken) throws
      PainterException {
    if (! (patoken.type == PAToken.PROCEDURE)) {
      throw new PainterException("for: wrong arguments");
    }
    if (PAContext.DebugCommandleveltrace) {
      System.out.println("for(" + initial + "," + increment + "," + limit + "," +
                         patoken.toString() + ")");
    }
    if (increment.intValue() > 0) {
      for (int i = initial.intValue(); i <= limit.intValue();
           i += increment.intValue()) {
        context.operands.push(new Integer(i));
        context.engine.process(patoken);
      }
    }
    else {
      for (int i = initial.intValue(); i >= limit.intValue();
           i -= increment.intValue()) {
        context.operands.push(new Integer(i));
        context.engine.process(patoken);
      }
    }
  }
}
