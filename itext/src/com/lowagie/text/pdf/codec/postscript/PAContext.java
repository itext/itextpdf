/*
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * This class is based on a sample class by SUN.
 * The original copyright notice is as follows:
 * 
 * Copyright 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 * 
 * The license agreement can be found at this URL:
 * http://java.sun.com/products/java-media/2D/samples/samples-license.html
 * See also the file sun.txt in directory com.lowagie.text.pdf
 */

package com.lowagie.text.pdf.codec.postscript;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;

import javax.swing.WindowConstants;

import com.lowagie.text.pdf.PdfGraphics2D;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.*;

public class PAContext {

  public PAPencil pencil;
  public Stack dictionaries;
  public Stack operands;
  public PAEngine engine;
  PAParser poorscript = null;
  protected Random randomNumberGenerator;
  InputStream is=null;

  protected Object lastUnknownIdentifier;
  public static boolean IgnoreUnknownCommands = false;
  public static boolean DebugExecution = false;

  public PAContext(Component component) {
    this(new PAPencil(component));
  }

  public PAContext(Graphics2D g, Dimension size) {
    this(new PAPencil(g, size));
  }

  public PAContext(PAPencil pencil) {
    super();
    this.pencil = pencil;
    this.dictionaries = new Stack();
    this.operands = new Stack();
    this.engine = new PAEngine(this);
    HashMap systemDict = this.constructSystemDict();
    this.dictionaries.push(systemDict);
    HashMap globalDict = this.constructGlobalDict();
    this.dictionaries.push(globalDict);
    HashMap userDict = this.constructUserDict();
    systemDict.put("userdict", userDict);
    this.dictionaries.push(userDict);
    this.randomNumberGenerator = new Random();
    this.lastUnknownIdentifier = null;
  }

  /**
   * draw
   *
   * @param inputStream InputStream
   * @throws PainterException
   */
  public void draw(InputStream inputStream) throws PainterException {
    try {
      String filename="init.ps";
//      poorscript = new PAParser(new NamedInputStream(PAContext.class.getResourceAsStream(filename),filename));
      InputStream inpstr=PAContext.class.getResourceAsStream(filename);
      poorscript = new PAParser(inpstr);
      poorscript.parse(this);
      try {
        inpstr.close();
      }
      catch (IOException ex) {
        ex.printStackTrace();
      }
//      poorscript.enable_tracing();
//      poorscript.token_source.setDebugStream(System.err);
//      byte[] b=null;
//      try {
//        b = RandomAccessFileOrArray.InputStreamToArray(inputStream);
//      }
//      catch (IOException ex) {
//        ex.printStackTrace();
//      }
//      ByteArrayInputStream bar=new ByteArrayInputStream(b);
//      is = bar;
      poorscript.ReInit(inputStream);
      poorscript.parse(this);
      // pencil.graphics.dispose();
    }
    catch (ParseException e) {
      e.printStackTrace();
      throw new PainterException(e.toString());
    }
  }



  public Object getLastUnknownIdentifier() {
    return this.lastUnknownIdentifier;
  }

  public double[] popNumberOperands(int n) throws PainterException {
    double[] result = new double[n];
    Object objectValue;
    double doubleValue;

    for (int i = n - 1; i >= 0; i--) {
      try {
        objectValue = this.operands.pop();
      }
      catch (EmptyStackException e) {
        throw new PainterException("Operand stack is empty poping " + n +
                                   " number operands");
      }
      if (objectValue instanceof Number) {
        doubleValue = ( (Number) objectValue).doubleValue();
      }
      else {
        throw new PainterException("Number expected on operand stack poping " +
                                   n + " number operands, found " +
                                   objectValue.getClass().getName());
      }
      result[i] = doubleValue;
    }
    return result;
  }

  public Object[] popOperands(int n) throws PainterException {
    Object[] result = new Object[n];
    Object objectValue;

    for (int i = n - 1; i >= 0; i--) {
      try {
        objectValue = this.operands.pop();
      }
      catch (EmptyStackException e) {
        throw new PainterException("Operand stack is empty poping " + n +
                                   " operands");
      }
      result[i] = objectValue;
    }
    return result;
  }

  public Object peekOperand() throws PainterException {
    Object objectValue;

    try {
      objectValue = this.operands.peek();
    }
    catch (EmptyStackException e) {
      throw new PainterException("Operand stack is empty peeking operand");
    }
    return objectValue;
  }

  public Object findIdentifier(Object identifier) {
    Object result = null;
    int i, n;

    n = this.dictionaries.size();
    i = n - 1;
    while (i >= 0 && result == null) {
      HashMap dictionary = (HashMap)this.dictionaries.elementAt(i);
      result = dictionary.get(identifier);
      i--;
    }
    if (result == null) {
      this.lastUnknownIdentifier = identifier;
    }
    return result;
  }

  public Object findDictionary(Object identifier) {
    Object result = null;
    HashMap dictionary = null;
    int i, n;

    n = this.dictionaries.size();
    i = n - 1;
    while (i >= 0 && result == null) {
      dictionary = (HashMap)this.dictionaries.elementAt(i);
      result = dictionary.get(identifier);
      i--;
    }
    if (result == null) {
      return result;
    }
    else {
      return dictionary;
    }
  }

  public void collectArray() throws PainterException {
    ArrayList result;
    Object objectValue;
    int i, n;
    boolean found = false;

    n = this.operands.size();
    for (i = n - 1; i >= 0; i--) {
      objectValue = this.operands.elementAt(i);
      if (objectValue instanceof PAToken &&
          ( (PAToken) objectValue).type == PAToken.START_ARRAY) {
        found = true;
        break;
      }
    }
    if (!found) {
      throw new PainterException("No array was started");
    }
    result = new ArrayList(n - i - 1);
    for (int j = 0; j < n - i - 1; j++) {
      result.add(null);
    }
    for (int j = n - 1; j > i; j--) {
      try {
        objectValue = this.operands.pop();
      }
      catch (EmptyStackException e) {
        throw new PainterException(
            "Operand stack is empty collecting array elements");
      }
      result.set(j - i - 1, objectValue);
    }
    try {
      this.operands.pop(); // the start array mark itself
    }
    catch (EmptyStackException e) {
      throw new PainterException(
          "Operand stack is empty removing begin array mark");
    }
    this.operands.push(result);
  }

  public void collectDict() throws PainterException {
    HashMap result; // = new HashMap();
    Object objectValue;
    int i, n;
    boolean found = false;

    n = this.operands.size();
    for (i = n - 1; i >= 0; i--) {
      objectValue = this.operands.elementAt(i);
      if (objectValue instanceof PAToken &&
          ( (PAToken) objectValue).type == PAToken.START_DICT) {
        found = true;
        break;
      }
    }
    if (!found) {
      throw new PainterException("No dict was started");
    }
//      result = new ArrayList(n - i - 1);
    result = new HashMap();
//      for (int j = 0; j < n - i - 1; j++) {
//        result.add(null);
//      }
    for (int j = n - 1; j > i; j -= 2) {
      Object targetValue;
      try {
        targetValue = this.operands.pop();
        objectValue = this.operands.pop();
      }
      catch (EmptyStackException e) {
        throw new PainterException(
            "Operand stack is empty collecting hashmap elements");
      }
      result.put(objectValue, targetValue);
    }
    try {
      this.operands.pop(); // the start array mark itself
    }
    catch (EmptyStackException e) {
      throw new PainterException(
          "Operand stack is empty removing begin array mark");
    }
    this.operands.push(result);
  }

  protected HashMap constructGlobalDict() {
    HashMap globalDict = new HashMap();
    return globalDict;
  }

  protected HashMap constructUserDict() {
    HashMap userDict = new HashMap();

    return userDict;
  }

  public static void main(String[] args) {
    javax.swing.JFrame jf = new javax.swing.JFrame();
    jf.setVisible(true);
    jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    PAContext pac = new PAContext(new PAPencil(jf));
    HashMap hm = (HashMap) pac.findDictionary("systemdict");
    Iterator it = new TreeSet(hm.keySet()).iterator();
    while (it.hasNext()) {

      String obname = it.next().toString();
      Object ob = hm.get(obname);
      String typname = ob.getClass().getName();
      System.out.println(obname + ":" + typname);
    }
    System.exit(0);
  }

  protected HashMap constructSystemDict() {
    HashMap systemDict = new HashMap();

    // newpath
    systemDict.put("newpath", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.pencil.newpath();
      }
    });

    // moveto
    systemDict.put("moveto", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        data = context.popNumberOperands(2);
        context.pencil.moveto(data[0], data[1]);
      }
    });

    // rmoveto
    systemDict.put("rmoveto", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(2);
        context.pencil.rmoveto(data[0], data[1]);
      }
    });

    // lineto
    systemDict.put("lineto", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(2);
        context.pencil.lineto(data[0], data[1]);
      }
    });

    // rlineto
    systemDict.put("rlineto", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(2);
        context.pencil.rlineto(data[0], data[1]);
      }
    });

    // arc
    systemDict.put("arc", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(5);
        context.pencil.arc(data[0], data[1], data[2], data[3], data[4]);
      }
    });

    // arcn
    systemDict.put("arcn", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(5);
        context.pencil.arcn(data[0], data[1], data[2], data[3], data[4]);
      }
    });

    // curveto
    systemDict.put("curveto", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(6);
        context.pencil.curveto(data[0], data[1], data[2], data[3], data[4],
                               data[5]);
      }
    });

    // rcurveto
    systemDict.put("rcurveto", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(6);
        context.pencil.rcurveto(data[0], data[1], data[2], data[3], data[4],
                                data[5]);
      }
    });

    // closepath
    systemDict.put("closepath", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.pencil.closepath();
      }
    });

    // gsave
    systemDict.put("gsave", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.pencil.gsave();
      }
    });

    // grestore
    systemDict.put("grestore", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.pencil.grestore();
      }
    });

    // translate
    systemDict.put("translate", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        if (context.peekOperand() instanceof Number) {
          double data[];
          AffineTransform at = new AffineTransform();
          AffineTransform ctm = context.pencil.graphics.getTransform();

          data = context.popNumberOperands(2);
          at.translate(data[0], data[1]);
          ctm.concatenate(at);
          context.pencil.graphics.setTransform(ctm);
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
        }
      }
    });

    // rotate
    systemDict.put("rotate", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        if (context.peekOperand() instanceof Number) {
          double data[];
          AffineTransform at = new AffineTransform();
          AffineTransform ctm = context.pencil.graphics.getTransform();

          data = context.popNumberOperands(1);
          at.rotate(data[0] * Math.PI / 180.0d);
          ctm.concatenate(at);
          context.pencil.graphics.setTransform(ctm);
        }
        else {
          Object data[];
          AffineTransform at = new AffineTransform();

          data = context.popOperands(2);
          if (! (data[0] instanceof Number)) {
            throw new PainterException("rotate: wrong arguments");
          }
          if (! (data[1] instanceof ArrayList)) {
            throw new PainterException("rotate: wrong arguments");
          }

          ArrayList array = (ArrayList) data[1];

          if (! (array.size() == 6)) {
            throw new PainterException("rotate: wrong arguments");
          }

          at.rotate( ( (Number) data[0]).doubleValue());

          double[] entries = new double[6];

          at.getMatrix(entries);

          for (int i = 0; i < 6; i++) {
            array.set(i, new Double(entries[i]));
          }
          context.operands.push(array);
        }
      }
    });

    // scale
    systemDict.put("scale", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        if (context.peekOperand() instanceof Number) {
          double data[];
          AffineTransform at = new AffineTransform();
          AffineTransform ctm = context.pencil.graphics.getTransform();

          data = context.popNumberOperands(2);
          at.scale(data[0], data[1]);
          ctm.concatenate(at);
          context.pencil.graphics.setTransform(ctm);
        }
        else {
          Object data[];

          data = context.popOperands(3);
          if (! (data[0] instanceof Number)) {
            throw new PainterException("scale: wrong arguments");
          }
          if (! (data[1] instanceof Number)) {
            throw new PainterException("scale: wrong arguments");
          }
          if (! (data[2] instanceof ArrayList)) {
            throw new PainterException("scale: wrong arguments");
          }

          ArrayList array = (ArrayList) data[2];

          double[] entries = new double[6];

          if (! (array.size() == 6)) {
            throw new PainterException("scale: wrong arguments");
          }

          entries[0] = ( (Number) data[0]).doubleValue();
          entries[1] = 0.0d;
          entries[2] = 0.0d;
          entries[3] = ( (Number) data[1]).doubleValue();
          entries[4] = 0.0d;
          entries[5] = 0.0d;

          for (int i = 0; i < 6; i++) {
            array.set(i, new Double(entries[i]));
          }
          context.operands.push(array);
        }
      }
    });
    // currentmatrix
    systemDict.put("currentmatrix", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
       data = context.popOperands(1);
       if (! (data[0] instanceof ArrayList)) {
         throw new PainterException("currentmatrix: wrong argument");
       }
       ArrayList array = (ArrayList) data[0];

         double[] entries = new double[6];

         if (! (array.size() == 6)) {
           throw new PainterException("currentmatrix: wrong arguments");
         }


         AffineTransform ctm = context.pencil.graphics.getTransform();
         ctm.getMatrix(entries);

         for (int i = 0; i < 6; i++) {
           array.set(i, new Double(entries[i]));
         }
         context.operands.push(array);
      }
    });

    // setmatrix
  systemDict.put("setmatrix", new PACommand() {
    public void execute(PAContext context) throws PainterException {
      Object data[];
      data = context.popOperands(1);
      if (! (data[0] instanceof ArrayList)) {
        throw new PainterException("setmatrix: wrong argument");
      }
      ArrayList array = (ArrayList) data[0];

      double[] entries = new double[6];

      if (! (array.size() == 6)) {
        throw new PainterException("setmatrix: wrong arguments");
      }
         entries[0] = ((Number)array.get(0)).doubleValue();
         entries[1] = ((Number)array.get(1)).doubleValue();
         entries[2] = ((Number)array.get(2)).doubleValue();
         entries[3] = ((Number)array.get(3)).doubleValue();
         entries[4] = ((Number)array.get(4)).doubleValue();
         entries[5] = ((Number)array.get(5)).doubleValue();

      AffineTransform at = new AffineTransform(entries);
      context.pencil.graphics.setTransform(at);
    }
  });

    // stroke
    systemDict.put("stroke", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.pencil.stroke();
      }
    });

    // fill
    systemDict.put("fill", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.pencil.fill();
      }
    });

    // eofill
    systemDict.put("eofill", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.pencil.eofill();
      }
    });

    // show
    systemDict.put("show", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];

        data = context.popOperands(1);
        if (! (data[0] instanceof String)) {
          throw new PainterException("show: wrong arguments");
        }
        context.pencil.show( (String) data[0]);
      }
    });

    // stringwidth
    systemDict.put("stringwidth", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        java.awt.Font font;

        data = context.popOperands(1);
        if (! (data[0] instanceof String)) {
          throw new PainterException("stringwidth: wrong arguments");
        }
        font = context.pencil.graphics.getFont();
        Rectangle2D rect = font.getStringBounds( (String) data[0],
                                                context.pencil.graphics.
                                                getFontRenderContext());
        context.operands.push(new Float(rect.getWidth()));
        context.operands.push(new Float(rect.getHeight()));
      }
    });

    // showpage
    systemDict.put("showpage", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.pencil.showpage();
      }
    });

    // findfont
    systemDict.put("findfont", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;
        data = context.popOperands(1);
        if (! (data[0] instanceof PAToken)) {
          throw new PainterException("findfont: wrong arguments");
        }
        patoken = (PAToken) data[0];
        if (! (patoken.type == PAToken.KEY)) {
          throw new PainterException("findfont: wrong arguments");
        }
        context.operands.push(context.pencil.findFont( (String) patoken.value));
      }
    });

    // makefont
        systemDict.put("makefont", new PACommand() {
          public void execute(PAContext context) throws PainterException {
            Object data[];
            data = context.popOperands(2);
            if (! (data[0] instanceof java.awt.Font)) {
              throw new PainterException("makefont: wrong arguments");
            }
            if (! (data[1] instanceof ArrayList)) {
              throw new PainterException("makefont: wrong arguments");
            }
            // @TODO implement!!!
            context.operands.push(data[0]);
          }
        });

    // scalefont
    systemDict.put("scalefont", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        data = context.popOperands(2);
        if (! (data[0] instanceof java.awt.Font)) {
          throw new PainterException("scalefont: wrong arguments");
        }
        if (! (data[1] instanceof Number)) {
          throw new PainterException("scalefont: wrong arguments");
        }
        java.awt.Font fn=( (java.awt.Font) data[0]).deriveFont( ( (Number)
            data[1]).
            floatValue());
        System.out.println("Fonthoehe:"+fn.getSize2D());
        context.operands.push(fn );
      }
    });

    // setfont
    systemDict.put("setfont", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        data = context.popOperands(1);
        if (! (data[0] instanceof java.awt.Font)) {
          throw new PainterException("setfont: wrong arguments");
        }
        java.awt.Font fn=(java.awt.Font)data[0];
        System.out.println("Fonthoehe:"+fn.getSize2D());
        /**
         * @todo two times the same?
         */
        context.pencil.graphics.setFont( fn);
        context.pencil.state.font=fn;
      }
    });

    // def
    systemDict.put("def", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;
        data = context.popOperands(2);
        if (! (data[0] instanceof PAToken)) {
          throw new PainterException("def: wrong arguments");
        }
        patoken = (PAToken) data[0];
        if (! (patoken.type == PAToken.KEY)) {
          throw new PainterException("def: wrong arguments");
        }
        try {
          ( (HashMap) context.dictionaries.peek()).put(patoken.value, data[1]);
        }
        catch (EmptyStackException e) {
          throw new PainterException(e.toString());
        }
      }
    });

    // bind
    systemDict.put("bind", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;
        data = context.popOperands(1);
        if (! (data[0] instanceof PAToken)) {
          throw new PainterException("bind: wrong arguments, not PAToken");
        }
        patoken = (PAToken) data[0];
        if (! (patoken.type == PAToken.PROCEDURE)) {
          throw new PainterException("bind: wrong arguments, not Procedure " +
                                     patoken.value);
        }
        context.engine.bindProcedure(patoken);
        context.operands.push(patoken);
      }
    });

    // mul
    systemDict.put("mul", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(2);
        context.operands.push(new Double(data[0] * data[1]));
      }
    });

    // div
    systemDict.put("div", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(2);
        context.operands.push(new Double(data[0] / data[1]));
      }
    });

    // mod
    systemDict.put("mod", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(2);
        int a, b, m;
        a = (int) data[0];
        b = (int) data[1];
        m = a % b;
        context.operands.push(new Integer(m));
      }
    });

    // add
    systemDict.put("add", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(2);
        context.operands.push(new Double(data[0] + data[1]));
      }
    });

    // neg
    systemDict.put("neg", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        data = context.popNumberOperands(1);
        context.operands.push(new Double( -data[0]));
      }
    });
    // ceiling
    systemDict.put("ceiling", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        data = context.popNumberOperands(1);
        context.operands.push(new Double(Math.ceil(data[0])));
      }
    });
    // sub
    systemDict.put("sub", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        data = context.popNumberOperands(2);
        context.operands.push(new Double(data[0] - data[1]));
      }
    });

    // atan
    systemDict.put("atan", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(2);
        context.operands.push(new Double(Math.atan2(data[0], data[1])));
      }
    });

    // sin
    systemDict.put("sin", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(1);
        context.operands.push(new Double(Math.sin(data[0] * Math.PI / 180.0)));
      }
    });

    // cos
    systemDict.put("cos", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(1);
        context.operands.push(new Double(Math.cos(data[0] * Math.PI / 180.0)));
      }
    });

    // sqrt
    systemDict.put("sqrt", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        data = context.popNumberOperands(1);
        context.operands.push(new Double(Math.sqrt(data[0])));
      }
    });
    // ln
    systemDict.put("log", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        data = context.popNumberOperands(1);
        context.operands.push(new Double(Math.log(data[0])));
      }
    });
    // exp
    systemDict.put("exp", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        data = context.popNumberOperands(2);
        context.operands.push(new Double(Math.pow(data[0], data[1])));
      }
    });

    // exch
    systemDict.put("exch", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];

        data = context.popOperands(2);
        context.operands.push(data[1]);
        context.operands.push(data[0]);
      }
    });

    // dup
    systemDict.put("dup", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];

        data = context.popOperands(1);
        context.operands.push(data[0]);
        context.operands.push(data[0]);
      }
    });

    // roll
    systemDict.put("roll", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        Object rollData[];

        data = context.popOperands(2);
        if (! (data[0] instanceof Number)) {
          throw new PainterException("roll: wrong arguments");
        }
        if (! (data[1] instanceof Number)) {
          throw new PainterException("roll: wrong arguments");
        }
        int numberOfElements, numberOfPositions, i;

        numberOfElements = ( (Number) data[0]).intValue();
        numberOfPositions = ( (Number) data[1]).intValue();

        if (numberOfPositions == 0 || numberOfElements <= 0) {
          return;
        }

        rollData = context.popOperands(numberOfElements);

        if (numberOfPositions < 0) {
          numberOfPositions = -numberOfPositions;
          numberOfPositions = numberOfPositions % numberOfElements;

          // downward roll
          for (i = numberOfPositions; i < numberOfElements; i++) {
            context.operands.push(rollData[i]);
          }
          for (i = 0; i < numberOfPositions; i++) {
            context.operands.push(rollData[i]);
          }
        }
        else {
          numberOfPositions = numberOfPositions % numberOfElements;

          // upward roll
          for (i = numberOfElements - numberOfPositions; i < numberOfElements;
               i++) {
            context.operands.push(rollData[i]);
          }
          for (i = 0; i < numberOfElements - numberOfPositions; i++) {
            context.operands.push(rollData[i]);
          }
        }
      }
    });

    // pop
    systemDict.put("pop", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.popOperands(1);
      }
    });

    // index
    systemDict.put("index", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        data = context.popOperands(1);
        if (! (data[0] instanceof Number)) {
          throw new PainterException("index: wrong arguments");
        }
        int index = ( (Number) data[0]).intValue();
        try {
          context.operands.push(context.operands.elementAt(index));
        }
        catch (ArrayIndexOutOfBoundsException e) {
          throw new PainterException(e.toString());
        }
      }
    });

    // mark
    systemDict.put("mark", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.operands.push(new PAToken(null, PAToken.MARK));
      }
    });

    // cvx
    systemDict.put("cvx", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data;
        data = context.operands.pop();
        ArrayList ar = (ArrayList) data;
        Stack stack = new Stack();
        for (int i = ar.size() - 1; i >= 0; i--) {
          stack.add(ar.get(i));
        }
        PAToken patoken = new PAToken(stack, PAToken.PROCEDURE);
//           patoken.type=PAToken.PROCEDURE;
        context.operands.push(patoken);
      }
    });
    // cleartomark
    systemDict.put("cleartomark", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data;
        boolean finished = false;

        while (!finished) {
          try {
            data = context.operands.pop();
            if (data instanceof PAToken) {
              if ( ( (PAToken) data).type == PAToken.MARK) {
                finished = true;
              }
            }
          }
          catch (EmptyStackException e) {
            throw new PainterException(e.toString());
          }
        }
      }
    });

    // copy
    systemDict.put("copy", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        data = context.popOperands(2);

        // decide if it's a simple copy or a composite object copy
        if ( (data[0] instanceof PAToken) && (data[1] instanceof PAToken)) {
          // composite object copy
          if ( ( (PAToken) data[0]).type == ( (PAToken) data[1]).type) {
            // our tokens are immutable so a copy is easy
            context.operands.push(data[0]);
            context.operands.push(data[0]);
          }
          else {
            throw new PainterException(
                "copy operation failed because composite objects on stack are not of same type");
          }
        }
        else {
          // restore first arg, we're not interested in it in this simple case
          context.operands.push(data[0]);

          if (data[1] instanceof Number) {
            int index = ( (Number) data[1]).intValue();
            int i, n;
            n = context.operands.size();
            Object[] copyData = new Object[index];
            for (i = n - index; i < n; i++) {
              try {
                copyData[i - n + index] = context.operands.elementAt(i);
              }
              catch (ArrayIndexOutOfBoundsException e) {
                throw new PainterException(e.toString());
              }
            }
            for (i = 0; i < index; i++) {
              context.operands.push(copyData[i]);
            }
          }
          else {
            throw new PainterException("I expect a number on stack, dude");
          }
        }
      }
    });

    // setgray
    systemDict.put("setgray", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(1);
        context.pencil.graphics.setPaint(new Color( (float) data[0],
            (float) data[0], (float) data[0]));
      }
    });

    // setrgbcolor
    systemDict.put("setrgbcolor", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(3);
        float[] fv = new float[3];
        fv[0] = (float) Math.max(Math.min(data[0], 1.0d), 0.0d);
        fv[1] = (float) Math.max(Math.min(data[1], 1.0d), 0.0d);
        fv[2] = (float) Math.max(Math.min(data[2], 1.0d), 0.0d);
        context.pencil.graphics.setPaint(new Color(fv[0], fv[1], fv[2]));
      }
    });

    // currentrgbcolor
systemDict.put("currentrgbcolor", new PACommand() {
  public void execute(PAContext context) throws PainterException {
    Color cl=context.pencil.graphics.getColor();
    float[] fv = cl.getRGBComponents(null);
    context.operands.push(new Float(fv[0]));
    context.operands.push(new Float(fv[1]));
    context.operands.push(new Float(fv[2]));
  }
});


    // PENDING(uweh): color stuff still shaky
    // sethsbcolor
    systemDict.put("sethsbcolor", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        data = context.popNumberOperands(3);
        float[] fv = new float[3];
        fv[0] = (float) Math.max(Math.min(data[0], 1.0d), 0.0d);
        fv[1] = (float) Math.max(Math.min(data[1], 1.0d), 0.0d);
        fv[2] = (float) Math.max(Math.min(data[2], 1.0d), 0.0d);
        context.pencil.graphics.setPaint(new Color(fv[0], fv[1], fv[2]));
      }
    });

    // PENDING(uweh): I have to convert these puppies myself to rgb ?
    // setcmykcolor
    systemDict.put("setcmykcolor", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        int rd, gr, bl;

        data = context.popNumberOperands(4);
        float[] fv = new float[4];
        fv[0] = (float) data[0];
        fv[1] = (float) data[1];
        fv[2] = (float) data[2];
        fv[3] = (float) data[3];
        rd = (int) (255 * Math.max(0, 1 - fv[0] - fv[3]));
        gr = (int) (255 * Math.max(0, 1 - fv[1] - fv[3]));
        bl = (int) (255 * Math.max(0, 1 - fv[2] - fv[3]));
        context.pencil.graphics.setPaint(new Color(rd, gr, bl));
      }
    });

    // setlinewidth
    systemDict.put("setlinewidth", new PACommand() {
      private double minLineWidth(double w, AffineTransform at) {
        double matrix[] = new double[4];
        at.getMatrix(matrix);
        double scale = matrix[0] * matrix[3] - matrix[1] * matrix[2];
        double minlw = .25 / Math.sqrt(Math.abs(scale));
        if (w < minlw) {
          w = minlw;
        }
        return w;
      }

      public void execute(PAContext context) throws PainterException {
        double data[];
        BasicStroke newStroke;
        Stroke oldStroke = context.pencil.graphics.getStroke();
        data = context.popNumberOperands(1);
        data[0] = this.minLineWidth(data[0],
                                    context.pencil.graphics.getTransform());
        if (oldStroke instanceof BasicStroke) {
          newStroke = new BasicStroke( (float) data[0],
                                      ( (BasicStroke) oldStroke).getEndCap(),
                                      ( (BasicStroke) oldStroke).getLineJoin(),
                                      ( (BasicStroke) oldStroke).getMiterLimit(),
                                      ( (BasicStroke) oldStroke).getDashArray(),
                                      ( (BasicStroke) oldStroke).getDashPhase());
        }
        else {
          newStroke = new BasicStroke( (float) data[0], BasicStroke.CAP_ROUND,
                                      BasicStroke.JOIN_ROUND);
        }
        /**
         * @todo two times the same?
         */
        context.pencil.graphics.setStroke(newStroke);
//        context.pencil.state.stroke=newStroke;
      }
    });

    // setlinecap
    systemDict.put("setlinecap", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        BasicStroke newStroke;
        Stroke oldStroke = context.pencil.graphics.getStroke();
        data = context.popNumberOperands(1);
        if (oldStroke instanceof BasicStroke) {
          newStroke = new BasicStroke( ( (BasicStroke) oldStroke).getLineWidth(),
                                      (int) data[0],
                                      ( (BasicStroke) oldStroke).getLineJoin(),
                                      ( (BasicStroke) oldStroke).getMiterLimit(),
                                      ( (BasicStroke) oldStroke).getDashArray(),
                                      ( (BasicStroke) oldStroke).getDashPhase());
        }
        else {
          newStroke = new BasicStroke(1.0f, (int) data[0],
                                      BasicStroke.JOIN_ROUND);
        }
        context.pencil.graphics.setStroke(newStroke);
      }
    });

    // setmiterlimit
    systemDict.put("setmiterlimit", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        BasicStroke newStroke;
        Stroke oldStroke = context.pencil.graphics.getStroke();
        data = context.popNumberOperands(1);
        if (oldStroke instanceof BasicStroke) {
          newStroke = new BasicStroke( ( (BasicStroke) oldStroke).getLineWidth(),
                                      ( (BasicStroke) oldStroke).getEndCap(),
                                      ( (BasicStroke) oldStroke).getLineJoin(),
                                      (float) data[0],
                                      ( (BasicStroke) oldStroke).getDashArray(),
                                      ( (BasicStroke) oldStroke).getDashPhase());
        }
        else {
          newStroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
                                      BasicStroke.JOIN_ROUND, (float) data[0]);
        }
        context.pencil.graphics.setStroke(newStroke);
      }
    });

    // setdash
    systemDict.put("setdash", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        BasicStroke newStroke;
        Stroke oldStroke = context.pencil.graphics.getStroke();
        data = context.popOperands(2);
        if (! (data[0] instanceof ArrayList)) {
          throw new PainterException("setdash: wrong arguments");
        }
        if (! (data[1] instanceof Number)) {
          throw new PainterException("setdash: wrong arguments");
        }

        ArrayList list = (ArrayList) data[0];

        if (list.size() == 0) {
          return;
        }
        float[] dashpattern = new float[list.size()];
        for (int i = 0; i < dashpattern.length; i++) {
          dashpattern[i] = ( (Number) list.get(i)).floatValue();
        }
        float dashoffset = ( (Number) data[1]).floatValue();
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
    });

    // setlinejoin
    systemDict.put("setlinejoin", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        BasicStroke newStroke;
        Stroke oldStroke = context.pencil.graphics.getStroke();
        data = context.popNumberOperands(1);
        if (oldStroke instanceof BasicStroke) {
          newStroke = new BasicStroke( ( (BasicStroke) oldStroke).getLineWidth(),
                                      ( (BasicStroke) oldStroke).getEndCap(),
                                      (int) data[0],
                                      ( (BasicStroke) oldStroke).getMiterLimit(),
                                      ( (BasicStroke) oldStroke).getDashArray(),
                                      ( (BasicStroke) oldStroke).getDashPhase());
        }
        else {
          newStroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, (int) data[0]);
        }
        context.pencil.graphics.setStroke(newStroke);
      }
    });

    // dumpstack
    systemDict.put("dumpstack", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Enumeration enumx = context.operands.elements();
        System.out.println("-------------Stack--------------");
        while (enumx.hasMoreElements()) {
          System.out.println(enumx.nextElement());
        }
        System.out.println("--------------------------------");
      }
    });

    // for
    systemDict.put("for", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;

        data = context.popOperands(4);
        if (! (data[3] instanceof PAToken)) {
          throw new PainterException("for: wrong arguments");
        }
        if (! (data[0] instanceof Number)) {
          throw new PainterException("for: wrong arguments");
        }
        if (! (data[1] instanceof Number)) {
          throw new PainterException("for: wrong arguments");
        }
        if (! (data[2] instanceof Number)) {
          throw new PainterException("for: wrong arguments");
        }
        patoken = (PAToken) data[3];
        if (! (patoken.type == PAToken.PROCEDURE)) {
          throw new PainterException("for: wrong arguments");
        }
        int i0, i1, i2;
        i0 = ( (Number) data[0]).intValue();
        i1 = ( (Number) data[1]).intValue();
        i2 = ( (Number) data[2]).intValue();

        if (i1 > 0) {
          for (int i = i0; i <= i2; i += i1) {
            context.operands.push(new Integer(i));
            context.engine.process(patoken);
          }
        }
        else {
          for (int i = i0; i >= i2; i -= i1) {
            context.operands.push(new Integer(i));
            context.engine.process(patoken);
          }
        }
      }
    });

    // repeat
    systemDict.put("repeat", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;
        data = context.popOperands(2);
        if (! (data[1] instanceof PAToken)) {
          throw new PainterException("repeat: wrong arguments");
        }
        if (! (data[0] instanceof Number)) {
          throw new PainterException("repeat: wrong arguments");
        }
        patoken = (PAToken) data[1];
        if (! (patoken.type == PAToken.PROCEDURE)) {
          throw new PainterException("repeat: wrong arguments");
        }
        int n = ( (Number) data[0]).intValue();
        for (int i = 0; i < n; i++) {
          context.engine.process(patoken);
        }
      }
    });

    // true
    systemDict.put("true", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.operands.push(new Boolean(true));
      }
    });

    // false
    systemDict.put("false", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.operands.push(new Boolean(false));
      }
    });

    // lt
    systemDict.put("lt", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];

        data = context.popOperands(2);
        if (! (data[0] instanceof Number) && ! (data[0] instanceof String)) {
          throw new PainterException("lt: wrong arguments");
        }
        if (data[0] instanceof Number) {
          if (! (data[1] instanceof Number)) {
            throw new PainterException("lt: wrong arguments");
          }
          double d0, d1;
          d0 = ( (Number) data[0]).doubleValue();
          d1 = ( (Number) data[1]).doubleValue();
          if (d0 < d1) {
            context.operands.push(new Boolean(true));
          }
          else {
            context.operands.push(new Boolean(false));
          }
        }
        else {
          if (! (data[1] instanceof String)) {
            throw new PainterException("lt: wrong arguments");
          }
          String s0, s1;
          s0 = (String) data[0];
          s1 = (String) data[1];
          if (s0.compareTo(s1) < 0) {
            context.operands.push(new Boolean(true));
          }
          else {
            context.operands.push(new Boolean(false));
          }
        }
      }
    });

    // gt
    systemDict.put("gt", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];

        data = context.popOperands(2);
        if (! (data[0] instanceof Number) && ! (data[0] instanceof String)) {
          throw new PainterException("gt: wrong arguments");
        }
        if (data[0] instanceof Number) {
          if (! (data[1] instanceof Number)) {
            throw new PainterException("gt: wrong arguments");
          }
          double d0, d1;
          d0 = ( (Number) data[0]).doubleValue();
          d1 = ( (Number) data[1]).doubleValue();
          if (d0 > d1) {
            context.operands.push(new Boolean(true));
          }
          else {
            context.operands.push(new Boolean(false));
          }
        }
        else {
          if (! (data[1] instanceof String)) {
            throw new PainterException("gt: wrong arguments");
          }
          String s0, s1;
          s0 = (String) data[0];
          s1 = (String) data[1];
          if (s0.compareTo(s1) > 0) {
            context.operands.push(new Boolean(true));
          }
          else {
            context.operands.push(new Boolean(false));
          }
        }
      }
    });
    // ge
    systemDict.put("ge", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];

        data = context.popOperands(2);
        if (! (data[0] instanceof Number) && ! (data[0] instanceof String)) {
          throw new PainterException("ge: wrong arguments");
        }
        if (data[0] instanceof Number) {
          if (! (data[1] instanceof Number)) {
            throw new PainterException("ge: wrong arguments");
          }
          double d0, d1;
          d0 = ( (Number) data[0]).doubleValue();
          d1 = ( (Number) data[1]).doubleValue();
          if (d0 >= d1) {
            context.operands.push(new Boolean(true));
          }
          else {
            context.operands.push(new Boolean(false));
          }
        }
        else {
          if (! (data[1] instanceof String)) {
            throw new PainterException("ge: wrong arguments");
          }
          String s0, s1;
          s0 = (String) data[0];
          s1 = (String) data[1];
          if (s0.compareTo(s1) >= 0) {
            context.operands.push(new Boolean(true));
          }
          else {
            context.operands.push(new Boolean(false));
          }
        }
      }
    });
    // ne
    systemDict.put("ne", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];

        data = context.popOperands(2);
        if (! (data[0] instanceof Number) && ! (data[0] instanceof String)) {
          throw new PainterException("ne: wrong arguments");
        }
        if (data[0] instanceof Number) {
          if (! (data[1] instanceof Number)) {
            throw new PainterException("ne: wrong arguments");
          }
          double d0, d1;
          d0 = ( (Number) data[0]).doubleValue();
          d1 = ( (Number) data[1]).doubleValue();
          if (d0 != d1) {
            context.operands.push(new Boolean(true));
          }
          else {
            context.operands.push(new Boolean(false));
          }
        }
        else {
          if (! (data[1] instanceof String)) {
            throw new PainterException("ne: wrong arguments");
          }
          String s0, s1;
          s0 = (String) data[0];
          s1 = (String) data[1];
          if (s0.equals(s1)) {
            context.operands.push(new Boolean(false));
          }
          else {
            context.operands.push(new Boolean(true));
          }
        }
      }
    });

    // eq
    systemDict.put("eq", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];

        data = context.popOperands(2);
        if (! (data[0] instanceof Number) && ! (data[0] instanceof String)) {
          throw new PainterException("eq: wrong arguments");
        }
        if (data[0] instanceof Number) {
          if (! (data[1] instanceof Number)) {
            throw new PainterException("eq: wrong arguments");
          }
          double d0, d1;
          d0 = ( (Number) data[0]).doubleValue();
          d1 = ( (Number) data[1]).doubleValue();
          if (d0 == d1) {
            context.operands.push(new Boolean(true));
          }
          else {
            context.operands.push(new Boolean(false));
          }
        }
        else {
          if (! (data[1] instanceof String)) {
            throw new PainterException("eq: wrong arguments");
          }
          String s0, s1;
          s0 = (String) data[0];
          s1 = (String) data[1];
          if (s0.compareTo(s1) == 0) {
            context.operands.push(new Boolean(true));
          }
          else {
            context.operands.push(new Boolean(false));
          }
        }
      }
    });

    // if
    systemDict.put("if", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;
        data = context.popOperands(2);
        if (! (data[0] instanceof Boolean)) {
          throw new PainterException("if: wrong arguments");
        }
        if (! (data[1] instanceof PAToken)) {
          throw new PainterException("if: wrong arguments");
        }
        patoken = (PAToken) data[1];
        if (! (patoken.type == PAToken.PROCEDURE)) {
          throw new PainterException("if: wrong arguments");
        }
        if ( ( (Boolean) data[0]).booleanValue()) {
          context.engine.process(patoken);
        }
      }
    });

    // ifelse
    systemDict.put("ifelse", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken1, patoken2;
        data = context.popOperands(3);
        if (! (data[0] instanceof Boolean)) {
          throw new PainterException("ifelse: wrong arguments");
        }
        if (! (data[1] instanceof PAToken)) {
          throw new PainterException("ifelse: wrong arguments");
        }
        if (! (data[2] instanceof PAToken)) {
          throw new PainterException("ifelse: wrong arguments");
        }
        patoken1 = (PAToken) data[1];
        patoken2 = (PAToken) data[2];
        if (! (patoken1.type == PAToken.PROCEDURE)) {
          throw new PainterException("ifelse: wrong arguments");
        }
        if (! (patoken2.type == PAToken.PROCEDURE)) {
          throw new PainterException("ifelse: wrong arguments");
        }
        if ( ( (Boolean) data[0]).booleanValue()) {
          context.engine.process(patoken1);
        }
        else {
          context.engine.process(patoken2);
        }
      }
    });

    // dict
    systemDict.put("dict", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        data = context.popNumberOperands(1);
        context.operands.push(new HashMap( (int) data[0]));
      }
    });

    // put
    systemDict.put("put", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;
        data = context.popOperands(3);
        if ( (data[0] instanceof HashMap) && (data[1] instanceof PAToken)) {
          patoken = (PAToken) data[1];
          if (! (patoken.type == PAToken.KEY)) {
            throw new PainterException("put: wrong arguments");
          }
          ( (HashMap) data[0]).put(patoken.value, data[2]);
        }
        else
        if ( (data[0] instanceof ArrayList) && (data[1] instanceof Number)) {
          ArrayList ar = (ArrayList) data[0];
          Number nr = (Number) data[1];
          ar.set(nr.intValue(), data[2]);
        }
        else
        if ( (data[0] instanceof StringBuffer) && (data[1] instanceof Number) &&
            (data[2] instanceof Number)) {
          StringBuffer text = (StringBuffer) data[0];
          Number nr = (Number) data[1];
          Number ch = (Number) data[2];
          text.setCharAt(nr.intValue(), (char) (ch.intValue()));
        }
        else {
          throw new PainterException("put: wrong arguments");
        }
      }
    });

    // get
    systemDict.put("get", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;
        data = context.popOperands(2);
        if (! (data[0] instanceof HashMap) && ! (data[0] instanceof ArrayList)) {
          throw new PainterException("get: wrong arguments");
        }
        if (data[0] instanceof HashMap) {
          if (! (data[1] instanceof PAToken)) {
            throw new PainterException("get: wrong arguments");
          }
          patoken = (PAToken) data[1];
          if (! (patoken.type == PAToken.KEY)) {
            throw new PainterException("get: wrong arguments");
          }
          context.operands.push( ( (HashMap) data[0]).get(patoken.value));
        }
        else if (data[0] instanceof ArrayList) {
          if (! (data[1] instanceof Number)) {
            throw new PainterException("get: wrong arguments");
          }
          context.operands.push( ( (ArrayList) data[0]).get( ( (Number) data[1]).
              intValue()));
        }
      }
    });
    // getinterval
    systemDict.put("getinterval", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;
        data = context.popOperands(3);
        if (! (data[0] instanceof HashMap) && ! (data[0] instanceof ArrayList)) {
          throw new PainterException("getinterval: wrong arguments");
        }
        if (data[0] instanceof HashMap) {
          if (! (data[1] instanceof PAToken)) {
            throw new PainterException("getinterval: wrong arguments");
          }
          patoken = (PAToken) data[1];
          if (! (patoken.type == PAToken.KEY)) {
            throw new PainterException("getinterval: wrong arguments");
          }
          if (! (data[2] instanceof Number)) {
            throw new PainterException("getinterval: wrong arguments");
          }
          context.operands.push( ( (HashMap) data[0]).get(patoken.value));
        }
        else if (data[0] instanceof ArrayList) {
          if (! (data[1] instanceof Number)) {
            throw new PainterException("getinterval: wrong arguments");
          }
          if (! (data[2] instanceof Number)) {
            throw new PainterException("getinterval: wrong arguments");
          }
          ArrayList source = ( (ArrayList) data[0]);
          int from = ( (Number) data[1]).intValue();
          int to = from + ( (Number) data[2]).intValue();
          ArrayList target = new ArrayList(source.subList(from, to));
          context.operands.push(target);
        }
      }
    });
    // load
    systemDict.put("load", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;
        data = context.popOperands(1);
        if (! (data[0] instanceof PAToken)) {
          throw new PainterException("load: wrong arguments");
        }
        patoken = (PAToken) data[0];
        if (! (patoken.type == PAToken.KEY)) {
          throw new PainterException("load: wrong arguments");
        }
        context.operands.push(context.findIdentifier(patoken.value));
      }
    });

    // length
    systemDict.put("length", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;
        int size = 0;
        data = context.popOperands(1);
        if (data[0] instanceof PAToken) {
          patoken = (PAToken) data[0];
          if (! (patoken.type == PAToken.KEY)) {
            throw new PainterException("length: wrong arguments");
          }
          size = ( (String) patoken.value).length();
        }
        else if (data[0] instanceof HashMap) {
          size = ( (HashMap) data[0]).size();
        }
        else if (data[0] instanceof ArrayList) {
          size = ( (ArrayList) data[0]).size();
        }
        else if (data[0] instanceof String) {
          size = ( (String) data[0]).length();
        }
        else {
          throw new PainterException("length: wrong arguments");
        }

        context.operands.push(new Integer(size));
      }
    });

    // begin
    systemDict.put("begin", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        data = context.popOperands(1);
        if (! (data[0] instanceof HashMap)) {
          throw new PainterException("begin: wrong arguments");
        }
        context.dictionaries.push(data[0]);
      }
    });

    // end
    systemDict.put("end", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        try {
          context.dictionaries.pop();
        }
        catch (EmptyStackException e) {
          throw new PainterException("Dictionary stack is empty");
        }
      }
    });

    // undef
    systemDict.put("undef", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;
        data = context.popOperands(2);
        if (! (data[0] instanceof HashMap)) {
          throw new PainterException("undef: wrong arguments");
        }
        if (! (data[1] instanceof PAToken)) {
          throw new PainterException("undef: wrong arguments");
        }
        patoken = (PAToken) data[1];
        if (! (patoken.type == PAToken.KEY)) {
          throw new PainterException("undef: wrong arguments");
        }
        // we don't do an actual undef because we don't care
      }
    });

    // known
    systemDict.put("known", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[], foundObject;
        PAToken patoken;
        data = context.popOperands(1);
        if (! (data[0] instanceof PAToken)) {
          throw new PainterException("known: wrong arguments");
        }
        patoken = (PAToken) data[0];
        if (! (patoken.type == PAToken.KEY)) {
          throw new PainterException("known: wrong arguments");
        }
        foundObject = context.findIdentifier(patoken.value);
        if (foundObject != null) {
          context.operands.push(new Boolean(true));
        }
        else {
          context.operands.push(new Boolean(false));
        }
      }
    });

    // where
    systemDict.put("where", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[], foundObject;
        PAToken patoken;
        data = context.popOperands(1);
        if (! (data[0] instanceof PAToken)) {
          throw new PainterException("where: wrong arguments");
        }
        patoken = (PAToken) data[0];
        if (! (patoken.type == PAToken.KEY)) {
          throw new PainterException("where: wrong arguments");
        }
        foundObject = context.findDictionary(patoken.value);
        if (foundObject != null) {
          context.operands.push(foundObject);
          context.operands.push(new Boolean(true));
        }
        else {
          context.operands.push(new Boolean(false));
        }
      }
    });

    // aload
    systemDict.put("aload", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object[] data;
        java.util.AbstractList list;
        data = context.popOperands(1);
        if (data[0] instanceof PAToken) {
          data[0] = ( (PAToken) data[0]).value;
        }
        if (! (data[0] instanceof java.util.AbstractList)) {
          throw new PainterException("aload: wrong arguments");
        }

        list = (java.util.AbstractList) data[0];
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
          context.operands.push(iterator.next());
        }
        context.operands.push(data[0]);
      }
    });

    // forall
    systemDict.put("forall", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        ArrayList list;
        PAToken patoken;

        data = context.popOperands(2);
        if (! (data[0] instanceof ArrayList)) {
          throw new PainterException("forall: wrong arguments");
        }
        if (! (data[1] instanceof PAToken)) {
          throw new PainterException("forall: wrong arguments");
        }

        patoken = (PAToken) data[1];
        if (! (patoken.type == PAToken.PROCEDURE)) {
          throw new PainterException("forall: wrong arguments");
        }

        list = (ArrayList) data[0];
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
          context.operands.push(iterator.next());
          context.engine.process(patoken);
        }

      }
    });

    // currentflat PENDING(uweh):placeholder for now
    systemDict.put("currentflat", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        PdfGraphics2D pdfg2d = (PdfGraphics2D) context.pencil.graphics;
        PdfContentByte cb = pdfg2d.getContent();
        context.operands.push(new Double(1.0f));
      }
    });

    // setflat PENDING(uweh):placeholder for now
    systemDict.put("setflat", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double[] data;
        data = context.popNumberOperands(1);
        PdfGraphics2D pdfg2d = (PdfGraphics2D) context.pencil.graphics;
        PdfContentByte cb = pdfg2d.getContent();
        cb.setFlatness( ( (float) data[0]));
      }
    });

    // round
    systemDict.put("round", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        data = context.popNumberOperands(1);
        context.operands.push(new Long(Math.round(data[0])));
      }
    });

    // abs
    systemDict.put("abs", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        data = context.popNumberOperands(1);
        context.operands.push(new Double(Math.abs(data[0])));
      }
    });

    // transform
    systemDict.put("transform", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        if (context.peekOperand() instanceof Number) {
          double data[];
          double[] transformedData = new double[2];
          data = context.popNumberOperands(2);
          AffineTransform at = context.pencil.graphics.getTransform();
          at.transform(data, 0, transformedData, 0, 1);
          context.operands.push(new Double(transformedData[0]));
          context.operands.push(new Double(transformedData[1]));
        }
        else {
          Object data[];

          data = context.popOperands(3);
          if (! (data[0] instanceof Number)) {
            throw new PainterException("transform: wrong arguments");
          }
          if (! (data[1] instanceof Number)) {
            throw new PainterException("transform: wrong arguments");
          }
          if (! (data[2] instanceof ArrayList)) {
            throw new PainterException("transform: wrong arguments");
          }

          ArrayList array = (ArrayList) data[2];

          double[] entries = new double[6];

          if (! (array.size() == 6)) {
            throw new PainterException("transform: wrong arguments");
          }

          for (int i = 0; i < 6; i++) {
            entries[i] = ( (Number) array.get(i)).doubleValue();
          }

          AffineTransform at = new AffineTransform(entries);

          double numberdata[] = new double[2];
          numberdata[0] = ( (Number) data[0]).doubleValue();
          numberdata[1] = ( (Number) data[1]).doubleValue();

          double[] transformedData = new double[2];

          at.transform(numberdata, 0, transformedData, 0, 1);
          context.operands.push(new Double(transformedData[0]));
          context.operands.push(new Double(transformedData[1]));
        }
      }
    });

    // itransform
    systemDict.put("itransform", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        if (context.peekOperand() instanceof Number) {
          double data[];
          double[] transformedData = new double[2];
          data = context.popNumberOperands(2);
          AffineTransform at = context.pencil.graphics.getTransform();
          try {
            at.inverseTransform(data, 0, transformedData, 0, 1);
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
    });

    // currentpoint
    // PENDING(uweh): what about CTM, same thing when you construct path
    // this is different than ps, might not work in a few instances
    systemDict.put("currentpoint", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Point2D currentPoint = context.pencil.state.path.getCurrentPoint();
        context.operands.push(new Double(currentPoint.getX()));
        context.operands.push(new Double(currentPoint.getY()));
      }
    });

    // clippath
    systemDict.put("clippath", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.pencil.clippath();
      }
    });

    // matrix
    systemDict.put("matrix", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        ArrayList identityMatrix = new ArrayList(6);

        identityMatrix.add(new Double(1.0d));
        identityMatrix.add(new Double(0.0d));
        identityMatrix.add(new Double(0.0d));
        identityMatrix.add(new Double(1.0d));
        identityMatrix.add(new Double(0.0d));
        identityMatrix.add(new Double(0.0d));
        context.operands.push(identityMatrix);
      }
    });

    // concatmatrix
    systemDict.put("concatmatrix", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        data = context.popOperands(3);
        if (! (data[0] instanceof ArrayList)) {
          throw new PainterException("concatmatrix: wrong arguments");
        }
        if (! (data[1] instanceof ArrayList)) {
          throw new PainterException("concatmatrix: wrong arguments");
        }
        if (! (data[2] instanceof ArrayList)) {
          throw new PainterException("concatmatrix: wrong arguments");
        }
        ArrayList arrayOne, arrayTwo, arrayThree;
        AffineTransform atOne, atTwo;

        arrayOne = (ArrayList) data[0];
        arrayTwo = (ArrayList) data[1];
        arrayThree = (ArrayList) data[2];

        double[] entries = new double[6];

        if (! (arrayOne.size() == 6)) {
          throw new PainterException("concatmatrix: wrong arguments");
        }
        if (! (arrayTwo.size() == 6)) {
          throw new PainterException("concatmatrix: wrong arguments");
        }
        if (! (arrayThree.size() == 6)) {
          throw new PainterException("concatmatrix: wrong arguments");
        }

        for (int i = 0; i < 6; i++) {
          entries[i] = ( (Number) arrayOne.get(i)).doubleValue();
        }
        atOne = new AffineTransform(entries);
        for (int i = 0; i < 6; i++) {
          entries[i] = ( (Number) arrayTwo.get(i)).doubleValue();
        }
        atTwo = new AffineTransform(entries);

        atOne.concatenate(atTwo);

        atOne.getMatrix(entries);
        for (int i = 0; i < 6; i++) {
          arrayThree.set(i, new Double(entries[i]));
        }
        context.operands.push(arrayThree);
      }
    });

    // pathbbox
    systemDict.put("pathbbox", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Rectangle2D pathBounds = context.pencil.state.path.getBounds2D();

        context.operands.push(new Double(pathBounds.getMinX()));
        context.operands.push(new Double(pathBounds.getMinY()));
        context.operands.push(new Double(pathBounds.getMaxX()));
        context.operands.push(new Double(pathBounds.getMaxY()));
      }
    });

    // initmatrix
    systemDict.put("initmatrix", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        PdfGraphics2D pdfg2d = (PdfGraphics2D) context.pencil.graphics;
        PdfContentByte cb = pdfg2d.getContent();
//       cb.transform(Affine);
      }
    });
    // initclip
    systemDict.put("initclip", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        PdfGraphics2D pdfg2d = (PdfGraphics2D) context.pencil.graphics;
        PdfContentByte cb = pdfg2d.getContent();
        context.pencil.clippath();
//       pdfg2d.setClip(context.);
//    if(!PAContext.IgnoreUnknownCommands)
//    throw new UnsupportedOperationException("initclip");
      }
    });

    // truncate
    systemDict.put("truncate", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];
        double truncated;

        data = context.popNumberOperands(1);
        if (data[0] < 0) {
          truncated = Math.ceil(data[0]);
        }
        else {
          truncated = Math.floor(data[0]);
        }
        context.operands.push(new Double(truncated));
      }
    });

    // rand
    systemDict.put("rand", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.operands.push(new Integer(Math.abs(randomNumberGenerator.
            nextInt( (1 << 31) - 1))));
      }
    });

    // srand
    systemDict.put("srand", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(1);
        randomNumberGenerator = new Random(Math.round(data[0]));
      }
    });
    // version
    systemDict.put("version", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.operands.push("2016");
      }
    });
    // cvi
    systemDict.put("cvi", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];

        data = context.popOperands(1);
        if (! (data[0] instanceof Number) && ! (data[0] instanceof String)) {
          throw new PainterException("cvi: wrong arguments");
        }
        if (data[0] instanceof Number) {
          int d;

          d = ( (Number) data[0]).intValue();
          context.operands.push(new Integer(d));
        }
        else {
          String s;
          s = (String) data[0];

          context.operands.push(new Integer(s));
        }
      }
    });
    // cvr
    systemDict.put("cvr", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];

        data = context.popOperands(1);
        if (! (data[0] instanceof Number) && ! (data[0] instanceof String)) {
          throw new PainterException("cvr: wrong arguments");
        }
        if (data[0] instanceof Number) {
          int d;

          d = ( (Number) data[0]).intValue();
          context.operands.push(new Double(d));
        }
        else {
          String s;
          s = (String) data[0];

          context.operands.push(new Double(s));
        }
      }
    });
    // usertime
    systemDict.put("usertime", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.operands.push(new Long(System.currentTimeMillis()));
      }
    });
// save
    systemDict.put("save", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        PdfGraphics2D pdfg2d = (PdfGraphics2D) context.pencil.graphics;
        PdfContentByte cb = pdfg2d.getContent();
        cb.saveState();
        context.operands.push(new Long(0));
      }
    });
// restore
    systemDict.put("restore", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        PdfGraphics2D pdfg2d = (PdfGraphics2D) context.pencil.graphics;
        PdfContentByte cb = pdfg2d.getContent();
        cb.restoreState();
        Object data[];
        data = context.popOperands(1);
      }
    });
// clear
    systemDict.put("clear", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.operands.clear();
      }
    });
    // readonly
    systemDict.put("readonly", new PACommand() {
      public void execute(PAContext context) throws PainterException {
      }
    });

// currentfile
    systemDict.put("currentfile", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        final JavaCharStream jcs=context.poorscript.jj_input_stream;
        InputStream ins=new InputStream(){
          /**
           * Reads the next byte of data from the input stream.
           *
           * @return the next byte of data, or <code>-1</code> if the end of the stream is reached.
           * @throws IOException if an I/O error occurs.
           * @todo Implement this java.io.InputStream method
           */
          public int read() throws IOException {
            return jcs.readChar();
          }

        };
        context.operands.push(ins);
      }
    });
    // flushfile
    systemDict.put("flushfile", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        data = context.popOperands(1);
        if (! (data[0] instanceof InputStream)) {
          throw new PainterException("flushfile: wrong arguments");
        }

        InputStream is = (InputStream) data[0];
        try {
          while (is.read() != -1) {
          }
        }
        catch (IOException ex) {
        }
      }
    });

    // closefile
    systemDict.put("closefile", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        data = context.popOperands(1);
        if (! (data[0] instanceof InputStream)) {
          throw new PainterException("closefile: wrong arguments");
        }

        InputStream is = (InputStream) data[0];
        try {
          is.close();
        }
        catch (IOException ex) {
        }
      }
    });

    // string
    systemDict.put("string", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        data = context.popOperands(1);
        if (! (data[0] instanceof Number)) {
          throw new PainterException("string: wrong arguments");
        }
        int d;
        d = ( (Number) data[0]).intValue();
        StringBuffer sb = new StringBuffer(d);
        sb.setLength(d);
        context.operands.push(sb);
      }
    });
    // null
    systemDict.put("null", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.operands.push(null);
      }
    });
    // currentscreen
    systemDict.put("currentscreen", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        if (!PAContext.IgnoreUnknownCommands) {
          throw new UnsupportedOperationException("currentscreen");
        }
        else {
          context.operands.push(new Double(60));
          context.operands.push(new Double(0));
          context.operands.push(new Double(0));
        }
      }
    });
    // setscreen
    systemDict.put("setscreen", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        data = context.popOperands(3);

//  if (!PAContext.IgnoreUnknownCommands)
//    throw new UnsupportedOperationException("setscreen");
//  else {
//
//  }
      }
    });

    // flattenpath
    systemDict.put("flattenpath", new PACommand() {
      public void execute(PAContext context) throws PainterException {

      }
    });
    // filter
    systemDict.put("filter", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        String filtername;
        filtername = (String) ( (PAToken) context.popOperands(1)[0]).value;
        Object obj;
        while (! ( (obj = context.peekOperand()) instanceof InputStream)) {
          Object param = context.popOperands(1);
        }

        InputStream datasrc;
        datasrc = (InputStream) (context.popOperands(1)[0]);

        InputStream dis;
        if (filtername.equals("ASCIIHexDecode")) {
          //          dis = new ASCIIHexInputStream(datasrc);
          final InputStream is=datasrc;
          dis=new InputStream(){

            /**
             * Reads the next byte of data from the input stream.
             *
             * @return the next byte of data, or <code>-1</code> if the end of the stream is reached.
             * @throws IOException if an I/O error occurs.
             * @todo Implement this java.io.InputStream method
             */
            public int read() throws IOException {
              int firstchar,secondchar;
              for(;;){
                firstchar=is.read();
                if(firstchar==-1)return -1;
                if(firstchar=='>')return -1;
                if(firstchar=='\n')continue;
                if(firstchar=='\r')continue;
                break;
              }
              for(;;){
                secondchar=is.read();
                if(secondchar=='>')return -1;
                if(secondchar==-1)return -1;
                if(secondchar=='\n')continue;
                if(secondchar=='\r')continue;
                break;
              }
              int highbyte=0;
              if(firstchar>=48&&firstchar<=57)highbyte=firstchar-48;
              if(firstchar>=65&&firstchar<=70)highbyte=firstchar-55;
              int lowbyte=0;
              if(secondchar>=48&&secondchar<=57)lowbyte=secondchar-48;
              if(secondchar>=65&&secondchar<=70)lowbyte=secondchar-55;

              return(highbyte*16+lowbyte);
            }
          };
        }
//        else
//        if (filtername.equals("DCTDecode")) {
//          dis = new DCTInputStream(datasrc);
//        }
        else {
          dis = datasrc;
        }

        context.operands.push(dis);
      }
    });
    // clip
    systemDict.put("clip", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.pencil.clip();
      }
    });
    // setcolorspace
    systemDict.put("setcolorspace", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        PdfGraphics2D pdfg2d = (PdfGraphics2D) context.pencil.graphics;
        PdfContentByte cb = pdfg2d.getContent();
        Object data[];
        data = context.popOperands(1);
        if (data[0] instanceof PAToken) {
          String colorspace = ( (String) ( (PAToken) data[0]).value);
          cb.setDefaultColorspace(PdfName.COLORSPACE, PdfName.DEVICERGB);

        }
      }
    });
    // image
    systemDict.put("image", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        PdfGraphics2D pdfg2d = (PdfGraphics2D) context.pencil.graphics;
        PdfContentByte cb = pdfg2d.getContent();
        Object data[];
        data = context.popOperands(1);
        if (data[0] instanceof Number) {
          /**
           * Level1 image
           */
          int width = ( (Number) data[0]).intValue();
          data = context.popOperands(4);
          int height = ( (Number) data[0]).intValue();
          int bits = ( (Number) data[1]).intValue();

        }else if (data[0] instanceof PAToken) {
          PAToken proc = (PAToken) data[0];

          data = context.popOperands(4);
           int width = ( (Number) data[0]).intValue();
          int height = ( (Number) data[1]).intValue();
          int bitspercomponent = ( (Number) data[2]).intValue();
          ArrayList ar = (ArrayList) data[3];
          System.out.println("I " + width + "*" + height + " " +
                                        bitspercomponent  + " " + ar);

//                     context.engine.process(proc);
        }
        else if (data[0] instanceof HashMap){
          HashMap hsm = (HashMap) data[0];
          Iterator it = hsm.keySet().iterator();
          int width = 0, height = 0, bitspercomponent = 0;
          int imagetype = 0;
          InputStream datasrc = null;
          Object decode = null;
          Object imagematrix = null;
          while (it.hasNext()) {
            PAToken token = (PAToken) it.next();
            if (token.value.toString().equals("ImageType")) {
              imagetype = ( (Number) hsm.get(token)).intValue();
            }
            if (token.value.toString().equals("DataSource")) {
              datasrc = (InputStream) hsm.get(token);
            }

            if (token.value.toString().equals("BitsPerComponent")) {
              bitspercomponent = ( (Number) hsm.get(token)).intValue();
            }
            if (token.value.toString().equals("Width")) {
              width = ( (Number) hsm.get(token)).intValue();
            }
            if (token.value.toString().equals("Height")) {
              height = ( (Number) hsm.get(token)).intValue();
            }
            if (token.value.toString().equals("Decode")) {
              decode = ( (Object) hsm.get(token));
            }
            if (token.value.toString().equals("ImageMatrix")) {
              imagematrix = ( (Object) hsm.get(token));
            }
          }

          try {
            byte[] barr = {};
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int aByte;
          while ( (aByte = datasrc.read()) >= 0) {
              bout.write(aByte);
//              System.out.print((char)aByte);
            }
            System.out.println("I " + width + "*" + height + " " +
                               bitspercomponent + " " + imagetype + " " +
                               decode + " " + imagematrix + " " + datasrc);
            barr = bout.toByteArray();
//            com.lowagie.text.Image img = new ImgRaw(width, height, 1,
//                bitspercomponent, barr);
            com.lowagie.text.Image img = new Jpeg(barr);
            try {
              cb.addImage(img,width,0,0,height,0,0);
            }
            catch (DocumentException ex1) {
              ex1.printStackTrace();
            }
          }
          catch (IOException ex) {
            ex.printStackTrace();
          }
          catch (BadElementException ex) {
            ex.printStackTrace();
          }

        }
      }
    });

    // imagemask
   systemDict.put("imagemask", new PACommand() {
     public void execute(PAContext context) throws PainterException {
       Object data[];
       data = context.popOperands(5);
//       if (data[0] instanceof PAToken) {
//         PAToken token = (PAToken) data[0];
//         context.engine.process(token);
//       }
     }
   });


    // exec
    systemDict.put("exec", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        data = context.popOperands(1);
        if (data[0] instanceof PAToken) {
          PAToken token = (PAToken) data[0];
          context.engine.process(token);
        }
      }
    });
    // currentdict
    systemDict.put("currentdict", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.operands.push(context.dictionaries.peek());
      }
    });

// cleardictstack
    systemDict.put("cleardictstack", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.dictionaries.clear();
        HashMap systemDict = context.constructSystemDict();
        context.dictionaries.push(systemDict);
        HashMap globalDict = context.constructGlobalDict();
        context.dictionaries.push(globalDict);
        HashMap userDict = context.constructUserDict();
        systemDict.put("userdict", userDict);
        systemDict.put("globaldict", globalDict);
        context.dictionaries.push(userDict);
      }
    });

    // charpath
    systemDict.put("charpath", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];

        data = context.popOperands(2);
        if (! (data[0] instanceof String)) {
          throw new PainterException("charpath: wrong arguments");
        }
        if (! (data[1] instanceof Boolean)) {
          throw new PainterException("charpath: wrong arguments");
        }

        context.pencil.charpath( (String) data[0],
                                ( (Boolean) data[1]).booleanValue());
      }
    });

    // PENDING(uweh): we only support procedure right now and always push false on the stack
    // stopped
    systemDict.put("stopped", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;
        data = context.popOperands(1);
        if (! (data[0] instanceof PAToken)) {
          throw new PainterException("stopped: wrong arguments");
        }

        patoken = (PAToken) data[0];
        if (! (patoken.type == PAToken.PROCEDURE)) {
          throw new PainterException("stopped: wrong arguments");
        }
        context.engine.process(patoken);
        context.operands.push(new Boolean(false));
      }
    });
    systemDict.put("systemdict", systemDict);
    return systemDict;
  }

}
