/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2011 1T3XT BVBA
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package com.itextpdf.tool.xml.html.table;

import com.itextpdf.text.BaseColor;

/**
 * @author Emiel Ackermann
 *
 */
public class TableStyleValues {
	 	private float horBorderSpacing;
	 	private float verBorderSpacing;
		private float borderLeftWidth;
		private BaseColor borderLeftColor;
		private float borderRightWidth;
		private BaseColor borderRightColor;
		private float borderTopWidth;
		private BaseColor borderTopColor;
		private float borderBottomWidth;
		private BaseColor borderBottomColor;
		private boolean isLastInRow = false;
		private BaseColor background;

		public TableStyleValues() {
		}
		public float getHorBorderSpacing() {
			return horBorderSpacing;
		}
		public void setHorBorderSpacing(final float horBorderSpacing) {
			this.horBorderSpacing = horBorderSpacing;
		}
		public void setVerBorderSpacing(final float verBorderSpacing) {
			this.verBorderSpacing = verBorderSpacing;
		}
		public float getVerBorderSpacing() {
			return verBorderSpacing;
		}
		public void setLastInRow(final boolean isLastInRow) {
			this.isLastInRow = isLastInRow;
		}
		public boolean isLastInRow() {
			return isLastInRow;
		}
		/**
		 * @return the borderLeftWidth
		 */
		public float getBorderWidthLeft() {
			return borderLeftWidth;
		}
		/**
		 * @param borderLeftWidth the borderLeftWidth to set
		 */
		public void setBorderWidthLeft(final float borderLeftWidth) {
			this.borderLeftWidth = borderLeftWidth;
		}
		/**
		 * @return the borderLeftColor
		 */
		public BaseColor getBorderColorLeft() {
			return borderLeftColor;
		}
		/**
		 * @param borderLeftColor the borderLeftColor to set
		 */
		public void setBorderColorLeft(final BaseColor borderLeftColor) {
			this.borderLeftColor = borderLeftColor;
		}
		/**
		 * @return the borderRightWidth
		 */
		public float getBorderWidthRight() {
			return borderRightWidth;
		}
		/**
		 * @param borderRightWidth the borderRightWidth to set
		 */
		public void setBorderWidthRight(final float borderRightWidth) {
			this.borderRightWidth = borderRightWidth;
		}
		/**
		 * @return the borderRightColor
		 */
		public BaseColor getBorderColorRight() {
			return borderRightColor;
		}
		/**
		 * @param borderRightColor the borderRightColor to set
		 */
		public void setBorderColorRight(final BaseColor borderRightColor) {
			this.borderRightColor = borderRightColor;
		}
		/**
		 * @return the borderTopWidth
		 */
		public float getBorderWidthTop() {
			return borderTopWidth;
		}
		/**
		 * @param borderTopWidth the borderTopWidth to set
		 */
		public void setBorderWidthTop(final float borderTopWidth) {
			this.borderTopWidth = borderTopWidth;
		}
		/**
		 * @return the borderTopColor
		 */
		public BaseColor getBorderColorTop() {
			return borderTopColor;
		}
		/**
		 * @param borderTopColor the borderTopColor to set
		 */
		public void setBorderColorTop(final BaseColor borderTopColor) {
			this.borderTopColor = borderTopColor;
		}
		/**
		 * @return the borderBottomWidth
		 */
		public float getBorderWidthBottom() {
			return borderBottomWidth;
		}
		/**
		 * @param borderBottomWidth the borderBottomWidth to set
		 */
		public void setBorderWidthBottom(final float borderBottomWidth) {
			this.borderBottomWidth = borderBottomWidth;
		}
		/**
		 * @return the borderBottomColor
		 */
		public BaseColor getBorderColorBottom() {
			return borderBottomColor;
		}
		/**
		 * @param borderBottomColor the borderBottomColor to set
		 */
		public void setBorderColorBottom(final BaseColor borderBottomColor) {
			this.borderBottomColor = borderBottomColor;
		}
		/**
		 * @param width
		 */
		public void setBorderWidth(final float width) {
			borderBottomWidth = width;
			borderLeftWidth = width;
			borderRightWidth = width;
			borderTopWidth = width;
		}
		/**
		 * @param color
		 */
		public void setBorderColor(final BaseColor color) {
			borderBottomColor = color;
			borderLeftColor = color;
			borderRightColor = color;
			borderTopColor = color;
		}
		public void setBackground(final BaseColor background) {
			this.background = background;
		}
		public BaseColor getBackground() {
			return background;
		}
	}