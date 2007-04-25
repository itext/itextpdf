package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: flattenpath</p>
 *
 * <p>Description: - flattenpath -</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class flattenpath
    implements PACommand, IPSLevel1 {
  public flattenpath() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    throw new UnsupportedOperationException();
  }
}
