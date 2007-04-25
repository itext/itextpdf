package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PAContext;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: currentmatrix</p>
 *
 * <p>Description: matrix currentmatrix matrix</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class currentmatrix implements PACommand,IPSLevel1{
  public currentmatrix() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
   data = context.popOperands(1);
   if (! (data[0] instanceof ArrayList)) {
     throw new PainterException("currentmatrix: wrong argument");
   }
   ArrayList array = (ArrayList) data[0];

     double[] entries = new double[6];

     if (! (array.size() == 6)) {
       throw new PainterException("currentmatrix: wrong arguments");
     }


     AffineTransform ctm = context.pencil.graphics.getTransform();
     ctm.getMatrix(entries);

     for (int i = 0; i < 6; i++) {
       array.set(i, new Double(entries[i]));
     }
     context.operands.push(array);
  }

}
