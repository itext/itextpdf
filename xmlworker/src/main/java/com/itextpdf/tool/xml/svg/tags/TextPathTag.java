/*
 * $Id$
 *
 * This file is part of the iText (R) project.
 * Copyright (c) 1998-2013 1T3XT BVBA
 * Authors: VVB, Bruno Lowagie, et al.
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
package com.itextpdf.tool.xml.svg.tags;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.NoCustomContextException;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.WorkerContext;
import com.itextpdf.tool.xml.exceptions.RuntimeWorkerException;
import com.itextpdf.tool.xml.svg.SvgPipelineContext;
import com.itextpdf.tool.xml.svg.graphic.Path;
import com.itextpdf.tool.xml.svg.graphic.TextGroup;
import com.itextpdf.tool.xml.svg.graphic.TextPathGroup;


public class TextPathTag extends TextTag {
	@Override
	public List<Element> end(WorkerContext ctx, Tag tag,
				List<Element> currentContent) {
		Path path = null;
		try{
			Map<String, String> attributes = tag.getAttributes();
			 SvgPipelineContext context = getSvgPipelineContext(ctx);
			 String id = attributes.get("xlink:href");
			 if(id != null){
				 List<Element> list = context.getSymbolById(id.substring(1));
				 //TODO need to check that the element list contains only one element 
				 //and the type needs to be Path
				 if(list != null && list.size() == 1){
					path = (Path) list.get(0);
				 }
			 }
		} catch (NoCustomContextException e) {
			throw new RuntimeWorkerException(e);
		}
		//if the path is not found, do exactly the same as when Text
		if(path == null && currentContent.size() > 0){
			TextGroup group = 
				new TextGroup(currentContent, 0, 0, 500f, 500f, tag.getCSS());
			List<Element> l = new ArrayList<Element>(1);
			l.add(group);
			return l;
		}else if(currentContent.size() > 0){
			TextPathGroup group = 
				new TextPathGroup(currentContent, 0, 0, 500f, 500f, tag.getCSS(), path);
			List<Element> l = new ArrayList<Element>(1);
			l.add(group);
			return l;
		}
		//draw the content
		return new ArrayList<Element>(0);       
	}

}
