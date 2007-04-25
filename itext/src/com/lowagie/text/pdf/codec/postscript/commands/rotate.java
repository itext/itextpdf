package com.lowagie.text.pdf.codec.postscript.commands;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class rotate
    implements PACommand, IPSLevel1 {
  public rotate() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    if (context.peekOperand() instanceof Number) {
      Number data[];
      AffineTransform at = new AffineTransform();
      AffineTransform ctm = context.pencil.graphics.getTransform();

      data = context.popNumberOperands(1);
      at.rotate(data[0].doubleValue() * Math.PI / 180.0d);
      ctm.concatenate(at);
      context.pencil.graphics.setTransform(ctm);
//      context.pencil.graphics.rotate(data[0].doubleValue());
      if(PAContext.DebugCommandleveltrace)System.out.println("rotate("+data[0].doubleValue()+")");
    }
    else {
      Object data[];
      AffineTransform at = new AffineTransform();

      data = context.popOperands(2);
      if (! (data[0] instanceof Number)) {
        throw new PainterException("rotate: wrong arguments");
      }
      if (! (data[1] instanceof ArrayList)) {
        throw new PainterException("rotate: wrong arguments");
      }

      ArrayList array = (ArrayList) data[1];

      if (! (array.size() == 6)) {
        throw new PainterException("rotate: wrong arguments");
      }

      at.rotate( ( (Number) data[0]).doubleValue());

      double[] entries = new double[6];

      at.getMatrix(entries);

      for (int i = 0; i < 6; i++) {
        array.set(i, new Double(entries[i]));
      }
      context.operands.push(array);
      if(PAContext.DebugCommandleveltrace)System.out.println("rotate6()");
    }
  }
}
