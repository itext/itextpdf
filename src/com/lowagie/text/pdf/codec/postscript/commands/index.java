package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class index implements PACommand,IPSLevel1{
  public index() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
       Object data[];
       data = context.popOperands(1);
       if (! (data[0] instanceof Number)) {
         throw new PainterException("index: wrong arguments");
       }
       int index = ( (Number) data[0]).intValue();
       try {
         context.operands.push(context.operands.elementAt(index));
       }
       catch (ArrayIndexOutOfBoundsException e) {
         throw new PainterException(e.toString());
       }
     }

}
