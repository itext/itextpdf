package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: curveto</p>
 *
 * <p>Description: x1 y1 x2 y2 x3 y3 curveto -</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class curveto implements PACommand, ISenaryExecute,IPSLevel1 {
  public curveto() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Number data[];
    data = context.popNumberOperands(6);
    this.execute(context, data[0], data[1], data[2], data[3], data[4], data[5]);
  }

  public void execute(PAContext context, Object operand1, Object operand2,
                      Object operand3, Object operand4, Object operand5,
                      Object operand6) throws
      PainterException {
    this.execute(context, (Number) operand1, (Number) operand2,
                 (Number) operand3, (Number) operand4, (Number) operand5,
                 (Number) operand6);
  }

  public void execute(PAContext context, Number operand1, Number operand2,
                      Number operand3, Number operand4, Number operand5,
                      Number operand6) throws
      PainterException {
    context.pencil.curveto(operand1.doubleValue(), operand2.doubleValue(),
                            operand3.doubleValue(), operand4.doubleValue(),
                            operand5.doubleValue(),
                            operand6.doubleValue());
  }
}
