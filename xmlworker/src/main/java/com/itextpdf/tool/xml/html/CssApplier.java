/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itextpdf.tool.xml.html;

import com.itextpdf.text.Element;
import com.itextpdf.tool.xml.Tag;
import com.itextpdf.tool.xml.css.apply.MarginMemory;
import com.itextpdf.tool.xml.css.apply.PageSizeContainable;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;

/**
 *
 * @author benoit
 */
public interface CssApplier<T extends Element> {
    T apply(T e, final Tag t, final MarginMemory mm, final PageSizeContainable psc, final HtmlPipelineContext ctx);
}
