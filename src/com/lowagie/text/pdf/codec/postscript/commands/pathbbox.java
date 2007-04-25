package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import java.awt.geom.Rectangle2D;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class pathbbox
    implements PACommand, IPSLevel1 {
  public pathbbox() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Rectangle2D pathBounds = context.pencil.state.path.getBounds2D();
    context.operands.push(new Double(pathBounds.getMinX()));
    context.operands.push(new Double(pathBounds.getMinY()));
    context.operands.push(new Double(pathBounds.getMaxX()));
    context.operands.push(new Double(pathBounds.getMaxY()));
  }
}
