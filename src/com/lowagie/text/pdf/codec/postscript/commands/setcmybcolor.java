package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import java.awt.Color;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class setcmybcolor implements PACommand, IQuaternaryExecute,IPSLevel1 {
  public setcmybcolor() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(4);
    this.execute(context, data[0], data[1], data[2],data[3]);
  }

  public void execute(PAContext context, Object c, Object m, Object y,Object b) throws
      PainterException {
    this.execute(context, (Number) c, (Number) m, (Number) y, (Number) b);
  }

  public void execute(PAContext context, Number c, Number m, Number y, Number b) throws
      PainterException {
    // PENDING(uweh): I have to convert these puppies myself to rgb ?
     int rd, gr, bl;
     float[] fv = new float[4];
     fv[0] = (float) c.doubleValue();
     fv[1] = (float) m.doubleValue();
     fv[2] = (float) y.doubleValue();
     fv[3] = (float) b.doubleValue();
     rd = (int) (255 * Math.max(0, 1 - fv[0] - fv[3]));
     gr = (int) (255 * Math.max(0, 1 - fv[1] - fv[3]));
     bl = (int) (255 * Math.max(0, 1 - fv[2] - fv[3]));
        context.pencil.graphics.setPaint(new Color(rd, gr, bl));
  }

}
