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
package com.itextpdf.text;

/**
 * A class that implements <CODE>DocListener</CODE> will perform some
 * actions when some actions are performed on a <CODE>Document</CODE>.
 *
 * @see		ElementListener
 * @see		Document
 * @see		DocWriter
 */

public interface DocListener extends ElementListener {
    
    // methods
    
	/**
	 * Signals that the <CODE>Document</CODE> has been opened and that
	 * <CODE>Elements</CODE> can be added.
	 */
    
    public void open(); // [L1]
    
    /**
     * Signals that the <CODE>Document</CODE> was closed and that no other
     * <CODE>Elements</CODE> will be added.
     * <P>
     * The outputstream of every writer implementing <CODE>DocListener</CODE> will be closed.
     */
        
    public void close(); // [L2] 
    
    /**
     * Signals that an new page has to be started.
     *
     * @return	<CODE>true</CODE> if the page was added, <CODE>false</CODE> if not.
     */
        
    public boolean newPage(); // [L3]
    
    /**
     * Sets the pagesize.
     *
     * @param	pageSize	the new pagesize
     * @return	a <CODE>boolean</CODE>
     */
        
    public boolean setPageSize(Rectangle pageSize); // [L4]
        
    /**
     * Sets the margins.
     *
     * @param	marginLeft		the margin on the left
     * @param	marginRight		the margin on the right
     * @param	marginTop		the margin on the top
     * @param	marginBottom	the margin on the bottom
     * @return	a <CODE>boolean</CODE>
     */
        
    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom);  // [L5]
        
    /**
     * Parameter that allows you to do left/right  margin mirroring (odd/even pages)
     * @param marginMirroring
     * @return true if successful
     */
    public boolean setMarginMirroring(boolean marginMirroring); // [L6]
    
    /**
     * Parameter that allows you to do top/bottom margin mirroring (odd/even pages)
     * @param marginMirroringTopBottom
     * @return true if successful
     * @since	2.1.6
     */
    public boolean setMarginMirroringTopBottom(boolean marginMirroringTopBottom); // [L6]
        
    /**
     * Sets the page number.
     *
     * @param	pageN		the new page number
     */
        
    public void setPageCount(int pageN); // [L7]
    
    /**
     * Sets the page number to 0.
     */
        
    public void resetPageCount(); // [L8]

}
