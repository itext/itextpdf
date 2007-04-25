package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class rand implements PACommand,IPSLevel1{
  public rand() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
       context.operands.push(new Integer(Math.abs(context.randomNumberGenerator.
           nextInt( (1 << 31) - 1))));
     }

}
