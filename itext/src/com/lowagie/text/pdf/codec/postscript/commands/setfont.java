package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class setfont
    implements PACommand, IUnaryExecute,IPSLevel1 {
  public setfont() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    if (! (operand1 instanceof java.awt.Font)) {
      throw new PainterException("setfont: wrong arguments");
    }

    this.execute(context, (java.awt.Font) operand1);
  }

  public void execute(PAContext context, java.awt.Font operand1) throws
      PainterException {
    if (PAContext.DebugExecution) {
      System.out.println("Fonthoehe:" + operand1.getSize2D());
    }
    //todo two times the same?
    context.pencil.graphics.setFont(operand1);
    if(PAContext.DebugCommandleveltrace)System.out.println("setfont("+operand1.getFontName()+","+operand1.getSize2D()+")");
//         context.pencil.state.font=fn;
  }

}
