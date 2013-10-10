package com.itextpdf.text.pdf.parser;

import java.util.ArrayList;
import java.util.List;

public class MultiFilteredRenderListener implements RenderListener {

    private final List<RenderListener> delegates;
    private final List<RenderFilter[]> filters;

    public MultiFilteredRenderListener() {
        delegates = new ArrayList<RenderListener>();
        filters = new ArrayList<RenderFilter[]>();
    }

    /**
     * Attaches a {@link RenderListener} for the corresponding filter set.
     * @param delegate RenderListener instance to be attached.
     * @param filterSet filter set to be attached. The delegate will be invoked if all the filters pass.
     */
    public <E extends RenderListener> E attachRenderListener(E delegate, RenderFilter... filterSet) {
        delegates.add(delegate);
        filters.add(filterSet);

        return delegate;
    }

    public void beginTextBlock() {
        for (RenderListener delegate : delegates) {
            delegate.beginTextBlock();
        }
    }

    public void renderText(TextRenderInfo renderInfo) {
        for (int i = 0; i < delegates.size(); i++) {
            boolean filtersPassed = true;
            for (RenderFilter filter : filters.get(i)) {
                if (!filter.allowText(renderInfo)) {
                    filtersPassed = false;
                    break;
                }
            }
            if (filtersPassed)
                delegates.get(i).renderText(renderInfo);
        }
    }

    public void endTextBlock() {
        for (RenderListener delegate : delegates) {
            delegate.endTextBlock();
        }
    }

    public void renderImage(ImageRenderInfo renderInfo) {
        for (int i = 0; i < delegates.size(); i++) {
            boolean filtersPassed = true;
            for (RenderFilter filter : filters.get(i)) {
                if (!filter.allowImage(renderInfo)) {
                    filtersPassed = false;
                    break;
                }
            }
            if (filtersPassed)
                delegates.get(i).renderImage(renderInfo);
        }
    }
}
