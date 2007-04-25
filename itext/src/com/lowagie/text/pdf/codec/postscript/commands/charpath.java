package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: charpath</p>
 *
 * <p>Description: string bool charpath</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class charpath
    implements PACommand, IBinaryExecute, IPSLevel1 {
  public charpath() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(2);
    this.execute(context, data[0], data[1]);
  }

  public void execute(PAContext context, Object operand1, Object operand2) throws
      PainterException {
    if (! (operand1 instanceof StringBuffer)) {
      throw new PainterException("charpath: wrong arguments");
    }
    if (! (operand2 instanceof Boolean)) {
      throw new PainterException("charpath: wrong arguments");
    }
    this.execute(context, (StringBuffer) operand1, (Boolean) operand2);
  }

  public void execute(PAContext context, StringBuffer operand1,
                      Boolean operand2) throws
      PainterException {
    context.pencil.charpath(operand1.toString(),
                            operand2.booleanValue());
    if (PAContext.DebugCommandleveltrace) {
      System.out.println("charpath(\"" + operand1.toString() + "\"," +
                         operand2.toString() + ")");
    }
  }
}
