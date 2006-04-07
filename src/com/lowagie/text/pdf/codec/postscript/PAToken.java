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

public class PAToken {

  static public final int IDENTIFIER = 0;
  static public final int KEY = 1;
  static public final int PROCEDURE = 2;
  static public final int MARK = 3;
  static public final int START_PROCEDURE = 4;
  static public final int END_PROCEDURE = 5;
  static public final int IMMEDIATE = 6;
  static public final int START_ARRAY = 7;
  static public final int END_ARRAY = 8;
  static public final int START_DICT = 9;
  static public final int END_DICT = 10;

  public Object value;
  public int type;

  public PAToken(Object value, int type) {
    super();
    this.value = value;
    this.type = type;
  }

  public String toString() {
    switch (this.type) {
      case IDENTIFIER:
        return "IDENTIFIER " + this.value.toString();
      case KEY:
        return "KEY " + this.value.toString();
      case PROCEDURE:
        return "PROCEDURE " + this.value.toString();
      case MARK:
        return "MARK";
      case START_PROCEDURE:
        return "START_PROCEDURE";
      case END_PROCEDURE:
        return "END_PROCEDURE";
      case IMMEDIATE:
        return "IMMEDIATE " + this.value.toString();
      case START_ARRAY:
        return "START_ARRAY";
      case END_ARRAY:
        return "END_ARRAY";
      case START_DICT:
        return "START_DICT";
      case END_DICT:
        return "END_DICT";
    }
    return this.value.toString();
  }

}
