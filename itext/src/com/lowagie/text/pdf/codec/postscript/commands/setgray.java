package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import java.awt.Color;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class setgray
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public setgray() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    this.execute(context, (Number) operand1);
  }

  public void execute(PAContext context, Number operand1) throws
      PainterException {
    context.pencil.graphics.setPaint(new Color( (float) operand1.doubleValue(),
                                               (float) operand1.doubleValue(),
                                               (float) operand1.doubleValue()));
    if (PAContext.DebugCommandleveltrace) {
      System.out.println("setgray(" + operand1.doubleValue() + ")");
    }
  }

}
