package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import java.awt.Color;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class ifelse_psop
    implements PACommand, ITernaryExecute, IPSLevel1 {
  public ifelse_psop() {
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
    if (! (operand1 instanceof Boolean)) {
      throw new PainterException("ifelse: wrong arguments");
    }
    if (! (operand2 instanceof PAToken)) {
      throw new PainterException("ifelse: wrong arguments");
    }
    if (! (operand3 instanceof PAToken)) {
      throw new PainterException("ifelse: wrong arguments");
    }

    this.execute(context, (Boolean) operand1, (PAToken) operand2,
                 (PAToken) operand3);
  }

  public void execute(PAContext context, Boolean operand1, PAToken patoken1,
                      PAToken patoken2) throws
      PainterException {
    if (! (patoken1.type == PAToken.PROCEDURE)) {
      throw new PainterException("ifelse: wrong arguments");
    }
    if (! (patoken2.type == PAToken.PROCEDURE)) {
      throw new PainterException("ifelse: wrong arguments");
    }
    if ( ( (Boolean) operand1).booleanValue()) {
      context.engine.process(patoken1);
    }
    else {
      context.engine.process(patoken2);
    }

  }

}
