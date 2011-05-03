/*
 * $Id: $
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
		private float tableBorderLeftWidth;
		private BaseColor tableBorderLeftColor;
		private float tableBorderRightWidth;
		private BaseColor tableBorderRightColor;
		private float tableBorderTopWidth;
		private BaseColor tableBorderTopColor;
		private float tableBorderBottomWidth;
		private BaseColor tableBorderBottomColor;

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
		/**
		 * @return the tableBorderLeftWidth
		 */
		public float getTableBorderLeftWidth() {
			return tableBorderLeftWidth;
		}
		/**
		 * @param tableBorderLeftWidth the tableBorderLeftWidth to set
		 */
		public void setTableBorderLeftWidth(final float tableBorderLeftWidth) {
			this.tableBorderLeftWidth = tableBorderLeftWidth;
		}
		/**
		 * @return the tableBorderLeftColor
		 */
		public BaseColor getTableBorderLeftColor() {
			return tableBorderLeftColor;
		}
		/**
		 * @param tableBorderLeftColor the tableBorderLeftColor to set
		 */
		public void setTableBorderLeftColor(final BaseColor tableBorderLeftColor) {
			this.tableBorderLeftColor = tableBorderLeftColor;
		}
		/**
		 * @return the tableBorderRightWidth
		 */
		public float getTableBorderRightWidth() {
			return tableBorderRightWidth;
		}
		/**
		 * @param tableBorderRightWidth the tableBorderRightWidth to set
		 */
		public void setTableBorderRightWidth(final float tableBorderRightWidth) {
			this.tableBorderRightWidth = tableBorderRightWidth;
		}
		/**
		 * @return the tableBorderRightColor
		 */
		public BaseColor getTableBorderRightColor() {
			return tableBorderRightColor;
		}
		/**
		 * @param tableBorderRightColor the tableBorderRightColor to set
		 */
		public void setTableBorderRightColor(final BaseColor tableBorderRightColor) {
			this.tableBorderRightColor = tableBorderRightColor;
		}
		/**
		 * @return the tableBorderTopWidth
		 */
		public float getTableBorderTopWidth() {
			return tableBorderTopWidth;
		}
		/**
		 * @param tableBorderTopWidth the tableBorderTopWidth to set
		 */
		public void setTableBorderTopWidth(final float tableBorderTopWidth) {
			this.tableBorderTopWidth = tableBorderTopWidth;
		}
		/**
		 * @return the tableBorderTopColor
		 */
		public BaseColor getTableBorderTopColor() {
			return tableBorderTopColor;
		}
		/**
		 * @param tableBorderTopColor the tableBorderTopColor to set
		 */
		public void setTableBorderTopColor(final BaseColor tableBorderTopColor) {
			this.tableBorderTopColor = tableBorderTopColor;
		}
		/**
		 * @return the tableBorderBottomWidth
		 */
		public float getTableBorderBottomWidth() {
			return tableBorderBottomWidth;
		}
		/**
		 * @param tableBorderBottomWidth the tableBorderBottomWidth to set
		 */
		public void setTableBorderBottomWidth(final float tableBorderBottomWidth) {
			this.tableBorderBottomWidth = tableBorderBottomWidth;
		}
		/**
		 * @return the tableBorderBottomColor
		 */
		public BaseColor getTableBorderBottomColor() {
			return tableBorderBottomColor;
		}
		/**
		 * @param tableBorderBottomColor the tableBorderBottomColor to set
		 */
		public void setTableBorderBottomColor(final BaseColor tableBorderBottomColor) {
			this.tableBorderBottomColor = tableBorderBottomColor;
		}
		/**
		 * @param width
		 */
		public void setTableBorderWidth(final float width) {
			tableBorderBottomWidth = width;
			tableBorderLeftWidth = width;
			tableBorderRightWidth = width;
			tableBorderTopWidth = width;
		}
		/**
		 * @param color
		 */
		public void setTableBorderColor(final BaseColor color) {
			tableBorderBottomColor = color;
			tableBorderLeftColor = color;
			tableBorderRightColor = color;
			tableBorderTopColor = color;
		}
	}