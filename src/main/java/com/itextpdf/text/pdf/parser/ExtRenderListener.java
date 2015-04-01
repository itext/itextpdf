package com.itextpdf.text.pdf.parser;

/**
 * Simply extends the {@link com.itextpdf.text.pdf.parser.RenderListener} interface to provide
 * additional methods.
 *
 * {@inheritDoc}
 *
 * @since 5.5.6
 */
public interface ExtRenderListener extends RenderListener {

    /**
     * Called when the current path is being modifying. E.g. new segment is being added,
     * new subpath is being started etc.
     *
     * @param renderInfo Contains information about the path segment being added to the current path.
     */
    void modifyPath(PathConstructionRenderInfo renderInfo);

    /**
     * Called when the current path should be rendered.
     *
     * @param renderInfo Contains information about the current path which should be rendered.
     * @return The path which can be used as a new clipping path.
     */
    Path renderPath(PathPaintingRenderInfo renderInfo);

    /**
     * Called when the current path should be set as a new clipping path.
     *
     * @param rule Either {@link PathPaintingRenderInfo#EVEN_ODD_RULE} or {@link PathPaintingRenderInfo#NONZERO_WINDING_RULE}
     */
    void clipPath(int rule);
}
