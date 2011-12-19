package com.itextpdf.tool.xml.util;

import java.util.ArrayList;
import java.util.List;

import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.html.HTML;

/**
 * Util class for everything related to a ParentTree of a Tag
 * 
 * 
 * @author Jeroen Nouws
 *
 */
public class ParentTreeUtil {
	
	/**
	 * creates a parentTree of all parents of a given Tag
	 * 
	 * @param t Tag the tag where you want to know it's parentTree of
	 * @return parentTree a arrayList<String> containing the parents of Tag t
	 */
	public List<String> getParentTree(Tag t){
		List<String> parentTree = new ArrayList<String>();
		while(t.getParent()!=null&&!(t.getParent().getName().equals(HTML.Tag.BODY))){
			parentTree.add(t.getParent().getName());
			t=t.getParent();
		}
		return parentTree;
	}
}
