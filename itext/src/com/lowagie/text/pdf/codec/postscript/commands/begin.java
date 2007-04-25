package com.lowagie.text.pdf.codec.postscript.commands;

import java.util.HashMap;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

/**
 *
 * <p>Title: begin</p>
 *
 * <p>Description: dict begin -</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class begin implements PACommand, IUnaryExecute, IPSLevel1 {
  public begin() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    if (! (operand1 instanceof HashMap)) {
         throw new PainterException("begin: wrong arguments");
       }
    this.execute(context, (HashMap) operand1);
  }

  public void execute(PAContext context, HashMap operand1) throws
      PainterException {
   context.dictionaries.push(operand1);
  }

}
