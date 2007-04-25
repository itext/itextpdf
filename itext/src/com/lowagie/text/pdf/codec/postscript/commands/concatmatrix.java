package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import java.awt.Color;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: concatmatrix</p>
 *
 * <p>Description: matrix1 matrix2 matrix3 concatmatrix matrix3</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class concatmatrix
    implements PACommand, ITernaryExecute, IPSLevel1 {
  public concatmatrix() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(3);
    this.execute(context, data[0], data[1], data[2]);
  }

  public void execute(PAContext context, Object operand1, Object operand2,
                      Object operand3) throws
      PainterException {
    if (! (operand1 instanceof ArrayList)) {
      throw new PainterException("concatmatrix: wrong arguments");
    }
    if (! (operand2 instanceof ArrayList)) {
      throw new PainterException("concatmatrix: wrong arguments");
    }
    if (! (operand3 instanceof ArrayList)) {
      throw new PainterException("concatmatrix: wrong arguments");
    }
    this.execute(context, (ArrayList) operand1, (ArrayList) operand2,
                 (ArrayList) operand3);
  }

  public void execute(PAContext context, ArrayList arrayOne, ArrayList arrayTwo,
                      ArrayList arrayThree) throws
      PainterException {
    AffineTransform atOne, atTwo;
    double[] entries = new double[6];

    if (! (arrayOne.size() == 6)) {
      throw new PainterException("concatmatrix: wrong arguments");
    }
    if (! (arrayTwo.size() == 6)) {
      throw new PainterException("concatmatrix: wrong arguments");
    }
    if (! (arrayThree.size() == 6)) {
      throw new PainterException("concatmatrix: wrong arguments");
    }

    for (int i = 0; i < 6; i++) {
      entries[i] = ( (Number) arrayOne.get(i)).doubleValue();
    }
    atOne = new AffineTransform(entries);
    for (int i = 0; i < 6; i++) {
      entries[i] = ( (Number) arrayTwo.get(i)).doubleValue();
    }
    atTwo = new AffineTransform(entries);

    atOne.concatenate(atTwo);

    atOne.getMatrix(entries);
    for (int i = 0; i < 6; i++) {
      arrayThree.set(i, new Double(entries[i]));
    }
    context.operands.push(arrayThree);
  }
}
