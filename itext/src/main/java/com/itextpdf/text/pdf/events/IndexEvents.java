/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2022 iText Group NV
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
 * ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
 * OF THIRD PARTY RIGHTS
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program; if not, see http://www.gnu.org/licenses or write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
 * Boston, MA, 02110-1301 USA, or download the license from the following URL:
 * http://itextpdf.com/terms-of-use/
 *
 * The interactive user interfaces in modified source and object code versions
 * of this program must display Appropriate Legal Notices, as required under
 * Section 5 of the GNU Affero General Public License.
 *
 * In accordance with Section 7(b) of the GNU Affero General Public License,
 * a covered work must retain the producer line in every PDF that is created
 * or manipulated using iText.
 *
 * You can be released from the requirements of the license by purchasing
 * a commercial license. Buying such a license is mandatory as soon as you
 * develop commercial activities involving the iText software without
 * disclosing the source code of your own applications.
 * These activities include: offering paid services to customers as an ASP,
 * serving PDFs on the fly in a web application, shipping iText with a closed
 * source product.
 *
 * For more information, please contact iText Software Corp. at this
 * address: sales@itextpdf.com
 */
package com.itextpdf.text.pdf.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Class for an index.
 *
 * @author Michael Niedermair
 */
public class IndexEvents extends PdfPageEventHelper {

    /**
     * keeps the indextag with the pagenumber
     */
    private Map<String, Integer> indextag = new TreeMap<String, Integer>();

    /**
     * All the text that is passed to this event, gets registered in the indexentry.
     *
     * @see com.itextpdf.text.pdf.PdfPageEventHelper#onGenericTag(
     *      com.itextpdf.text.pdf.PdfWriter, com.itextpdf.text.Document,
     *      com.itextpdf.text.Rectangle, java.lang.String)
     */
    @Override
    public void onGenericTag(PdfWriter writer, Document document,
            Rectangle rect, String text) {
        indextag.put(text, Integer.valueOf(writer.getPageNumber()));
    }

    // --------------------------------------------------------------------
    /**
     * indexcounter
     */
    private long indexcounter = 0;

    /**
     * the list for the index entry
     */
    private List<Entry> indexentry = new ArrayList<Entry>();

    /**
     * Create an index entry.
     *
     * @param text  The text for the Chunk.
     * @param in1   The first level.
     * @param in2   The second level.
     * @param in3   The third level.
     * @return Returns the Chunk.
     */
    public Chunk create(final String text, final String in1, final String in2,
            final String in3) {

        Chunk chunk = new Chunk(text);
        String tag = "idx_" + indexcounter++;
        chunk.setGenericTag(tag);
        chunk.setLocalDestination(tag);
        Entry entry = new Entry(in1, in2, in3, tag);
        indexentry.add(entry);
        return chunk;
    }

    /**
     * Create an index entry.
     *
     * @param text  The text for the Chunk.
     * @param in1   The first level.
     * @return Returns the Chunk.
     */
    public Chunk create(final String text, final String in1) {
        return create(text, in1, "", "");
    }

    /**
     * Create an index entry.
     *
     * @param text  The text for the Chunk.
     * @param in1   The first level.
     * @param in2   The second level.
     * @return Returns the Chunk.
     */
    public Chunk create(final String text, final String in1, final String in2) {
        return create(text, in1, in2, "");
    }

    /**
     * Create an index entry.
     *
     * @param text  The text.
     * @param in1   The first level.
     * @param in2   The second level.
     * @param in3   The third level.
     */
    public void create(final Chunk text, final String in1, final String in2,
            final String in3) {

        String tag = "idx_" + indexcounter++;
        text.setGenericTag(tag);
        text.setLocalDestination(tag);
        Entry entry = new Entry(in1, in2, in3, tag);
        indexentry.add(entry);
    }

    /**
     * Create an index entry.
     *
     * @param text  The text.
     * @param in1   The first level.
     */
    public void create(final Chunk text, final String in1) {
        create(text, in1, "", "");
    }

    /**
     * Create an index entry.
     *
     * @param text  The text.
     * @param in1   The first level.
     * @param in2   The second level.
     */
    public void create(final Chunk text, final String in1, final String in2) {
        create(text, in1, in2, "");
    }

    /**
     * Comparator for sorting the index
     */
    private Comparator<Entry> comparator = new Comparator<Entry>() {

        public int compare(Entry en1, Entry en2) {
            int rt = 0;
            if (en1.getIn1() != null && en2.getIn1() != null) {
                if ((rt = en1.getIn1().compareToIgnoreCase(en2.getIn1())) == 0) {
                    // in1 equals
                    if (en1.getIn2() != null && en2.getIn2() != null) {
                        if ((rt = en1.getIn2()
                                .compareToIgnoreCase(en2.getIn2())) == 0) {
                            // in2 equals
                            if (en1.getIn3() != null && en2.getIn3() != null) {
                                rt = en1.getIn3().compareToIgnoreCase(
                                        en2.getIn3());
                            }
                        }
                    }
                }
            }
            return rt;
        }
    };

    /**
     * Set the comparator.
     * @param aComparator The comparator to set.
     */
    public void setComparator(Comparator<Entry> aComparator) {
        comparator = aComparator;
    }

    /**
     * Returns the sorted list with the entries and the collected page numbers.
     * @return Returns the sorted list with the entries and the collected page numbers.
     */
    public List<Entry> getSortedEntries() {

        Map<String, Entry> grouped = new HashMap<String, Entry>();

        for (int i = 0; i < indexentry.size(); i++) {
            Entry e = indexentry.get(i);
            String key = e.getKey();

            Entry master = grouped.get(key);
            if (master != null) {
                master.addPageNumberAndTag(e.getPageNumber(), e.getTag());
            } else {
                e.addPageNumberAndTag(e.getPageNumber(), e.getTag());
                grouped.put(key, e);
            }
        }

        // copy to a list and sort it
        List<Entry> sorted = new ArrayList<Entry>(grouped.values());
        Collections.sort(sorted, comparator);
        return sorted;
    }

    // --------------------------------------------------------------------
    /**
     * Class for an index entry.
     * <p>
     * In the first step, only in1, in2,in3 and tag are used.
     * After the collections of the index entries, pagenumbers are used.
     * </p>
     */
    public class Entry {

        /**
         * first level
         */
        private String in1;

        /**
         * second level
         */
        private String in2;

        /**
         * third level
         */
        private String in3;

        /**
         * the tag
         */
        private String tag;

        /**
         * the list of all page numbers.
         */
        private List<Integer> pagenumbers = new ArrayList<Integer>();

        /**
         * the list of all tags.
         */
        private List<String> tags = new ArrayList<String>();

        /**
         * Create a new object.
         * @param aIn1   The first level.
         * @param aIn2   The second level.
         * @param aIn3   The third level.
         * @param aTag   The tag.
         */
        public Entry(final String aIn1, final String aIn2, final String aIn3,
                final String aTag) {
            in1 = aIn1;
            in2 = aIn2;
            in3 = aIn3;
            tag = aTag;
        }

        /**
         * Returns the in1.
         * @return Returns the in1.
         */
        public String getIn1() {
            return in1;
        }

        /**
         * Returns the in2.
         * @return Returns the in2.
         */
        public String getIn2() {
            return in2;
        }

        /**
         * Returns the in3.
         * @return Returns the in3.
         */
        public String getIn3() {
            return in3;
        }

        /**
         * Returns the tag.
         * @return Returns the tag.
         */
        public String getTag() {
            return tag;
        }

        /**
         * Returns the pagenumber for this entry.
         * @return Returns the pagenumber for this entry.
         */
        public int getPageNumber() {
            int rt = -1;
            Integer i = indextag.get(tag);
            if (i != null) {
                rt = i.intValue();
            }
            return rt;
        }

        /**
         * Add a pagenumber.
         * @param number    The page number.
         * @param tag
         */
        public void addPageNumberAndTag(final int number, final String tag) {
            pagenumbers.add(Integer.valueOf(number));
            tags.add(tag);
        }

        /**
         * Returns the key for the map-entry.
         * @return Returns the key for the map-entry.
         */
        public String getKey() {
            return in1 + "!" + in2 + "!" + in3;
        }

        /**
         * Returns the pagenumbers.
         * @return Returns the pagenumbers.
         */
        public List<Integer> getPagenumbers() {
            return pagenumbers;
        }

        /**
         * Returns the tags.
         * @return Returns the tags.
         */
        public List<String> getTags() {
            return tags;
        }

        /**
         * print the entry (only for test)
         * @return the toString implementation of the entry
         */
        @Override
        public String toString() {
            StringBuffer buf = new StringBuffer();
            buf.append(in1).append(' ');
            buf.append(in2).append(' ');
            buf.append(in3).append(' ');
            for (int i = 0; i < pagenumbers.size(); i++) {
                buf.append(pagenumbers.get(i)).append(' ');
            }
            return buf.toString();
        }
    }
}
