package com.itextpdf.text.pdf;

import com.itextpdf.text.pdf.security.XpathConstructor;

/**
 * Constructor for xpath expression for signing XfaForm
 */
public class XfaXpathConstructor implements XpathConstructor {

    /**
     * Possible xdp packages to sign
     */
    public enum XdpPackage {
        Config,
        ConnectionSet,
        Datasets,
        LocaleSet,
        Pdf,
        SourceSet,
        Stylesheet,
        Template,
        Xdc,
        Xfdf,
        Xmpmeta
    }

    private final String CONFIG = "config";
    private final String CONNECTIONSET = "connectionSet";
    private final String DATASETS = "datasets";
    private final String LOCALESET = "localeSet";
    private final String PDF = "pdf";
    private final String SOURCESET = "sourceSet";
    private final String STYLESHEET = "stylesheet";
    private final String TEMPLATE = "template";
    private final String XDC = "xdc";
    private final String XFDF = "xfdf";
    private final String XMPMETA = "xmpmeta";

    /**
     * Empty constructor, no transform.
     */
    public XfaXpathConstructor() {
        this.xpathExpression = "";
    }

    /**
     * Construct for Xpath2 expression. Depends from selected xdp package.
     * @param xdpPackage
     */
    public XfaXpathConstructor(XdpPackage xdpPackage) {
        String strPackage;
        switch (xdpPackage) {
            case Config:
                strPackage = CONFIG;
                break;
            case ConnectionSet:
                strPackage = CONNECTIONSET;
                break;
            case Datasets:
                strPackage = DATASETS;
                break;
            case LocaleSet:
                strPackage = LOCALESET;
                break;
            case Pdf:
                strPackage = PDF;
                break;
            case SourceSet:
                strPackage = SOURCESET;
                break;
            case Stylesheet:
                strPackage = STYLESHEET;
                break;
            case Template:
                strPackage = TEMPLATE;
                break;
            case Xdc:
                strPackage = XDC;
                break;
            case Xfdf:
                strPackage = XFDF;
                break;
            case Xmpmeta:
                strPackage = XMPMETA;
                break;
            default:
                xpathExpression = "";
                return;
        }

        StringBuilder builder = new StringBuilder("/xdp:xdp/*[local-name()='");
        builder.append(strPackage);
        builder.append("']");
        xpathExpression = builder.toString();
    }

    private String xpathExpression;

    /**
     * Get XPath2 expression
     */
    public String getXpathExpression() {
        return xpathExpression;
    }
}
