package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import java.awt.BasicStroke;
import java.awt.Stroke;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class setlinecap
    implements PACommand, IUnaryExecute ,IPSLevel1{
  public setlinecap() {
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
    BasicStroke newStroke;
    Stroke oldStroke = context.pencil.graphics.getStroke();
    if (oldStroke instanceof BasicStroke) {
      newStroke = new BasicStroke( ( (BasicStroke) oldStroke).getLineWidth(),
                                  (int) operand1.doubleValue(),
                                  ( (BasicStroke) oldStroke).getLineJoin(),
                                  ( (BasicStroke) oldStroke).getMiterLimit(),
                                  ( (BasicStroke) oldStroke).getDashArray(),
                                  ( (BasicStroke) oldStroke).getDashPhase());
    }
    else {
      newStroke = new BasicStroke(1.0f, (int) operand1.doubleValue(),
                                  BasicStroke.JOIN_ROUND);
    }
    context.pencil.graphics.setStroke(newStroke);
  }
}
