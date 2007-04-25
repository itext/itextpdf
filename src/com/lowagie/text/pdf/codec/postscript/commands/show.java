package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class show implements PACommand,IPSLevel1{
  public show() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
       Object data[];

       data = context.popOperands(1);
       if ( (data[0] instanceof StringBuffer)) {
        data[0]=((StringBuffer)data[0]).toString();
      }

       if (! (data[0] instanceof String)) {
         throw new PainterException("show: wrong arguments");
       }
       context.pencil.show( (String) data[0]);
       if(PAContext.DebugCommandleveltrace)System.out.println("show(\""+data[0]+"\")");
     }

}
