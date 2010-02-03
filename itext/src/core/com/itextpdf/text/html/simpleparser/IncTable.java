/*
 * $Id$
 *
 * This file is part of the iText project.
 * Copyright (c) 1998-2009 1T3XT BVBA
 * Authors: Bruno Lowagie, Paulo Soares, et al.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License version 3
 * as published by the Free Software Foundation with the addition of the
 * following permission added to Section 15 as permitted in Section 7(a):
 * FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY 1T3XT,
 * 1T3XT DISCLAIMS THE WARRANTY OF NON INFRINGEMENT OF THIRD PARTY RIGHTS.
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
 * you must retain the producer line in every PDF that is created or manipulated
 * using iText.
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
package com.itextpdf.text.html.simpleparser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 *
 * @author  psoares
 */
public class IncTable implements Element {
    private HashMap<String, String> props = new HashMap<String, String>();
    private ArrayList<ArrayList<PdfPCell>> rows = new ArrayList<ArrayList<PdfPCell>>();
    private ArrayList<PdfPCell> cols;
    /** Creates a new instance of IncTable */
    public IncTable(HashMap<String, String> props) {
        this.props.putAll(props);
    }

    public void addCol(PdfPCell cell) {
        if (cols == null)
            cols = new ArrayList<PdfPCell>();
        cols.add(cell);
    }

    public void addCols(ArrayList<PdfPCell> ncols) {
        if (cols == null)
            cols = new ArrayList<PdfPCell>(ncols);
        else
            cols.addAll(ncols);
    }

    public void endRow() {
        if (cols != null) {
            Collections.reverse(cols);
            rows.add(cols);
            cols = null;
        }
    }

    public ArrayList<ArrayList<PdfPCell>> getRows() {
        return rows;
    }

    public PdfPTable buildTable() {
        if (rows.isEmpty())
            return new PdfPTable(1);
        int ncol = 0;
        for (PdfPCell pc : rows.get(0)) {
            ncol += pc.getColspan();
        }
        PdfPTable table = new PdfPTable(ncol);
        String width = props.get("width");
        if (width == null)
            table.setWidthPercentage(100);
        else {
            if (width.endsWith("%"))
                table.setWidthPercentage(Float.parseFloat(width.substring(0, width.length() - 1)));
            else {
                table.setTotalWidth(Float.parseFloat(width));
                table.setLockedWidth(true);
            }
        }
        for (ArrayList<PdfPCell> col : rows) {
            for (PdfPCell pc : col) {
                table.addCell(pc);
            }
        }
        return table;
    }

    /**
     * @since 5.0.1
     */
    public ArrayList<Chunk> getChunks() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @since 5.0.1
     */
    public boolean isContent() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @since 5.0.1
     */
    public boolean isNestable() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @since 5.0.1
     */
    public boolean process(ElementListener listener) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * @since 5.0.1
     */
    public int type() {
        // TODO Auto-generated method stub
        return 0;
    }

}
