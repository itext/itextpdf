/*
 *
 * This file is part of the iText (R) project.
    Copyright (c) 1998-2017 iText Group NV
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
package com.itextpdf.tool.xml.html;

import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.css.CSS;
import com.itextpdf.tool.xml.css.CssUtils;
import com.itextpdf.tool.xml.exceptions.LocaleMessages;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author redlab_b
 *
 */
public class HorizontalRule extends AbstractTagProcessor {

	@Override
	public List<Element> start(WorkerContext ctx, Tag tag) {
		try {
			List<Element> list = new ArrayList<Element>();
			HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext(ctx);
			LineSeparator lineSeparator = (LineSeparator) getCssAppliers().apply(new LineSeparator(), tag, htmlPipelineContext);
			Paragraph p = new Paragraph();
            Map<String, String> css = tag.getCSS();
            float fontSize = 12;
            if (css.get(CSS.Property.FONT_SIZE) != null) {
                fontSize = CssUtils.getInstance().parsePxInCmMmPcToPt(css.get(CSS.Property.FONT_SIZE));
            }
            String marginTop = css.get(CSS.Property.MARGIN_TOP);
            if (marginTop == null) {
                marginTop = "0.5em";
            }
            String marginBottom = css.get(CSS.Property.MARGIN_BOTTOM);
            if (marginBottom == null) {
                marginBottom = "0.5em";
            }
            p.setSpacingBefore(p.getSpacingBefore() + CssUtils.getInstance().parseValueToPt(marginTop, fontSize));
            p.setSpacingAfter(p.getSpacingAfter() + CssUtils.getInstance().parseValueToPt(marginBottom, fontSize));
            p.setLeading(0);
			p.add(lineSeparator);
			list.add(p);
			return list;
		} catch (NoCustomContextException e) {
			throw new RuntimeWorkerException(LocaleMessages.getInstance().getMessage(LocaleMessages.NO_CUSTOM_CONTEXT), e);
		}
	}


}
