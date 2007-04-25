package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class newpath implements PACommand,IPSLevel1{
  public newpath() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
       context.pencil.newpath();
     }

}
