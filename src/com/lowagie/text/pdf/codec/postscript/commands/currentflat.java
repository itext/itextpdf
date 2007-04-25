package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

/**
 *
 * <p>Title: currentflat</p>
 *
 * <p>Description: - currentflat num</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class currentflat
    implements PACommand, IPSLevel1 {
  public currentflat() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    // currentflat PENDING(uweh):placeholder for now
    context.operands.push(new Double(1.0f));
  }
}
