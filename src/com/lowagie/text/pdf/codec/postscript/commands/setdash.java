package com.lowagie.text.pdf.codec.postscript.commands;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.ArrayList;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IBinaryExecute;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class setdash
    implements PACommand, IBinaryExecute,IPSLevel1 {
  public setdash() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
    Object data[];
    data = context.popOperands(2);
    this.execute(context, data[0], data[1]);
  }

  public void execute(PAContext context, Object operand1, Object operand2) throws
      PainterException {
    if (! (operand1 instanceof ArrayList)) {
      throw new PainterException("setdash: wrong arguments");
    }
    if (! (operand2 instanceof Number)) {
      throw new PainterException("setdash: wrong arguments");
    }
    this.execute(context, (ArrayList) operand1, (Number) operand2);
  }

  public void execute(PAContext context, ArrayList operand1, Number operand2) throws
      PainterException {
    BasicStroke newStroke;
    Stroke oldStroke = context.pencil.graphics.getStroke();
    if (operand1.isEmpty()) {
      return;
    }
    float[] dashpattern = new float[operand1.size()];
    for (int i = 0; i < dashpattern.length; i++) {
      dashpattern[i] = ( (Number) operand1.get(i)).floatValue();
    }
    float dashoffset = ( (Number) operand2).floatValue();
    if (oldStroke instanceof BasicStroke) {
      newStroke = new BasicStroke( ( (BasicStroke) oldStroke).getLineWidth(),
                                  ( (BasicStroke) oldStroke).getEndCap(),
                                  ( (BasicStroke) oldStroke).getLineJoin(),
                                  ( (BasicStroke) oldStroke).getMiterLimit(),
                                  dashpattern,
                                  dashoffset);
    }
    else {
      newStroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
                                  BasicStroke.JOIN_ROUND, 1.0f, dashpattern,
                                  dashoffset);
    }
    context.pencil.graphics.setStroke(newStroke);

  }
}
