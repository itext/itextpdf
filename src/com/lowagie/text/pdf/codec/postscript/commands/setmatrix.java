package com.lowagie.text.pdf.codec.postscript.commands;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

public class setmatrix
    implements PACommand, IUnaryExecute,IPSLevel1 {
  public setmatrix() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    if (! (operand1 instanceof ArrayList)) {
      throw new PainterException("setmatrix: wrong argument");
    }
    this.execute(context, (ArrayList) operand1);
  }

  public void execute(PAContext context, ArrayList operand1) throws
      PainterException {
    double[] entries = new double[6];
    if (! (operand1.size() == 6)) {
      throw new PainterException("setmatrix: wrong arguments");
    }
    entries[0] = ( (Number) operand1.get(0)).doubleValue();
    entries[1] = ( (Number) operand1.get(1)).doubleValue();
    entries[2] = ( (Number) operand1.get(2)).doubleValue();
    entries[3] = ( (Number) operand1.get(3)).doubleValue();
    entries[4] = ( (Number) operand1.get(4)).doubleValue();
    entries[5] = ( (Number) operand1.get(5)).doubleValue();
    AffineTransform at = new AffineTransform(entries);
    context.pencil.graphics.setTransform(at);
  }
}
