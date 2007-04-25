package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class imagemask
    implements PACommand, IPSLevel1 {
  public imagemask() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(5);
//       if (data[0] instanceof PAToken) {
//         PAToken token = (PAToken) data[0];
//         context.engine.process(token);
//       }
    if(PAContext.DebugCommandleveltrace)System.out.println("imagemask5");
  }
}
