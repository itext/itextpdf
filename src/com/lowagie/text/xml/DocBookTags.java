/*
 * $Id$
 * $Name$
 *
 * Copyright 2001, 2002 by Bruno Lowagie.
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
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
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

package com.lowagie.text.xml;

/**
 * A class that contains all DocBook tags.
 */
public class DocBookTags {
	/** An abbreviation, especially one followed by a period. */
	public static final String ABBREV = "abbrev";

	/** A summary. */
	public static final String ABSTRACT = "abstract";

	/** A graphical user interface (GUI) keyboard shortcut. */
	public static final String ACCEL = "accel";

	/** Acknowledgements in an Article. */
	public static final String ACKNO = "ackno";

	/** An often pronounceable word made from the initial (or selected) letters of a name or phrase. */
	public static final String ACRONYM = "acronym";

	/** A response to a user event. */
	public static final String ACTION = "action";

	/** A real-world address, generally a postal address. */
	public static final String ADDRESS = "address";

	/** The institutional affiliation of an individual. */
	public static final String AFFILIATION = "affiliation";

	/** Text representation for a graphical element. */
	public static final String ALT = "alt";

	/** A spot in the document. */
	public static final String ANCHOR = "anchor";

	/** An answer to a question posed in a QandASet. */
	public static final String ANSWER = "answer";

	/** An appendix in a Book or Article. */
	public static final String APPENDIX = "appendix";

	/** - Meta-information for an Appendix. */
	public static final String APPENDIXINFO = "appendixinfo";

	/** The name of a software program. */
	public static final String APPLICATION = "application";

	/** A region defined for a Callout in a graphic or code example. */
	public static final String AREA = "area";

	/** A set of related areas in a graphic or code example. */
	public static final String AREASET = "areaset";

	/** A collection of regions in a graphic or code example. */
	public static final String AREASPEC = "areaspec";

	/** An argument in a CmdSynopsis. */
	public static final String ARG = "arg";

	/** An article. */
	public static final String ARTICLE = "article";

	/** - Meta-information for an Article. */
	public static final String ARTICLEINFO = "articleinfo";

	/** The page numbers of an article as published. */
	public static final String ARTPAGENUMS = "artpagenums";

	/** The source of a block quote or epigraph. */
	public static final String ATTRIBUTION = "attribution";

	/** Pointer to external audio data. */
	public static final String AUDIODATA = "audiodata";

	/** A wrapper for audio data and its associated meta-information. */
	public static final String AUDIOOBJECT = "audioobject";

	/** The name of an individual author. */
	public static final String AUTHOR = "author";

	/** A short description or note about an author. */
	public static final String AUTHORBLURB = "authorblurb";

	/** Wrapper for author information when a document has multiple authors or collabarators. */
	public static final String AUTHORGROUP = "authorgroup";

	/** The initials or other short identifier for an author. */
	public static final String AUTHORINITIALS = "authorinitials";

	/** Thelocation of a page break in a print version of thedocument. */
	public static final String BEGINPAGE = "beginpage";

	/** A section of a Bibliography. */
	public static final String BIBLIODIV = "bibliodiv";

	/** An entry in a Bibliography. */
	public static final String BIBLIOENTRY = "biblioentry";

	/** A bibliography. */
	public static final String BIBLIOGRAPHY = "bibliography";

	/** - Meta-information for a Bibliography. */
	public static final String BIBLIOGRAPHYINFO = "bibliographyinfo";

	/** Untyped bibliographic information. */
	public static final String BIBLIOMISC = "bibliomisc";

	/** An entry in a Bibliography. */
	public static final String BIBLIOMIXED = "bibliomixed";

	/** A cooked container for related bibliographic information. */
	public static final String BIBLIOMSET = "bibliomset";

	/** A raw container for related bibliographic information. */
	public static final String BIBLIOSET = "biblioset";

	/** A quotation set off from the main text. */
	public static final String BLOCKQUOTE = "blockquote";

	/** Abook. */
	public static final String BOOK = "book";

	/** Meta-information for a Book. */
	public static final String BOOKINFO = "bookinfo";

	/** A free-floating heading. */
	public static final String BRIDGEHEAD = "bridgehead";

	/** A “called out” description of a marked Area. */
	public static final String CALLOUT = "callout";

	/** A list of Callouts. */
	public static final String CALLOUTLIST = "calloutlist";

	/** A caption. */
	public static final String CAPTION = "caption";

	/** A note of caution. */
	public static final String CAUTION = "caution";

	/** A chapter, as of a book. */
	public static final String CHAPTER = "chapter";

	/** - Meta-information for a Chapter. */
	public static final String CHAPTERINFO = "chapterinfo";

	/** An inline bibliographic reference to another published work. */
	public static final String CITATION = "citation";

	/** A citation to a reference page. */
	public static final String CITEREFENTRY = "citerefentry";

	/** The title of a cited work. */
	public static final String CITETITLE = "citetitle";

	/** The name of a city in an address. */
	public static final String CITY = "city";

	/** Thename of a class, in the object-oriented programmingsense. */
	public static final String CLASSNAME = "classname";

	/** - The syntax summary for a class definition. */
	public static final String CLASSSYNOPSIS = "classsynopsis";

	/** - Information supplementing the contents of a ClassSynopsis. */
	public static final String CLASSSYNOPSISINFO = "classsynopsisinfo";

	/** A syntax summary for a software command. */
	public static final String CMDSYNOPSIS = "cmdsynopsis";

	/** The location of a callout embedded in text. */
	public static final String CO = "co";

	/** Identifies a collaborator. */
	public static final String COLLAB = "collab";

	/** The name of a collaborator. */
	public static final String COLLABNAME = "collabname";

	/** Textat the back of a book describing facts about itsproduction. */
	public static final String COLOPHON = "colophon";

	/** Specifications for a column in a table. */
	public static final String COLSPEC = "colspec";

	/** The name of an executable program or other software command. */
	public static final String COMMAND = "command";

	/** Data, generally text, displayed or presented by a computer. */
	public static final String COMPUTEROUTPUT = "computeroutput";

	/** The dates of a conference for which a document was written. */
	public static final String CONFDATES = "confdates";

	/** A wrapper for document meta-information about a conference. */
	public static final String CONFGROUP = "confgroup";

	/** An identifier, frequently numerical, associated with a conference for which a document was written. */
	public static final String CONFNUM = "confnum";

	/** The sponsor of a conference for which a document was written. */
	public static final String CONFSPONSOR = "confsponsor";

	/** The title of a conference for which a document was written. */
	public static final String CONFTITLE = "conftitle";

	/** A programming or system constant. */
	public static final String CONSTANT = "constant";

	/** (EBNF)A constraint in an EBNF production. */
	public static final String CONSTRAINT = "constraint";

	/** (EBNF)The definition of a constraint in an EBNF production. */
	public static final String CONSTRAINTDEF = "constraintdef";

	/** - A syntax summary for a constructor. */
	public static final String CONSTRUCTORSYNOPSIS = "constructorsynopsis";

	/** The contract number of a document. */
	public static final String CONTRACTNUM = "contractnum";

	/** The sponsor of a contract. */
	public static final String CONTRACTSPONSOR = "contractsponsor";

	/** Asummary of the contributions made to a document by a creditedsource. */
	public static final String CONTRIB = "contrib";

	/** Copyright information about a document. */
	public static final String COPYRIGHT = "copyright";

	/** A corporate author, as opposed to an individual. */
	public static final String CORPAUTHOR = "corpauthor";

	/** The name of a corporation. */
	public static final String CORPNAME = "corpname";

	/** The name of a country. */
	public static final String COUNTRY = "country";

	/** The name of a database, or part of a database. */
	public static final String DATABASE = "database";

	/** The date of publication or revision of a document. */
	public static final String DATE = "date";

	/** A wrapper for the dedication section of a book. */
	public static final String DEDICATION = "dedication";

	/** - A syntax summary for a destructor. */
	public static final String DESTRUCTORSYNOPSIS = "destructorsynopsis";

	/** The name or number of an edition of a document. */
	public static final String EDITION = "edition";

	/** The name of the editor of a document. */
	public static final String EDITOR = "editor";

	/** An email address. */
	public static final String EMAIL = "email";

	/** Emphasized text. */
	public static final String EMPHASIS = "Emphasis";

	/** A cell in a table. */
	public static final String ENTRY = "entry";

	/** A subtable appearing in place of an Entry in a table. */
	public static final String ENTRYTBL = "entrytbl";

	/** A software environment variable. */
	public static final String ENVAR = "envar";

	/** Ashort inscription at the beginning of a document orcomponent. */
	public static final String EPIGRAPH = "epigraph";

	/** A displayed mathematical equation. */
	public static final String EQUATION = "equation";

	/** - An error code. */
	public static final String ERRORCODE = "errorcode";

	/** - An error name. */
	public static final String ERRORNAME = "errorname";

	/** - The classification of an error message. */
	public static final String ERRORTYPE = "errortype";

	/** A formal example, with a title. */
	public static final String EXAMPLE = "example";

	/** - The name of an exception. */
	public static final String EXCEPTIONNAME = "exceptionname";

	/** A fax number. */
	public static final String FAX = "fax";

	/** - The name of a field in a class definition. */
	public static final String FIELDSYNOPSIS = "fieldsynopsis";

	/** A formal figure, generally an illustration, with a title. */
	public static final String FIGURE = "figure";

	/** The name of a file. */
	public static final String FILENAME = "filename";

	/** The first name of a person. */
	public static final String FIRSTNAME = "firstname";

	/** The first occurrence of a term. */
	public static final String FIRSTTERM = "firstterm";

	/** A footnote. */
	public static final String FOOTNOTE = "footnote";

	/** A cross reference to a footnote (a footnote mark). */
	public static final String FOOTNOTEREF = "footnoteref";

	/** A word or phrase in a language other than the primary language of the document. */
	public static final String FOREIGNPHRASE = "foreignphrase";

	/** A paragraph with a title. */
	public static final String FORMALPARA = "formalpara";

	/** A function (subroutine) name and its return type. */
	public static final String FUNCDEF = "funcdef";

	/** Parameters for a function referenced through a function pointer in a synopsis. */
	public static final String FUNCPARAMS = "funcparams";

	/** The prototype of a function. */
	public static final String FUNCPROTOTYPE = "funcprototype";

	/** The syntax summary for a function definition. */
	public static final String FUNCSYNOPSIS = "funcsynopsis";

	/** Information supplementing the FuncDefs of a FuncSynopsis. */
	public static final String FUNCSYNOPSISINFO = "funcsynopsisinfo";

	/** Thename of a function or subroutine, as in a programminglanguage. */
	public static final String FUNCTION = "function";

	/** A glossary. */
	public static final String GLOSSARY = "glossary";

	/** - Meta-information for a Glossary. */
	public static final String GLOSSARYINFO = "glossaryinfo";

	/** A definition in a GlossEntry. */
	public static final String GLOSSDEF = "glossdef";

	/** A division in a Glossary. */
	public static final String GLOSSDIV = "glossdiv";

	/** An entry in a Glossary or GlossList. */
	public static final String GLOSSENTRY = "glossentry";

	/** A wrapper for a set of GlossEntrys. */
	public static final String GLOSSLIST = "glosslist";

	/** A cross-reference from one GlossEntry to another. */
	public static final String GLOSSSEE = "glosssee";

	/** A cross-reference from one GlossEntry to another. */
	public static final String GLOSSSEEALSO = "glossseealso";

	/** A glossary term. */
	public static final String GLOSSTERM = "glossterm";

	/** A displayed graphical object (not an inline). */
	public static final String GRAPHIC = "graphic";

	/** A graphic that contains callout areas. */
	public static final String GRAPHICCO = "graphicco";

	/** A group of elements in a CmdSynopsis. */
	public static final String GROUP = "group";

	/** The text on a button in a GUI. */
	public static final String GUIBUTTON = "guibutton";

	/** Graphic and/or text appearing as a icon in a GUI. */
	public static final String GUIICON = "guiicon";

	/** The text of a label in a GUI. */
	public static final String GUILABEL = "guilabel";

	/** The name of a menu in a GUI. */
	public static final String GUIMENU = "guimenu";

	/** The name of a terminal menu item in a GUI. */
	public static final String GUIMENUITEM = "guimenuitem";

	/** The name of a submenu in a GUI. */
	public static final String GUISUBMENU = "guisubmenu";

	/** A physical part of a computer system. */
	public static final String HARDWARE = "hardware";

	/** A summary of the main points of the discussed component. */
	public static final String HIGHLIGHTS = "highlights";

	/** The name of the individual or organization that holds a copyright. */
	public static final String HOLDER = "holder";

	/** The title of a person. */
	public static final String HONORIFIC = "honorific";

	/** Pointer to external image data. */
	public static final String IMAGEDATA = "imagedata";

	/** A wrapper for image data and its associated meta-information. */
	public static final String IMAGEOBJECT = "imageobject";

	/** A wrapper for an image object with callouts. */
	public static final String IMAGEOBJECTCO = "imageobjectco";

	/** An admonition set off from the text. */
	public static final String IMPORTANT = "important";

	/** An index. */
	public static final String INDEX = "index";

	/** A division in an index. */
	public static final String INDEXDIV = "indexdiv";

	/** An entry in an index. */
	public static final String INDEXENTRY = "indexentry";

	/** - Meta-information for an Index. */
	public static final String INDEXINFO = "indexinfo";

	/** A wrapper for terms to be indexed. */
	public static final String INDEXTERM = "indexterm";

	/** A displayed mathematical equation without a title. */
	public static final String INFORMALEQUATION = "informalequation";

	/** A displayed example without a title. */
	public static final String INFORMALEXAMPLE = "informalexample";

	/** A untitled figure. */
	public static final String INFORMALFIGURE = "informalfigure";

	/** A table without a title. */
	public static final String INFORMALTABLE = "informaltable";

	/** - The initializer for a FieldSynopsis. */
	public static final String INITIALIZER = "initializer";

	/** A mathematical equation or expression occurring inline. */
	public static final String INLINEEQUATION = "inlineequation";

	/** An object containing or pointing to graphical data that will be rendered inline. */
	public static final String INLINEGRAPHIC = "inlinegraphic";

	/** An inline media object (video, audio, image, and so on). */
	public static final String INLINEMEDIAOBJECT = "inlinemediaobject";

	/** An element of a GUI. */
	public static final String INTERFACE = "interface";

	/** - The name of an interface. */
	public static final String INTERFACENAME = "interfacename";

	/** An inventory part number. */
	public static final String INVPARTNUMBER = "invpartnumber";

	/** The International Standard Book Number of a document. */
	public static final String ISBN = "isbn";

	/** The International Standard Serial Number of a periodical. */
	public static final String ISSN = "issn";

	/** The number of an issue of a journal. */
	public static final String ISSUENUM = "issuenum";

	/** Alist in which each entry is marked with a bullet or otherdingbat. */
	public static final String ITEMIZEDLIST = "itemizedlist";

	/** A set of index terms in the meta-information of a document. */
	public static final String ITERMSET = "itermset";

	/** The title of an individual in an organization. */
	public static final String JOBTITLE = "jobtitle";

	/** The text printed on a key on a keyboard. */
	public static final String KEYCAP = "keycap";

	/** Theinternal, frequently numeric, identifier for a key on akeyboard. */
	public static final String KEYCODE = "keycode";

	/** A combination of input actions. */
	public static final String KEYCOMBO = "keycombo";

	/** The symbolic name of a key on a keyboard. */
	public static final String KEYSYM = "keysym";

	/** One of a set of keywords describing the content of a document. */
	public static final String KEYWORD = "keyword";

	/** A set of keywords describing the content of a document. */
	public static final String KEYWORDSET = "keywordset";

	/** A label on a Question or Answer. */
	public static final String LABEL = "label";

	/** A statement of legal obligations or requirements. */
	public static final String LEGALNOTICE = "legalnotice";

	/** (EBNF)The left-hand side of an EBNF production. */
	public static final String LHS = "lhs";

	/** Theportion of a person's name indicating a relationship toancestors. */
	public static final String LINEAGE = "lineage";

	/** A comment on a line in a verbatim listing. */
	public static final String LINEANNOTATION = "lineannotation";

	/** A hypertext link. */
	public static final String LINK = "link";

	/** A wrapper for the elements of a list item. */
	public static final String LISTITEM = "listitem";

	/** Inline text that is some literal value. */
	public static final String LITERAL = "literal";

	/** A block of text in which line breaks and white space are to be reproduced faithfully. */
	public static final String LITERALLAYOUT = "literallayout";

	/** A list ofthe titles of formal objects (as tables or figures) in adocument. */
	public static final String LOT = "lot";

	/** An entry in a list of titles. */
	public static final String LOTENTRY = "lotentry";

	/** A reference volume number. */
	public static final String MANVOLNUM = "manvolnum";

	/** A stringof formatting markup in text that is to be representedliterally. */
	public static final String MARKUP = "markup";

	/** (MathML)A MathML equation. */
	public static final String MML_MATH = "mml:math";

	/** Aname that identifies the physical medium on which some informationresides. */
	public static final String MEDIALABEL = "medialabel";

	/** A displayed media object (video, audio, image, etc.). */
	public static final String MEDIAOBJECT = "mediaobject";

	/** A media object that contains callouts. */
	public static final String MEDIAOBJECTCO = "mediaobjectco";

	/** An element of a simple list. */
	public static final String MEMBER = "member";

	/** A selection or series of selections from a menu. */
	public static final String MENUCHOICE = "menuchoice";

	/** - The name of a method. */
	public static final String METHODNAME = "methodname";

	/** - Parameters to a method. */
	public static final String METHODPARAM = "methodparam";

	/** - A syntax summary for a method. */
	public static final String METHODSYNOPSIS = "methodsynopsis";

	/**  -Application-specific information necessary for the completion of anOLink. */
	public static final String MODESPEC = "modespec";

	/** - Modifiers in a synopsis. */
	public static final String MODIFIER = "modifier";

	/** The conventional name of a mouse button. */
	public static final String MOUSEBUTTON = "mousebutton";

	/** A message in a message set. */
	public static final String MSG = "msg";

	/** The audience to which a message in a message set is relevant. */
	public static final String MSGAUD = "msgaud";

	/** A wrapper for an entry in a message set. */
	public static final String MSGENTRY = "msgentry";

	/** Explanatory material relating to a message in a message set. */
	public static final String MSGEXPLAN = "msgexplan";

	/** Information about a message in a message set. */
	public static final String MSGINFO = "msginfo";

	/** Thelevel of importance or severity of a message in a messageset. */
	public static final String MSGLEVEL = "msglevel";

	/** The primary component of a message in a message set . */
	public static final String MSGMAIN = "msgmain";

	/** The origin of a message in a message set. */
	public static final String MSGORIG = "msgorig";

	/** A related component of a message in a message set. */
	public static final String MSGREL = "msgrel";

	/** A detailed set of messages, usually error messages. */
	public static final String MSGSET = "msgset";

	/** A subcomponent of a message in a message set. */
	public static final String MSGSUB = "msgsub";

	/** The actual text of a message component in a message set. */
	public static final String MSGTEXT = "msgtext";

	/** (EBNF)A non-terminal in an EBNF production. */
	public static final String NONTERMINAL = "nonterminal";

	/** A message set off from the text. */
	public static final String NOTE = "note";

	/** Meta-information for an object. */
	public static final String OBJECTINFO = "objectinfo";

	/** A link that addresses its target indirectly, through an entity. */
	public static final String OLINK = "olink";

	/** - A class in an object-oriented programming language. */
	public static final String OOCLASS = "ooclass";

	/** - An exception in an object-oriented programming language. */
	public static final String OOEXCEPTION = "ooexception";

	/** - An interface in an object-oriented programming language. */
	public static final String OOINTERFACE = "oointerface";

	/** An option for a software command. */
	public static final String OPTION = "option";

	/** Optional information. */
	public static final String OPTIONAL = "optional";

	/** Alist in which each entry is marked with a sequentially incrementedlabel. */
	public static final String ORDEREDLIST = "orderedlist";

	/** A division of an organization. */
	public static final String ORGDIV = "orgdiv";

	/** The name of an organization other than a corporation . */
	public static final String ORGNAME = "orgname";

	/** Uncategorized information in address. */
	public static final String OTHERADDR = "otheraddr";

	/** Aperson or entity, other than an author or editor, credited in adocument. */
	public static final String OTHERCREDIT = "othercredit";

	/** A component of a persons name that is not a first name, surname, or lineage. */
	public static final String OTHERNAME = "othername";

	/** Thenumbers of the pages in a book, for use in a bibliographicentry. */
	public static final String PAGENUMS = "pagenums";

	/** A paragraph. */
	public static final String PARA = "para";

	/**  -Information about a function parameter in a programminglanguage. */
	public static final String PARAMDEF = "paramdef";

	/** A value or a symbolic reference to a value. */
	public static final String PARAMETER = "parameter";

	/** A division in a book. */
	public static final String PART = "part";

	/** - Meta-information for a Part. */
	public static final String PARTINFO = "partinfo";

	/** An introduction to the contents of a part. */
	public static final String PARTINTRO = "partintro";

	/** A telephone number. */
	public static final String PHONE = "phone";

	/** A span of text. */
	public static final String PHRASE = "phrase";

	/** A post office box in an address. */
	public static final String POB = "pob";

	/** A postal code in an address. */
	public static final String POSTCODE = "postcode";

	/** Introductory matter preceding the first chapter of a book. */
	public static final String PREFACE = "preface";

	/** - Meta-information for a Preface. */
	public static final String PREFACEINFO = "prefaceinfo";

	/** Theprimary word or phrase under which an index term should besorted. */
	public static final String PRIMARY = "primary";

	/** A primary term in an index entry, not in the text. */
	public static final String PRIMARYIE = "primaryie";

	/** The printing history of a document. */
	public static final String PRINTHISTORY = "printhistory";

	/** Alist of operations to be performed in a well-definedsequence. */
	public static final String PROCEDURE = "procedure";

	/** (EBNF)A production in a set of EBNF productions. */
	public static final String PRODUCTION = "production";

	/** (EBNF)A cross-reference to an EBNF production. */
	public static final String PRODUCTIONRECAP = "productionrecap";

	/** (EBNF)A set of EBNF productions. */
	public static final String PRODUCTIONSET = "productionset";

	/** The formal name of a product. */
	public static final String PRODUCTNAME = "productname";

	/** A number assigned to a product. */
	public static final String PRODUCTNUMBER = "productnumber";

	/** A literal listing of all or part of a program. */
	public static final String PROGRAMLISTING = "programlisting";

	/** A program listing with associated areas used in callouts. */
	public static final String PROGRAMLISTINGCO = "programlistingco";

	/** A character or string indicating the start of an input field in a computer display. */
	public static final String PROMPT = "prompt";

	/** A unit of data associated with some part of a computer system. */
	public static final String PROPERTY = "property";

	/** The date of publication of a document. */
	public static final String PUBDATE = "pubdate";

	/** The publisher of a document. */
	public static final String PUBLISHER = "publisher";

	/** The name of the publisher of a document. */
	public static final String PUBLISHERNAME = "publishername";

	/** A number assigned to a publication other than an ISBN or ISSN or inventory part number. */
	public static final String PUBSNUMBER = "pubsnumber";

	/** A titled division in a QandASet. */
	public static final String QANDADIV = "qandadiv";

	/** A question/answer set within a QandASet. */
	public static final String QANDAENTRY = "qandaentry";

	/** A question-and-answer set. */
	public static final String QANDASET = "qandaset";

	/** A question in a QandASet. */
	public static final String QUESTION = "question";

	/** An inline quotation. */
	public static final String QUOTE = "quote";

	/** Thescope or other indication of applicability of a referenceentry. */
	public static final String REFCLASS = "refclass";

	/** A description of the topic of a reference page. */
	public static final String REFDESCRIPTOR = "refdescriptor";

	/** A reference page (originally a UNIX man-style reference page). */
	public static final String REFENTRY = "refentry";

	/** - Meta-information for a Refentry. */
	public static final String REFENTRYINFO = "refentryinfo";

	/** The title of a reference page. */
	public static final String REFENTRYTITLE = "refentrytitle";

	/** A collection of reference entries. */
	public static final String REFERENCE = "reference";

	/** - Meta-information for a Reference. */
	public static final String REFERENCEINFO = "referenceinfo";

	/** Meta-information for a reference entry. */
	public static final String REFMETA = "refmeta";

	/** Meta-information for a reference entry other than the title and volume number. */
	public static final String REFMISCINFO = "refmiscinfo";

	/** The name of (one of) the subject(s) of a reference page. */
	public static final String REFNAME = "refname";

	/** The name, purpose, and classification of a reference page. */
	public static final String REFNAMEDIV = "refnamediv";

	/** Ashort (one sentence) synopsis of the topic of a referencepage. */
	public static final String REFPURPOSE = "refpurpose";

	/** A major subsection of a reference entry. */
	public static final String REFSECT1 = "refsect1";

	/** Meta-information for a RefSect1. */
	public static final String REFSECT1INFO = "refsect1info";

	/** A subsection of a RefSect1. */
	public static final String REFSECT2 = "refsect2";

	/** Meta-information for a RefSect2. */
	public static final String REFSECT2INFO = "refsect2info";

	/** A subsection of a RefSect2. */
	public static final String REFSECT3 = "refsect3";

	/** Meta-information for a RefSect3. */
	public static final String REFSECT3INFO = "refsect3info";

	/** A syntactic synopsis of the subject of the reference page. */
	public static final String REFSYNOPSISDIV = "refsynopsisdiv";

	/** Meta-information for a RefSynopsisDiv. */
	public static final String REFSYNOPSISDIVINFO = "refsynopsisdivinfo";

	/** Information about a particular release of a document. */
	public static final String RELEASEINFO = "releaseinfo";

	/** - A remark (or comment) intended for presentation in a draft manuscript. */
	public static final String REMARK = "remark";

	/** Content that may or must be replaced by the user. */
	public static final String REPLACEABLE = "replaceable";

	/** The value returned by a function. */
	public static final String RETURNVALUE = "returnvalue";

	/** - A extended description of a revision to a document. */
	public static final String REVDESCRIPTION = "revdescription";

	/** A history of the revisions to a document. */
	public static final String REVHISTORY = "revhistory";

	/** An entry describing a single revision in the history of the revisions to a document. */
	public static final String REVISION = "revision";

	/** A document revision number. */
	public static final String REVNUMBER = "revnumber";

	/** A description of a revision to a document. */
	public static final String REVREMARK = "revremark";

	/** (EBNF)The right-hand side of an EBNF production. */
	public static final String RHS = "rhs";

	/** A row in a table. */
	public static final String ROW = "row";

	/** An explicit line break in a command synopsis. */
	public static final String SBR = "sbr";

	/** Text that a user sees or might see on a computer screen. */
	public static final String SCREEN = "screen";

	/** A screen with associated areas used in callouts. */
	public static final String SCREENCO = "screenco";

	/** Information about how a screen shot was produced. */
	public static final String SCREENINFO = "screeninfo";

	/** Arepresentation of what the user sees or might see on a computerscreen. */
	public static final String SCREENSHOT = "screenshot";

	/** A secondary word or phrase in an index term. */
	public static final String SECONDARY = "secondary";

	/** A secondary term in an index entry, rather than in the text. */
	public static final String SECONDARYIE = "secondaryie";

	/** A top-level section of document. */
	public static final String SECT1 = "sect1";

	/** Meta-information for a Sect1. */
	public static final String SECT1INFO = "sect1info";

	/** A subsection within a Sect1. */
	public static final String SECT2 = "sect2";

	/** Meta-information for a Sect2. */
	public static final String SECT2INFO = "sect2info";

	/** A subsection within a Sect2. */
	public static final String SECT3 = "sect3";

	/** Meta-information for a Sect3. */
	public static final String SECT3INFO = "sect3info";

	/** A subsection within a Sect3. */
	public static final String SECT4 = "sect4";

	/** Meta-information for a Sect4. */
	public static final String SECT4INFO = "sect4info";

	/** A subsection within a Sect4. */
	public static final String SECT5 = "sect5";

	/** Meta-information for a Sect5. */
	public static final String SECT5INFO = "sect5info";

	/** A recursive section. */
	public static final String SECTION = "section";

	/**  -Meta-information for a recursive section. */
	public static final String SECTIONINFO = "sectioninfo";

	/** Part of anindex term directing the reader instead to another entry in theindex. */
	public static final String SEE = "see";

	/** Part of an index term directing the reader also to another entry in the index. */
	public static final String SEEALSO = "seealso";

	/** A See also entry in an index, rather than in the text. */
	public static final String SEEALSOIE = "seealsoie";

	/** A See entry in an index, rather than in the text. */
	public static final String SEEIE = "seeie";

	/** An element of a list item in a segmented list. */
	public static final String SEG = "seg";

	/** A list item in a segmented list. */
	public static final String SEGLISTITEM = "seglistitem";

	/** A segmented list, a list of sets of elements. */
	public static final String SEGMENTEDLIST = "segmentedlist";

	/** The title of an element of a list item in a segmented list. */
	public static final String SEGTITLE = "segtitle";

	/** Numbers of the volumes in a series of books. */
	public static final String SERIESVOLNUMS = "seriesvolnums";

	/** A collection of books. */
	public static final String SET = "set";

	/** An index to a set of books. */
	public static final String SETINDEX = "setindex";

	/** - Meta-information for a SetIndex. */
	public static final String SETINDEXINFO = "setindexinfo";

	/** Meta-information for a Set. */
	public static final String SETINFO = "setinfo";

	/** A component of SGML markup. */
	public static final String SGMLTAG = "sgmltag";

	/** A brief description of an affiliation. */
	public static final String SHORTAFFIL = "shortaffil";

	/** A keycombination for an action that is also accessible through amenu. */
	public static final String SHORTCUT = "shortcut";

	/** Aportion of a document that is isolated from the main narrativeflow. */
	public static final String SIDEBAR = "sidebar";

	/** - Meta-information for a Sidebar. */
	public static final String SIDEBARINFO = "sidebarinfo";

	/** Aparagraph that contains only text and inline markup, no blockelements. */
	public static final String SIMPARA = "simpara";

	/** An undecorated list of single words or short phrases. */
	public static final String SIMPLELIST = "simplelist";

	/** - A wrapper for a simpler entry in a message set. */
	public static final String SIMPLEMSGENTRY = "simplemsgentry";

	/** A section of a document with no subdivisions. */
	public static final String SIMPLESECT = "simplesect";

	/** Formatting information for a spanned column in a table. */
	public static final String SPANSPEC = "spanspec";

	/** A state or province in an address. */
	public static final String STATE = "state";

	/** A unit of action in a procedure. */
	public static final String STEP = "step";

	/** A street address in an address. */
	public static final String STREET = "street";

	/** A field in a structure (in the programming language sense). */
	public static final String STRUCTFIELD = "structfield";

	/** The name of a structure (in the programming language sense). */
	public static final String STRUCTNAME = "structname";

	/** One ofa group of terms describing the subject matter of adocument. */
	public static final String SUBJECT = "subject";

	/** A set of terms describing the subject matter of a document. */
	public static final String SUBJECTSET = "subjectset";

	/** Aterm in a group of terms describing the subject matter of adocument. */
	public static final String SUBJECTTERM = "subjectterm";

	/** A subscript (as in H2O, the molecular formula for water). */
	public static final String SUBSCRIPT = "subscript";

	/** A wrapper for steps that occur within steps in a procedure. */
	public static final String SUBSTEPS = "substeps";

	/** The subtitle of a document. */
	public static final String SUBTITLE = "subtitle";

	/** A superscript (as in x2, the mathematical notation for x multiplied by itself). */
	public static final String SUPERSCRIPT = "superscript";

	/** A family name; in western cultures the last name. */
	public static final String SURNAME = "surname";

	/** (SVG)An SVG graphic. */
	public static final String SVG_SVG = "svg:svg";

	/** A name that is replaced by a value before processing. */
	public static final String SYMBOL = "symbol";

	/** Aportion of a CmdSynopsis broken out from the main body of thesynopsis. */
	public static final String SYNOPFRAGMENT = "synopfragment";

	/** A reference to a fragment of a command synopsis. */
	public static final String SYNOPFRAGMENTREF = "synopfragmentref";

	/** A general-purpose element for representing the syntax of commands or functions. */
	public static final String SYNOPSIS = "synopsis";

	/** A system-related item or term. */
	public static final String SYSTEMITEM = "systemitem";

	/** A formal table in a document. */
	public static final String TABLE = "table";

	/** A wrapper for the rows of a table or informal table. */
	public static final String TBODY = "tbody";

	/** The word or phrase being defined or described in a variable list. */
	public static final String TERM = "term";

	/** A tertiary word or phrase in an index term. */
	public static final String TERTIARY = "tertiary";

	/** A tertiary term in an index entry, rather than in the text. */
	public static final String TERTIARYIE = "tertiaryie";

	/** A wrapper for a text description of an object and its associated meta-information. */
	public static final String TEXTOBJECT = "textobject";

	/** A table footer consisting of one or more rows. */
	public static final String TFOOT = "tfoot";

	/** A wrapper for the main content of a table, or part of a table. */
	public static final String TGROUP = "tgroup";

	/** A table header consisting of one or more rows. */
	public static final String THEAD = "thead";

	/** A suggestion to the user, set off from the text. */
	public static final String TIP = "tip";

	/** The text of the title of a section of a document or of a formal block-level element. */
	public static final String TITLE = "title";

	/** The abbreviation of a Title. */
	public static final String TITLEABBREV = "titleabbrev";

	/** A table of contents. */
	public static final String TOC = "toc";

	/** An entry in a table of contents for a back matter component. */
	public static final String TOCBACK = "tocback";

	/** Anentry in a table of contents for a component in the body of adocument. */
	public static final String TOCCHAP = "tocchap";

	/** A component title in a table of contents. */
	public static final String TOCENTRY = "tocentry";

	/** An entry in a table of contents for a front matter component. */
	public static final String TOCFRONT = "tocfront";

	/** A top-level entry within a table of contents entry for a chapter-like component. */
	public static final String TOCLEVEL1 = "toclevel1";

	/** A second-level entry within a table of contents entry for a chapter-like component. */
	public static final String TOCLEVEL2 = "toclevel2";

	/** A third-level entry within a table of contents entry for a chapter-like component. */
	public static final String TOCLEVEL3 = "toclevel3";

	/** A fourth-level entry within a table of contents entry for a chapter-like component. */
	public static final String TOCLEVEL4 = "toclevel4";

	/** A fifth-level entry within a table of contents entry for a chapter-like component. */
	public static final String TOCLEVEL5 = "toclevel5";

	/** An entry in a table of contents for a part of a book. */
	public static final String TOCPART = "tocpart";

	/** A unit of information. */
	public static final String TOKEN = "token";

	/** A trademark. */
	public static final String TRADEMARK = "trademark";

	/** The classification of a value. */
	public static final String TYPE = "type";

	/** A linkthat addresses its target by means of a URL (Uniform ResourceLocator). */
	public static final String ULINK = "ulink";

	/** Dataentered by the user. */
	public static final String USERINPUT = "userinput";

	/** Anempty element in a function synopsis indicating a variable number ofarguments. */
	public static final String VARARGS = "varargs";

	/** A list in which each entry is composed of a set of one or more terms and an associated description. */
	public static final String VARIABLELIST = "variablelist";

	/** A wrapper for a set of terms and the associated description in a variable list. */
	public static final String VARLISTENTRY = "varlistentry";

	/** The name of a variable. */
	public static final String VARNAME = "varname";

	/** Pointer to external video data. */
	public static final String VIDEODATA = "videodata";

	/** A wrapper for video data and its associated meta-information. */
	public static final String VIDEOOBJECT = "videoobject";

	/** An empty element in a function synopsis indicating that the function in question takes no arguments. */
	public static final String VOID = "void";

	/** The volume number of a document in a set (as of books in a set or articles in a journal). */
	public static final String VOLUMENUM = "volumenum";

	/** An admonition set off from the text. */
	public static final String WARNING = "warning";

	/** A word meant specifically as a word and not representing anything else. */
	public static final String WORDASWORD = "wordasword";

	/** A cross reference to another part of the document. */
	public static final String XREF = "xref";

	/** The year of publication of a document. */
	public static final String YEAR = "year";
}
