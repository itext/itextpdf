/*
 * This code sample was written in the context of
 *
 * JavaOne 2014: PDF is dead. Long live PDF... and Java!
 * Tutorial Session by Bruno Lowagie and Raf Hens
 *
 * Copyright 2014, iText Group NV
 */
package javaone.edition14.part4.helper;

import java.util.List;

/**
 * MyItem implementation that gets its coordinates and color from a list
 * of lines that belong to the same structure.
 */
public class Structure extends Line {
    /**
     * Creates a Structure object based on a list of lines that belong
     * together in the same structure.
     * @param items a list of MyItem objects
     */
     public Structure(List<MyItem> items) {
        super(items);
    }
}
