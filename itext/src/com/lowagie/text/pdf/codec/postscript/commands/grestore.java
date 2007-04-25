package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class grestore
    implements PACommand, IPSLevel1 {
  public grestore() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    context.pencil.grestore();
    if(PAContext.DebugCommandleveltrace)System.out.println("grestore");
  }
}
