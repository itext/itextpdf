package com.itextpdf.text.xml.xmp;

import com.itextpdf.xmp.XMPConst;
import com.itextpdf.xmp.XMPException;
import com.itextpdf.xmp.XMPMeta;
import com.itextpdf.xmp.XMPUtils;
import com.itextpdf.xmp.options.PropertyOptions;

public class DublinCoreProperties {
    /** External Contributors to the resource (other than the authors). */
    public static final String CONTRIBUTOR = "contributor";
    /** The extent or scope of the resource. */
    public static final String COVERAGE = "coverage";
    /** The authors of the resource (listed in order of precedence, if significant). */
    public static final String CREATOR = "creator";
    /** Date(s) that something interesting happened to the resource. */
    public static final String DATE = "date";
    /** A textual description of the content of the resource. Multiple values may be present for different languages. */
    public static final String DESCRIPTION = "description";
    /** The file format used when saving the resource. Tools and applications should set this property to the save format of the data. It may include appropriate qualifiers. */
    public static final String FORMAT = "format";
    /** Unique identifier of the resource. */
    public static final String IDENTIFIER = "identifier";
    /** An unordered array specifying the languages used in the	resource. */
    public static final String LANGUAGE = "language";
    /** Publishers. */
    public static final String PUBLISHER = "publisher";
    /** Relationships to other documents. */
    public static final String RELATION = "relation";
    /** Informal rights statement, selected by language. */
    public static final String RIGHTS = "rights";
    /** Unique identifier of the work from which this resource was derived. */
    public static final String SOURCE = "source";
    /** An unordered array of descriptive phrases or keywords that specify the topic of the content of the resource. */
    public static final String SUBJECT = "subject";
    /** The title of the document, or the name given to the resource. Typically, it will be a name by which the resource is formally known. */
    public static final String TITLE = "title";
    /** A document type; for example, novel, poem, or working paper. */
    public static final String TYPE = "type";

    /**
     * Adds a title.
     *
     * @param xmpMeta
     * @param title
     */
    static public void addTitle(XMPMeta xmpMeta, String title) throws XMPException {
        xmpMeta.appendArrayItem(XMPConst.NS_DC, TITLE, new PropertyOptions(PropertyOptions.ARRAY_ALTERNATE), title, null);
    }

    /**
     * Sets a title.
     *
     * @param xmpMeta
     * @param title
     * @param genericLang  The name of the generic language
     * @param specificLang The name of the specific language
     */
    static public void setTitle(XMPMeta xmpMeta, String title, String genericLang, String specificLang) throws XMPException {
        xmpMeta.setLocalizedText(XMPConst.NS_DC, TITLE, genericLang, specificLang, title);
    }

    /**
     * Adds a description.
     *
     * @param xmpMeta
     * @param desc
     */
    static public void addDescription(XMPMeta xmpMeta, String desc) throws XMPException {
        xmpMeta.appendArrayItem(XMPConst.NS_DC, DESCRIPTION, new PropertyOptions(PropertyOptions.ARRAY_ALTERNATE), desc, null);

    }

    /**
     * Sets a description.
     *
     * @param xmpMeta
     * @param desc
     * @param genericLang  The name of the generic language
     * @param specificLang The name of the specific language
     */
    static public void setDescription(XMPMeta xmpMeta, String desc, String genericLang, String specificLang) throws XMPException {
        xmpMeta.setLocalizedText(XMPConst.NS_DC, DESCRIPTION, genericLang, specificLang, desc);
    }

    /**
     * Adds a subject.
     *
     * @param xmpMeta
     * @param subject
     */
    static public void addSubject(XMPMeta xmpMeta, String subject) throws XMPException {
        xmpMeta.appendArrayItem(XMPConst.NS_DC, SUBJECT, new PropertyOptions(PropertyOptions.ARRAY), subject, null);
    }


    /**
     * Sets a subject.
     *
     * @param xmpMeta
     * @param subject array of subjects
     */
    static public void setSubject(XMPMeta xmpMeta, String[] subject) throws XMPException {
        XMPUtils.removeProperties(xmpMeta, XMPConst.NS_DC, SUBJECT, true, true);
        for (int i = 0; i < subject.length; i++) {
            xmpMeta.appendArrayItem(XMPConst.NS_DC, SUBJECT, new PropertyOptions(PropertyOptions.ARRAY), subject[i], null);
        }
    }

    /**
     * Adds a single author.
     *
     * @param xmpMeta
     * @param author
     */
    static public void addAuthor(XMPMeta xmpMeta, String author) throws XMPException {
        xmpMeta.appendArrayItem(XMPConst.NS_DC, CREATOR, new PropertyOptions(PropertyOptions.ARRAY_ORDERED), author, null);
    }

    /**
     * Sets an array of authors.
     *
     * @param xmpMeta
     * @param author
     */
    static public void setAuthor(XMPMeta xmpMeta, String[] author) throws XMPException {
        XMPUtils.removeProperties(xmpMeta, XMPConst.NS_DC, CREATOR, true, true);
        for (int i = 0; i < author.length; i++) {
            xmpMeta.appendArrayItem(XMPConst.NS_DC, CREATOR, new PropertyOptions(PropertyOptions.ARRAY_ORDERED), author[i], null);
        }
    }

    /**
     * Adds a single publisher.
     *
     * @param xmpMeta
     * @param publisher
     */
    static public void addPublisher(XMPMeta xmpMeta, String publisher) throws XMPException {
        xmpMeta.appendArrayItem(XMPConst.NS_DC, PUBLISHER, new PropertyOptions(PropertyOptions.ARRAY_ORDERED), publisher, null);
    }

    /**
     * Sets an array of publishers.
     *
     * @param xmpMeta
     * @param publisher
     */
    static public void setPublisher(XMPMeta xmpMeta, String[] publisher) throws XMPException {
        XMPUtils.removeProperties(xmpMeta, XMPConst.NS_DC, PUBLISHER, true, true);
        for (int i = 0; i < publisher.length; i++) {
            xmpMeta.appendArrayItem(XMPConst.NS_DC, PUBLISHER, new PropertyOptions(PropertyOptions.ARRAY_ORDERED), publisher[i], null);
        }
    }
}
