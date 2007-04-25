package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import java.awt.Stroke;
import java.awt.BasicStroke;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class setmiterlimit  implements PACommand, IUnaryExecute ,IPSLevel1{
  public setmiterlimit() {
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
                                    ( (BasicStroke) oldStroke).getEndCap(),
                                    ( (BasicStroke) oldStroke).getLineJoin(),
                                    (float) operand1.doubleValue(),
                                    ( (BasicStroke) oldStroke).getDashArray(),
                                    ( (BasicStroke) oldStroke).getDashPhase());
      }
      else {
        newStroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
                                    BasicStroke.JOIN_ROUND, (float) operand1.doubleValue());
      }
      context.pencil.graphics.setStroke(newStroke);
   }
}
