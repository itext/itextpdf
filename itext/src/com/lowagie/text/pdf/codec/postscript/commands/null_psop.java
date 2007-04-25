package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class null_psop
    implements PACommand, IPSLevel1 {
  public null_psop() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    context.operands.push(null);
  }
}
