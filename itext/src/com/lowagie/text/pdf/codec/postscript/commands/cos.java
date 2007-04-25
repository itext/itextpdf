package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: cos</p>
 *
 * <p>Description: angle cos real</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class cos extends sqrt
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public cos() {
    super();
  }

  public void execute(PAContext context, Number operand1) throws
      PainterException {
    context.operands.push(new Double(Math.cos(operand1.doubleValue() * Math.PI /
                                              180.0)));
  }
}
