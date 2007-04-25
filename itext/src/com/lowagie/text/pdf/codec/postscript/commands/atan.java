package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

/**
 *
 * <p>Title: atan</p>
 *
 * <p>Description: num den atan angle</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class atan implements PACommand, IBinaryExecute,IPSLevel1 {
  public atan() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
  Number data[];
  data = context.popNumberOperands(2);
  this.execute(context, data[0], data[1]);
}

public void execute(PAContext context, Object operand1, Object operand2) throws
    PainterException {
  this.execute(context, (Number) operand1, (Number) operand2);
}

public void execute(PAContext context, Number operand1, Number operand2) throws
    PainterException {
  context.operands.push(new Double(Math.atan2(operand1.doubleValue(), operand2.doubleValue())));
}

}
