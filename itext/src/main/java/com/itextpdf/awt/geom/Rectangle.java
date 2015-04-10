/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  This code was originally part of the Apache Harmony project.
 *  The Apache Harmony project has been discontinued.
 *  That's why we imported the code into iText.
 */
/**
 * @author Denis M. Kishenko
 */
package com.itextpdf.awt.geom;

import java.io.Serializable;

public class Rectangle extends Rectangle2D implements Shape, Serializable {

    private static final long serialVersionUID = -4345857070255674764L;

    public double x;
    public double y;
    public double width;
    public double height;

    public Rectangle() {
        setBounds(0, 0, 0, 0);
    }

    public Rectangle(Point p) {
        setBounds(p.x, p.y, 0, 0);
    }

    public Rectangle(Point p, Dimension d) {
        setBounds(p.x, p.y, d.width, d.height);
    }

    public Rectangle(double x, double y, double width, double height) {
        setBounds(x, y, width, height);
    }

    public Rectangle(int width, int height) {
        setBounds(0, 0, width, height);
    }

    public Rectangle(Rectangle r) {
        setBounds(r.x, r.y, r.width, r.height);
    }
    
    public Rectangle(com.itextpdf.text.Rectangle r) {
    	r.normalize();
    	setBounds(r.getLeft(), r.getBottom(), r.getWidth(), r.getHeight());
    }

    public Rectangle(Dimension d) {
        setBounds(0, 0, d.width, d.height);
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public boolean isEmpty() {
        return width <= 0 || height <= 0;
    }

    public Dimension getSize() {
        return new Dimension(width, height);
    }

    public void setSize(int mx, int my) {
    	setSize((double)mx, (double)my);
    }
    public void setSize(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public void setSize(Dimension d) {
        setSize(d.width, d.height);
    }

    public Point getLocation() {
        return new Point(x, y);
    }

    public void setLocation(int mx, int my) {
    	setLocation((double)mx, (double)my);
    }
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setLocation(Point p) {
        setLocation(p.x, p.y);
    }

    @Override
    public void setRect(double x, double y, double width, double height) {
        int x1 = (int)Math.floor(x);
        int y1 = (int)Math.floor(y);
        int x2 = (int)Math.ceil(x + width);
        int y2 = (int)Math.ceil(y + height);
        setBounds(x1, y1, x2 - x1, y2 - y1);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public Rectangle2D getBounds2D() {
        return getBounds();
    }

    public void setBounds(int x, int y, int width, int height) {
        setBounds((double)x, (double)y, (double)width, (double)height);
    }
    public void setBounds(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
    }

    public void setBounds(Rectangle r) {
        setBounds(r.x, r.y, r.width, r.height);
    }

    public void grow(int mx, int my) {
    	translate((double)mx, (double)my);
    }
    public void grow(double dx, double dy) {
        x -= dx;
        y -= dy;
        width += dx + dx;
        height += dy + dy;
    }

    public void translate(int mx, int my) {
    	translate((double)mx, (double)my);
    }
    public void translate(double mx, double my) {
        x += mx;
        y += my;
    }

    public void add(int px, int py) {
    	add((double)px, (double)py);
    }
    public void add(double px, double py) {
        double x1 = Math.min(x, px);
        double x2 = Math.max(x + width, px);
        double y1 = Math.min(y, py);
        double y2 = Math.max(y + height, py);
        setBounds(x1, y1, x2 - x1, y2 - y1);
    }

    public void add(Point p) {
        add(p.x, p.y);
    }

    public void add(Rectangle r) {
        double x1 = Math.min(x, r.x);
        double x2 = Math.max(x + width, r.x + r.width);
        double y1 = Math.min(y, r.y);
        double y2 = Math.max(y + height, r.y + r.height);
        setBounds(x1, y1, x2 - x1, y2 - y1);
    }

    public boolean contains(int px, int py) {
        return contains((double)px, (double)py);
    }
    public boolean contains(double px, double py) {
        if (isEmpty()) {
            return false;
        }
        if (px < x || py < y) {
            return false;
        }
        px -= x;
        py -= y;
        return px < width && py < height;
    }

    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }

    public boolean contains(int rx, int ry, int rw, int rh) {
        return contains(rx, ry) && contains(rx + rw - 1, ry + rh - 1);
    }

    public boolean contains(double rx, double ry, double rw, double rh) {
        return contains(rx, ry) && contains(rx + rw - 0.01, ry + rh - 0.01);
    }

    public boolean contains(Rectangle r) {
        return contains(r.x, r.y, r.width, r.height);
    }

    @Override
    public Rectangle2D createIntersection(Rectangle2D r) {
        if (r instanceof Rectangle) {
            return intersection((Rectangle) r);
        }
        Rectangle2D dst = new Rectangle2D.Double();
        Rectangle2D.intersect(this, r, dst);
        return dst;
    }

    public Rectangle intersection(Rectangle r) {
        double x1 = Math.max(x, r.x);
        double y1 = Math.max(y, r.y);
        double x2 = Math.min(x + width, r.x + r.width);
        double y2 = Math.min(y + height, r.y + r.height);
        return new Rectangle(x1, y1, x2 - x1, y2 - y1);
    }

    public boolean intersects(Rectangle r) {
        return !intersection(r).isEmpty();
    }

    @Override
    public int outcode(double px, double py) {
        int code = 0;

        if (width <= 0) {
            code |= OUT_LEFT | OUT_RIGHT;
        } else
            if (px < x) {
                code |= OUT_LEFT;
            } else
                if (px > x + width) {
                    code |= OUT_RIGHT;
                }

        if (height <= 0) {
            code |= OUT_TOP | OUT_BOTTOM;
        } else
            if (py < y) {
                code |= OUT_TOP;
            } else
                if (py > y + height) {
                    code |= OUT_BOTTOM;
                }

        return code;
    }

    @Override
    public Rectangle2D createUnion(Rectangle2D r) {
        if (r instanceof Rectangle) {
            return union((Rectangle)r);
        }
        Rectangle2D dst = new Rectangle2D.Double();
        Rectangle2D.union(this, r, dst);
        return dst;
    }

    public Rectangle union(Rectangle r) {
        Rectangle dst = new Rectangle(this);
        dst.add(r);
        return dst;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Rectangle) {
            Rectangle r = (Rectangle)obj;
            return r.x == x && r.y == y && r.width == width && r.height == height;
        }
        return false;
    }

    @Override
    public String toString() {
        // The output format based on 1.5 release behaviour. It could be obtained in the following way
        // System.out.println(new Rectangle().toString())
        return getClass().getName() + "[x=" + x + ",y=" + y + //$NON-NLS-1$ //$NON-NLS-2$
            ",width=" + width + ",height=" + height + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

}

