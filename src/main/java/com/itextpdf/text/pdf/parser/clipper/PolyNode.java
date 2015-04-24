package com.itextpdf.text.pdf.parser.clipper;

import java.util.ArrayList;
import java.util.List;

public class PolyNode {
    private PolyNode parent;
    protected List<IntPoint> polygon = new ArrayList<IntPoint>();
    private int index;
    private JoinType jointype;
    private EndType endtype;
    private List<PolyNode> childs = new ArrayList<PolyNode>();
    private boolean isOpen;
    private boolean isHole;

    private boolean isHoleNode() {
        boolean result = true;
        PolyNode node = parent;
        while (node != null) {
            result = !result;
            node = node.parent;
        }
        return result;
    }

    public int getChildCount() {
        return childs.size();
    }

    public JoinType getJointype() {
        return jointype;
    }

    public void setJointype(JoinType jointype) {
        this.jointype = jointype;
    }

    public EndType getEndtype() {
        return endtype;
    }

    public void setEndtype(EndType endtype) {
        this.endtype = endtype;
    }

    public List<IntPoint> getContour() {
        return polygon;
    }

    protected void addChild(PolyNode child) {
        int cnt = childs.size();
        childs.add(child);
        child.setParent(this);
        child.setIndex(cnt);
    }

    public PolyNode GetNext() {
        if (childs.size() > 0)
            return childs.get(0);
        else
            return GetNextSiblingUp();
    }

    private PolyNode GetNextSiblingUp() {
        if (parent == null)
            return null;
        else if (index == parent.childs.size() - 1)
            return parent.GetNextSiblingUp();
        else
            return parent.childs.get(index + 1);
    }

    public List<PolyNode> getChilds() {
        return childs;
    }

    public PolyNode getParent() {
        return parent;
    }

    public void setParent(PolyNode polyNode) {
        this.parent = polyNode;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean IsHole() {
        return isHoleNode();
    }

    public boolean isHole() {
        return isHole;
    }

    public void setHole(boolean isHole) {
        this.isHole = isHole;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }
}
