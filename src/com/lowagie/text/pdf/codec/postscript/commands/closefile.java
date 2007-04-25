package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PAContext;
import java.io.InputStream;
import java.io.IOException;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: closefile</p>
 *
 * <p>Description: file closefile -</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class closefile
    implements PACommand, IUnaryExecute, IPSLevel1 {
  public closefile() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(1);
    this.execute(context, data[0]);
  }

  public void execute(PAContext context, Object operand1) throws
      PainterException {
    if (! (operand1 instanceof InputStream)) {
      throw new PainterException("closefile: wrong arguments");
    }
    this.execute(context, (InputStream) operand1);
  }

  public void execute(PAContext context, InputStream operand1) throws
      PainterException {
    try {
      operand1.close();
    }
    catch (IOException ex) {
    }
  }
}
