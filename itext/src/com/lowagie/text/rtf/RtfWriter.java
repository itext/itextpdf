/**
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Mark Hall
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the “GNU LIBRARY GENERAL PUBLIC LICENSE”), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text.rtf;

import com.lowagie.text.*;

import java.io.*;
import java.util.*;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

/**
 * A <CODE>DocWriter</CODE> class for Rich Text Files (RTF).
 * <P>
 * A <CODE>RtfWriter</CODE> can be added as a <CODE>DocListener</CODE>
 * to a certain <CODE>Document</CODE> by getting an instance.
 * Every <CODE>Element</CODE> added to the original <CODE>Document</CODE>
 * will be written to the <CODE>OutputStream</CODE> of this <CODE>RtfWriter</CODE>.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * // creation of the document with a certain size and certain margins
 * Document document = new Document(PageSize.A4, 50, 50, 50, 50);
 * try {
 *    // this will write RTF to the Standard OutputStream
 *    <STRONG>RtfWriter.getInstance(document, System.out);</STRONG>
 *    // this will write Rtf to a file called text.rtf
 *    <STRONG>RtfWriter.getInstance(document, new FileOutputStream("text.rtf"));</STRONG>
 *    // this will write Rtf to for instance the OutputStream of a HttpServletResponse-object
 *    <STRONG>RtfWriter.getInstance(document, response.getOutputStream());</STRONG>
 * }
 * catch(DocumentException de) {
 *    System.err.println(de.getMessage());
 * }
 * // this will close the document and all the OutputStreams listening to it
 * <STRONG>document.close();</CODE>
 * </PRE></BLOCKQUOTE>
 * <P>
 * <STRONG>LIMITATIONS</STRONG><BR>
 * There are currently still a few limitations on what the RTF Writer can do:
 * <UL>
 *  <LI>Only PNG / JPEG Images are supported.</LI>
 *  <LI>Rotating of Images is not supported.</LI>
 *  <LI>Nested Tables are not supported.</LI>
 *  <LI>The <CODE>Leading</CODE> is not supported.</LI>
 * </UL>
 * <br />
 * Parts of this Class were contributed by Steffen Stundzig. Many thanks for the
 * improvements.
 */
public class RtfWriter extends DocWriter implements DocListener
{
  /**
   * Static Constants
   */
    
  /**
   * General
   */
    
  /** This is the escape character which introduces RTF tags. */
    public static final byte escape = (byte) '\\';

  /** This is another escape character which introduces RTF tags. */
    private static final byte[] extendedEscape = "\\*\\".getBytes();

  /** This is the delimiter between RTF tags and normal text. */
    protected static final byte delimiter = (byte) ' ';

  /** This is another delimiter between RTF tags and normal text. */
    private static final byte commaDelimiter = (byte) ';';

  /** This is the character for beginning a new group. */
    public static final byte openGroup = (byte) '{';
    
  /** This is the character for closing a group. */
    public static final byte closeGroup = (byte) '}';
    
  /**
   * RTF Information
   */
    
  /** RTF begin and version. */
    private static final byte[] docBegin = "rtf1".getBytes();
    
  /** RTF encoding. */
    private static final byte[] ansi = "ansi".getBytes();
    
  /** RTF encoding codepage. */
    private static final byte[] ansiCodepage = "ansicpg".getBytes();
    
  /**
   *Font Data
   */
    
  /** Begin the font table tag. */
    private static final byte[] fontTable = "fonttbl".getBytes();
    
  /** Font number tag. */
    protected static final byte fontNumber = (byte) 'f';
    
  /** Font size tag. */
    protected static final byte[] fontSize = "fs".getBytes();

  /** Font color tag. */
    protected static final byte[] fontColor = "cf".getBytes();
    
  /** Modern font tag. */
    private static final byte[] fontModern = "fmodern".getBytes();
    
  /** Swiss font tag. */
    private static final byte[] fontSwiss = "fswiss".getBytes();
    
  /** Roman font tag. */
    private static final byte[] fontRoman = "froman".getBytes();
    
  /** Tech font tag. */
    private static final byte[] fontTech = "ftech".getBytes();
    
  /** Font charset tag. */
    private static final byte[] fontCharset = "fcharset".getBytes();
    
  /** Font Courier tag. */
    private static final byte[] fontCourier = "Courier".getBytes();
    
  /** Font Arial tag. */
    private static final byte[] fontArial = "Arial".getBytes();
    
  /** Font Symbol tag. */
    private static final byte[] fontSymbol = "Symbol".getBytes();
    
  /** Font Times New Roman tag. */
    private static final byte[] fontTimesNewRoman = "Times New Roman".getBytes();
    
  /** Font Windings tag. */
    private static final byte[] fontWindings = "Windings".getBytes();
    
  /** Default Font. */
    private static final byte[] defaultFont = "deff".getBytes();
    
  /** First indent tag. */
    private static final byte[] firstIndent = "fi".getBytes();
    
  /** List indent tag. */
    private static final byte[] listIndent = "li".getBytes();
    
  /**
   * Sections / Paragraphs
   */
    
  /** Reset section defaults tag. */
    private static final byte[] sectionDefaults = "sectd".getBytes();
    
  /** Begin new section tag. */
    private static final byte[] section = "sect".getBytes();

  /** Reset paragraph defaults tag. */
    public static final byte[] paragraphDefaults = "pard".getBytes();

  /** Begin new paragraph tag. */
    public static final byte[] paragraph = "par".getBytes();
    
  /**
   * Lists
   */

  /** Begin the List Table */
    private static final byte[] listtableGroup = "listtable".getBytes();
    
  /** Begin the List Override Table */
    private static final byte[] listoverridetableGroup = "listoverridetable".getBytes();
    
  /** Begin a List definition */
    private static final byte[] listDefinition = "list".getBytes();
    
  /** List Template ID */
    private static final byte[] listTemplateID = "listtemplateid".getBytes();
    
  /** RTF Writer outputs hybrid lists */
    private static final byte[] hybridList = "hybrid".getBytes();
    
  /** Current List level */
    private static final byte[] listLevelDefinition = "listlevel".getBytes();
    
  /** Level numbering (old) */
    private static final byte[] listLevelTypeOld = "levelnfc".getBytes();
    
  /** Level numbering (new) */
    private static final byte[] listLevelTypeNew = "levelnfcn".getBytes();
    
  /** Level alignment (old) */
    private static final byte[] listLevelAlignOld = "leveljc".getBytes();
    
  /** Level alignment (new) */
    private static final byte[] listLevelAlignNew = "leveljcn".getBytes();
    
  /** Level starting number */
    private static final byte[] listLevelStartAt = "levelstartat".getBytes();
    
  /** Level text group */
    private static final byte[] listLevelTextDefinition = "leveltext".getBytes();
    
  /** Filler for Level Text Length */
    private static final byte[] listLevelTextLength = "\'0".getBytes();
    
  /** Level Text Numbering Style */
    private static final byte[] listLevelTextStyleNumbers = "\'00.".getBytes();
    
  /** Level Text Bullet Style */
    private static final byte[] listLevelTextStyleBullet = "u-3913 ?".getBytes();
    
  /** Level Numbers Definition */
    private static final byte[] listLevelNumbersDefinition = "levelnumbers".getBytes();
    
  /** Filler for Level Numbers */
    private static final byte[] listLevelNumbers = "\\'0".getBytes();
    
  /** Tab Stop */
    private static final byte[] tabStop = "tx".getBytes();
    
  /** Actual list begin */
    private static final byte[] listBegin = "ls".getBytes();
    
  /** Current list level */
    private static final byte[] listCurrentLevel = "ilvl".getBytes();
    
  /** List text group for older browsers */
    private static final byte[] listTextOld = "listtext".getBytes();
    
  /** Tab */
    private static final byte[] tab = "tab".getBytes();
    
  /** Old Bullet Style */
    private static final byte[] listBulletOld = "\'b7".getBytes();
    
  /** Current List ID */
    private static final byte[] listID = "listid".getBytes();

  /** List override */
    private static final byte[] listOverride = "listoverride".getBytes();

  /** Number of overrides */
    private static final byte[] listOverrideCount = "listoverridecount".getBytes();
    
  /**
   * Text Style
   */
    
  /** Bold tag. */
    protected static final byte bold = (byte) 'b';
    
  /** Italic tag. */
    protected static final byte italic = (byte) 'i';
    
  /** Underline tag. */
    protected static final byte[] underline = "ul".getBytes();
    
  /** Strikethrough tag. */
    protected static final byte[] strikethrough = "strike".getBytes();
    
  /** Text alignment left tag. */
    public static final byte[] alignLeft = "ql".getBytes();

  /** Text alignment center tag. */
    private static final byte[] alignCenter = "qc".getBytes();
    
  /** Text alignment right tag. */
    private static final byte[] alignRight = "qr".getBytes();
    
  /** Text alignment justify tag. */
    private static final byte[] alignJustify = "qj".getBytes();
    
  /**
   * Colors
   */
    
  /** Begin colour table tag. */
    private static final byte[] colorTable = "colortbl".getBytes();

  /** Red value tag. */
    private static final byte[] colorRed = "red".getBytes();

  /** Green value tag. */
    private static final byte[] colorGreen = "green".getBytes();

  /** Blue value tag. */
    private static final byte[] colorBlue = "blue".getBytes();
    
  /**
   * Information Group
   */
    
  /** Begin the info group tag.*/
    private static final byte[] infoBegin = "info".getBytes();

  /** Author tag. */
    private static final byte[] metaAuthor = "author".getBytes();
    
  /** Subject tag. */
    private static final byte[] metaSubject = "subject".getBytes();
    
  /** Keywords tag. */
    private static final byte[] metaKeywords = "keywords".getBytes();

  /** Title tag. */
    private static final byte[] metaTitle = "title".getBytes();
    
  /** Producer tag. */
    private static final byte[] metaProducer = "operator".getBytes();

  /** Creation Date tag. */
    private static final byte[] metaCreationDate = "creationdate".getBytes();

  /** Year tag. */
    private static final byte[] year = "yr".getBytes();

  /** Month tag. */
    private static final byte[] month = "mo".getBytes();

  /** Day tag. */
    private static final byte[] day = "dy".getBytes();

  /** Hour tag. */
    private static final byte[] hour = "hr".getBytes();

  /** Minute tag. */
    private static final byte[] minute = "min".getBytes();

  /** Second tag. */
    private static final byte[] second = "sec".getBytes();

  /** Start superscript. */
    private static final byte[] startSuper = "super".getBytes();

  /** Start subscript. */
    private static final byte[] startSub = "sub".getBytes();

  /** End super/sub script. */
    private static final byte[] endSuperSub = "nosupersub".getBytes();

  /**
   * Header / Footer
   */

  /** Title Page tag */
    private static final byte[] titlePage = "titlepg".getBytes();

  /** Facing pages tag */
    private static final byte[] facingPages = "facingp".getBytes();

  /** Begin header group tag. */
    private static final byte[] headerBegin = "header".getBytes();

  /** Begin footer group tag. */
    private static final byte[] footerBegin = "footer".getBytes();

    // header footer 'left', 'right', 'first'
    private static final byte[] headerlBegin = "headerl".getBytes();

    private static final byte[] footerlBegin = "footerl".getBytes();

    private static final byte[] headerrBegin = "headerr".getBytes();

    private static final byte[] footerrBegin = "footerr".getBytes();

    private static final byte[] headerfBegin = "headerf".getBytes();

    private static final byte[] footerfBegin = "footerf".getBytes();

  /**
   * Paper Properties
   */

  /** Paper width tag. */
    private static final byte[] rtfPaperWidth = "paperw".getBytes();

  /** Paper height tag. */
    private static final byte[] rtfPaperHeight = "paperh".getBytes();

  /** Margin left tag. */
    private static final byte[] rtfMarginLeft = "margl".getBytes();

  /** Margin right tag. */
    private static final byte[] rtfMarginRight = "margr".getBytes();

  /** Margin top tag. */
    private static final byte[] rtfMarginTop = "margt".getBytes();

  /** Margin bottom tag. */
    private static final byte[] rtfMarginBottom = "margb".getBytes();

  /** New Page tag. */
    private static final byte[] newPage = "page".getBytes();

  /** Document Landscape tag 1. */
    private static final byte[] landscapeTag1 = "landscape".getBytes();

  /** Document Landscape tag 2. */
    private static final byte[] landscapeTag2 = "lndscpsxn".getBytes();

  /**
   * Annotations
   */
    
  /** Annotation ID tag. */
    private static final byte[] annotationID = "atnid".getBytes();
    
  /** Annotation Author tag. */
    private static final byte[] annotationAuthor = "atnauthor".getBytes();
    
  /** Annotation text tag. */
    private static final byte[] annotation = "annotation".getBytes();
    
  /**
   * Images
   */
    
  /** Begin the main Picture group tag */
    private static final byte[] pictureGroup = "shppict".getBytes();

  /** Begin the picture tag */
    private static final byte[] picture = "pict".getBytes();

  /** PNG Image */
    private static final byte[] picturePNG = "pngblip".getBytes();

  /** JPEG Image */
    private static final byte[] pictureJPEG = "jpegblip".getBytes();

  /** Picture width */
    private static final byte[] pictureWidth = "picw".getBytes();

  /** Picture height */
    private static final byte[] pictureHeight = "pich".getBytes();
    
  /** Picture scale horizontal percent */
    private static final byte[] pictureScaleX = "picscalex".getBytes();

  /** Picture scale vertical percent */
    private static final byte[] pictureScaleY = "picscaley".getBytes();
    
  /**
   * Fields (for page numbering)
   */
    
  /** Begin field tag */
    protected static final byte[] field = "field".getBytes();
    
  /** Content fo the field */
    protected static final byte[] fieldContent = "fldinst".getBytes();
    
  /** PAGE numbers */
    protected static final byte[] fieldPage = "PAGE".getBytes();
    
  /** HYPERLINK field */
    protected static final byte[] fieldHyperlink = "HYPERLINK".getBytes();
    
  /** Last page number (not used) */
    protected static final byte[] fieldDisplay = "fldrslt".getBytes();

    
  /** Class variables */
    
  /**
   * Because of the way RTF works and the way itext works, the text has to be
   * stored and is only written to the actual OutputStream at the end.
   */
    
  /** This <code>Vector</code> contains all fonts used in the document. */
    private Vector fontList = new Vector();
    
  /** This <code>Vector</code> contains all colours used in the document. */
    private Vector colorList = new Vector();

  /** This <code>ByteArrayOutputStream</code> contains the main body of the document. */
    private ByteArrayOutputStream content = null;
    
  /** This <code>ByteArrayOutputStream</code> contains the information group. */
    private ByteArrayOutputStream info = null;
    
  /** This <code>ByteArrayOutputStream</code> contains the list table. */
    private ByteArrayOutputStream listtable = null;
    
  /** This <code>ByteArrayOutputStream</code> contains the list override table. */
    private ByteArrayOutputStream listoverride = null;
    
  /** Document header. */
    private HeaderFooter header = null;
    
  /** Document footer. */
    private HeaderFooter footer = null;
    
  /** Left margin. */
    private int marginLeft = 1800;
    
  /** Right margin. */
    private int marginRight = 1800;

  /** Top margin. */
    private int marginTop = 1440;
    
  /** Bottom margin. */
    private int marginBottom = 1440;

  /** Page width. */
    private int pageWidth = 11906;

  /** Page height. */
    private int pageHeight = 16838;
    
  /** Factor to use when converting. */
    public final static double twipsFactor = 20;//20.57140;
    
  /** Current list ID. */
    private int currentListID = 1;
    
  /** List of current Lists. */
    private Vector listIds = null;

  /** Current List Level. */
    private int listLevel = 0;

  /** Current maximum List Level. */
    private int maxListLevel = 0;

  /** Write a TOC */
    private boolean writeTOC = false;

  /** Special title page */
    private boolean hasTitlePage = false;

  /** Currently writing either Header or Footer */
    private boolean inHeaderFooter = false;

    /** Currently writing a Table */
    private boolean inTable = false;

  /** Landscape or Portrait Document */
    private boolean landscape = false;

  /** Protected Constructor */

  /**
   * Constructs a <CODE>RtfWriter</CODE>.
   *
   * @param doc         The <CODE>Document</CODE> that is to be written as RTF
   * @param os          The <CODE>OutputStream</CODE> the writer has to write to.
   */

    protected RtfWriter(Document doc, OutputStream os)
    {
        super(doc, os);
        document.addDocListener(this);
        initDefaults();
    }

  /** Public functions special to the RtfWriter */

  /**
   * This method controls whether TOC entries are automatically generated
   *
   * @param writeTOC    boolean value indicating whether a TOC is to be generated
   */
  public void setGenerateTOCEntries(boolean writeTOC)
  {
    this.writeTOC = writeTOC;
  }

  /**
   * Gets the current setting of writeTOC
   *
   * @return    boolean value indicating whether a TOC is being generated
   */
  public boolean getGeneratingTOCEntries()
  {
    return writeTOC;
  }

  /**
   * This method controls whether the first page is a title page
   *
   * @param hasTitlePage    boolean value indicating whether the first page is a title page
   */
  public void setHasTitlePage(boolean hasTitlePage)
  {
    this.hasTitlePage = hasTitlePage;
  }

  /**
   * Gets the current setting of hasTitlePage
   *
   * @return    boolean value indicating whether the first page is a title page
   */
  public boolean getHasTitlePage()
  {
    return hasTitlePage;
  }

  /**
   * Explicitly sets the page format to use.
   * Otherwise the RtfWriter will try to guess the format by comparing pagewidth and pageheight
   *
   * @param landscape boolean value indicating whether we are using landscape format or not
   */
  public void setLandscape(boolean landscape)
  {
    this.landscape = landscape;
  }

  /**
   * Returns the current landscape setting
   *
   * @return boolean value indicating the current page format
   */
  public boolean getLandscape()
  {
    return landscape;
  }

  /** Public functions from the DocWriter Interface */

  /**
   * Gets an instance of the <CODE>RtfWriter</CODE>.
   *
   * @param document    The <CODE>Document</CODE> that has to be written
   * @param os  The <CODE>OutputStream</CODE> the writer has to write to.
   * @return    a new <CODE>RtfWriter</CODE>
   */
    public static RtfWriter getInstance(Document document, OutputStream os)
    {
        return(new RtfWriter(document, os));
    }

  /**
   * Signals that the <CODE>Document</CODE> has been opened and that
   * <CODE>Elements</CODE> can be added.
   */
    public void open()
    {
        super.open();
    }
    
  /**
   * Signals that the <CODE>Document</CODE> was closed and that no other
   * <CODE>Elements</CODE> will be added.
   * <p>
   * The content of the font table, color table, information group, content, header, footer are merged into the final
   * <code>OutputStream</code>
   */
    public void close()
    {
        writeDocument();
        super.close();
    }
    
  /**
   * Adds the footer to the bottom of the <CODE>Document</CODE>.
   */
    public void setFooter(HeaderFooter footer)
    {
        this.footer = footer;
    }
    
  /**
   * Adds the header to the top of the <CODE>Document</CODE>.
   */
    public void setHeader(HeaderFooter header)
    {
        this.header = header;
    }
    
  /**
   * Resets the footer.
   */
    public void resetFooter()
    {
        setFooter(null);
    }
    
  /**
   * Resets the header.
   */
    public void resetHeader()
    {
        setHeader(null);
    }
    
  /**
   * Tells the <code>RtfWriter</code> that a new page is to be begun.
   *
   * @return <code>true</code> if a new Page was begun.
   * @throws DocumentException if the Document was not open or had been closed.
   */
    public boolean newPage() throws DocumentException
    {
        try
        {
            content.write(escape);
            content.write(newPage);
            content.write(escape);
            content.write(paragraph);
        }
        catch(IOException e)
        {
            return false;
        }
        return true;
    }
    
  /**
   * Sets the page margins
   *
   * @param marginLeft The left margin
   * @param marginRight The right margin
   * @param marginTop The top margin
   * @param marginBottom The bottom margin
   *
   * @return <code>true</code> if the page margins were set.
   */
    public boolean setMargins(float marginLeft, float marginRight, float marginTop, float marginBottom)
    {
        this.marginLeft = (int) (marginLeft * twipsFactor);
        this.marginRight = (int) (marginRight * twipsFactor);
        this.marginTop = (int) (marginTop * twipsFactor);
        this.marginBottom = (int) (marginBottom * twipsFactor);
        return true;
    }

  /**
   * Sets the page size
   *
   * @param pageSize A <code>Rectangle</code> specifying the page size
   *
   * @return <code>true</code> if the page size was set
   */
    public boolean setPageSize(Rectangle pageSize)
    {
      if(!parseFormat(pageSize, false))
      {
        pageWidth = (int) (pageSize.width() * twipsFactor);
        pageHeight = (int) (pageSize.height() * twipsFactor);
	landscape = pageWidth > pageHeight;
      }
      return true;
    }

  /**
   * Write the table of contents.
   *
   * @param tocTitle The title that will be displayed above the TOC
   * @param titleFont The <code>Font</code> that will be used for the tocTitle
   * @param showTOCasEntry Set this to true if you want the TOC to appear as an entry in the TOC
   * @param showTOCEntryFont Use this <code>Font</code> to specify what Font to use when showTOCasEntry is true
   *
   * @return <code>true</code> if the TOC was added.
   */
  public boolean writeTOC(String tocTitle, Font titleFont, boolean showTOCasEntry, Font showTOCEntryFont)
  {
    try
      {
	RtfTOC toc = new RtfTOC(tocTitle, titleFont);
	if(showTOCasEntry) { toc.addTOCAsTOCEntry(tocTitle, showTOCEntryFont); }
	add(new Paragraph(toc));
      }
    catch(DocumentException de) { return false; }
    return true;
  }
    
  /**
   * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>.
   *
   * @return    <CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
   * @throws    DocumentException   if a document isn't open yet, or has been closed
   */
    public boolean add(Element element) throws DocumentException
    {
        return addElement(element, content);
    }
    

  /** Private functions */
    
  /**
   * Adds an <CODE>Element</CODE> to the <CODE>Document</CODE>.
   * @return    <CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
   * @throws    DocumentException   if a document isn't open yet, or has been closed
   */
    protected boolean addElement(Element element, ByteArrayOutputStream out) throws DocumentException
    {
        try
        {
            switch(element.type())
            {
                case Element.CHUNK        : writeChunk((Chunk) element, out);                break;
                case Element.PARAGRAPH    : writeParagraph((Paragraph) element, out);        break;
                case Element.ANCHOR       : writeAnchor((Anchor) element, out);              break;
                case Element.PHRASE       : writePhrase((Phrase) element, out);              break;
                case Element.CHAPTER      :
                case Element.SECTION      : writeSection((Section) element, out);            break;
                case Element.LIST         : writeList((com.lowagie.text.List) element, out); break;
                case Element.TABLE        : writeTable((Table) element, out);                  break;
                case Element.ANNOTATION   : writeAnnotation((Annotation) element, out);      break;
                case Element.PNG          :
                case Element.JPEG         : writeImage((Image) element, out);                break;
                
                case Element.AUTHOR       : writeMeta(metaAuthor, (Meta) element);       break;
                case Element.SUBJECT      : writeMeta(metaSubject, (Meta) element);      break;
                case Element.KEYWORDS     : writeMeta(metaKeywords, (Meta) element);     break;
                case Element.TITLE        : writeMeta(metaTitle, (Meta) element);        break;
                case Element.PRODUCER     : writeMeta(metaProducer, (Meta) element);     break;
                case Element.CREATIONDATE : writeMeta(metaCreationDate, (Meta) element); break;
            }
        }
        catch(IOException e)
        {
            return false;
        }
        return true;
    }

  /**
   * Write the beginning of a new <code>Section</code>
   *
   * @param sectionElement The <code>Section</code> be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
  private void writeSection(Section sectionElement, ByteArrayOutputStream out) throws IOException, DocumentException
    {
        if(sectionElement.type() == Element.CHAPTER)
	  {
	    out.write(escape);
	    out.write(sectionDefaults);
	    writeSectionDefaults(out);
	  }
        if(sectionElement.title() != null)
        {
	    if(writeTOC)
	      {
		StringBuffer title = new StringBuffer("");
		for(ListIterator li = sectionElement.title().getChunks().listIterator(); li.hasNext();)
		  {
		    title.append(((Chunk) li.next()).content());
		  }
		add(new RtfTOCEntry(title.toString(), sectionElement.title().font()));
	      }
	    else
	      {
		sectionElement.title().process(this);
	      }
            out.write(escape);
            out.write(paragraph);
        }
        sectionElement.process(this);
        if(sectionElement.type() == Element.CHAPTER)
	  {
	    out.write(escape);
	    out.write(section);
	  }
	if(sectionElement.type() == Element.SECTION)
	  {
	    out.write(escape);
	    out.write(paragraph);
	  }
    }
    
  /**
   * Write the beginning of a new <code>Paragraph</code>
   *
   * @param paragraphElement The <code>Paragraph</code> to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
    private void writeParagraph(Paragraph paragraphElement, ByteArrayOutputStream out) throws IOException
    {
        out.write(escape);
        out.write(paragraphDefaults);
        if(inTable)
        {
            out.write(escape);
            out.write(RtfCell.cellInTable);
        }
        switch(paragraphElement.alignment())
        {
            case Element.ALIGN_LEFT      : out.write(escape); out.write(alignLeft); break;
            case Element.ALIGN_RIGHT     : out.write(escape); out.write(alignRight); break;
            case Element.ALIGN_CENTER    : out.write(escape); out.write(alignCenter); break;
            case Element.ALIGN_JUSTIFIED : out.write(escape); out.write(alignJustify); break;
        }
        out.write(escape);
        out.write(listIndent);
        writeInt(out, (int) (paragraphElement.indentationLeft() * twipsFactor));
        Iterator chunks = paragraphElement.getChunks().iterator();
        while(chunks.hasNext())
        {
            Chunk ch = (Chunk) chunks.next();
            ch.setFont(paragraphElement.font().difference(ch.font()));
        }
	ByteArrayOutputStream save = content;
	content = out;
        paragraphElement.process(this);
	content = save;
      if(!inTable)
      {
        out.write(escape);
        out.write(paragraph);
      }
    }

  /**
   * Write a <code>Phrase</code>.
   *
   * @param phrase  The <code>Phrase</code> item to be written
   * @param out     The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
    private void writePhrase(Phrase phrase, ByteArrayOutputStream out) throws IOException
    {
      out.write(escape);
      out.write(paragraphDefaults);
      if(inTable)
      {
          out.write(escape);
          out.write(RtfCell.cellInTable);
      }
      Iterator chunks = phrase.getChunks().iterator();
      while(chunks.hasNext())
      {
        Chunk ch = (Chunk) chunks.next();
        ch.setFont(phrase.font().difference(ch.font()));
      }
      ByteArrayOutputStream save = content;
      content = out;
      phrase.process(this);
      content = save;
    }

  /**
   * Write an <code>Anchor</code>. Anchors are treated like Phrases.
   *
   * @param anchor  The <code>Chunk</code> item to be written
   * @param out     The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
    private void writeAnchor(Anchor anchor, ByteArrayOutputStream out) throws IOException
    {
        if (anchor.url() != null) {
            out.write(openGroup);
            out.write(escape);
            out.write(field);
            out.write(openGroup);
            out.write(extendedEscape);
            out.write(fieldContent);
            out.write(openGroup);
            out.write(fieldHyperlink);
            out.write(delimiter);
            out.write(anchor.url().toString().getBytes());
            out.write(closeGroup);
            out.write(closeGroup);
            out.write(openGroup);
            out.write(escape);
            out.write(fieldDisplay);
            out.write(delimiter);
            writePhrase(anchor, out);
            out.write(closeGroup);
            out.write(closeGroup);
        }
        else {
            writePhrase(anchor, out);
        }
    }

  /**
   * Write a <code>Chunk</code> and all its font properties.
   *
   * @param chunk The <code>Chunk</code> item to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
  private void writeChunk(Chunk chunk, ByteArrayOutputStream out) throws IOException, DocumentException
    {
        if (chunk instanceof RtfField) {
            ((RtfField)chunk).write( this, out );
        } else {
	  if(chunk.getImage() != null)
	  {
	    writeImage(chunk.getImage(), out);
	  }
	  else
	  {
	    writeInitialFontSignature(out, chunk);
 	    out.write( filterSpecialChar( chunk.content() ).getBytes() );
	    writeFinishingFontSignature( out, chunk );
	  }
        }
    }


    protected void writeInitialFontSignature( OutputStream out, Chunk chunk ) throws IOException {
        Font font = chunk.font();

        out.write(escape);
        out.write(fontNumber);
        if (!font.getFamilyname().equalsIgnoreCase("unknown")) {
            writeInt(out, addFont( font ));
        } else {
            writeInt(out, 0);
        }
        out.write(escape);
        out.write(fontSize);
        if (font.size() > 0) {
            writeInt( out, (int)(font.size() * 2) );
        } else {
            writeInt(out, 20);
        }
        out.write(escape);
        out.write(fontColor);
        writeInt(out, addColor( font.color() ) );
        if (font.isBold()) {
            out.write(escape);
            out.write(bold);
        }
        if (font.isItalic()) {
            out.write(escape);
            out.write(italic);
        }
        if (font.isUnderlined()) {
            out.write(escape);
            out.write(underline);
        }
        if (font.isStrikethru()) {
            out.write(escape);
            out.write(strikethrough);
        }

        /*
         * Superscript / Subscript added by Scott Dietrich (sdietrich@emlab.com)
         */
        if (chunk.getAttributes() != null) {
            Float f = (Float)chunk.getAttributes().get(Chunk.SUBSUPSCRIPT);
            if (f != null)
                if (f.floatValue() > 0)
                {
                    out.write(escape);
                    out.write(startSuper);
                }
                else if (f.floatValue() < 0)
                {
                    out.write(escape);
                    out.write(startSub);
                }
        }

        out.write( delimiter );
    }


    protected void writeFinishingFontSignature( OutputStream out, Chunk chunk) throws IOException {
        Font font = chunk.font();

        if (font.isBold()) {
            out.write(escape);
            out.write(bold);
            writeInt(out, 0);
        }
        if (font.isItalic()) {
            out.write(escape);
            out.write(italic);
            writeInt(out, 0);
        }
        if (font.isUnderlined()) {
            out.write(escape);
            out.write(underline);
            writeInt(out, 0);
        }
        if (font.isStrikethru()) {
            out.write(escape);
            out.write(strikethrough);
            writeInt(out, 0);
        }

        /*
         * Superscript / Subscript added by Scott Dietrich (sdietrich@emlab.com)
         */
        if (chunk.getAttributes() != null) {
            Float f = (Float)chunk.getAttributes().get(Chunk.SUBSUPSCRIPT);
            if (f != null)
                if (f.floatValue() != 0)
                {
                    out.write(escape);
                    out.write(endSuperSub);
                }
        }
    }
  /**
   * Write a <code>ListItem</code>
   *
   * @param listItem The <code>ListItem</code> to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
    private void writeListElement(ListItem listItem, ByteArrayOutputStream out) throws IOException, DocumentException
    {
        Iterator chunks = listItem.getChunks().iterator();
        while(chunks.hasNext())
        {
            Chunk ch = (Chunk) chunks.next();
            addElement(ch, out);
        }
        out.write(escape);
        out.write(paragraph);
    }
    
  /**
   * Write a <code>List</code>
   *
   * @param list The <code>List</code> to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws    IOException
   * @throws    DocumentException
   */
    private void writeList(com.lowagie.text.List list, ByteArrayOutputStream out) throws IOException, DocumentException
    {
        int type = 0;
        int align = 0;
        int fontNr = addFont(new Font(Font.SYMBOL, 10, Font.NORMAL, new Color(0, 0, 0)));
        if(!list.isNumbered()) type = 23;
        if(listLevel == 0)
        {
            maxListLevel = 0;
            listtable.write(openGroup);
            listtable.write(escape);
            listtable.write(listDefinition);
            int i = getRandomInt();
            listtable.write(escape);
            listtable.write(listTemplateID);
            writeInt(listtable, i);
            listtable.write(escape);
            listtable.write(hybridList);
            listtable.write((byte) '\n');
        }
        if(listLevel >= maxListLevel)
        {
            maxListLevel++;
            listtable.write(openGroup);
            listtable.write(escape);
            listtable.write(listLevelDefinition);
            listtable.write(escape);
            listtable.write(listLevelTypeOld);
            writeInt(listtable, type);
            listtable.write(escape);
            listtable.write(listLevelTypeNew);
            writeInt(listtable, type);
            listtable.write(escape);
            listtable.write(listLevelAlignOld);
            writeInt(listtable, align);
            listtable.write(escape);
            listtable.write(listLevelAlignNew);
            writeInt(listtable, align);
            listtable.write(escape);
            listtable.write(listLevelStartAt);
            writeInt(listtable, 1);
            listtable.write(openGroup);
            listtable.write(escape);
            listtable.write(listLevelTextDefinition);
            listtable.write(escape);
            listtable.write(listLevelTextLength);
            if(list.isNumbered()) { writeInt(listtable, 2); } else { writeInt(listtable, 1); }
            listtable.write(escape);
            if(list.isNumbered()) { listtable.write(listLevelTextStyleNumbers); } else { listtable.write(listLevelTextStyleBullet); }
            listtable.write(commaDelimiter);
            listtable.write(closeGroup);
            listtable.write(openGroup);
            listtable.write(escape);
            listtable.write(listLevelNumbersDefinition);
            if(list.isNumbered()) { listtable.write(delimiter); listtable.write(listLevelNumbers); writeInt(listtable, listLevel + 1); }
            listtable.write(commaDelimiter);
            listtable.write(closeGroup);
            if(!list.isNumbered()) { listtable.write(escape); listtable.write(fontNumber); writeInt(listtable, fontNr); }
            listtable.write(escape);
            listtable.write(firstIndent);
            writeInt(listtable, (int) (list.indentationLeft() * twipsFactor * -1));
            listtable.write(escape);
            listtable.write(listIndent);
            writeInt(listtable, (int) ((list.indentationLeft() + list.symbolIndent()) * twipsFactor));
            listtable.write(escape);
            listtable.write(tabStop);
            writeInt(listtable, (int) (list.symbolIndent() * twipsFactor));
            listtable.write(closeGroup);
            listtable.write((byte) '\n');
        }
        // Actual List Begin in Content
        out.write(escape);
        out.write(paragraphDefaults);
        out.write(escape);
        out.write(alignLeft);
        out.write(escape);
        out.write(firstIndent);
        writeInt(out, (int) (list.indentationLeft() * twipsFactor * -1));
        out.write(escape);
        out.write(listIndent);
        writeInt(out, (int) ((list.indentationLeft() + list.symbolIndent()) * twipsFactor));
        out.write(escape);
        out.write(fontSize);
        writeInt(out, 20);
        out.write(escape);
        out.write(listBegin);
        writeInt(out, currentListID);
        if(listLevel > 0)
        {
            out.write(escape);
            out.write(listCurrentLevel);
            writeInt(out, listLevel);
        }
        out.write(openGroup);
        ListIterator listItems = list.getItems().listIterator();
        Element listElem;
        int count = 1;
        while(listItems.hasNext())
        {
            listElem = (Element) listItems.next();
            if(listElem.type() == Element.CHUNK) { listElem = new ListItem((Chunk) listElem); }
            if(listElem.type() == Element.LISTITEM)
            {
                out.write(openGroup);
                out.write(escape);
                out.write(listTextOld);
                out.write(escape);
                out.write(paragraphDefaults);
                out.write(escape);
                out.write(fontNumber);
                if(list.isNumbered()) { writeInt(out, addFont(new Font(Font.TIMES_NEW_ROMAN, Font.NORMAL, 10, new Color(0, 0, 0)))); } else { writeInt(out, fontNr); }
                out.write(escape);
                out.write(firstIndent);
                writeInt(out, (int) (list.indentationLeft() * twipsFactor * -1));
                out.write(escape);
                out.write(listIndent);
                writeInt(out, (int) ((list.indentationLeft() + list.symbolIndent()) * twipsFactor));
                out.write(delimiter);
                if(list.isNumbered()) { writeInt(out, count); out.write(".".getBytes()); } else { out.write(escape); out.write( listBulletOld); }
                out.write(escape);
                out.write(tab);
                out.write(closeGroup);
                writeListElement((ListItem) listElem, out);
                count++;
            }
            else if(listElem.type() == Element.LIST)
            {
                listLevel++;
                writeList((com.lowagie.text.List) listElem, out);
                listLevel--;
                out.write(escape);
                out.write(paragraphDefaults);
                out.write(escape);
                out.write(alignLeft);
                out.write(escape);
                out.write(firstIndent);
                writeInt(out, (int) (list.indentationLeft() * twipsFactor * -1));
                out.write(escape);
                out.write(listIndent);
                writeInt(out, (int) ((list.indentationLeft() + list.symbolIndent()) * twipsFactor));
                out.write(escape);
                out.write(fontSize);
                writeInt(out, 20);
                out.write(escape);
                out.write(listBegin);
                writeInt(out, currentListID);
                if(listLevel > 0)
                {
                    out.write(escape);
                    out.write(listCurrentLevel);
                    writeInt(out, listLevel);
                }
            }
            out.write((byte)'\n');
        }
        out.write(closeGroup);
        if(listLevel == 0)
        {
            int i = getRandomInt();
            listtable.write(escape);
            listtable.write(listID);
            writeInt(listtable, i);
            listtable.write(closeGroup);
            listtable.write((byte) '\n');
            listoverride.write(openGroup);
            listoverride.write(escape);
            listoverride.write(listOverride);
            listoverride.write(escape);
            listoverride.write(listID);
            writeInt(listoverride, i);
            listoverride.write(escape);
            listoverride.write(listOverrideCount);
            writeInt(listoverride, 0);
            listoverride.write(escape);
            listoverride.write(listBegin);
            writeInt(listoverride, currentListID);
            currentListID++;
            listoverride.write(closeGroup);
            listoverride.write((byte) '\n');
        }
    }
    
  /**
   * Write a <code>Table</code>.
   *
   * @param table The <code>table</code> to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * Currently no nesting of tables is supported. If a cell contains anything but a Cell Object it is ignored.
   *
   * @throws IOException
   * @throws DocumentException
   */
    private void writeTable(Table table, ByteArrayOutputStream out) throws IOException, DocumentException
    {
      inTable = true;
        table.complete();
        RtfTable rtfTable = new RtfTable(this);
        rtfTable.importTable(table, pageWidth);
        rtfTable.writeTable(out);
      inTable = false;
    }
    
    
  /**
   * Write an <code>Image</code>.
   *
   * @param image The <code>image</code> to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * At the moment only PNG and JPEG Images are supported.
   *
   * @throws IOException
   * @throws DocumentException
   */
    private void writeImage(Image image, ByteArrayOutputStream out) throws IOException, DocumentException
    {
        if(!image.isPng() && !image.isJpeg()) throw new DocumentException("Only PNG and JPEG images are supported by the RTF Writer");
        switch(image.alignment())
        {
            case Element.ALIGN_LEFT      : out.write(escape); out.write(alignLeft); break;
            case Element.ALIGN_RIGHT     : out.write(escape); out.write(alignRight); break;
            case Element.ALIGN_CENTER    : out.write(escape); out.write(alignCenter); break;
            case Element.ALIGN_JUSTIFIED : out.write(escape); out.write(alignJustify); break;
        }
        out.write(openGroup);
        out.write(extendedEscape);
        out.write(pictureGroup);
        out.write(openGroup);
        out.write(escape);
        out.write(picture);
        out.write(escape);
        if(image.isPng()) out.write(picturePNG);
        if(image.isJpeg())  out.write(pictureJPEG);
        out.write(escape);
        out.write(pictureWidth);
        writeInt(out, (int) (image.plainWidth() * twipsFactor));
        out.write(escape);
        out.write(pictureHeight);
        writeInt(out, (int) (image.plainHeight() * twipsFactor));


// For some reason this messes up the intended image size. It makes it too big. Weird
//
//        out.write(escape);
//        out.write(pictureIntendedWidth);
//        writeInt(out, (int) (image.plainWidth() * twipsFactor));
//        out.write(escape);
//        out.write(pictureIntendedHeight);
//        writeInt(out, (int) (image.plainHeight() * twipsFactor));


        if(image.width() > 0)
        {
            out.write(escape);
            out.write(pictureScaleX);
            writeInt(out, (int) (100 / image.width() * image.plainWidth()));
        }
        if(image.height() > 0)
        {
            out.write(escape);
            out.write(pictureScaleY);
            writeInt(out, (int) (100 / image.height() * image.plainHeight()));
        }
        out.write(delimiter);
        InputStream imgIn;
        if(image.rawData() == null) { imgIn = image.url().openStream(); } else { imgIn = new ByteArrayInputStream(image.rawData()); }
        int buffer = -1;
        int count = 0;
        out.write((byte) '\n');
        while((buffer = imgIn.read()) != -1)
        {
            String helperStr = Integer.toHexString(buffer);
            if(helperStr.length() < 2) helperStr = "0" + helperStr;
            out.write(helperStr.getBytes());
            count++;
            if(count == 64) { out.write((byte) '\n'); count = 0; }
        }
        out.write(closeGroup);
        out.write(closeGroup);
        out.write((byte) '\n');
    }
    
  /**
   * Write an <code>Annotation</code>
   *
   * @param annotationElement The <code>Annotation</code> to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   *
   * @throws IOException
   */
    private void writeAnnotation(Annotation annotationElement, ByteArrayOutputStream out) throws IOException
    {
        int id = getRandomInt();
        out.write(openGroup);
        out.write(extendedEscape);
        out.write(annotationID);
        out.write(delimiter);
        writeInt(out, id);
        out.write(closeGroup);
        out.write(openGroup);
        out.write(extendedEscape);
        out.write(annotationAuthor);
        out.write(delimiter);
        out.write(annotationElement.title().getBytes());
        out.write(closeGroup);
        out.write(openGroup);
        out.write(extendedEscape);
        out.write(annotation);
        out.write(escape);
        out.write(paragraphDefaults);
        out.write(delimiter);
        out.write(annotationElement.content().getBytes());
        out.write(closeGroup);
    }

  /**
   * Add a <code>Meta</code> element. It is written to the Inforamtion Group
   * and merged with the main <code>ByteArrayOutputStream</code> when the
   * Document is closed.
   *
   * @param metaName The type of <code>Meta</code> element to be added
   * @param meta The <code>Meta</code> element to be added
   *
   * Currently only the Meta Elements Author, Subject, Keywords, Title, Producer and CreationDate are supported.
   *
   * @throws IOException
   */
    private void writeMeta(byte[] metaName, Meta meta) throws IOException
    {
        info.write(openGroup);
        try
        {
            info.write(escape);
            info.write(metaName);
            info.write(delimiter);
            if(meta.type() == Meta.CREATIONDATE) { writeFormatedDateTime(meta.content()); } else { info.write(meta.content().getBytes()); }
        }
        finally
        {
            info.write(closeGroup);
        }
    }
    
  /**
   * Writes a date. The date is formated <strong>Year, Month, Day, Hour, Minute, Second</strong>
   *
   * @param date The date to be written
   *
   * @throws IOException
   */
    private void writeFormatedDateTime(String date) throws IOException
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        ParsePosition pp = new ParsePosition(0);
        Date d = sdf.parse(date, pp);
        if(d == null) { d = new Date(); }
        cal.setTime(d);
        info.write(escape);
        info.write(year);
        writeInt(info, cal.get(Calendar.YEAR));
        info.write(escape);
        info.write(month);
        writeInt(info, cal.get(Calendar.MONTH));
        info.write(escape);
        info.write(day);
        writeInt(info, cal.get(Calendar.DAY_OF_MONTH));
        info.write(escape);
        info.write(hour);
        writeInt(info, cal.get(Calendar.HOUR_OF_DAY));
        info.write(escape);
        info.write(minute);
        writeInt(info, cal.get(Calendar.MINUTE));
        info.write(escape);
        info.write(second);
        writeInt(info, cal.get(Calendar.SECOND));
    }
    
  /**
   * Add a new <code>Font</code> to the list of fonts. If the <code>Font</code>
   * already exists in the list of fonts, then it is not added again.
   *
   * @param newFont The <code>Font</code> to be added
   *
   * @return The index of the <code>Font</code> in the font list
   */
    protected int addFont(Font newFont)
    {
        int fn = -1;

        for(int i = 0; i < fontList.size(); i++)
        {
            if(newFont.getFamilyname().equals(((Font)fontList.get(i)).getFamilyname())) { fn = i; }
        }
        if(fn == -1)
        {
            fontList.add(newFont);
            return fontList.size()-1;
        }
        return fn;
    }
    
  /**
   * Add a new <code>Color</code> to the list of colours. If the <code>Color</code>
   * already exists in the list of colours, then it is not added again.
   *
   * @param newColor The <code>Color</code> to be added
   *
   * @return The index of the <code>color</code> in the colour list
   */
    protected int addColor(Color newColor)
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
    
  /**
   * Merge all the different <code>Vector</code>s and <code>ByteArrayOutputStream</code>s
   * to the final <code>ByteArrayOutputStream</code>
   *
   * @return <code>true</code> if all information was sucessfully written to the <code>ByteArrayOutputStream</code>
   */
    private boolean writeDocument()
    {
        try
        {
            writeDocumentIntro();
            os.write((byte)'\n');
            writeFontList();
            os.write((byte)'\n');
            writeColorList();
            os.write((byte)'\n');
            writeList();
            os.write((byte)'\n');
            writeInfoGroup();
            os.write((byte)'\n');
            writeDocumentFormat();
            os.write((byte)'\n');
            ByteArrayOutputStream hf = new ByteArrayOutputStream();
            writeSectionDefaults(hf);
            hf.writeTo(os);
            content.writeTo(os);
            os.write(closeGroup);
            return true;
        }
        catch(IOException e)
        {
            System.err.println(e.getMessage());
            return false;
        }
        
    }
    
  /** Write the Rich Text file settings
   */
    private void writeDocumentIntro() throws IOException
    {
        os.write(openGroup);
        os.write(escape);
        os.write(docBegin);
        os.write(escape);
        os.write(ansi);
        os.write(escape);
        os.write(ansiCodepage);
        writeInt(os, 1252);
        os.write(escape);
        os.write(defaultFont);
        writeInt(os, 0);
    }
    
  /**
   * Write the font list to the final <code>ByteArrayOutputStream</code>
   */
    private void writeFontList() throws IOException
    {
        Font fnt;

        os.write(openGroup);
        os.write(escape);
        os.write(fontTable);
        for(int i = 0; i < fontList.size(); i++)
        {
            fnt = (Font) fontList.get(i);
            os.write(openGroup);
            os.write(escape);
            os.write(fontNumber);
            writeInt(os, i);
            os.write(escape);
            switch(Font.getFamilyIndex(fnt.getFamilyname()))
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
		default: os.write(fnt.getFamilyname().getBytes());
		    os.write(escape);
		    os.write(fontCharset);
		    writeInt(os, 0);
		    os.write(delimiter);
		    os.write(fnt.getFamilyname().getBytes());
            }
            os.write(commaDelimiter);
            os.write(closeGroup);
        }
        os.write(closeGroup);
    }

  /**
   * Write the colour list to the final <code>ByteArrayOutputStream</code>
   */
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
    
  /**
   * Write the Information Group to the final <code>ByteArrayOutputStream</code>
   */
    private void writeInfoGroup() throws IOException
    {
        os.write(openGroup);
        os.write(escape);
        os.write(infoBegin);
        info.writeTo(os);
        os.write(closeGroup);
    }
    
  /**
   * Write the listtable and listoverridetable to the final <code>ByteArrayOutputStream</code>
   */
    private void writeList() throws IOException
    {
        listtable.write(closeGroup);
        listoverride.write(closeGroup);
        listtable.writeTo(os);
        os.write((byte)'\n');
        listoverride.writeTo(os);
    }

  /**
   * Write an integer
   *
   * @param out The <code>OuputStream</code> to which the <code>int</code> value is to be written
   * @param i The <code>int</code> value to be written
   */
    public final static void writeInt(OutputStream out, int i) throws IOException
    {
        out.write(Integer.toString(i).getBytes());
    }
    
  /**
   * Get a random integer.
   * This returns a <b>unique</b> random integer to be used with listids.
   *
   * @return Random <code>int</code> value.
   */
    private int getRandomInt()
    {
        boolean ok = false;
        Integer newInt = null;
        Integer oldInt = null;
        while(!ok)
        {
            newInt = new Integer((int) (Math.random() * Integer.MAX_VALUE));
            ok = true;
            for(int i = 0; i < listIds.size(); i++)
            {
                oldInt = (Integer) listIds.get(i);
                if(oldInt.equals(newInt)) { ok = true; }
            }
        }
        listIds.add(newInt);
        return newInt.intValue();
    }

  /**
   * Write the current header and footer to a <code>ByteArrayOutputStream</code>
   *
   * @param os		The <code>ByteArrayOutputStream</code> to which the header and footer will be written.
   */
    public void writeHeadersFooters(ByteArrayOutputStream os) throws IOException
    {
      if (this.footer instanceof RtfHeaderFooters) {
        RtfHeaderFooters rtfHf = (RtfHeaderFooters)this.footer;
        HeaderFooter hf = rtfHf.get( RtfHeaderFooters.ALL_PAGES );
        if (hf != null) {
          writeHeaderFooter( hf, footerBegin, os );
        }
        hf = rtfHf.get( RtfHeaderFooters.LEFT_PAGES );
        if (hf != null) {
          writeHeaderFooter( hf, footerlBegin, os );
        }
        hf = rtfHf.get( RtfHeaderFooters.RIGHT_PAGES );
        if (hf != null) {
          writeHeaderFooter( hf, footerrBegin, os );
        }
        hf = rtfHf.get( RtfHeaderFooters.FIRST_PAGE );
        if (hf != null) {
          writeHeaderFooter( hf, footerfBegin, os );
        }
      } else {
        writeHeaderFooter(this.footer, footerBegin, os);
      }
      if (this.header instanceof RtfHeaderFooters) {
        RtfHeaderFooters rtfHf = (RtfHeaderFooters)this.header;
        HeaderFooter hf = rtfHf.get( RtfHeaderFooters.ALL_PAGES );
        if (hf != null) {
          writeHeaderFooter( hf, headerBegin, os );
        }
        hf = rtfHf.get( RtfHeaderFooters.LEFT_PAGES );
        if (hf != null) {
          writeHeaderFooter( hf, headerlBegin, os );
        }
        hf = rtfHf.get( RtfHeaderFooters.RIGHT_PAGES );
        if (hf != null) {
          writeHeaderFooter( hf, headerrBegin, os );
        }
        hf = rtfHf.get( RtfHeaderFooters.FIRST_PAGE );
        if (hf != null) {
          writeHeaderFooter( hf, headerfBegin, os );
        }
      } else {
        writeHeaderFooter(this.header, headerBegin, os);
      }
    }

  /**
   * Write a <code>HeaderFooter</code> to a <code>ByteArrayOutputStream</code>
   *
   * @param headerFooter	The <code>HeaderFooter</code> object to be written.
   * @param hfType		The type of header or footer to be added.
   * @param target		The <code>ByteArrayOutputStream</code> to which the <code>HeaderFooter</code> will be written.
   */
    private void writeHeaderFooter(HeaderFooter headerFooter, byte[] hfType, ByteArrayOutputStream target) throws IOException
    {
      inHeaderFooter = true;
        try
        {
            target.write(openGroup);
            target.write(escape);
            target.write(hfType);
	    target.write(delimiter);
            if(headerFooter != null) {
                if (headerFooter instanceof RtfHeaderFooter
                        && ((RtfHeaderFooter)headerFooter).content() != null) {
                    this.addElement( ((RtfHeaderFooter)headerFooter).content(), target );
                } else {
		if(headerFooter.getBefore() != null) { this.addElement(headerFooter.getBefore(), target); }
		if(headerFooter.isNumbered()) { this.addElement(new RtfPageNumber("", headerFooter.getBefore().font()), target); }
		if(headerFooter.getAfter() != null) { this.addElement(headerFooter.getAfter(), target); }
//                    Element[] headerElements = new Element[3];
/*                    java.util.List chunks = headerFooter.paragraph().getChunks();
                    int headerCount = chunks.size();
//                    if(headerCount >= 1) { headerElements[0] = (Element) headerFooter.paragraph().getChunks().get(0); }
//                    if(headerCount >= 2) { headerElements[1] = (Element) headerFooter.paragraph().getChunks().get(1); }
//                    if(headerCount >= 3) { headerElements[2] = (Element) headerFooter.paragraph().getChunks().get(2); }
                    if(headerCount >= 1) {
                        add( (Element)chunks.get( 0 ) );
                    }
                    if (headerCount >= 2) {
                        Element chunk = (Element)chunks.get(1);
                        if (chunk.type() == Element.CHUNK) {
                            try {
                                Integer.parseInt( ((Chunk)chunk).content() );
                                content.write(openGroup);
                                content.write(escape);
                                content.write(field);
                                content.write(openGroup);
                                content.write(extendedEscape);
                                content.write(fieldContent);
                                content.write(openGroup);
                                content.write(delimiter);
                                content.write(fieldPage);
                                content.write(delimiter);
                                content.write(closeGroup);
                                content.write(closeGroup);
                                content.write(openGroup);
                                content.write(escape);
                                content.write(fieldDisplay);
                                content.write(openGroup);
                                content.write(closeGroup);
                                content.write(closeGroup);
                                content.write(closeGroup);
                            }
                            catch(NumberFormatException nfe)
                            {
                                add( chunk );
                            }
                        } else {
                            add( chunk );
                        }
//                        if(headerElements[1].type() == Element.PHRASE) { writePhrase((Phrase) headerElements[1], content); }
                    }
                    if(headerCount >= 3) {
                        add( (Element)chunks.get( 2 ) );
//                        if(headerElements[2].type() == Element.CHUNK) { writeChunk((Chunk) headerElements[2], content); }
//                        if(headerElements[2].type() == Element.PHRASE) { writePhrase((Phrase) headerElements[2], content); }
                    }*/
                }
            }
	    target.write(closeGroup);
        }
        catch(DocumentException e)
        {
            throw new IOException("DocumentException - "+e.getMessage());
        }
      inHeaderFooter = false;
    }

  /**
   *  Write the <code>Document</code>'s Paper and Margin Size
   *  to the final <code>ByteArrayOutputStream</code>
   */
    private void writeDocumentFormat() throws IOException
    {
//        os.write(openGroup);
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
//        os.write(closeGroup);
    }
    
  /**
   * Initialise all helper classes.
   * Clears alls lists, creates new <code>ByteArrayOutputStream</code>'s
   */
    private void initDefaults()
    {
        fontList.clear();
        colorList.clear();
        info = new ByteArrayOutputStream();
        content = new ByteArrayOutputStream();
        listtable = new ByteArrayOutputStream();
        listoverride = new ByteArrayOutputStream();
        document.addProducer();
        document.addCreationDate();
        addFont(new Font(Font.TIMES_NEW_ROMAN, 10, Font.NORMAL));
        addColor(new Color(0, 0, 0));
        addColor(new Color(255, 255, 255));
        listIds = new Vector();
        try
        {
            listtable.write(openGroup);
            listtable.write(extendedEscape);
            listtable.write(listtableGroup);
            listtable.write((byte) '\n');
            listoverride.write(openGroup);
            listoverride.write(extendedEscape);
            listoverride.write(listoverridetableGroup);
            listoverride.write((byte) '\n');
        }
        catch(IOException e)
        {
            System.err.println("InitDefaultsError" + e);
        }
    }

    /**
     * Writes the default values for the current Section
     *
     * @param out The <code>ByteArrayOutputStream</code> to be written to
     */
    private void writeSectionDefaults(ByteArrayOutputStream out) throws IOException
    {
      if(header instanceof RtfHeaderFooters || footer  instanceof RtfHeaderFooters)
      {
        out.write(escape);
	    out.write(facingPages);
      }
      if(hasTitlePage)
      {
        out.write(escape);
        out.write(titlePage);
      }
      writeHeadersFooters(out);
      if(landscape)
      {
        out.write(escape);
        out.write(landscapeTag1);
        out.write(escape);
        out.write(landscapeTag2);
      }
    }

  /**
   * This method tries to fit the <code>Rectangle pageSize</code> to one of the predefined PageSize rectangles.
   * If a match is found the pageWidth and pageHeight will be set according to values determined from files
   * generated by MS Word2000 and OpenOffice 641. If no match is found the method will try to match the rotated
   * Rectangle by calling itself with the parameter rotate set to true.
   */
  private boolean parseFormat(Rectangle pageSize, boolean rotate)
  {
      if(rotate) { pageSize = pageSize.rotate(); }
      if(rectEquals(pageSize, PageSize.A3))
      {
	pageWidth = 16837;
	pageHeight = 23811;
	landscape = rotate;
	return true;
      }
      if(rectEquals(pageSize, PageSize.A4))
      {
        pageWidth = 11907;
	pageHeight = 16840;
	landscape = rotate;
	return true;
      }
      if(rectEquals(pageSize, PageSize.A5))
      {
        pageWidth = 8391;
	pageHeight = 11907;
	landscape = rotate;
	return true;
      }
      if(rectEquals(pageSize, PageSize.A6))
      {
        pageWidth = 5959;
	pageHeight = 8420;
	landscape = rotate;
	return true;
      }
      if(rectEquals(pageSize, PageSize.B4))
      {
        pageWidth = 14570;
	pageHeight = 20636;
	landscape = rotate;
	return true;
      }
      if(rectEquals(pageSize, PageSize.B5))
      {
        pageWidth = 10319;
	pageHeight = 14572;
	landscape = rotate;
	return true;
      }
      if(rectEquals(pageSize, PageSize.HALFLETTER))
      {
        pageWidth = 7927;
	pageHeight = 12247;
	landscape = rotate;
	return true;
      }
      if(rectEquals(pageSize, PageSize.LETTER))
      {
        pageWidth = 12242;
	pageHeight = 15842;
	landscape = rotate;
	return true;
      }
      if(rectEquals(pageSize, PageSize.LEGAL))
      {
        pageWidth = 12252;
	pageHeight = 20163;
	landscape = rotate;
	return true;
      }
      if(!rotate && parseFormat(pageSize, true))
      {
        int x = pageWidth;
	pageWidth = pageHeight;
	pageHeight = x;
	return true;
      }
      return false;
  }

  /**
   * This method compares to Rectangles. They are considered equal if width and height are the same
   */
  private boolean rectEquals(Rectangle rect1, Rectangle rect2)
  {
    return (rect1.width() == rect2.width()) && (rect1.height() == rect2.height());
  }

  /**
   * Returns whether we are currently writing a header or footer
   *
   * @return the value of inHeaderFooter
   */
  public boolean writingHeaderFooter()
  {
    return inHeaderFooter;
  }

  /**
   * Replaces special characters with their unicode values
   *
   * @param str The original <code>String</code>
   * @return The converted String
   */
  public final static String filterSpecialChar(String str)
  {
       int length = str.length();
       int z = (int)'z';
       StringBuffer ret = new StringBuffer( length );
       for(int i = 0; i < length; i++) {
           char ch = str.charAt( i );

           if(ch == '\\')
             {
               ret.append("\\\\");
             }
           else if(ch == '\n')
             {
               ret.append("\\par ");
             }
           else if( ((int)ch) > z )
             {
               ret.append( "\\u" ).append( (long)ch ).append( 'G' );
             }
           else
             {
               ret.append( ch );
             }
       }
       return ret.toString();
  }
}

