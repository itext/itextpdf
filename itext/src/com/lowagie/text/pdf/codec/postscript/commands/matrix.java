package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import java.util.ArrayList;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class matrix implements PACommand, IPSLevel1 {
  public matrix() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
        ArrayList identityMatrix = new ArrayList(6);

        identityMatrix.add(new Double(1.0d));
        identityMatrix.add(new Double(0.0d));
        identityMatrix.add(new Double(0.0d));
        identityMatrix.add(new Double(1.0d));
        identityMatrix.add(new Double(0.0d));
        identityMatrix.add(new Double(0.0d));
        context.operands.push(identityMatrix);
        if(PAContext.DebugCommandleveltrace)System.out.println("matrix");
      }
}
