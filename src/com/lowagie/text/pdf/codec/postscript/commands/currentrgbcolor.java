package com.lowagie.text.pdf.codec.postscript.commands;

import java.awt.Color;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

/**
 *
 * <p>Title: currentrgbcolor</p>
 *
 * <p>Description: - currentgrbcolor red green blue</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class currentrgbcolor
    implements PACommand, IPSLevel1 {
  public currentrgbcolor() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Color cl = context.pencil.graphics.getColor();
    float[] fv = cl.getRGBComponents(null);
    context.operands.push(new Float(fv[0]));
    context.operands.push(new Float(fv[1]));
    context.operands.push(new Float(fv[2]));
  }
}
