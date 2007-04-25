package com.lowagie.text.pdf.codec.postscript.commands;

import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import com.lowagie.text.pdf.codec.postscript.interfaces.IPSLevel1;

public class translate implements PACommand,IPSLevel1{
  public translate() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
       if (context.peekOperand() instanceof Number) {
         Number data[];
         AffineTransform at = new AffineTransform();
         AffineTransform ctm = context.pencil.graphics.getTransform();

         data = context.popNumberOperands(2);
         at.translate(data[0].doubleValue(), data[1].doubleValue());
         ctm.concatenate(at);
         context.pencil.graphics.setTransform(ctm);
         if(PAContext.DebugCommandleveltrace)System.out.println("translate("+data[0].doubleValue()+","+data[1].doubleValue()+")");
       }
       else {
         Object data[];

         data = context.popOperands(3);
         if (! (data[0] instanceof Number)) {
           throw new PainterException("translate: wrong arguments");
         }
         if (! (data[1] instanceof Number)) {
           throw new PainterException("translate: wrong arguments");
         }
         if (! (data[2] instanceof ArrayList)) {
           throw new PainterException("translate: wrong arguments");
         }

         ArrayList array = (ArrayList) data[2];

         if (! (array.size() == 6)) {
           throw new PainterException("translate: wrong arguments");
         }

         AffineTransform at = new AffineTransform();

         at.translate( ( (Number) data[0]).doubleValue(),
                      ( (Number) data[1]).doubleValue());

         double[] entries = new double[6];

         at.getMatrix(entries);

         for (int i = 0; i < 6; i++) {
           array.set(i, new Double(entries[i]));
         }
         context.operands.push(array);
         if(PAContext.DebugCommandleveltrace)System.out.println("translate("+entries[0]+","+entries[1]+","+entries[2]+","+entries[3]+","+entries[4]+","+entries[5]+")");
       }
     }

}
