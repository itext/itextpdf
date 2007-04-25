package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class showpage implements PACommand,IPSLevel1{
  public showpage() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
       context.pencil.showpage();
       if(PAContext.DebugCommandleveltrace)System.out.println("showpage");
     }

}
