/*
 * $Id$
 * Copyright (C) 2001 The Apache Software Foundation. All rights reserved.
 * For details on use and redistribution please refer to the
 * LICENSE file included with these sources.
 */

package com.lowagie.text.pdf.hyphenation;

import java.io.*;
import java.util.Hashtable;

/**
 * This class is the main entry point to the hyphenation package.
 * You can use only the static methods or create an instance.
 *
 * @author Carlos Villegas <cav@uniscope.co.jp>
 */
public class Hyphenator {
    static Hashtable hyphenTrees = new Hashtable();

    private HyphenationTree hyphenTree = null;
    private int remainCharCount = 2;
    private int pushCharCount = 2;
    private static boolean errorDump = false;
   
    /** Holds value of property hyphenDir. */
    private static String hyphenDir = "";    

    public Hyphenator(String lang, String country, int leftMin,
                      int rightMin) {
        hyphenTree = getHyphenationTree(lang, country);
        remainCharCount = leftMin;
        pushCharCount = rightMin;
    }

    public static HyphenationTree getHyphenationTree(String lang,
            String country) {
        String key = lang;
        // check whether the country code has been used
        if (country != null &&!country.equals("none"))
            key += "_" + country;
            // first try to find it in the cache
        if (hyphenTrees.containsKey(key))
            return (HyphenationTree)hyphenTrees.get(key);
        if (hyphenTrees.containsKey(lang))
            return (HyphenationTree)hyphenTrees.get(lang);

        HyphenationTree hTree = getFopHyphenationTree(key);
        if (hTree == null) {
            //String hyphenDir = "e:\\winprog2\\Fop-0.20.2\\hyph";
                //Configuration.getStringValue("hyphenation-dir");
            if (hyphenDir != null) {
                hTree = getUserHyphenationTree(key, hyphenDir);
            }
        }
        // put it into the pattern cache
        if (hTree != null) {
            hyphenTrees.put(key, hTree);
        } else {
            System.out.println("Couldn't find hyphenation pattern "
                                   + key);
        }
        return hTree;
    }

    private static InputStream getResourceStream(String key) {
        InputStream is = null;
        // Try to use Context Class Loader to load the properties file.
        try {
            java.lang.reflect.Method getCCL =
                Thread.class.getMethod("getContextClassLoader", new Class[0]);
            if (getCCL != null) {
                ClassLoader contextClassLoader =
                    (ClassLoader)getCCL.invoke(Thread.currentThread(),
                                               new Object[0]);
                is = contextClassLoader.getResourceAsStream("hyph/" + key
                                                            + ".hyp");
            }
        } catch (Exception e) {}

        if (is == null) {
            is = Hyphenator.class.getResourceAsStream("hyph/" + key
                                                      + ".hyp");
        }

        return is;
    }

    public static HyphenationTree getFopHyphenationTree(String key) {
        HyphenationTree hTree = null;
        InputStream is = null;
        try {
            is = getResourceStream(key);
            if (is == null) {
                if (key.length() == 5) {
                    is = getResourceStream(key.substring(0, 2));
                    if (is != null) {
                        System.out.println("Couldn't find hyphenation pattern  "
                                               + key
                                               + "\nusing general language pattern "
                                               + key.substring(0, 2)
                                               + " instead.");
                    } else {
                        if (errorDump) {
                            System.out.println("Couldn't find precompiled "
                                                   + "hyphenation pattern "
                                                   + key + ".hyp");
                        }
                        return null;
                    }
                } else {
                    if (errorDump) {
                        System.out.println("Couldn't find precompiled "
                                               + "hyphenation pattern "
                                               + key + ".hyp");
                    }
                    return null;
                }
            }
            hTree = new HyphenationTree();
            hTree.loadInternalPatterns(is);
        } catch (Exception e) {
            // empty on purpose
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    System.out.println("can't close hyphenation stream");
                }
            }
        }
        return hTree;
    }

    /**
     * load tree from serialized file or xml file
     * using configuration settings
     */
    public static HyphenationTree getUserHyphenationTree(String key,
            String hyphenDir) {
        HyphenationTree hTree = null;
        // I use here the following convention. The file name specified in
        // the configuration is taken as the base name. First we try
        // name + ".hyp" assuming a serialized HyphenationTree. If that fails
        // we try name + ".xml", assumming a raw hyphenation pattern file.

        // first try serialized object
        File hyphenFile = new File(hyphenDir, key + ".hyp");
        if (hyphenFile.exists()) {
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(new FileInputStream(hyphenFile));
                hTree = (HyphenationTree)ois.readObject();
            } catch (Exception e) {
                // empty on purpose
            }
            finally {
                if (ois != null) {
                    try {
                        ois.close();
                    } catch (IOException e) {}
                }
            }
            return hTree;
        } else {

            // try the file
            hyphenFile = new File(hyphenDir, key + ".hyp");
            if (hyphenFile.exists()) {
                hTree = new HyphenationTree();
                if (errorDump) {
                    System.out.println("reading " + hyphenDir + key
                                           + ".hyp");
                }
                try {
                    hTree.loadInternalPatterns(hyphenFile.getPath());
                    if (errorDump) {
                        System.out.println("Stats: ");
                        hTree.printStats();
                    }
                    return hTree;
                } catch (HyphenationException ex) {
                    if (errorDump) {
                        System.out.println("Can't load user patterns "
                                               + "from file " + hyphenDir
                                               + key + ".hyp");
                    }
                    return null;
                }
            } else {
                if (errorDump) {
                    System.out.println("Tried to load "
                                           + hyphenFile.toString()
                                           + "\nCannot find compiled nor xml file for "
                                           + "hyphenation pattern" + key);
                }
                return null;
            }
        }
    }

    public static Hyphenation hyphenate(String lang, String country,
                                        String word, int leftMin,
                                        int rightMin) {
        HyphenationTree hTree = getHyphenationTree(lang, country);
        if (hTree == null) {
            System.out.println("Error building hyphenation tree for language "
                                   + lang);
            return null;
        }
        return hTree.hyphenate(word, leftMin, rightMin);
    }

    public static Hyphenation hyphenate(String lang, String country,
                                        char[] word, int offset, int len,
                                        int leftMin, int rightMin) {
        HyphenationTree hTree = getHyphenationTree(lang, country);
        if (hTree == null) {
            System.out.println("Error building hyphenation tree for language "
                                   + lang);
            return null;
        }
        return hTree.hyphenate(word, offset, len, leftMin, rightMin);
    }

    public void setMinRemainCharCount(int min) {
        remainCharCount = min;
    }

    public void setMinPushCharCount(int min) {
        pushCharCount = min;
    }

    public void setLanguage(String lang, String country) {
        hyphenTree = getHyphenationTree(lang, country);
    }

    public Hyphenation hyphenate(char[] word, int offset, int len) {
        if (hyphenTree == null)
            return null;
        return hyphenTree.hyphenate(word, offset, len, remainCharCount,
                                    pushCharCount);
    }

    public Hyphenation hyphenate(String word) {
        if (hyphenTree == null)
            return null;
        return hyphenTree.hyphenate(word, remainCharCount, pushCharCount);
    }

    /** Getter for property hyphenDir.
     * @return Value of property hyphenDir.
     */
    public static String getHyphenDir() {
        return hyphenDir;
    }
    
    /** Setter for property hyphenDir.
     * @param _hyphenDir New value of property hyphenDir.
     */
    public static void setHyphenDir(String _hyphenDir) {
        hyphenDir = _hyphenDir;
    }
    
}
