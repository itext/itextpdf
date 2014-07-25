/*
 * $Id: XfaXpathConstructor.java 6134 2013-12-23 13:15:14Z blowagie $
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2014 iText Group NV
 * Authors: Pavel Alay, Bruno Lowagie, et al.
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
package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.security.XpathConstructor;

/**
 * Constructor for xpath expression for signing XfaForm
 */
public class XfaXpathConstructor implements XpathConstructor {

    /**
     * Possible xdp packages to sign
     */
    public enum XdpPackage {
        Config,
        ConnectionSet,
        Datasets,
        LocaleSet,
        Pdf,
        SourceSet,
        Stylesheet,
        Template,
        Xdc,
        Xfdf,
        Xmpmeta
    }

    private final String CONFIG = "config";
    private final String CONNECTIONSET = "connectionSet";
    private final String DATASETS = "datasets";
    private final String LOCALESET = "localeSet";
    private final String PDF = "pdf";
    private final String SOURCESET = "sourceSet";
    private final String STYLESHEET = "stylesheet";
    private final String TEMPLATE = "template";
    private final String XDC = "xdc";
    private final String XFDF = "xfdf";
    private final String XMPMETA = "xmpmeta";

    /**
     * Empty constructor, no transform.
     */
    public XfaXpathConstructor() {
        this.xpathExpression = "";
    }

    /**
     * Construct for XPath expression. Depends from selected xdp package.
     * @param xdpPackage
     */
    public XfaXpathConstructor(XdpPackage xdpPackage) {
        String strPackage;
        switch (xdpPackage) {
            case Config:
                strPackage = CONFIG;
                break;
            case ConnectionSet:
                strPackage = CONNECTIONSET;
                break;
            case Datasets:
                strPackage = DATASETS;
                break;
            case LocaleSet:
                strPackage = LOCALESET;
                break;
            case Pdf:
                strPackage = PDF;
                break;
            case SourceSet:
                strPackage = SOURCESET;
                break;
            case Stylesheet:
                strPackage = STYLESHEET;
                break;
            case Template:
                strPackage = TEMPLATE;
                break;
            case Xdc:
                strPackage = XDC;
                break;
            case Xfdf:
                strPackage = XFDF;
                break;
            case Xmpmeta:
                strPackage = XMPMETA;
                break;
            default:
                xpathExpression = "";
                return;
        }

        StringBuilder builder = new StringBuilder("/xdp:xdp/*[local-name()='");
        builder.append(strPackage);
        builder.append("']");
        xpathExpression = builder.toString();
    }

    private String xpathExpression;

    /**
     * Get XPath expression
     */
    public String getXpathExpression() {
        return xpathExpression;
    }
}
