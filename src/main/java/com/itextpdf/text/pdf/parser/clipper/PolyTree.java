package com.itextpdf.text.pdf.parser.clipper;

import java.util.ArrayList;
import java.util.List;

public class PolyTree extends PolyNode {
    protected List<PolyNode> allPolys = new ArrayList<PolyNode>();

//The GC probably handles this cleanup more efficiently ...
//~PolyTree(){Clear();}

    public void Clear() {
        for (int i = 0; i < allPolys.size(); i++) {
            allPolys.set(i, null);
        }
        allPolys.clear();
        getChilds().clear();
    }

    public PolyNode getFirst() {
        if (getChilds().size() > 0)
            return getChilds().get(0);
        else
            return null;
    }

    public int getTotal() {

        int result = allPolys.size();
        //with negative offsets, ignore the hidden outer polygon ...
        if (result > 0 && getChilds().get(0) != allPolys.get(0))
            result--;
        return result;

    }

}
