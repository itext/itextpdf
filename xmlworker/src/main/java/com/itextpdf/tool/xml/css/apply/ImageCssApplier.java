/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2015 iText Group NV
 * Authors: Balder Van Camp, Emiel Ackermann, et al.
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
package com.itextpdf.tool.xml.css.apply;

import com.itextpdf.text.Image;
import com.itextpdf.text.html.HtmlUtilities;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.html.HTML;

import java.util.Map;

/**
 * Class that applies the parsed CSS to an Image object.
 *
 * @author redlab_b
 */
public class ImageCssApplier {

    /**
     * Applies CSS to an Image. Currently supported:
     * - width
     * - height
     * - borders (color, width)
     * - spacing before and after
     *
     * @param img the image
     * @param tag the tag with the css
     * @return a styled Image
     */
    public Image apply(final Image img, final Tag tag) {
        Map<String, String> cssMap = tag.getCSS();

        String widthValue = cssMap.get(HTML.Attribute.WIDTH);
        if (widthValue == null) {
            widthValue = tag.getAttributes().get(HTML.Attribute.WIDTH);
        }

        String heightValue = cssMap.get(HTML.Attribute.HEIGHT);
        if (heightValue == null) {
            heightValue = tag.getAttributes().get(HTML.Attribute.HEIGHT);
        }

        if (widthValue == null) {
            img.setScaleToFitLineWhenOverflow(true);
        } else {
            img.setScaleToFitLineWhenOverflow(false);
        }

        img.setScaleToFitHeight(false);


        CssUtils utils = CssUtils.getInstance();
        float widthInPoints = utils.parsePxInCmMmPcToPt(widthValue);

        float heightInPoints = utils.parsePxInCmMmPcToPt(heightValue);

        if (widthInPoints > 0 && heightInPoints > 0) {
            img.scaleAbsolute(widthInPoints, heightInPoints);
        } else if (widthInPoints > 0) {
            heightInPoints = img.getHeight() * widthInPoints / img.getWidth();
            img.scaleAbsolute(widthInPoints, heightInPoints);
        } else if (heightInPoints > 0) {
            widthInPoints = img.getWidth() * heightInPoints / img.getHeight();
            img.scaleAbsolute(widthInPoints, heightInPoints);
        }

        // apply border CSS
        String borderTopColor = cssMap.get(CSS.Property.BORDER_TOP_COLOR);
        if (borderTopColor != null) {
            img.setBorderColorTop(HtmlUtilities.decodeColor(borderTopColor));
        }

        String borderTopWidth = cssMap.get(CSS.Property.BORDER_TOP_WIDTH);
        if (borderTopWidth != null) {
            img.setBorderWidthTop(utils.parseValueToPt(borderTopWidth, 1f));
        }

        String borderRightColor = cssMap.get(CSS.Property.BORDER_RIGHT_COLOR);
        if (borderRightColor != null) {
            img.setBorderColorRight(HtmlUtilities.decodeColor(borderRightColor));
        }

        String borderRightWidth = cssMap.get(CSS.Property.BORDER_RIGHT_WIDTH);
        if (borderRightWidth != null) {
            img.setBorderWidthRight(utils.parseValueToPt(borderRightWidth, 1f));
        }

        String borderBottomColor = cssMap.get(CSS.Property.BORDER_BOTTOM_COLOR);
        if (borderBottomColor != null) {
            img.setBorderColorBottom(HtmlUtilities.decodeColor(borderBottomColor));
        }

        String borderBottomWidth = cssMap.get(CSS.Property.BORDER_BOTTOM_WIDTH);
        if (borderBottomWidth != null) {
            img.setBorderWidthBottom(utils.parseValueToPt(borderBottomWidth, 1f));
        }

        String borderLeftColor = cssMap.get(CSS.Property.BORDER_LEFT_COLOR);
        if (borderLeftColor != null) {
            img.setBorderColorLeft(HtmlUtilities.decodeColor(borderLeftColor));
        }

        String borderLeftWidth = cssMap.get(CSS.Property.BORDER_LEFT_WIDTH);
        if (borderLeftWidth != null) {
            img.setBorderWidthLeft(utils.parseValueToPt(borderLeftWidth, 1f));
        }
        // end of border CSS

        String before = cssMap.get(CSS.Property.BEFORE);
        if (before != null) {
            img.setSpacingBefore(Float.parseFloat(before));
        }
        String after = cssMap.get(CSS.Property.AFTER);
        if (after != null) {
            img.setSpacingAfter(Float.parseFloat(after));
        }

        img.setWidthPercentage(0);
        return img;
    }
}