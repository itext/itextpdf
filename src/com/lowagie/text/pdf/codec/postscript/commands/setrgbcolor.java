package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import java.awt.geom.AffineTransform;
import java.awt.Color;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class setrgbcolor
    implements PACommand, ITernaryExecute,IPSLevel1 {
  public setrgbcolor() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(3);
    this.execute(context, data[0], data[1], data[2]);
  }

  public void execute(PAContext context, Object r, Object g, Object b) throws
      PainterException {
    this.execute(context, (Number) r, (Number) g, (Number) b);
  }

  public void execute(PAContext context, Number r, Number g, Number b) throws
      PainterException {
    float[] fv = new float[3];
    fv[0] = (float) Math.max(Math.min(r.doubleValue(), 1.0d), 0.0d);
    fv[1] = (float) Math.max(Math.min(g.doubleValue(), 1.0d), 0.0d);
    fv[2] = (float) Math.max(Math.min(b.doubleValue(), 1.0d), 0.0d);
    context.pencil.graphics.setPaint(new Color(fv[0], fv[1], fv[2]));
    if(PAContext.DebugCommandleveltrace)System.out.println("setrgbcolor("+fv[0]+","+fv[1]+","+fv[2]+")");
  }
}
