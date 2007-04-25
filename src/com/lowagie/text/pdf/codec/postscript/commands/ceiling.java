package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

/**
 *
 * <p>Title: ceiling</p>
 *
 * <p>Description: num1 ceiling num2</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class ceiling extends sqrt
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public ceiling() {
    super();
  }

  public void execute(PAContext context, Number operand1) throws
      PainterException {
    context.operands.push(new Double(Math.ceil(operand1.doubleValue())));
  }
}
