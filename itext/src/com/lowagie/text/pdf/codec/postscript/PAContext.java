/*
 * Copyright 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package com.lowagie.text.pdf.codec.postscript;

import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*;

public class PAContext
    extends Object {

  public PAPencil pencil;
  public Stack dictionaries;
  public Stack operands;
  public PAEngine engine;
  PAParser poorscript = null;
  protected Random randomNumberGenerator;

  protected Object lastUnknownIdentifier;
  public static boolean IgnoreUnknownCommands=true;

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
    this.dictionaries.push(this.constructSystemDict());
    this.dictionaries.push(this.constructGlobalDict());
    this.dictionaries.push(new HashMap());
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
        poorscript = new PAParser(PAContext.class.getResourceAsStream("init.ps"));
        poorscript.parse(this);
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
        throw new PainterException("Operand stack is empty");
      }
      if (objectValue instanceof Number) {
        doubleValue = ( (Number) objectValue).doubleValue();
      }
      else {
        throw new PainterException("Number expected on operand stack");
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
        throw new PainterException("Operand stack is empty");
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
      throw new PainterException("Operand stack is empty");
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
        throw new PainterException("Operand stack is empty");
      }
      result.set(j - i - 1, objectValue);
    }
    try {
      this.operands.pop(); // the start array mark itself
    }
    catch (EmptyStackException e) {
      throw new PainterException("Operand stack is empty");
    }
    this.operands.push(result);
  }

  protected HashMap constructGlobalDict() {
    HashMap globalDict = new HashMap();

    return globalDict;
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
        float[] result;
        Font font;

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

    // scalefont
    systemDict.put("scalefont", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        data = context.popOperands(2);
        if (! (data[0] instanceof Font)) {
          throw new PainterException("scalefont: wrong arguments");
        }
        if (! (data[1] instanceof Number)) {
          throw new PainterException("scalefont: wrong arguments");
        }
        context.operands.push( ( (Font) data[0]).deriveFont( ( (Number) data[1]).
            floatValue()));
      }
    });

    // setfont
    systemDict.put("setfont", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        data = context.popOperands(1);
        if (! (data[0] instanceof Font)) {
          throw new PainterException("setfont: wrong arguments");
        }
        context.pencil.graphics.setFont( (Font) data[0]);
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
          throw new PainterException("bind: wrong arguments");
        }
        patoken = (PAToken) data[0];
        if (! (patoken.type == PAToken.PROCEDURE)) {
          throw new PainterException("bind: wrong arguments");
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
        context.pencil.graphics.setStroke(newStroke);
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
        context.operands.push(new HashMap());
      }
    });

    // userdict
    systemDict.put("userdict", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(1);
        context.operands.push(new HashMap());
      }
    });
    // put
    systemDict.put("put", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];
        PAToken patoken;
        data = context.popOperands(3);
        if (! (data[0] instanceof HashMap)) {
          throw new PainterException("put: wrong arguments");
        }
        if (! (data[1] instanceof PAToken)) {
          throw new PainterException("put: wrong arguments");
        }
        patoken = (PAToken) data[1];
        if (! (patoken.type == PAToken.KEY)) {
          throw new PainterException("put: wrong arguments");
        }
        ( (HashMap) data[0]).put(patoken.value, data[2]);
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
        Object data[];
        ArrayList list;
        data = context.popOperands(1);
        if (! (data[0] instanceof ArrayList)) {
          throw new PainterException("aload: wrong arguments");
        }
        list = (ArrayList) data[0];
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
        if(!PAContext.IgnoreUnknownCommands)
        context.operands.push(new Double(1.0f));
      }
    });

    // setflat PENDING(uweh):placeholder for now
    systemDict.put("setflat", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        double data[];

        data = context.popNumberOperands(1);
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
        if(!PAContext.IgnoreUnknownCommands)
        throw new UnsupportedOperationException("initmatrix");
      }
    });
    // initclip
systemDict.put("initclip", new PACommand() {
  public void execute(PAContext context) throws PainterException {
    if(!PAContext.IgnoreUnknownCommands)
    throw new UnsupportedOperationException("initclip");
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

//        context.operands.push(new Integer(randomNumberGenerator.nextInt(231)));
        context.operands.push(new Integer(Math.abs(randomNumberGenerator.nextInt((1<<31)-1))));
//        Math.abs(random.nextInt())
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
         context.pencil.gsave(); // Wrong! but at the moment not there..
//        if(!PAContext.IgnoreUnknownCommands)
//        throw new UnsupportedOperationException("save");
      }
    });
// restore
    systemDict.put("restore", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        context.pencil.grestore(); // Wrong! but at the moment not there..

//        if(!PAContext.IgnoreUnknownCommands)
//        throw new UnsupportedOperationException("restore");
      }
    });
// clear
    systemDict.put("clear", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        if(!PAContext.IgnoreUnknownCommands)
        throw new UnsupportedOperationException("clear");
      }
    });
// currentfile
    systemDict.put("currentfile", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        if(!PAContext.IgnoreUnknownCommands)
        throw new UnsupportedOperationException("currentfile");
      }
    });
    // filter
        systemDict.put("filter", new PACommand() {
          public void execute(PAContext context) throws PainterException {
            if(!PAContext.IgnoreUnknownCommands)
            throw new UnsupportedOperationException("filter");
          }
    });
    // string
    systemDict.put("string", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        Object data[];

  data = context.popOperands(1);
  if (! (data[0] instanceof Number) ) {
    throw new PainterException("string: wrong arguments");
  }

    int d;

    d = ( (Number) data[0]).intValue();
    context.operands.push(new String());
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
       if (!PAContext.IgnoreUnknownCommands)
         throw new UnsupportedOperationException("systemdict");
       else {
         context.operands.push(new Double(60));
         context.operands.push(new Double(0));
         context.operands.push(new Double(0));
       }
     }
});

    // systemdict
    systemDict.put("systemdict", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        if(!PAContext.IgnoreUnknownCommands)
        throw new UnsupportedOperationException("systemdict");
      }
});
    // statusdict
    systemDict.put("statusdict", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        if(!PAContext.IgnoreUnknownCommands)
        throw new UnsupportedOperationException("statusdict");
      }
});

// cleardictstack
    systemDict.put("cleardictstack", new PACommand() {
      public void execute(PAContext context) throws PainterException {
        if(!PAContext.IgnoreUnknownCommands)
        throw new UnsupportedOperationException("cleardictstack");
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

    return systemDict;
  }

}
