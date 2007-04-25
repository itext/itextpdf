package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import java.util.EmptyStackException;
import java.util.HashMap;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: def</p>
 *
 * <p>Description: key value def -</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class def
    implements PACommand, IBinaryExecute,IPSLevel1 {
  public def() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(2);
    this.execute(context, data[0], data[1]);
  }

  public void execute(PAContext context, Object operand1, Object operand2) throws
      PainterException {
    if (! (operand1 instanceof PAToken)) {
      throw new PainterException("def: wrong arguments");
    }
    this.execute(context, (PAToken) operand1, operand2);
  }

  public void execute(PAContext context, PAToken operand1, Object operand2) throws
      PainterException {
    if (! (operand1.type == PAToken.KEY)) {
      throw new PainterException("def: wrong arguments");
    }
    try {
      ( (HashMap) context.dictionaries.peek()).put(operand1.value, operand2);
    }
    catch (EmptyStackException e) {
      throw new PainterException(e.toString());
    }
  }
}
