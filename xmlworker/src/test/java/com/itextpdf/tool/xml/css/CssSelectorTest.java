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
package com.itextpdf.tool.xml.css;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.itextpdf.text.log.LoggerFactory;
import com.itextpdf.text.log.SysoLogger;
import com.itextpdf.tool.xml.Tag;

/**
 * @author redlab_b
 *
 */
public class CssSelectorTest {

	private CssSelector css;
	private Tag root;
	private Tag rChild;
	private Tag idroot;
	@Before
	public void setup() {
		LoggerFactory.getInstance().setLogger(new SysoLogger(3));
		css = new CssSelector();
		root = new Tag("root");
		rChild = new Tag("rChild");
		rChild.setParent(root);
		root.getChildren().add(rChild);

		Map<String, String> rootAttr = new HashMap<String, String>();
		rootAttr.put("class", "rootClass");
		rootAttr.put("id", "rootId");
		idroot = new Tag("root", rootAttr);
		Map<String, String> classAttr = new HashMap<String, String>();
		classAttr.put("class", "childClass");
		classAttr.put("id", "childId");
	}

	@Test
	public void validateRootSelector() {
		Set<String> rootSelectors = css.createTagSelectors(root);
		Assert.assertTrue("Not found root", rootSelectors.contains("root"));
		Assert.assertEquals("Too many entries",1,  rootSelectors.size());
	}

	@Test
	public void validateChildSelectors() {
		Set<String> rootSelectors = css.createTagSelectors(root);
		Set<String> childSelectors = css.createTagSelectors(rChild);
		Assert.assertTrue("Not found root", rootSelectors.contains("root"));
		Assert.assertEquals("Too many entries for root",1,  rootSelectors.size());
		Assert.assertEquals("Too many entries for child",6,  childSelectors.size());
		Assert.assertTrue("Not found root>rChild", childSelectors.contains("root>rChild"));
		Assert.assertTrue("Not found root+rChild", childSelectors.contains("root+rChild"));
		Assert.assertTrue("Not found rChild", childSelectors.contains("rChild"));
		Assert.assertTrue("Not found root + rChild", childSelectors.contains("root + rChild"));
		Assert.assertTrue("Not found root > rChild", childSelectors.contains("root > rChild"));
		Assert.assertTrue("Not found root rChild", childSelectors.contains("root rChild"));
	}
	@Test
	public void validateIdRootSelector() {
		Set<String> rootSelectors = css.createAllSelectors(idroot);
		Assert.assertTrue("Not found root", rootSelectors.contains("root"));
		Assert.assertTrue("Not found rootId", rootSelectors.contains("#rootId"));
		Assert.assertTrue("Not found rootClass", rootSelectors.contains(".rootClass"));
		Assert.assertEquals("Too many entries",5,  rootSelectors.size());
	}

	@Test
	public void createClassSelectorsMultipleCSSClasses() {
		Tag t = new Tag("dummy");
		t.getAttributes().put("class", "klass1 klass2 klass3");
		Set<String> set = css.createClassSelectors(t);
		Assert.assertEquals("should have found 6 selectors", 6, set.size());
	}

}
