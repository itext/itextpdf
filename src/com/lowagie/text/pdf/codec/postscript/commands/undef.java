package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.PAToken;
import java.util.HashMap;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class undef implements PACommand, IBinaryExecute,IPSLevel1 {
  public undef() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(2);
    this.execute(context, data[0],data[1]);
  }

  public void execute(PAContext context, Object operand1, Object operand2) throws
      PainterException {
    if (! (operand1 instanceof HashMap)) {
       throw new PainterException("undef: wrong arguments");
     }
     if (! (operand2 instanceof PAToken)) {
       throw new PainterException("undef: wrong arguments");
     }
    this.execute(context, (HashMap) operand1, (PAToken) operand1);
  }

  public void execute(PAContext context,HashMap operand1, PAToken patoken) throws
      PainterException {
       if (! (patoken.type == PAToken.KEY)) {
         throw new PainterException("undef: wrong arguments");
       }
       // we don't do an actual undef because we don't care
  }
}
