package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.*;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: abs </p>
 *
 * <p>Description: num1 abs num2 </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class abs extends sqrt implements PACommand, IUnaryExecute,IPSLevel1 {

  public abs() {
    super();
  }

  public void execute(PAContext context, Number operand1) throws
      PainterException {
    context.operands.push(new Double(Math.abs(operand1.doubleValue())));
  }
}
