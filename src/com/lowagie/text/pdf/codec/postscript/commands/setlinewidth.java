package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import java.awt.geom.AffineTransform;
import java.awt.BasicStroke;
import java.awt.Stroke;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class setlinewidth implements PACommand, IUnaryExecute,IPSLevel1 {
  public setlinewidth() {
    super();
  }
  private double minLineWidth(double w, AffineTransform at) {
         double matrix[] = new double[4];
         at.getMatrix(matrix);
         double scale = matrix[0] * matrix[3] - matrix[1] * matrix[2];
         double minlw = .25 / Math.sqrt(Math.abs(scale));
         if (w < minlw) {
           w = minlw;
         }
         return w;
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
      operand1 = new Double(this.minLineWidth(operand1.doubleValue(),
                                  context.pencil.graphics.getTransform()));
      if (oldStroke instanceof BasicStroke) {
        newStroke = new BasicStroke( (float) operand1.doubleValue(),
                                    ( (BasicStroke) oldStroke).getEndCap(),
                                    ( (BasicStroke) oldStroke).getLineJoin(),
                                    ( (BasicStroke) oldStroke).getMiterLimit(),
                                    ( (BasicStroke) oldStroke).getDashArray(),
                                    ( (BasicStroke) oldStroke).getDashPhase());
      }
      else {
        newStroke = new BasicStroke( (float) operand1.doubleValue(), BasicStroke.CAP_ROUND,
                                    BasicStroke.JOIN_ROUND);
      }
      //todo two times the same?
      context.pencil.graphics.setStroke(newStroke);
//      context.pencil.state.stroke=newStroke;
   }

}
