package com.lowagie.text.pdf.codec.postscript.commands;

import java.util.EmptyStackException;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

/**
 *
 * <p>Title: end</p>
 *
 * <p>Description: - end -</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class end
    implements PACommand, IPSLevel1 {
  public end() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    try {
      context.dictionaries.pop();
    }
    catch (EmptyStackException e) {
      throw new PainterException("Dictionary stack is empty");
    }
  }
}
