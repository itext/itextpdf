/** 
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
  private static final byte delimiter = (byte) ' ';
  
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
  private static final byte fontNumber = (byte) 'f';
  
  /** Font size tag. */
  private static final byte[] fontSize = "fs".getBytes();
  
  /** Font color tag. */
  private static final byte[] fontColor = "cf".getBytes();
  
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
  private static final byte[] paragraphDefaults = "pard".getBytes();
  
  /** Begin new paragraph tag. */
  private static final byte[] paragraph = "par".getBytes();

  /**
   * Lists
   */
  
  private static final byte[] listDefinition = "list".getBytes();
  private static final byte[] listTemplateID = "listtemplateid".getBytes();
  private static final byte[] hybridList = "hybrid".getBytes();
  private static final byte[] listLevelDefinition = "listlevel".getBytes();
  private static final byte[] listLevelTypeOld = "levelnfc".getBytes();
  private static final byte[] listLevelTypeNew = "levelnfcn".getBytes();
  private static final byte[] listLevelAlignOld = "leveljc".getBytes();
  private static final byte[] listLevelAlignNew = "leveljcn".getBytes();
  private static final byte[] listLevelStartAt = "levelstartat".getBytes();
  private static final byte[] listLevelTextDefinition = "leveltext".getBytes();
  private static final byte[] listLevelTextLength = "\'0".getBytes();
  private static final byte[] listLevelTextStyleNumbers = "\'00.".getBytes();
  private static final byte[] listLevelTextStyleBullet = "u-3913 ?".getBytes();
  private static final byte[] listLevelNumbersDefinition = "levelnumbers".getBytes();
  private static final byte[] listLevelNumbers = "\\'0".getBytes();
  private static final byte[] tabStop = "tx".getBytes();
  private static final byte[] listBegin = "ls".getBytes();
  private static final byte[] listCurrentLevel = "ilvl".getBytes();
  private static final byte[] listTextOld = "listtext".getBytes();
  private static final byte[] tab = "tab".getBytes();
  private static final byte[] listBulletOld = "\'b7".getBytes();
  private static final byte[] listID = "listid".getBytes();
  private static final byte[] listOverride = "listoverride".getBytes();
  private static final byte[] listOverrideCount = "listoverridecount".getBytes();

  /** 
   * Text Style 
   */
  
  /** Bold tag. */
  private static final byte bold = (byte) 'b';
  
  /** Italic tag. */
  private static final byte italic = (byte) 'i';
  
  /** Underline tag. */
  private static final byte[] underline = "ul".getBytes();
  
  /** Strikethrough tag. */
  private static final byte[] strikethrough = "strike".getBytes();

  /** Text alignment left tag. */
  private static final byte[] alignLeft = "ql".getBytes();
  
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
  
  /** 
   * Header / Footer 
   */
  
  /** Begin header group tag. */
  private static final byte[] headerBegin = "header".getBytes();
  
  /** Begin footer group tag. */
  private static final byte[] footerBegin = "footer".getBytes();
  
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
  
  /** Begin GIF / PNG image tag. */
  private static final byte[] imageGifPng = "pngblibp".getBytes();

  /** Begin JPEG image tag. */
  private static final byte[] imageJpeg = "jpegblibp".getBytes();
  
  /** Begin picture group tag. */
  private static final byte[] pictGroupBegin = "shppict".getBytes();
  
  /** Picture tag. */
  private static final byte[] picture = "pict".getBytes();
  
  /** Picture width tag. */
  private static final byte[] pictureWidth = "picwgoal".getBytes();
  
  /** Picture height tag. */
  private static final byte[] pictureHeight = "pichgoal".getBytes();
  
  /** Begin binary data tag. */
  private static final byte[] binaryData = "bin".getBytes();
  
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
  
  /** Which font is currently being used. */
  private int listFont = 0;
  
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
  private int pageWidth = 12240;
  
  /** Page height. */
  private int pageHeight = 15840;
  
  /** Factor to use when converting. */
  protected double twipsFactor = 20.5714;
  
  /** Current annotation ID. */
  private int currentAnnotationID = 0;
  
  /** Current list ID. */
  private int currentListID = 1;
  
  /** List of current Lists. */
  private Vector listIds = null;
  
  /** Current List Level. */
  private int listLevel = 0;
  
  /** Current maximum List Level. */
  private int maxListLevel = 0;
  
  /** Protected Constructor */
  
  /**
   * Constructs a <CODE>RtfWriter</CODE>.
   *
   * @param	document	The <CODE>Document</CODE> that is to be written as RTF
   * @param	os			The <CODE>OutputStream</CODE> the writer has to write to.
   */
  
  protected RtfWriter(Document doc, OutputStream os)
  {
    super(doc, os);
    document.addDocListener(this);
    initDefaults();
  }
  
  /** Public functions from the DocWriter Interface */
  
  /**
   * Gets an instance of the <CODE>RtfWriter</CODE>.
   *
   * @param	document	The <CODE>Document</CODE> that has to be written
   * @param	os	The <CODE>OutputStream</CODE> the writer has to write to.
   * @return	a new <CODE>RtfWriter</CODE>
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
    this.footer = null;
  }
  
  /**
   * Resets the header.
   */
  public void resetHeader()
  {
    this.header = null;
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
    pageWidth = (int) (pageSize.width() * twipsFactor);
    pageHeight = (int) (pageSize.height() * twipsFactor);
    return true;
  }
  
  /**
   * Signals that an <CODE>Element</CODE> was added to the <CODE>Document</CODE>.
   *
   * @return	<CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
   * @throws	DocumentException	if a document isn't open yet, or has been closed
   */
  public boolean add(Element element) throws DocumentException
  {
    return addElement(element, content);
  }

  /** Private functions */

  /**
   * Adds an <CODE>Element</CODE> was added to the <CODE>Document</CODE>.
   *
   * @return	<CODE>true</CODE> if the element was added, <CODE>false</CODE> if not.
   * @throws	DocumentException	if a document isn't open yet, or has been closed
   */
  private boolean addElement(Element element, ByteArrayOutputStream out) throws DocumentException
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
	  case Element.TABLE        : writeTable((Table) element, out);	               break;
	  
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
  private void writeSection(Section sectionElement, ByteArrayOutputStream out) throws IOException
  {
    out.write(escape);
    out.write(sectionDefaults);
    if(sectionElement.title() != null)
      {
	sectionElement.title().process(this);
	out.write(escape);
	out.write(paragraph);
      }
    sectionElement.process(this);
    out.write(escape);
    out.write(section);
  }
  
  /** 
   * Write the beginning of a new <code>Paragraph</code>
   *
   * @param paragraphElement The <code>Paragraph</code> to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   * 
   * @throws IOException, DocumentExceptipn
   */
  private void writeParagraph(Paragraph paragraphElement, ByteArrayOutputStream out) throws IOException, DocumentException
  {
    out.write(escape);
    out.write(paragraphDefaults);
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
	ch.setFont(ch.font().difference(paragraphElement.font()));
	addElement(ch, out);
      }
    out.write(escape);
    out.write(paragraph);
  }
  
  /** 
   * Write a <code>Phrase</code>.
   *
   * @param chunk The <code>Phrase</code> item to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   * 
   * @throws IOException, DocumentException
   */
  private void writePhrase(Phrase phrase, ByteArrayOutputStream out) throws IOException, DocumentException
  {
    out.write(escape);
    out.write(paragraphDefaults);
    Iterator chunks = phrase.getChunks().iterator();
    while(chunks.hasNext())
      {
	Chunk ch = (Chunk) chunks.next();
	addElement(ch, out);
      }
  }
  
  /** 
   * Write an <code>Anchor</code>. Anchors are treated like Phrases.
   *
   * @param chunk The <code>Chunk</code> item to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   * 
   * @throws IOException
   */
  private void writeAnchor(Anchor anchor, ByteArrayOutputStream out) throws IOException, DocumentException
  {
    writePhrase(anchor, out);
    Font urlFont = anchor.font();
    urlFont.setColor(new Color(0,0,255));
    urlFont.setStyle("underline");
    Chunk href = new Chunk("(" + anchor.url().toString() + ")", urlFont);
    out.write(delimiter);
    writeChunk(href, out);
    out.write(delimiter);
  }
  
  /** 
   * Write a <code>Chunk</code> and all its font properties.
   *
   * @param chunk The <code>Chunk</code> item to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   * 
   * @throws IOException
   */
  private void writeChunk(Chunk chunk, ByteArrayOutputStream out) throws IOException
  {
    out.write(escape);
    out.write(fontNumber);
    if(chunk.font().family() != Font.UNDEFINED) { writeInt(out, addFont(chunk.font())); } else { writeInt(out, 0); }
    out.write(escape);
    out.write(fontSize);
    if(chunk.font().size() > 0) { writeInt(out, (int) (chunk.font().size() * 2)); } else { writeInt(out, 20); }
    out.write(escape);
    out.write(fontColor);
    writeInt(out, addColor(chunk.font().color()));
    if(chunk.font().isBold()) { out.write(escape); out.write(bold); }
    if(chunk.font().isItalic()) { out.write(escape); out.write(italic); }
    if(chunk.font().isUnderlined()) { out.write(escape); out.write(underline); } 
    if(chunk.font().isStrikethru()) { out.write(escape); out.write(strikethrough); }
    out.write(delimiter);
    out.write(chunk.content().getBytes());
    if(chunk.font().isBold()) { out.write(escape); out.write(bold); writeInt(out, 0); }
    if(chunk.font().isItalic()) { out.write(escape); out.write(italic); writeInt(out, 0); }
    if(chunk.font().isUnderlined()) { out.write(escape); out.write(underline); writeInt(out, 0); }
    if(chunk.font().isStrikethru()) { out.write(escape); out.write(strikethrough); writeInt(out, 0); }
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
   * @throws IOException, DocumentException
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
   * 
   * @throws IOException, DocumentException
   */
  
  private void writeTable(Table table, ByteArrayOutputStream out) throws IOException, DocumentException
  {
    RtfTable rtfTable = new RtfTable(this);
    rtfTable.importTable(table, 12239);
    rtfTable.writeTable(out);
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
  }
  
  /** 
   * Write am <code>Image</code> and all its font properties.
   *
   * @param image The <code>Image</code> to be written
   * @param imageType The type of image to be written
   * @param out The <code>ByteArrayOutputStream</code> to write to
   * 
   * @throws IOException
   */

  //  I'm having problems with this, because image.rawData() always returns null
  //  Don't know why
  private void writeImage(Image image, byte[] imageType, ByteArrayOutputStream out) throws IOException
  {
  }
  
  /** 
   * Add a <code>Meta</code> element. It is written to the Inforamtion Group
   * and merged with the main <code>ByteArrayOutputStream</code> when the
   * Document is closed.
   *
   * @param metaName The type of <code>Meta</code> element to be added
   * @param meta The <code>Meta</code> element to be added
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
     *
     * @return <code>true</code if the writing operation succeded
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
  private void writeInt(OutputStream out, int i) throws IOException
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
   * Write the header to the final <code>ByteArrayOutputStream</code>
   */
  private void writeHeader() throws IOException
  {
  }
  
  /** 
   * Write the footer to the final <code>ByteArrayOutputStream</code>
   */
  private void writeFooter() throws IOException
  {
  }
  
  /** 
   *  Write the <code>Document</code>'s Paper and Margin Size
   *  to the final <code>ByteArrayOutputStream</code>
   */
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

  /**
   * Initialise all helper classes.
   * Clears alls lists, creates new <code>ByteArrayOutputStream</code>'s
   *
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
	listtable.write("listtable".getBytes());
	listtable.write((byte) '\n');
	listoverride.write(openGroup);
	listoverride.write(extendedEscape);
	listoverride.write("listoverridetable".getBytes());
	listoverride.write((byte) '\n');
      }
    catch(IOException e)
      {
	System.out.println(e);
      }
  }
}
