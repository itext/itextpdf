package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

/**
 *
 * <p>Title: currentscreen</p>
 *
 * <p>Description: - currentscreen frequency angle halftone</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class currentscreen
    implements PACommand, IPSLevel1 {
  public currentscreen() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    if (!PAContext.IgnoreUnknownCommands) {
      throw new UnsupportedOperationException("currentscreen");
    }
    else {
      context.operands.push(new Double(60));
      context.operands.push(new Double(0));
      context.operands.push(new Double(0));
    }
  }
}
