/*
 * Copyright 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.itextpdf.text.pdf.qrcode;

import java.util.HashMap;

/**
 * Encapsulates a Character Set ECI, according to "Extended Channel Interpretations" 5.3.1.1
 * of ISO 18004.
 *
 * @author Sean Owen
 */
public final class CharacterSetECI extends ECI {

  private static HashMap<Integer,CharacterSetECI> VALUE_TO_ECI;
  private static HashMap<String,CharacterSetECI> NAME_TO_ECI;

  private static void initialize() {
    VALUE_TO_ECI = new HashMap<Integer, CharacterSetECI>(29);
    NAME_TO_ECI = new HashMap<String, CharacterSetECI>(29);
    // TODO figure out if these values are even right!
    addCharacterSet(0, "Cp437");
    addCharacterSet(1, new String[] {"ISO8859_1", "ISO-8859-1"});
    addCharacterSet(2, "Cp437");
    addCharacterSet(3, new String[] {"ISO8859_1", "ISO-8859-1"});
    addCharacterSet(4, new String[] {"ISO8859_2", "ISO-8859-2"});
    addCharacterSet(5, new String[] {"ISO8859_3", "ISO-8859-3"});
    addCharacterSet(6, new String[] {"ISO8859_4", "ISO-8859-4"});
    addCharacterSet(7, new String[] {"ISO8859_5", "ISO-8859-5"});
    addCharacterSet(8, new String[] {"ISO8859_6", "ISO-8859-6"});
    addCharacterSet(9, new String[] {"ISO8859_7", "ISO-8859-7"});
    addCharacterSet(10, new String[] {"ISO8859_8", "ISO-8859-8"});
    addCharacterSet(11, new String[] {"ISO8859_9", "ISO-8859-9"});
    addCharacterSet(12, new String[] {"ISO8859_10", "ISO-8859-10"});
    addCharacterSet(13, new String[] {"ISO8859_11", "ISO-8859-11"});
    addCharacterSet(15, new String[] {"ISO8859_13", "ISO-8859-13"});
    addCharacterSet(16, new String[] {"ISO8859_14", "ISO-8859-14"});
    addCharacterSet(17, new String[] {"ISO8859_15", "ISO-8859-15"});
    addCharacterSet(18, new String[] {"ISO8859_16", "ISO-8859-16"});
    addCharacterSet(20, new String[] {"SJIS", "Shift_JIS"});
  }

  private final String encodingName;

  private CharacterSetECI(int value, String encodingName) {
    super(value);
    this.encodingName = encodingName;
  }

  public String getEncodingName() {
    return encodingName;
  }

  private static void addCharacterSet(int value, String encodingName) {
    CharacterSetECI eci = new CharacterSetECI(value, encodingName);
    VALUE_TO_ECI.put(new Integer(value), eci); // can't use valueOf
    NAME_TO_ECI.put(encodingName, eci);
  }

  private static void addCharacterSet(int value, String[] encodingNames) {
    CharacterSetECI eci = new CharacterSetECI(value, encodingNames[0]);
    VALUE_TO_ECI.put(new Integer(value), eci); // can't use valueOf
    for (int i = 0; i < encodingNames.length; i++) {
      NAME_TO_ECI.put(encodingNames[i], eci);
    }
  }

  /**
   * @param value character set ECI value
   * @return {@link CharacterSetECI} representing ECI of given value, or null if it is legal but
   *   unsupported
   * @throws IllegalArgumentException if ECI value is invalid
   */
  public static CharacterSetECI getCharacterSetECIByValue(int value) {
    if (VALUE_TO_ECI == null) {
      initialize();
    }
    if (value < 0 || value >= 900) {
      throw new IllegalArgumentException("Bad ECI value: " + value);
    }
    return VALUE_TO_ECI.get(new Integer(value));
  }

  /**
   * @param name character set ECI encoding name
   * @return {@link CharacterSetECI} representing ECI for character encoding, or null if it is legal
   *   but unsupported
   */
  public static CharacterSetECI getCharacterSetECIByName(String name) {
    if (NAME_TO_ECI == null) {
      initialize();
    }
    return NAME_TO_ECI.get(name);
  }

}