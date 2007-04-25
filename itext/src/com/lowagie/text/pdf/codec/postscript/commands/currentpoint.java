package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import java.awt.geom.Point2D;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

/**
 *
 * <p>Title: currentpoint</p>
 *
 * <p>Description: - currentpoint x y</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Carsten Hammer
 * @version 1.0
 */
public class currentpoint
    implements PACommand, IPSLevel1 {
  public currentpoint() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    // PENDING(uweh): what about CTM, same thing when you construct path
    // this is different than ps, might not work in a few instances
    Point2D currentPoint = context.pencil.state.path.getCurrentPoint();
    context.operands.push(new Double(currentPoint.getX()));
    context.operands.push(new Double(currentPoint.getY()));
  }
}
