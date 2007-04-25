package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class repeat_psop
    implements PACommand, IBinaryExecute,IPSLevel1 {
  public repeat_psop() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(2);
    this.execute(context, data[0], data[1]);
  }

  public void execute(PAContext context, Object operand1, Object operand2) throws
      PainterException {
    if (! (operand2 instanceof PAToken)) {
      throw new PainterException("repeat: wrong arguments");
    }
    if (! (operand1 instanceof Number)) {
      throw new PainterException("repeat: wrong arguments");
    }
    this.execute(context, (Number) operand1, (PAToken) operand2);
  }

  public void execute(PAContext context, Number operand1, PAToken patoken) throws
      PainterException {
    if (! (patoken.type == PAToken.PROCEDURE)) {
      throw new PainterException("repeat: wrong arguments");
    }
    int n = ( (Number) operand1).intValue();
    for (int i = 0; i < n; i++) {
      context.engine.process(patoken);
    }
  }
}
