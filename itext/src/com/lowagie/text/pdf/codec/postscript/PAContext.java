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

import java.io.*;
import java.util.*;

import java.awt.*;
import javax.swing.*;

import com.lowagie.text.pdf.codec.postscript.commands.*;



public class PAContext {

  public PAPencil pencil;
  public Stack dictionaries;
  public Stack operands;
  public PAEngine engine;
  public PAParser poorscript = null;
  public Random randomNumberGenerator;
  InputStream is=null;

  protected Object lastUnknownIdentifier;
  public static boolean IgnoreUnknownCommands = false;
  public static boolean DebugExecution = false;
  public static boolean DebugCommandleveltrace = false;

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
 public JavaCharStream getJj_input_stream(){
   return poorscript.jj_input_stream;
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

  public Number[] popNumberOperands(int n) throws PainterException {
    Number[] result = new Number[n];
    Object objectValue;
    Number doubleValue;

    for (int i = n - 1; i >= 0; i--) {
      try {
        objectValue = this.operands.pop();
      }
      catch (EmptyStackException e) {
        throw new PainterException("Operand stack is empty poping " + n +
                                   " number operands");
      }
      if (objectValue instanceof Number) {
        doubleValue = ( (Number) objectValue);
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

  public HashMap constructGlobalDict() {
    HashMap globalDict = new HashMap();
    return globalDict;
  }

  public HashMap constructUserDict() {
    HashMap userDict = new LimitedHashMap(200);
    return userDict;
  }

  public static void main(String[] args) {
    javax.swing.JFrame jf = new javax.swing.JFrame();
    jf.setVisible(true);
    jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    PAContext pac = new PAContext(new PAPencil(jf));
    HashMap hm = (HashMap) pac.findDictionary("systemdict");
    Iterator it = hm.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      String obname = entry.getKey().toString();
      Object ob = entry.getValue();
      String typname = ob.getClass().getName();
      System.out.println(obname + ":" + typname);
    }
    System.exit(0);
  }

  public HashMap constructSystemDict() {
    HashMap systemDict = new HashMap();
    // math
    systemDict.put("mul", new mul());
    systemDict.put("div", new div());
    systemDict.put("mod", new mod());
    systemDict.put("add", new add());
    systemDict.put("neg", new neg());
    systemDict.put("ceiling", new ceiling());
    systemDict.put("sub", new sub());
    systemDict.put("atan", new atan());
    systemDict.put("sin", new sin());
    systemDict.put("cos", new cos());
    systemDict.put("sqrt", new sqrt());
    systemDict.put("log", new log());
    systemDict.put("exp", new exp());
    systemDict.put("round", new round());
    systemDict.put("abs", new abs());
    systemDict.put("rand", new rand());
    systemDict.put("srand", new srand());
    systemDict.put("truncate", new truncate());
    // draw
    systemDict.put("newpath", new newpath());
    systemDict.put("moveto", new moveto());
    systemDict.put("rmoveto", new rmoveto());
    systemDict.put("lineto", new lineto());
    systemDict.put("rlineto", new rlineto());
    systemDict.put("arc", new arc());
    systemDict.put("arcn", new arcn());
    systemDict.put("curveto", new curveto());
    systemDict.put("rcurveto", new rcurveto());
    systemDict.put("closepath", new closepath());
    // gfx state
    systemDict.put("gsave", new gsave());
    systemDict.put("grestore", new grestore());
    systemDict.put("save", new save());
    systemDict.put("restore", new restore());
    systemDict.put("translate", new translate());
    systemDict.put("rotate", new rotate());
    systemDict.put("scale", new scale());
    systemDict.put("currentmatrix", new currentmatrix());
    systemDict.put("setmatrix", new setmatrix());
    systemDict.put("stroke", new stroke());
    systemDict.put("fill", new fill());
    systemDict.put("eofill", new eofill());
    systemDict.put("show", new show());
    systemDict.put("setlinewidth", new setlinewidth());
    systemDict.put("setlinecap", new setlinecap());
    systemDict.put("setmiterlimit", new setmiterlimit());
    systemDict.put("setdash", new setdash());
    systemDict.put("setlinejoin", new setlinejoin());
    systemDict.put("currentflat", new currentflat());
    systemDict.put("setflat", new setflat());
    systemDict.put("transform", new transform());
    systemDict.put("itransform", new itransform());
    systemDict.put("currentpoint", new currentpoint());
    systemDict.put("clippath", new clippath());
    systemDict.put("matrix", new matrix());
    systemDict.put("concatmatrix", new concatmatrix());
    systemDict.put("pathbbox", new pathbbox());
    systemDict.put("initmatrix", new initmatrix());
    systemDict.put("initclip", new initclip());
    // string
    systemDict.put("stringwidth", new stringwidth());
    systemDict.put("string", new string_psop());
    systemDict.put("cvx", new cvx());
    systemDict.put("cvi", new cvi());
    systemDict.put("cvr", new cvr());
    // font
    systemDict.put("findfont", new findfont());
    systemDict.put("makefont", new makefont());
    systemDict.put("scalefont", new scalefont());
    systemDict.put("setfont", new setfont());
    // stack handling
    systemDict.put("def", new def());
    systemDict.put("bind", new bind());
    systemDict.put("exch", new exch());
    systemDict.put("dup", new dup());
    systemDict.put("roll", new roll());
    systemDict.put("pop", new pop());
    systemDict.put("index", new index());
    systemDict.put("mark", new mark());
    systemDict.put("cleartomark", new cleartomark());
    systemDict.put("copy", new copy());
    systemDict.put("dumpstack", new dumpstack());
    // color
    systemDict.put("setgray", new setgray());
    systemDict.put("setrgbcolor", new setrgbcolor());
    systemDict.put("currentrgbcolor", new currentrgbcolor());
    systemDict.put("sethsbcolor", new sethsbcolor());
    systemDict.put("setcmykcolor", new setcmybcolor());
    // general
    systemDict.put("for", new for_psop());
    systemDict.put("repeat", new repeat_psop());
    systemDict.put("true", new true_psop());
    systemDict.put("false", new false_psop());
    systemDict.put("lt", new lt());
    systemDict.put("gt", new gt());
    systemDict.put("ge", new ge());
    systemDict.put("ne", new ne());
    systemDict.put("eq", new eq());
    systemDict.put("if", new if_psop());
    systemDict.put("ifelse", new ifelse_psop());
    systemDict.put("dict", new dict());
    systemDict.put("put", new put());
    systemDict.put("get", new get());
    systemDict.put("getinterval", new getinterval());
    systemDict.put("load", new load());
    systemDict.put("length", new length());
    systemDict.put("begin", new begin());
    systemDict.put("end", new end());
    systemDict.put("undef", new undef());
    systemDict.put("known", new known());
    systemDict.put("where", new where());
    systemDict.put("aload", new aload());
    systemDict.put("forall", new forall_psop());
    systemDict.put("showpage", new showpage());
    systemDict.put("version", new version());
    systemDict.put("usertime", new usertime());
    systemDict.put("clear", new clear());
    systemDict.put("readonly", new readonly());
    systemDict.put("currentfile", new currentfile());
    systemDict.put("flushfile", new flushfile());
    systemDict.put("closefile", new closefile());
    systemDict.put("null", new null_psop());
    systemDict.put("currentscreen", new currentscreen());
    systemDict.put("setscreen", new setscreen());
    systemDict.put("flattenpath", new flattenpath());
    systemDict.put("filter", new filter());
    systemDict.put("clip", new clip());
    systemDict.put("setcolorspace", new setcolorspace());
    systemDict.put("image", new image());
    systemDict.put("imagemask", new imagemask());
    systemDict.put("exec", new exec());
    systemDict.put("currentdict", new currentdict());
    systemDict.put("cleardictstack", new cleardictstack());
    systemDict.put("charpath", new charpath());
    systemDict.put("stopped", new stopped());
    systemDict.put("systemdict", systemDict);
    return systemDict;
  }

}
