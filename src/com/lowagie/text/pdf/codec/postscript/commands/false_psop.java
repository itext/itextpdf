package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: false</p>
 *
 * <p>Description: - false false</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class false_psop
    implements PACommand, IPSLevel1 {
  public false_psop() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    context.operands.push(Boolean.FALSE);
  }
}
