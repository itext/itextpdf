/*
 * $Id$
 * $Name$
 * 
 * Copyright 1999, 2000, 2001 by Bruno Lowagie.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either version 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License along
 * with this library; if not, write to the Free Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 *
 * ir-arch Bruno Lowagie,
 * Adolf Baeyensstraat 121
 * 9040 Sint-Amandsberg
 * BELGIUM
 * tel. +32 (0)9 228.10.97
 * bruno@lowagie.com
 *        
 */

package com.lowagie.text;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A <CODE>Table</CODE> is a <CODE>Rectangle</CODE> that contains <CODE>Cell</CODE>s,
 * ordered in some kind of matrix.
 * <P>
 * Tables that span multiple pages are cut into different parts automatically.
 * If you want a table header to be repeated on every page, you may not forget to
 * mark the end of the header section by using the method <CODE>endHeaders()</CODE>.
 * <P>
 * The matrix of a table is not necessarily an m x n-matrix. It can contain holes
 * or cells that are bigger than the unit. Believe me or not, but it took some serious
 * thinking to make this as userfriendly as possible. I hope you wil find the result
 * quite simple (I love simple solutions, especially for complex problems).
 * I didn't want it to be something as complex as the Java <CODE>GridBagLayout</CODE>.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * // Remark: You MUST know the number of columns when constructing a Table.
 * //         The number of rows is not important.
 * <STRONG>Table table = new Table(3);</STRONG>
 * <STRONG>table.setBorderWidth(1);</STRONG>
 * <STRONG>table.setBorderColor(new Color(0, 0, 255));</STRONG>
 * <STRONG>table.setCellpadding(5);</STRONG>
 * <STRONG>table.setCellspacing(5);</STRONG>
 * Cell cell = new Cell("header");
 * cell.setHeader(true);
 * cell.setColspan(3);
 * <STRONG>table.addCell(cell);</STRONG>
 * <STRONG>table.endHeaders();</STRONG>
 * cell = new Cell("example cell with colspan 1 and rowspan 2");
 * cell.setRowspan(2);
 * cell.setBorderColor(new Color(255, 0, 0));
 * <STRONG>table.addCell(cell);</STRONG>
 * <STRONG>table.addCell("1.1");</STRONG>
 * <STRONG>table.addCell("2.1");</STRONG>
 * <STRONG>table.addCell("1.2");</STRONG>
 * <STRONG>table.addCell("2.2");</STRONG>
 * <STRONG>table.addCell("cell test1");</STRONG>
 * cell = new Cell("big cell");
 * cell.setRowspan(2);
 * cell.setColspan(2);
 * <STRONG>table.addCell(cell);</STRONG>
 * <STRONG>table.addCell("cell test2");</STRONG>
 * </PRE></BLOCKQUOTE>
 * The result of this code is a table:
 *      <TABLE ALIGN="Center" BORDER="1" BORDERCOLOR="#0000ff" CELLPADDING="5" CELLSPACING="5">
 *              <TR ALIGN="Left" VALIGN="Left">
 *                      <TH ALIGN="Left" COLSPAN="3" VALIGN="Left">
 *                              header
 *                      </TH>
 *              </TR>
 *              <TR ALIGN="Left" VALIGN="Left">
 *                      <TD ALIGN="Left" BORDERCOLOR="#ff0000" ROWSPAN="2" VALIGN="Left">
 *                              example cell with colspan 1 and rowspan 2
 *                      </TD>
 *                      <TD ALIGN="Left" VALIGN="Left">
 *                              1.1
 *                      </TD>
 *                      <TD ALIGN="Left" VALIGN="Left">
 *                              2.1
 *                      </TD>
 *              </TR>
 *              <TR ALIGN="Left" VALIGN="Left">
 *                      <TD ALIGN="Left" VALIGN="Left">
 *                              1.2
 *                      </TD>
 *                      <TD ALIGN="Left" VALIGN="Left">
 *                              2.2
 *                      </TD>
 *              </TR>
 *              <TR ALIGN="Left" VALIGN="Left">
 *                      <TD ALIGN="Left" VALIGN="Left">
 *                              cell test1
 *                      </TD>
 *                      <TD ALIGN="Left" COLSPAN="2" ROWSPAN="2" VALIGN="Left">
 *                              big cell
 *                      </TD>
 *              </TR>
 *              <TR ALIGN="Left" VALIGN="Left">
 *                      <TD ALIGN="Left" VALIGN="Left">
 *                              cell test2
 *                      </TD>
 *              </TR>
 *      </TABLE>
 *
 * @see         Rectangle
 * @see         Element
 * @see         Row
 * @see         Cell
 *
 * @author  bruno@lowagie.com
 */

public class Table extends Rectangle implements Element {

// membervariables

        // these variables contain the data of the table

        /** This is the number of columns in the <CODE>Table</CODE>. */
        private int columns;

        /** This is the currentRow. */
        private int currentRow;

        /** This is the list of <CODE>Row</CODE>s. */
        private ArrayList rows = new ArrayList();

        // these variables contain the layout of the table

        /** This Empty Cell contains the DEFAULT layout of each Cell added with the method addCell(String content). */
        private Cell defaultLayout = new Cell();

        /** This is the number of the last row of the table headers. */
        private int lastHeaderRow = -1;

        /** This is the horizontal alignment. */
        private int alignment = Element.ALIGN_CENTER;

        /** This is cellpadding. */
        private float cellpadding;

        /** This is cellspacing. */
        private float cellspacing;

        /** This is the width of the table (in percent of the available space). */
        private float widthPercentage = 80;

		// member variable added by Evelyne De Cordier
		/** This is the width of the table (in pixels). */
		private String absWidth = "";
		
        /** This is an array containing the widths (in percentages) of every column. */
        private float[] widths;

// constructors

        /**
         * Constructs a <CODE>Table</CODE> with a certain number of columns.
         *
         * @param       columns         The number of columns in the table
         * @throws      BadElementException if the creator was called with less than 1 column
         */

        public Table(int columns) throws BadElementException {
                this(columns, 1);
        }

        /**
         * Constructs a <CODE>Table</CODE> with a certain number of columns
         * and a certain number of <CODE>Row</CODE>s.
         *
         * @param       columns         The number of columns in the table
         * @param       rows            The number of rows
         * @throws      BadElementException if the creator was called with less than 1 column
         */

        public Table(int columns, int rows) throws BadElementException {
                // a Rectangle is create with BY DEFAULT a border with a width of 1
                super(0, 0, 0, 0);
                setBorder(BOX);
                setBorderWidth(1);

                // a table should have at least 1 column
                if (columns <= 0) {
                        throw new BadElementException("A table should have at least 1 column.");
                }
                this.columns = columns;

                // a certain number of rows are created
                for (int i = 0; i < rows; i++) {
                        this.rows.add(new Row(columns));
                }
                currentRow = 0;

                // the DEFAULT widths are calculated
                widths = new float[columns];
                float width = 100f / columns;
                for (int i = 0; i < columns; i++) {
                        widths[i] = width;
                }
        }

// implementation of the Element-methods

    /**
     * Processes the element by adding it (or the different parts) to an
         * <CODE>ElementListener</CODE>. 
     *
         * @param       listener        an <CODE>ElementListener</CODE>
         * @return <CODE>true</CODE> if the element was processed successfully
     */

    public final boolean process(ElementListener listener) {
                try {
                        return listener.add(this);
                }
                catch(DocumentException de) {
                        return false;
                }
        }

    /**
     * Gets the type of the text element. 
     *
     * @return  a type
     */

    public final int type() {
                return Element.TABLE;
        }               

    /**
     * Gets all the chunks in this element. 
     *
     * @return  an <CODE>ArrayList</CODE>
     */

    public final ArrayList getChunks() {
                 return new ArrayList();
        }

// methods to add content to the table

        /**
     * Adds a <CODE>Cell</CODE> to the <CODE>Table</CODE>. 
     *
         * @param       cell    The <CODE>Cell</CODE> to add
         */

        public final void addCell(Cell cell) {
                // we look in the current row if there is enough space left to add a cell
                Row row = (Row) rows.get(currentRow);
                int position;
                while ((position = row.addCell(cell)) < 0) {
                        // if not, we move to the next row
                        currentRow++;
                        // if there is no next row, we make one
                        if (currentRow >= rows.size()) {
                                row = new Row(row.columns());
                                rows.add(row);
                        }
                        row = (Row) rows.get(currentRow);
                }

                // if the colspan/rowspan > 1, we reserve all the necessary cells
                for (int i = 1; i < cell.colspan(); i++) {
                        row.reserve(position + i);                      
                }
                for (int i = 1; i < cell.rowspan(); i++) {
                        for (int j = 0; j < cell.colspan(); j++) {
                                // when needed we dynamically augment the number of rows
                                if (currentRow + i >= rows.size()) {
                                        rows.add(new Row(row.columns()));
                                }
                                row = (Row) rows.get(currentRow + i);
                                row.reserve(position + j);
                        }
                }
        }

        /**
     * Adds a <CODE>Cell</CODE> to the <CODE>Table</CODE>. 
         * <P>
         * This is a shortcut for <CODE>addCell(Cell cell)</CODE>.
         * The <CODE>Phrase</CODE> will be converted to a <CODE>Cell</CODE>.
     *
         * @param       content         a <CODE>Phrase</CODE>
         * @throws      BadElementException this should never happen
         */

        public final void addCell(Phrase content) throws BadElementException {
                Cell cell = new Cell(content);
                cell.setBorder(defaultLayout.border());
                cell.setBorderWidth(defaultLayout.borderWidth());
                cell.setBorderColor(defaultLayout.borderColor());
                cell.setBackgroundColor(defaultLayout.backgroundColor());
                cell.setGrayFill(defaultLayout.grayFill());
                cell.setHorizontalAlignment(defaultLayout.horizontalAlignment());
                cell.setVerticalAlignment(defaultLayout.verticalAlignment());
                cell.setColspan(defaultLayout.colspan());
                cell.setRowspan(defaultLayout.rowspan());
                addCell(cell);
        }

        /**
     * Adds a <CODE>Cell</CODE> to the <CODE>Table</CODE>. 
         * <P>
         * This is a shortcut for <CODE>addCell(Cell cell)</CODE>.
         * The <CODE>String</CODE> will be converted to a <CODE>Cell</CODE>.
     *
         * @param       content         a <CODE>String</CODE>
         * @throws      BadElementException this should never happen
         */

        public final void addCell(String content) throws BadElementException {
                addCell(new Paragraph(content));
        }

        /**
         * Changes the border in the default layout of the <CODE>Cell</CODE>s
         * added with method <CODE>addCell(String content)</CODE>.
         *
         * @param       value   the new border value
         */

        public final void setDefaultCellBorder(int value) {
                defaultLayout.setBorder(value);
        }                                                  

        /**
         * Changes the width of the borders in the default layout of the <CODE>Cell</CODE>s
         * added with method <CODE>addCell(String content)</CODE>.
         *
         * @param       value   the new width
         */

        public final void setDefaultCellBorderWidth(float value) {
                defaultLayout.setBorderWidth(value);
        }                                                               

        /**
         * Changes the bordercolor in the default layout of the <CODE>Cell</CODE>s
         * added with method <CODE>addCell(String content)</CODE>.
         *
         * @param       color   the new color
         */

        public final void setDefaultCellBorderColor(Color color) {
                defaultLayout.setBorderColor(color);
        }                                                               

        /**
         * Changes the backgroundcolor in the default layout of the <CODE>Cell</CODE>s
         * added with method <CODE>addCell(String content)</CODE>.
         *
         * @param       color   the new color
         */

        public final void setDefaultCellBackgroundColor(Color color) {
                defaultLayout.setBackgroundColor(color);
        }                                                               

        /**
         * Changes the grayfill in the default layout of the <CODE>Cell</CODE>s
         * added with method <CODE>addCell(String content)</CODE>.
         *
         * @param       value   the new value
         */

        public final void setDefaultCellGrayFill(float value) {
                if (value >= 0 && value <= 1) {
                        defaultLayout.setGrayFill(value);
                }
        }

        /**
         * Changes the horizontalAlignment in the default layout of the <CODE>Cell</CODE>s
         * added with method <CODE>addCell(String content)</CODE>.
         *
         * @param       value   the new alignment value
         */

        public final void setDefaultHorizontalAlignment(int value) {
                defaultLayout.setHorizontalAlignment(value);
        }

        /**
         * Changes the verticalAlignment in the default layout of the <CODE>Cell</CODE>s
         * added with method <CODE>addCell(String content)</CODE>.
         *
         * @param       value   the new alignment value
         */

        public final void setDefaultVerticalAlignment(int value) {
                defaultLayout.setVerticalAlignment(value);
        }                                                  

        /**
         * Changes the rowspan in the default layout of the <CODE>Cell</CODE>s
         * added with method <CODE>addCell(String content)</CODE>.
         *
         * @param       value   the new rowspan value
         */

        public final void setDefaultRowspan(int value) {
                defaultLayout.setRowspan(value);
        }                                                       

        /**
         * Changes the colspan in the default layout of the <CODE>Cell</CODE>s
         * added with method <CODE>addCell(String content)</CODE>.
         *
         * @param       value   the new colspan value
         */

        public final void setDefaultColspan(int value) {
                defaultLayout.setColspan(value);
        }

// methods

        /**
         * Deletes a column in this table.
         *
         * @param       column  the number of the column that has to be deleted
         */

        public final void deleteColumn(int column) throws DocumentException {
                float newWidths[] = new float[--columns];
                for (int i = 0; i < column; i++) {
                        newWidths[i] = widths[i];
                }
                for (int i = column; i < columns; i++) {
                        newWidths[i] = widths[i + 1];
                }
                setWidths(newWidths);
                for (int i = 0; i < columns; i++) {
                        newWidths[i] = widths[i];
                }
                widths = newWidths;
                Row row;
                int size = rows.size();
                for (int i = 0; i < size; i++) {
                        row = (Row) rows.get(i);
                        row.deleteColumn(column);
                        rows.set(i, row);
                }
        }

        /**
         * Deletes a row.
         * 
         * @param       row             the number of the row to delete
         * @return      boolean <CODE>true</CODE> if the row was deleted; <CODE>false</CODE> if not
         */

        public final boolean deleteRow(int row) {
                if (row < 0 || row >= rows.size()) {
                        return false;
                }
                rows.remove(row);
                return true;
        }

        /**
         * Deletes the last row in this table.
         *
         * @return      boolean <CODE>true</CODE> if the row was deleted; <CODE>false</CODE> if not
         */

        public final boolean deleteLastRow() {
                return deleteRow(rows.size() - 1);
        }

        /**
         * Marks the last row of the table headers.
         *
         * @return      the number of the last row of the table headers
         */

        public int endHeaders() {
                lastHeaderRow = currentRow;
                return lastHeaderRow;
        }

// methods to set the membervariables

    /**
     * Sets the horizontal alignment.  
     *
         * @param       value   the new value
     */

    public final void setAlignment(int value) {
                alignment = value;
        }                                         

    /**
     * Sets the cellpadding. 
     *
         * @param       value   the new value
     */

    public final void setCellpadding(float value) {
                cellpadding = value;
        }                                         

    /**
     * Sets the cellspacing. 
     *
         * @param       value   the new value
     */

    public final void setCellspacing(float value) {
                cellspacing = value;
        }

        /**
         * Sets the width of this table (in percentage of the available space).
         *
         * @param       width           the width
         */

        public final void setWidth(float width) {
                this.widthPercentage = width;
        }
		
	/**
	 * Sets the width of this table (in percentage of the available space).
	 *
	 * @param   width           the width
	 * @author  Evelyne De Cordier
	 */
	 
	public final void setAbsWidth(String width) {
		this.absWidth = width;
	}

        /**
         * Sets the widths of the different columns (percentages).
         * <P>
         * You can give up relative values of borderwidths.
         * The sum of these values will be considered 100%.
         * The values will be recalculated as percentages of this sum.
         * <P>
         * example:
         * <BLOCKQUOTE><PRE>
         * float[] widths = {2, 1, 1};
         * <STRONG>table.setWidths(widths)</STRONG>
         * </PRE></BLOCKQUOTE>
         * The widths will be: a width of 50% for the first column,
         * 25% for the second and third column.
         *
         * @param       an array with values
         */

        public final void setWidths(float[] widths) throws DocumentException {
                if (widths.length != columns) {
                        throw new DocumentException("Wrong number of columns.");
                }

                // The sum of all values is 100%
                float hundredPercent = 0;
                for (int i = 0; i < columns; i++) {
                        hundredPercent += widths[i];
                }

                // The different percentages are calculated
                float width;
                this.widths[columns - 1] = 100;
                for (int i = 0; i < columns - 1; i++) {
                        width = (100.0f * widths[i]) / hundredPercent;
                        this.widths[i] = width;
                        this.widths[columns - 1] -= width;
                }
        }

        public final void setWidths(int[] widths) throws DocumentException {
        float tb[] = new float[widths.length];
        for (int k = 0; k < widths.length; ++k)
            tb[k] = widths[k];
        setWidths(tb);
    }
// methods to retrieve the membervariables

        /**
         * Gets the number of rows in this <CODE>Table</CODE>.
         *
         * @return      the number of rows in this <CODE>Table</CODE>
         */

        public final int size() {
                return rows.size();
        }

        /**
         * Gets an <CODE>Iterator</CODE> of all the <CODE>Row</CODE>s.
         *
         * @return      an <CODE>Iterator</CODE>
         */

        public Iterator iterator() {
                return rows.iterator();
        }

    /**
     * Gets the horizontal alignment. 
     *
     * @return  a value
     */

    public int alignment() {
                return alignment;
        }                                         

    /**
     * Gets the cellpadding. 
     *
     * @return  a value
     */

    public float cellpadding() {
                return cellpadding;
        }                                         

    /**
     * Gets the cellspacing. 
     *
     * @return  a value
     */

    public float cellspacing() {
                return cellspacing;
        }

        /**
         * Gets the table width (a percentage).
         *
         * @return      the table width
         * @author      Leslie Baski
         */

        public float widthPercentage() {
                return widthPercentage;
        }

		/**
		 * Gets the table width (in pixels).
		 *
		 * @return  the table width
		 * @author  Evelyne De Cordier
         */
		 
		public String absWidth() {
			return absWidth;
		}
		
        /**
         * Gets the first number of the row that doesn't contain headers.
         *
         * @return      a rownumber
         */

        public int firstDataRow() {
                return lastHeaderRow + 1;
        }

        /**
         * Gets an array with the positions of the borders between every column.
         * <P>
         * This method translates the widths expressed in percentages into the
         * x-coordinate of the borders of the columns on a real document.
         *
         * @param       left            this is the position of the first border at the left (cellpadding not included)
         * @param       totalWidth      this is the space between the first border at the left
         *                                              and the last border at the right (cellpadding not included)
         * @return      an array with borderpositions
         */

        public float[] getWidths(float left, float totalWidth) {
                // for x columns, there are x+1 borders
                float[] w = new float[columns + 1];
                // the border at the left is calculated
                switch(alignment) {
                case Element.ALIGN_LEFT:
                        w[0] = left;
                        break;
                case Element.ALIGN_RIGHT:
                        w[0] = left + (totalWidth * (100 - widthPercentage)) / 100;
                        break;
                case Element.ALIGN_CENTER:
                default:
                        w[0] = left + (totalWidth * (100 - widthPercentage)) / 200;
                }
                // the total available width is changed
                totalWidth = (totalWidth * widthPercentage) / 100; 
                // the inner borders are calculated
                for (int i = 1; i < columns; i++) {
                        w[i] = w[i - 1] + (widths[i - 1] * totalWidth / 100);
                }
                // the border at the right is calculated
                w[columns] = w[0] + totalWidth;
                return w;
        }

        /**
         * Returns a representation of this <CODE>Row</CODE>.
         *
         * @return      a <CODE>String</CODE>
         */

        public String toString() {
                StringBuffer buf = new StringBuffer("<TABLE WIDTH=\"");
			    // Changed by Evelyne De Cordier
				if (!absWidth.equals("")){
					buf.append(absWidth);
				}
				else{
                	buf.append(widthPercentage);
				}
                buf.append("%\" HORIZONTAL_ALIGNMENT=\"");
                switch(alignment) {
                case Element.ALIGN_LEFT:
                        buf.append("Left");
                        break;
                case Element.ALIGN_CENTER:
                        buf.append("Center");
                        break;
                case Element.ALIGN_RIGHT:
                        buf.append("Right");
                        break;
                case Element.ALIGN_JUSTIFIED:
                        buf.append("Justify");
                        break;
                default:
                        buf.append("Default");
                }
                buf.append("\" CELLPADDING=\"");
                buf.append(cellpadding);
                buf.append("\" CELLSPACING=\"");
                buf.append(cellspacing);
                buf.append("\">\n");
                // widths
                buf.append("\t<WIDTHS>\n");
                for (int i = 0; i < widths.length; i++) {
                        buf.append("\t\t<WITDH COLUMN=\"");
                        buf.append(i);
                        buf.append("\">");
                        buf.append(widths[i]);
                        buf.append("</WIDTH>\n");
                }
                buf.append("\t</WIDTHS>\n");
                // border
                if (borderWidth > 0) {
                        buf.append("\t<BORDER WIDTH=\"");
                        buf.append(borderWidth);
                        buf.append("\">");
                        if (hasBorder(TOP)) {
                                buf.append("\t\t<SIDE>");
                                buf.append("top");
                                buf.append("</SIDE>\n");
                        }
                        if (hasBorder(BOTTOM)) {
                                buf.append("\t\t<SIDE>");
                                buf.append("bottom");
                                buf.append("</SIDE>\n");
                        }                                                                               
                        if (hasBorder(LEFT)) {
                                buf.append("\t\t<SIDE>");
                                buf.append("left");
                                buf.append("</SIDE>\n");
                        }                                                                         
                        if (hasBorder(RIGHT)) {
                                buf.append("\t\t<SIDE>");
                                buf.append("right");
                                buf.append("</SIDE>\n");
                        }
                        if (color != null) {
                                buf.append("\t\t<COLOR>\n");
                                buf.append("\t\t\t<RED>");
                                buf.append(color.getRed());
                                buf.append("</RED>\n");;
                                buf.append("\t\t\t<GREEN>");
                                buf.append(color.getGreen());
                                buf.append("</GREEN>\n"); ;
                                buf.append("\t\t\t<BLUE>");
                                buf.append(color.getBlue());
                                buf.append("</BLUE>\n");
                                buf.append("\t\t</COLOR>\n");
                        }
                        buf.append("\t</BORDER>\n");
                }
                Row row;
                for (Iterator iterator = rows.iterator(); iterator.hasNext(); ) {
                        row = (Row) iterator.next();
                        buf.append(row.toString());
                }
                buf.append("</TABLE>\n");                                                               
                return buf.toString();
        }
}