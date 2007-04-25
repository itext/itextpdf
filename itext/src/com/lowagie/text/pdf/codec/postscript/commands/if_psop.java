package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class if_psop
    implements PACommand, IBinaryExecute, IPSLevel1 {
  public if_psop() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(2);
    this.execute(context, data[0], data[1]);
  }

  public void execute(PAContext context, Object operand1, Object operand2) throws
      PainterException {

    if (! (operand1 instanceof Boolean)) {
      throw new PainterException("if: wrong arguments");
    }
    if (! (operand2 instanceof PAToken)) {
      throw new PainterException("if: wrong arguments");
    }

    this.execute(context, (Boolean) operand1, (PAToken) operand2);
  }

  public void execute(PAContext context, Boolean operand1, PAToken patoken) throws
      PainterException {
    if (! (patoken.type == PAToken.PROCEDURE)) {
      throw new PainterException("if: wrong arguments");
    }
    if ( ( (Boolean) operand1).booleanValue()) {
      context.engine.process(patoken);
    }

  }

}
