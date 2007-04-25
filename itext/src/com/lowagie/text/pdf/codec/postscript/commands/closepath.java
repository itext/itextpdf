package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

/**
 *
 * <p>Title: closepath</p>
 *
 * <p>Description: - closepath -</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class closepath
    implements PACommand, IPSLevel1 {
  public closepath() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    context.pencil.closepath();
    if(PAContext.DebugCommandleveltrace)System.out.println("closepath");
  }

}
