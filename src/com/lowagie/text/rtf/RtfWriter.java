/*
 * $Id$
 * $Name$
 *
 * Copyright 2001 by Mark Hall
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Library General Public License as published
 * by the Free Software Foundation; either versioni 2 of the License, or any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 *
 */

package com.lowagie.text.rtf;

import com.lowagie.text.*;

import java.io.*;
import java.util.*;
import java.awt.Color;

public class RtfWriter extends DocWriter implements DocListener
{
    
  /** Static Constants */
    
  /** General */
    private static final byte escape = (byte) '\\';
    
    private static final byte[] extendedEscape = "\\*\\".getBytes();
    
    private static final byte delimiter = (byte) ' ';
    
    private static final byte commaDelimiter = (byte) ';';
    
    private static final byte openGroup = (byte) '{';
    
    private static final byte closeGroup = (byte) '}';
    
    
  /** RTF Information */
    private static final byte[] docBegin = "rtf1".getBytes();
    
    private static final byte[] ansi = "ansi".getBytes();
    
    private static final byte[] ansiCodepage = "ansicpg".getBytes();
    
    
  /** Font Data */
    private static final byte[] fontTable = "fonttbl".getBytes();
    
    private static final byte fontNumber = (byte) 'f';
    
    private static final byte[] fontSize = "fs".getBytes();
    
    private static final byte[] fontColor = "cf".getBytes();
    
    private static final byte[] fontModern = "fmodern".getBytes();
    
    private static final byte[] fontSwiss = "fswiss".getBytes();
    
    private static final byte[] fontRoman = "froman".getBytes();
    
    private static final byte[] fontTech = "ftech".getBytes();
    
    private static final byte[] fontCharset = "fcharset".getBytes();
    
    private static final byte[] fontCourier = "Courier".getBytes();
    
    private static final byte[] fontArial = "Arial".getBytes();
    
    private static final byte[] fontSymbol = "Symbol".getBytes();
    
    private static final byte[] fontTimesNewRoman = "Times New Roman".getBytes();
    
    private static final byte[] fontWindings = "Windings".getBytes();
    
    
  /** Sections / Paragraphs */
    private static final byte[] sectionDefaults = "sectd".getBytes();
    
    private static final byte[] section = "sect".getBytes();
    
    private static final byte[] paragraph = "par".getBytes();
    
    private static final byte[] paragraphDefaults = "pard".getBytes();
    
    
  /** Lists */
    private static final byte[] listText = "pntext".getBytes();
    
    private static final byte[] bulletCharacter = "'B7".getBytes();
    
    private static final byte[] tab = "tab".getBytes();
    
    private static final byte[] listBegin = "pn".getBytes();
    
    private static final byte[] lvlbodyNumbering = "pnlvlbody".getBytes();
    
    private static final byte[] decimalNumbering = "pndec".getBytes();
    
    private static final byte[] listBullets = "pnlvlblt".getBytes();
    
    private static final byte[] listFontNumber = "pnf".getBytes();
    
    private static final byte[] listFirstIndent  = "pnindent".getBytes();
    
    private static final byte[] listBulletDefine  = "pntextb".getBytes();
    
    private static final byte[] firstIndent = "fi-".getBytes();
    
    private static final byte[] listIndent = "li".getBytes();
    
    
  /** Text Style */
    private static final byte bold = (byte) 'b';
    
    private static final byte italic = (byte) 'i';
    
    private static final byte[] underline = "ul".getBytes();
    
    private static final byte[] strikethrough = "strike".getBytes();
    
    private static final byte[] alignLeft = "ql".getBytes();
    
    private static final byte[] alignCenter = "qc".getBytes();
    
    private static final byte[] alignRight = "qr".getBytes();
    
    
  /** Colors */
    private static final byte[] colorTable = "colortbl".getBytes();
    
    private static final byte[] colorRed = "red".getBytes();
    
    private static final byte[] colorGreen = "green".getBytes();
    
    private static final byte[] colorBlue = "blue".getBytes();
    
    
  /** Information Group */
    private static final byte[] infoBegin = "info".getBytes();
    
    private static final byte[] metaAuthor = "author".getBytes();
    
    private static final byte[] metaSubject = "subject".getBytes();
    
    private static final byte[] metaKeywords = "keywords".getBytes();
    
    private static final byte[] metaTitle = "title".getBytes();
    
    private static final byte[] metaProducer = "operator".getBytes();
    
    private static final byte[] metaCreationDate = "creationdate".getBytes();
    
    private static final byte[] year = "yr".getBytes();
    
    private static final byte[] month = "mo".getBytes();
    
    private static final byte[] day = "dy".getBytes();
    
    private static final byte[] hour = "hr".getBytes();
    
    private static final byte[] minute = "min".getBytes();
    
    private static final byte[] second = "sec".getBytes();
    
    
  /** Tables */
    private static final byte[] tableRowBegin = "trowd".getBytes();
    
    private static final byte[] tableRowEnd = "row".getBytes();
    
    private static final byte[] cellRightPosition = "cellx".getBytes();
    
    private static final byte[] cellEnd = "cell".getBytes();
    
    private static final byte[] inTableBegin = "intbl".getBytes();
    
    private static final byte[] tableAlignLeft = "trql".getBytes();
    
    private static final byte[] tableAlignCenter = "trqc".getBytes();
    
    private static final byte[] tableAlignRight = "trqr".getBytes();
    
    private static final byte[] mergeHorizontalBegin = "clmgf".getBytes();
    
    private static final byte[] mergeHorizontal = "clmrg".getBytes();
    
    private static final byte[] mergeVerticalBegin = "clvmgf".getBytes();
    
    private static final byte[] mergeVertical = "clvmrg".getBytes();
    
    private static final byte[] tablePaddingLeft = "trpaddl".getBytes();
    
    private static final byte[] tablePaddingRight = "trpaddr".getBytes();
    
    private static final byte[] cellVertAlignBottom = "clvertalt".getBytes();
    
    private static final byte[] cellVertAlignMiddle = "clvertalc".getBytes();
    
    private static final byte[] cellVertAlignTop = "clvertalb".getBytes();
    
    
  /** Header / Footer */
    private static final byte[] headerBegin = "header".getBytes();
    
    private static final byte[] footerBegin = "footer".getBytes();
    
    
  /** Paper Properties */
    private static final byte[] rtfPaperWidth = "paperw".getBytes();
    
    private static final byte[] rtfPaperHeight = "paperh".getBytes();
    
    private static final byte[] rtfMarginLeft = "margl".getBytes();
    
    private static final byte[] rtfMarginRight = "margr".getBytes();
    
    private static final byte[] rtfMarginTop = "margt".getBytes();
    
    private static final byte[] rtfMarginBottom = "margb".getBytes();
    
    
  /** Annotations */
    private static final byte[] annotationID = "atnid".getBytes();
    
    private static final byte[] annotationAuthor = "atnauthor".getBytes();
    
    private static final byte[] annotation = "annotation".getBytes();
    
    
  /** Images */
    private static final byte[] imageGifPng = "pngblibp".getBytes();
    
    private static final byte[] imageJpeg = "jpegblibp".getBytes();
    
    private static final byte[] pictGroupBegin = "shppict".getBytes();
    
    private static final byte[] picture = "pict".getBytes();
    
    private static final byte[] pictureWidth = "picwgoal".getBytes();
    
    private static final byte[] pictureHeight = "pichgoal".getBytes();
    
    private static final byte[] binaryData = "bin".getBytes();
    
    
    
  /** Class variables */
    
  /** Because of the way RTF works and the way itext works, the text has to be
   *  stored and is only written to the actual OutputStream at the end */
    
    private static Vector fontList = new Vector();
    
    private static Vector colorList = new Vector();
    
    private static ByteArrayOutputStream content = null;
    
    private static ByteArrayOutputStream info = null;
    
    private static ByteArrayOutputStream[] rows = null;
    
    private static int listFont = 0;
    
    private static int rowNr = 0;
    
    private static boolean tableProcessing = false;
    
    private static HeaderFooter header = null;
    
    private static HeaderFooter footer = null;
    
    private static int marginLeft = 1800;
    
    private static int marginRight = 1800;
    
    private static int marginTop = 1440;
    
    private static int marginBottom = 1440;
    
    private static int pageWidth = 12240;
    
    private static int pageHeight = 15840;
    
    private static double twipsFactor = 20.5714;
    
    private static int currentAnnotationID = 0;
    
    
  /** Protected Constructor */
    protected RtfWriter(Document doc, OutputStream os)
    {
        super(doc, os);
        document.addDocListener(this);
        writeDocumentBegin();
        fontList.clear();
        colorList.clear();
        info = new ByteArrayOutputStream();
        content = new ByteArrayOutputStream();
        document.addProducer();
        document.addCreationDate();
    }
    
    
  /** Public functions from the DocWriter Interface */
    
  /** Create a new RtfWriter */
    public static RtfWriter getInstance(Document document, OutputStream os)
    {
        return(new RtfWriter(document, os));
    }
    
  /** Add an Element */
    public boolean add(Element element)
    {
        ByteArrayOutputStream cOut = null;
        if(tableProcessing) { cOut = rows[rowNr]; } else { cOut = content; }
        try
        {
            switch(element.type())
            {
                case Element.LISTITEM   : writeListElement((ListItem) element, cOut);       break;
                case Element.LIST       : writeList((com.lowagie.text.List) element, cOut); break;
                case Element.CHAPTER    :
                case Element.SECTION    : writeSection((Section) element, cOut);            break;
                case Element.PARAGRAPH  : writeParagraph((Paragraph) element, cOut);        break;
                case Element.CHUNK      : writeChunk((Chunk) element, cOut);                break;
                case Element.ANCHOR     :
                case Element.PHRASE     : element.process(this);                            break;
                case Element.TABLE      : writeTable((Table) element);                      break;
                case Element.ANNOTATION : writeAnnotation((Annotation) element, cOut);      break;
                case Element.GIF        :
                case Element.PNG        : writeImage((Image) element, imageGifPng, cOut);   break;
                case Element.JPEG       : writeImage((Image) element, imageJpeg, cOut);     break;
                
                case Element.AUTHOR       : writeMeta(metaAuthor, (Meta) element);       break;
                case Element.SUBJECT      : writeMeta(metaSubject, (Meta) element);      break;
                case Element.KEYWORDS     : writeMeta(metaKeywords, (Meta) element);     break;
                case Element.TITLE        : writeMeta(metaTitle, (Meta) element);        break;
                case Element.PRODUCER     : writeMeta(metaProducer, (Meta) element);     break;
                case Element.CREATIONDATE : writeMeta(metaCreationDate, (Meta) element); break;
            }
        }
        catch(Exception e)
        {
            System.out.println("Error: "+e);
            return false;
        }
        return true;
    }
    
    public void open()
    {
        super.open();
    }
    
    public void close()
    {
        writeDocumentEnd();
        super.close();
    }
    
    public void setFooter(HeaderFooter footer)
    {
        this.footer = footer;
    }
    
    public void setHeader(HeaderFooter header)
    {
        this.header = header;
    }
    
    public void resetFooter()
    {
        this.footer = null;
    }
    
    public void resetHeader()
    {
        this.header = null;
    }
    
    public boolean newPage()
    {
        try
        {
            content.write(escape);
            content.write(section);
            content.write(escape);
            content.write(sectionDefaults);
        }
        catch(IOException e)
        {
            System.err.println("Error: "+e);
            return false;
        }
        return true;
    }
    
    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom)
    {
        this.marginLeft = (int) (marginLeft * twipsFactor);
        this.marginRight = (int) (marginRight * twipsFactor);
        this.marginTop = (int) (marginTop * twipsFactor);
        this.marginBottom = (int) (marginBottom * twipsFactor);
        return true;
    }
    
    public boolean setPageSize(Rectangle pageSize)
    {
        pageWidth = (int) (pageSize.width() * twipsFactor);
        pageHeight = (int) (pageSize.height() * twipsFactor);
        return true;
    }
    
  /** Private functions */
    
  /** Write a list Item */
    private void writeListElement(ListItem listItem, ByteArrayOutputStream out) throws IOException
    {
        out.write(openGroup);
        out.write(escape);
        out.write(listText);
        out.write(escape);
        out.write(fontNumber);
        writeInt(out, listFont);
        out.write(escape);
        out.write(bulletCharacter);
        out.write(escape);
        out.write(tab);
        out.write(closeGroup);
        listItem.process(this);
        out.write(escape);
        out.write(paragraph);
    }
    
  /** List initialisation and wrapper */
    private void writeList(com.lowagie.text.List list, ByteArrayOutputStream out) throws IOException
    {
        out.write(openGroup);
        out.write(extendedEscape);
        out.write(listBegin);
        out.write(escape);
        if(list.isNumbered())
        {
            Font bulletFont = new Font(Font.COURIER, 10, Font.NORMAL, new Color(0, 0, 0));
            listFont = addFont(bulletFont);
            out.write(lvlbodyNumbering);
            out.write(escape);
            out.write(decimalNumbering);
        }
        else
        {
            Font bulletFont = new Font(Font.SYMBOL, 10, Font.NORMAL, new Color(0, 0, 0));
            listFont = addFont(bulletFont);
            out.write(listBullets);
        }
        out.write(escape);
        out.write(listFontNumber);
        writeInt(out, listFont);
        out.write(escape);
        out.write(listFirstIndent);
        writeInt(out, 0);
        out.write(closeGroup);
        out.write(escape);
        out.write(firstIndent);
        writeInt(out, 220);
        out.write(escape);
        out.write(listIndent);
        writeInt(out, 320+(int)(list.indentationLeft()*twipsFactor));
        list.process(this);
        out.write(escape);
        out.write(paragraphDefaults);
        out.write(escape);
        out.write(paragraph);
    }
    
  /** Section initialisation and wrapper */
    private void writeSection(Section sectionElement, ByteArrayOutputStream out) throws IOException
    {
        if(sectionElement.type() == Element.CHAPTER)
        {
            out.write(escape);
            out.write(sectionDefaults);
        }
        if(sectionElement.title() != null)
        {
            out.write(escape);
            out.write(paragraphDefaults);
            sectionElement.title().process(this);
            out.write(escape);
            out.write(paragraph);
            out.write(escape);
            out.write(paragraph);
        }
        sectionElement.process(this);
        out.write(escape);
        out.write(paragraph);
        out.write(escape);
        out.write(paragraph);
        if(sectionElement.type() == Element.CHAPTER)
        {
            out.write(escape);
            out.write(section);
        }
    }
    
  /** Paragraph initialisation and wrapper */
    private void writeParagraph(Paragraph paragraphElement, ByteArrayOutputStream out) throws IOException
    {
        out.write(escape);
        out.write(paragraphDefaults);
        out.write(escape);
        switch(paragraphElement.alignment())
        {
            case Element.ALIGN_LEFT   : out.write(alignLeft);   break;
            case Element.ALIGN_CENTER : out.write(alignCenter); break;
            case Element.ALIGN_RIGHT  : out.write(alignRight);  break;
        }
        out.write(escape);
        out.write(listIndent);
        writeInt(out, (int) (paragraphElement.indentationLeft()*twipsFactor));
        paragraphElement.process(this);
        out.write(escape);
        out.write(paragraph);
    }
    
  /** Write a Chunk and all the font properties it has */
    private void writeChunk(Chunk chunk, ByteArrayOutputStream out) throws IOException
    {
        out.write(escape);
        out.write(fontNumber);
        if(chunk.font().family() != -1) { writeInt(out, addFont(chunk.font())); } else { writeInt(out, 0); }
        out.write(escape);
        out.write(fontSize);
        if(chunk.font().size() > 0) { writeInt(out, (int) chunk.font().size() * 2); } else { writeInt(out, 20); }
        out.write(escape);
        out.write(fontColor);
        writeInt(out, addColor(chunk.font().color()));
        switch(chunk.font().style())
        {
            case Font.BOLD       :
                out.write(escape);
                out.write(bold);
                break;
            case Font.ITALIC     :
                out.write(escape);
                out.write(italic);
                break;
            case Font.UNDERLINE  :
                out.write(escape);
                out.write(underline);
                break;
            case Font.STRIKETHRU :
                out.write(escape);
                out.write(strikethrough);
                break;
            case Font.BOLDITALIC :
                out.write(escape);
                out.write(bold);
                out.write(escape);
                out.write(italic);
                break;
        }
        out.write(delimiter);
        out.write(chunk.content().getBytes());
        switch(chunk.font().style())
        {
            case Font.BOLD :
                out.write(escape);
                out.write(bold);
                writeInt(out, 0);
                break;
            case Font.ITALIC :
                out.write(escape);
                out.write(italic);
                writeInt(out, 0);
                break;
            case Font.UNDERLINE :
                out.write(escape);
                out.write(underline);
                writeInt(out, 0);
                break;
            case Font.STRIKETHRU :
                out.write(escape);
                out.write(strikethrough);
                writeInt(out, 0);
                break;
            case Font.BOLDITALIC :
                out.write(escape);
                out.write(bold);
                writeInt(out,0);
                out.write(escape);
                out.write(italic);
                writeInt(out, 0);
                break;
        }
    }
    
  /** Write a Meta Tag. They are stored in the Info group */
    private void writeMeta(byte[] metaName, Meta meta) throws IOException
    {
        info.write(openGroup);
        info.write(escape);
        info.write(metaName);
        info.write(delimiter);
        if(meta.type() != Element.CREATIONDATE) { info.write(meta.content().getBytes()); } else { writeFormatedDateTime(meta.content()); }
        info.write(closeGroup);
    }
    
    private void writeFormatedDateTime(String date) throws IOException
    {
        String dummyStr = "";
        
        writeDateTimeNumber(year, date, 25, 29);
        info.write(escape);
        info.write(month);
        dummyStr = date.substring(4, 7);
        if(dummyStr.equals("Jan")) { writeInt(info, 1); }
        else if (dummyStr.equals("Feb")) { writeInt(info, 2); }
        else if (dummyStr.equals("Mar")) { writeInt(info, 3); }
        else if (dummyStr.equals("Apr")) { writeInt(info, 4); }
        else if (dummyStr.equals("May")) { writeInt(info, 5); }
        else if (dummyStr.equals("Jun")) { writeInt(info, 6); }
        else if (dummyStr.equals("Jul")) { writeInt(info, 7); }
        else if (dummyStr.equals("Aug")) { writeInt(info, 8); }
        else if (dummyStr.equals("Sep")) { writeInt(info, 9); }
        else if (dummyStr.equals("Okt")) { writeInt(info, 10); }
        else if (dummyStr.equals("Nov")) {  writeInt(info, 11);}
        else { writeInt(info, 12); }
        writeDateTimeNumber(day, date, 8, 10);
        writeDateTimeNumber(hour, date, 11, 13);
        writeDateTimeNumber(minute, date, 14, 16);
        writeDateTimeNumber(second, date, 17, 19);
    }
    
    private void writeDateTimeNumber(byte [] identifier, String date, int begin, int end) throws IOException
    {
        int dummyInt = 0;
        info.write(escape);
        info.write(identifier);
        dummyInt = Integer.valueOf(date.substring(begin, end)).intValue();
        writeInt(info, dummyInt);
    }
    
  /** Write a table
   *
   *  ATTENTION. THIS IS A KLUDGE
   *
   *  It's ugly and it's slow and it works. All I can say */
    private void writeTable(Table table) throws IOException
    {
        rows = new ByteArrayOutputStream[table.size()];
        Row currentRow = null;
        Element element = null;
        Iterator rowIterator = table.iterator();
        Iterator cellIterator = null;
        Vector emptyCells = new Vector();
        int tableWidth = 8500;
        String dummyStr = null;
        
        if(!table.absWidth().equals("")) { tableWidth = Integer.parseInt(table.absWidth()) * 29; }
        for(int i = 0; i < table.size(); i++)
        { rows[i] = new ByteArrayOutputStream(); }
        rowNr = 0;
        tableProcessing = true;
        
        for(int i = 0; i < table.size(); i++)
        {
            currentRow = (Row) rowIterator.next();
            rowNr = i;
            rows[i].write(escape);
            rows[i].write(tableRowBegin);
            rows[i].write(escape);
            rows[i].write(inTableBegin);
            rows[i].write(escape);
            switch(table.alignment())
            {
                case Element.ALIGN_LEFT   : rows[i].write(tableAlignLeft);   break;
                case Element.ALIGN_CENTER : rows[i].write(tableAlignCenter); break;
                case Element.ALIGN_RIGHT  : rows[i].write(tableAlignRight);  break;
            }
            rows[i].write(escape);
            rows[i].write(tablePaddingLeft);
            writeInt(rows[i], 10);
            rows[i].write(escape);
            rows[i].write(tablePaddingRight);
            writeInt(rows[i], 10);
            for(int j = 0; j < currentRow.columns(); j++)
            {
                Cell currentCell = null;
                try {
                    currentCell = (Cell)currentRow.getCell(j);
                    if (currentCell == null) {
                        continue;
                    }
                }
                catch(ClassCastException cce) {
                    continue;
                }
                if(emptyCells.indexOf(new String(j+"-"+i+"h")) == -1)
                {
                    if(currentCell.colspan() != 1)
                    {
                        rows[i].write(escape);
                        rows[i].write(mergeHorizontalBegin);
                        for(int k = j + 1; k <= j + currentCell.colspan()-1; k++)
                        { emptyCells.add(new String(k+"-"+i+"h")); }
                    }
                }
                else
                {
                    rows[i].write(escape);
                    rows[i].write(mergeHorizontal);
                }
                if(emptyCells.indexOf(new String(j+"-"+i+"v")) == -1)
                {
                    if(currentCell.rowspan() != 1)
                    {
                        rows[i].write(escape);
                        rows[i].write(mergeVerticalBegin);
                        for(int k = i + 1; k <= i + currentCell.rowspan()-1; k++)
                        { emptyCells.add(new String(j+"-"+k+"v")); }
                    }
                }
                else
                {
                    rows[i].write(escape);
                    rows[i].write(mergeVertical);
                }
                if(currentRow.getCell(j) != null)
                {
                    rows[i].write(escape);
                    switch(currentCell.verticalAlignment())
                    {
                        case Element.ALIGN_BASELINE :
                        case Element.ALIGN_BOTTOM   : rows[i].write(cellVertAlignBottom); break;
                        case Element.ALIGN_MIDDLE   : rows[i].write(cellVertAlignMiddle); break;
                        case Element.ALIGN_TOP      : rows[i].write(cellVertAlignTop);    break;
                        default                     : rows[i].write(cellVertAlignBottom);
                    }
                }
                rows[i].write(escape);
                rows[i].write(cellRightPosition);
                writeInt(rows[i], (int) tableWidth / currentRow.columns() * (j+1) );
            }
            rows[i].write(openGroup);
            for(int j = 0; j < currentRow.columns(); j++)
            {
                if((emptyCells.indexOf(new String(j+"-"+i+"h")) == -1) && (emptyCells.indexOf(new String(j+"-"+i+"v")) == -1))
                {
                    Cell currentCell = null;
                    try {
                        currentCell = (Cell)currentRow.getCell(j);
                        if (currentCell == null) {
                            continue;
                        }
                    }
                    catch(ClassCastException cce) {
                        continue;
                    }
                    for(cellIterator = currentCell.getElements(); cellIterator.hasNext();)
                    {
                        element = (Element) cellIterator.next();
                        rows[i].write(escape);
                        rows[i].write(paragraphDefaults);
                        rows[i].write(escape);
                        rows[i].write(inTableBegin);
                        rows[i].write(escape);
                        switch(currentCell.horizontalAlignment())
                        {
                            case Element.ALIGN_LEFT   : rows[i].write(alignLeft);   break;
                            case Element.ALIGN_CENTER : rows[i].write(alignCenter); break;
                            case Element.ALIGN_RIGHT  : rows[i].write(alignRight);  break;
                        }
                        element.process(this);
                        rows[i].write(escape);
                        rows[i].write(paragraph);
                    }
                }
                rows[i].write(escape);
                rows[i].write(cellEnd);
            }
            rows[i].write(closeGroup);
            rows[i].write(escape);
            rows[i].write(tableRowEnd);
        }
        tableProcessing = false;
        for(int i = 0; i < rows.length; i++)
        { rows[i].writeTo(content); }
        content.write(escape);
        content.write(paragraphDefaults);
        content.write(escape);
        content.write(paragraph);
    }
    
  /** Write an Annotation */
    private void writeAnnotation(Annotation annotationElement, ByteArrayOutputStream out) throws IOException
    {
        out.write(openGroup);
        out.write(escape);
        out.write(annotationID);
        writeInt(out, currentAnnotationID);
        out.write(closeGroup);
        out.write(openGroup);
        out.write(escape);
        out.write(annotationAuthor);
        out.write("itext".getBytes());
        out.write(closeGroup);
        out.write(openGroup);
        out.write(escape);
        out.write(annotation);
        out.write(delimiter);
        annotationElement.process(this);
        out.write(closeGroup);
    }
    
  /** Write an Image
   *
   *  I'm having problems with this, because image.rawData() always return null
   *  Don't know why */
    private void writeImage(Image image, byte[] imageType, ByteArrayOutputStream out) throws IOException
    {
        if(image.rawData() != null)
        {
            out.write(escape);
            switch(image.alignment())
            {
                case Element.ALIGN_LEFT   : out.write(alignLeft);   break;
                case Element.ALIGN_CENTER : out.write(alignCenter); break;
                case Element.ALIGN_RIGHT  : out.write(alignRight);  break;
                default                   : out.write(alignLeft);
            }
            out.write(openGroup);
            out.write(extendedEscape);
            out.write(pictGroupBegin);
            out.write(openGroup);
            out.write(escape);
            out.write(picture);
            out.write(escape);
            out.write(imageType);
            out.write(escape);
            out.write(pictureWidth);
            writeInt(out, (int) image.scaledWidth());
            out.write(escape);
            out.write(pictureHeight);
            writeInt(out, (int) image.scaledHeight());
            out.write(escape);
            out.write(binaryData);
            out.write(delimiter);
            out.write(image.rawData());
            out.write(closeGroup);
            out.write(closeGroup);
        }
    }
    
  /** Write the Document Head */
    private boolean writeDocumentBegin()
    {
        try
        {
            os.write(openGroup);
            os.write(escape);
            os.write(docBegin);
            os.write(escape);
            os.write(ansi);
            os.write(escape);
            os.write(ansiCodepage);
            writeInt(os, 1252);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
  /** This adds a Font to the fontList if it is not in the list yet.
   *  The return value is the font's index in the list */
    private int addFont(Font newFont)
    {
        int fn = -1;
        
        for(int i = 0; i < fontList.size(); i++)
        {
            if(newFont.family() == ((Font)fontList.get(i)).family()) { fn = i; }
        }
        if(fn == -1)
        {
            fontList.add(newFont);
            return fontList.size()-1;
        }
        return fn;
    }
    
  /** Same as for the Font only for the Colors */
    private int addColor(Color newColor)
    {
        int cn = 0;
        if(newColor == null) { return cn; }
        cn = colorList.indexOf(newColor);
        if(cn == -1)
        {
            colorList.add(newColor);
            return colorList.size()-1;
        }
        return cn;
    }
    
  /** Writes all the accumulated data to the final OutputStream */
    private boolean writeDocumentEnd()
    {
        try
        {
            writeFontList();
            writeColorList();
            writeDocumentFormat();
            writeInfoList();
            writeHeader();
            writeFooter();
            content.writeTo(os);
            os.write(closeGroup);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    
  /** Write the fontList to the final OutputStream */
    private void writeFontList() throws IOException
    {
        Font fnt;
        
        os.write(openGroup);
        os.write(escape);
        os.write(fontTable);
        os.write(escape);
        os.write("deff0".getBytes());
        for(int i = 0; i < fontList.size(); i++)
        {
            fnt = (Font) fontList.get(i);
            os.write(openGroup);
            os.write(escape);
            os.write((byte) 'f');
            writeInt(os, i);
            os.write(escape);
            switch(fnt.family())
            {
                case Font.COURIER:
                    os.write(fontModern);
                    os.write(escape);
                    os.write(fontCharset);
                    writeInt(os, 0);
                    os.write(delimiter);
                    os.write(fontCourier);
                    break;
                case Font.HELVETICA:
                    os.write(fontSwiss);
                    os.write(escape);
                    os.write(fontCharset);
                    writeInt(os, 0);
                    os.write(delimiter);
                    os.write(fontArial);
                    break;
                case Font.SYMBOL:
                    os.write(fontRoman);
                    os.write(escape);
                    os.write(fontCharset);
                    writeInt(os, 2);
                    os.write(delimiter);
                    os.write(fontSymbol);
                    break;
                case Font.TIMES_NEW_ROMAN:
                    os.write(fontRoman);
                    os.write(escape);
                    os.write(fontCharset);
                    writeInt(os, 0);
                    os.write(delimiter);
                    os.write(fontTimesNewRoman);
                    break;
                case Font.ZAPFDINGBATS:
                    os.write(fontTech);
                    os.write(escape);
                    os.write(fontCharset);
                    writeInt(os, 0);
                    os.write(delimiter);
                    os.write(fontWindings);
                    break;
            }
            os.write(commaDelimiter);
            os.write(closeGroup);
        }
        os.write(closeGroup);
    }
    
  /** Write the colors to the final OutputStream */
    private void writeColorList() throws IOException
    {
        Color color = null;
        
        os.write(openGroup);
        os.write(escape);
        os.write(colorTable);
        for(int i = 0; i < colorList.size(); i++)
        {
            color = (Color) colorList.get(i);
            os.write(escape);
            os.write(colorRed);
            writeInt(os, color.getRed());
            os.write(escape);
            os.write(colorGreen);
            writeInt(os, color.getGreen());
            os.write(escape);
            os.write(colorBlue);
            writeInt(os, color.getBlue());
            os.write(commaDelimiter);
        }
        os.write(closeGroup);
    }
    
  /** Write the Information Group to the final OutputStream */
    private void writeInfoList() throws IOException
    {
        os.write(openGroup);
        os.write(escape);
        os.write(infoBegin);
        info.writeTo(os);
        os.write(closeGroup);
    }
    
  /** Write an integer to the OutputStream passed in out */
    private void writeInt(OutputStream out, int i) throws IOException
    {
        out.write(Integer.toString(i).getBytes());
    }
    
  /** Write the header */
    private void writeHeader() throws IOException
    {
        if(header != null)
        {
            os.write(openGroup);
            os.write(escape);
            os.write(headerBegin);
            rows = new ByteArrayOutputStream[1];
            rows[0] = new ByteArrayOutputStream();
            rowNr = 0;
            tableProcessing = true;
            header.process(this);
            tableProcessing = false;
            rows[0].writeTo(os);
            os.write(closeGroup);
        }
    }
    
  /** Write the footer */
    private void writeFooter() throws IOException
    {
        if(footer != null)
        {
            os.write(openGroup);
            os.write(escape);
            os.write(footerBegin);
            rows = new ByteArrayOutputStream[1];
            rows[0] = new ByteArrayOutputStream();
            rowNr = 0;
            tableProcessing = true;
            footer.process(this);
            tableProcessing = false;
            rows[0].writeTo(os);
            os.write(closeGroup);
        }
    }
    
  /** Write the Document's Paper properties */
    private void writeDocumentFormat() throws IOException
    {
        os.write(openGroup);
        os.write(escape);
        os.write(rtfPaperWidth);
        writeInt(os, pageWidth);
        os.write(escape);
        os.write(rtfPaperHeight);
        writeInt(os, pageHeight);
        os.write(escape);
        os.write(rtfMarginLeft);
        writeInt(os, marginLeft);
        os.write(escape);
        os.write(rtfMarginRight);
        writeInt(os, marginRight);
        os.write(escape);
        os.write(rtfMarginTop);
        writeInt(os, marginTop);
        os.write(escape);
        os.write(rtfMarginBottom);
        writeInt(os, marginBottom);
        os.write(closeGroup);
    }
    
}
