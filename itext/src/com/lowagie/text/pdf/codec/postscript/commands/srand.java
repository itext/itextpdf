package com.lowagie.text.pdf.codec.postscript.commands;

import java.util.Random;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;
import com.lowagie.text.pdf.codec.postscript.interfaces.IUnaryExecute;

public class srand implements PACommand, IUnaryExecute,IPSLevel1 {
  public srand() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Number data[];
    data = context.popNumberOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    this.execute(context, (Number) operand1);
  }

  public void execute(PAContext context, Number operand1) throws
      PainterException {
    context.randomNumberGenerator = new Random(Math.round(operand1.doubleValue()));
  }

}
