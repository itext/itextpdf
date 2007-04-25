package com.lowagie.text.pdf.codec.postscript.commands;

import java.util.Iterator;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

public class aload
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public aload() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    if (operand1 instanceof PAToken) {
      operand1 = ( (PAToken) operand1).value;
    }
    if (! (operand1 instanceof java.util.AbstractList)) {
      throw new PainterException("aload: wrong arguments");
    }
    this.execute(context, (java.util.AbstractList) operand1);
  }

  public void execute(PAContext context, java.util.AbstractList list) throws
      PainterException {
    Iterator iterator = list.iterator();
    while (iterator.hasNext()) {
      context.operands.push(iterator.next());
    }
    context.operands.push(list);
  }
}
