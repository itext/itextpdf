package com.lowagie.text.pdf.codec.postscript.commands;

import com.lowagie.text.pdf.codec.postscript.PACommand;
import com.lowagie.text.pdf.codec.postscript.PAContext;
import java.awt.geom.NoninvertibleTransformException;
import java.util.ArrayList;
import com.lowagie.text.pdf.codec.postscript.PainterException;
import java.awt.geom.AffineTransform;
import com.lowagie.text.pdf.codec.postscript.interfaces.*;

public class itransform implements PACommand, IPSLevel1 {
  public itransform() {
    super();
  }

  public void execute(PAContext context) throws PainterException {
       if (context.peekOperand() instanceof Number) {
         Number data[];
         double[] transformedData = new double[2];
         data = context.popNumberOperands(2);
         double[] data2=new double[2];
         data2[0]=data[0].doubleValue();
         data2[1]=data[1].doubleValue();

         AffineTransform at = context.pencil.graphics.getTransform();
         try {
           at.inverseTransform(data2, 0, transformedData, 0, 1);
         }
         catch (NoninvertibleTransformException e) {
           throw new PainterException(e.toString());
         }
         context.operands.push(new Double(transformedData[0]));
         context.operands.push(new Double(transformedData[1]));
       }
       else {
         Object data[];

         data = context.popOperands(3);
         if (! (data[0] instanceof Number)) {
           throw new PainterException("itransform: wrong arguments");
         }
         if (! (data[1] instanceof Number)) {
           throw new PainterException("itransform: wrong arguments");
         }
         if (! (data[2] instanceof ArrayList)) {
           throw new PainterException("itransform: wrong arguments");
         }

         ArrayList array = (ArrayList) data[2];

         double[] entries = new double[6];

         if (! (array.size() == 6)) {
           throw new PainterException("itransform: wrong arguments");
         }

         for (int i = 0; i < 6; i++) {
           entries[i] = ( (Number) array.get(i)).doubleValue();
         }

         AffineTransform at = new AffineTransform(entries);

         double numberdata[] = new double[2];
         numberdata[0] = ( (Number) data[0]).doubleValue();
         numberdata[1] = ( (Number) data[0]).doubleValue();

         double[] transformedData = new double[2];

         try {
           at.inverseTransform(numberdata, 0, transformedData, 0, 1);
         }
         catch (NoninvertibleTransformException e) {
           throw new PainterException(e.toString());
         }
         context.operands.push(new Double(transformedData[0]));
         context.operands.push(new Double(transformedData[1]));
       }
     }

}
