package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class scale
    implements PACommand, IBinaryExecute, ITernaryExecute,IPSLevel1 {
  public scale() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    if (context.peekOperand() instanceof Number) {
      Object data[];
      data = context.popNumberOperands(2);
      execute(context, data[0], data[1]);
    }
    else {
      Object data[];
      data = context.popOperands(3);
      execute(context, data[0], data[1], data[2]);
    }
  }

  public void execute(PAContext context, Object sx, Object sy) throws
      PainterException {
    this.execute(context, (Number) sx, (Number) sy);
  }

  public void execute(PAContext context, Number sx, Number sy) throws
      PainterException {
    AffineTransform at = new AffineTransform();
    AffineTransform ctm = context.pencil.graphics.getTransform();

    at.scale(sx.doubleValue(), sy.doubleValue());
    ctm.concatenate(at);
    context.pencil.graphics.setTransform(ctm);
    if(PAContext.DebugCommandleveltrace)System.out.println("scale("+sx.doubleValue()+","+sy.doubleValue()+")");
  }

  public void execute(PAContext context, Object sx, Object sy, Object matrix) throws
      PainterException {
    if (! (sx instanceof Number)) {
      throw new PainterException("scale: wrong arguments");
    }
    if (! (sy instanceof Number)) {
      throw new PainterException("scale: wrong arguments");
    }
    if (! (matrix instanceof ArrayList)) {
      throw new PainterException("scale: wrong arguments");
    }

    ArrayList array = (ArrayList) matrix;

    if (! (array.size() == 6)) {
      throw new PainterException("scale: wrong arguments");
    }
    this.execute(context, (Number) sx, (Number) sy, array);
  }

  public void execute(PAContext context, Number sx, Number sy, ArrayList matrix) throws
      PainterException {

    double[] entries = new double[6];

    entries[0] = sx.doubleValue();
    entries[1] = 0.0d;
    entries[2] = 0.0d;
    entries[3] = sy.doubleValue();
    entries[4] = 0.0d;
    entries[5] = 0.0d;

    for (int i = 0; i < 6; i++) {
      matrix.set(i, new Double(entries[i]));
    }
    context.operands.push(matrix);
    if(PAContext.DebugCommandleveltrace)System.out.println("scale3("+sx.doubleValue()+","+sy.doubleValue()+")");
  }
}
