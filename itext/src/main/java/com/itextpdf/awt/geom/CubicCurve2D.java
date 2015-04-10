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

import java.util.NoSuchElementException;

import com.itextpdf.awt.geom.gl.Crossing;
import com.itextpdf.awt.geom.misc.Messages;

public abstract class CubicCurve2D implements Shape, Cloneable {

    public static class Float extends CubicCurve2D {

        public float x1;
        public float y1;
        public float ctrlx1;
        public float ctrly1;
        public float ctrlx2;
        public float ctrly2;
        public float x2;
        public float y2;

        public Float() {
        }

        public Float(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
            setCurve(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
        }

        @Override
        public double getX1() {
            return x1;
        }

        @Override
        public double getY1() {
            return y1;
        }

        @Override
        public double getCtrlX1() {
            return ctrlx1;
        }

        @Override
        public double getCtrlY1() {
            return ctrly1;
        }

        @Override
        public double getCtrlX2() {
            return ctrlx2;
        }

        @Override
        public double getCtrlY2() {
            return ctrly2;
        }

        @Override
        public double getX2() {
            return x2;
        }

        @Override
        public double getY2() {
            return y2;
        }

        @Override
        public Point2D getP1() {
            return new Point2D.Float(x1, y1);
        }

        @Override
        public Point2D getCtrlP1() {
            return new Point2D.Float(ctrlx1, ctrly1);
        }

        @Override
        public Point2D getCtrlP2() {
            return new Point2D.Float(ctrlx2, ctrly2);
        }

        @Override
        public Point2D getP2() {
            return new Point2D.Float(x2, y2);
        }

        @Override
        public void setCurve(double x1, double y1, double ctrlx1, double ctrly1,
                double ctrlx2, double ctrly2, double x2, double y2)
        {
            this.x1 = (float)x1;
            this.y1 = (float)y1;
            this.ctrlx1 = (float)ctrlx1;
            this.ctrly1 = (float)ctrly1;
            this.ctrlx2 = (float)ctrlx2;
            this.ctrly2 = (float)ctrly2;
            this.x2 = (float)x2;
            this.y2 = (float)y2;
        }

        public void setCurve(float x1, float y1, float ctrlx1, float ctrly1,
                float ctrlx2, float ctrly2, float x2, float y2)
        {
            this.x1 = x1;
            this.y1 = y1;
            this.ctrlx1 = ctrlx1;
            this.ctrly1 = ctrly1;
            this.ctrlx2 = ctrlx2;
            this.ctrly2 = ctrly2;
            this.x2 = x2;
            this.y2 = y2;
        }

        public Rectangle2D getBounds2D() {
            float rx1 = Math.min(Math.min(x1, x2), Math.min(ctrlx1, ctrlx2));
            float ry1 = Math.min(Math.min(y1, y2), Math.min(ctrly1, ctrly2));
            float rx2 = Math.max(Math.max(x1, x2), Math.max(ctrlx1, ctrlx2));
            float ry2 = Math.max(Math.max(y1, y2), Math.max(ctrly1, ctrly2));
            return new Rectangle2D.Float(rx1, ry1, rx2 - rx1, ry2 - ry1);
        }
    }

    public static class Double extends CubicCurve2D {

        public double x1;
        public double y1;
        public double ctrlx1;
        public double ctrly1;
        public double ctrlx2;
        public double ctrly2;
        public double x2;
        public double y2;

        public Double() {
        }

        public Double(double x1, double y1, double ctrlx1, double ctrly1,
                double ctrlx2, double ctrly2, double x2, double y2) {
            setCurve(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
        }

        @Override
        public double getX1() {
            return x1;
        }

        @Override
        public double getY1() {
            return y1;
        }

        @Override
        public double getCtrlX1() {
            return ctrlx1;
        }

        @Override
        public double getCtrlY1() {
            return ctrly1;
        }

        @Override
        public double getCtrlX2() {
            return ctrlx2;
        }

        @Override
        public double getCtrlY2() {
            return ctrly2;
        }

        @Override
        public double getX2() {
            return x2;
        }

        @Override
        public double getY2() {
            return y2;
        }

        @Override
        public Point2D getP1() {
            return new Point2D.Double(x1, y1);
        }

        @Override
        public Point2D getCtrlP1() {
            return new Point2D.Double(ctrlx1, ctrly1);
        }

        @Override
        public Point2D getCtrlP2() {
            return new Point2D.Double(ctrlx2, ctrly2);
        }

        @Override
        public Point2D getP2() {
            return new Point2D.Double(x2, y2);
        }

        @Override
        public void setCurve(double x1, double y1, double ctrlx1, double ctrly1,
                double ctrlx2, double ctrly2, double x2, double y2)
        {
            this.x1 = x1;
            this.y1 = y1;
            this.ctrlx1 = ctrlx1;
            this.ctrly1 = ctrly1;
            this.ctrlx2 = ctrlx2;
            this.ctrly2 = ctrly2;
            this.x2 = x2;
            this.y2 = y2;
        }

        public Rectangle2D getBounds2D() {
            double rx1 = Math.min(Math.min(x1, x2), Math.min(ctrlx1, ctrlx2));
            double ry1 = Math.min(Math.min(y1, y2), Math.min(ctrly1, ctrly2));
            double rx2 = Math.max(Math.max(x1, x2), Math.max(ctrlx1, ctrlx2));
            double ry2 = Math.max(Math.max(y1, y2), Math.max(ctrly1, ctrly2));
            return new Rectangle2D.Double(rx1, ry1, rx2 - rx1, ry2 - ry1);
        }
    }

    /*
     * CubicCurve2D path iterator 
     */
    class Iterator implements PathIterator {

        /**
         * The source CubicCurve2D object
         */
        CubicCurve2D c;
        
        /**
         * The path iterator transformation
         */
        AffineTransform t;
        
        /**
         * The current segmenet index
         */
        int index;

        /**
         * Constructs a new CubicCurve2D.Iterator for given line and transformation
         * @param c - the source CubicCurve2D object
         * @param at - the AffineTransform object to apply rectangle path
         */
        Iterator(CubicCurve2D c, AffineTransform t) {
            this.c = c;
            this.t = t;
        }

        public int getWindingRule() {
            return WIND_NON_ZERO;
        }

        public boolean isDone() {
            return index > 1;
        }

        public void next() {
            index++;
        }

        public int currentSegment(double[] coords) {
            if (isDone()) {
                throw new NoSuchElementException(Messages.getString("awt.4B")); //$NON-NLS-1$
            }
            int type;
            int count;
            if (index == 0) {
                type = SEG_MOVETO;
                coords[0] = c.getX1();
                coords[1] = c.getY1();
                count = 1;
            } else {
                type = SEG_CUBICTO;
                coords[0] = c.getCtrlX1();
                coords[1] = c.getCtrlY1();
                coords[2] = c.getCtrlX2();
                coords[3] = c.getCtrlY2();
                coords[4] = c.getX2();
                coords[5] = c.getY2();
                count = 3;
            }
            if (t != null) {
                t.transform(coords, 0, coords, 0, count);
            }
            return type;
        }

        public int currentSegment(float[] coords) {
            if (isDone()) {
                throw new NoSuchElementException(Messages.getString("awt.4B")); //$NON-NLS-1$
            }
            int type;
            int count;
            if (index == 0) {
                type = SEG_MOVETO;
                coords[0] = (float)c.getX1();
                coords[1] = (float)c.getY1();
                count = 1;
            } else {
                type = SEG_CUBICTO;
                coords[0] = (float)c.getCtrlX1();
                coords[1] = (float)c.getCtrlY1();
                coords[2] = (float)c.getCtrlX2();
                coords[3] = (float)c.getCtrlY2();
                coords[4] = (float)c.getX2();
                coords[5] = (float)c.getY2();
                count = 3;
            }
            if (t != null) {
                t.transform(coords, 0, coords, 0, count);
            }
            return type;
        }

    }

    protected CubicCurve2D() {
    }

    public abstract double getX1();

    public abstract double getY1();

    public abstract Point2D getP1();

    public abstract double getCtrlX1();

    public abstract double getCtrlY1();

    public abstract Point2D getCtrlP1();

    public abstract double getCtrlX2();

    public abstract double getCtrlY2();

    public abstract Point2D getCtrlP2();

    public abstract double getX2();

    public abstract double getY2();

    public abstract Point2D getP2();

    public abstract void setCurve(double x1, double y1, double ctrlx1, double ctrly1,
            double ctrlx2, double ctrly2, double x2, double y2);

    public void setCurve(Point2D p1, Point2D cp1, Point2D cp2, Point2D p2) {
        setCurve(
                p1.getX(), p1.getY(),
                cp1.getX(), cp1.getY(),
                cp2.getX(), cp2.getY(),
                p2.getX(), p2.getY());
    }

    public void setCurve(double[] coords, int offset) {
        setCurve(
                coords[offset + 0], coords[offset + 1],
                coords[offset + 2], coords[offset + 3],
                coords[offset + 4], coords[offset + 5],
                coords[offset + 6], coords[offset + 7]);
    }

    public void setCurve(Point2D[] points, int offset) {
        setCurve(
                points[offset + 0].getX(), points[offset + 0].getY(),
                points[offset + 1].getX(), points[offset + 1].getY(),
                points[offset + 2].getX(), points[offset + 2].getY(),
                points[offset + 3].getX(), points[offset + 3].getY());
    }

    public void setCurve(CubicCurve2D curve) {
        setCurve(
                curve.getX1(), curve.getY1(),
                curve.getCtrlX1(), curve.getCtrlY1(),
                curve.getCtrlX2(), curve.getCtrlY2(),
                curve.getX2(), curve.getY2());
    }

    public double getFlatnessSq() {
        return getFlatnessSq(
                getX1(), getY1(),
                getCtrlX1(), getCtrlY1(),
                getCtrlX2(), getCtrlY2(),
                getX2(), getY2());
    }

    public static double getFlatnessSq(double x1, double y1, double ctrlx1, double ctrly1,
            double ctrlx2, double ctrly2, double x2, double y2)
    {
        return Math.max(
                Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx1, ctrly1),
                Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx2, ctrly2));
    }

    public static double getFlatnessSq(double coords[], int offset) {
        return getFlatnessSq(
                coords[offset + 0], coords[offset + 1],
                coords[offset + 2], coords[offset + 3],
                coords[offset + 4], coords[offset + 5],
                coords[offset + 6], coords[offset + 7]);
    }

    public double getFlatness() {
        return getFlatness(
                getX1(), getY1(),
                getCtrlX1(), getCtrlY1(),
                getCtrlX2(), getCtrlY2(),
                getX2(), getY2());
    }

    public static double getFlatness(double x1, double y1, double ctrlx1, double ctrly1,
            double ctrlx2, double ctrly2, double x2, double y2)
    {
        return Math.sqrt(getFlatnessSq(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2));
    }

    public static double getFlatness(double coords[], int offset) {
        return getFlatness(
                coords[offset + 0], coords[offset + 1],
                coords[offset + 2], coords[offset + 3],
                coords[offset + 4], coords[offset + 5],
                coords[offset + 6], coords[offset + 7]);
    }

    public void subdivide(CubicCurve2D left, CubicCurve2D right) {
        subdivide(this, left, right);
    }

    public static void subdivide(CubicCurve2D src, CubicCurve2D left, CubicCurve2D right) {
        double x1 = src.getX1();
        double y1 = src.getY1();
        double cx1 = src.getCtrlX1();
        double cy1 = src.getCtrlY1();
        double cx2 = src.getCtrlX2();
        double cy2 = src.getCtrlY2();
        double x2 = src.getX2();
        double y2 = src.getY2();
        double cx = (cx1 + cx2) / 2.0;
        double cy = (cy1 + cy2) / 2.0;
        cx1 = (x1 + cx1) / 2.0;
        cy1 = (y1 + cy1) / 2.0;
        cx2 = (x2 + cx2) / 2.0;
        cy2 = (y2 + cy2) / 2.0;
        double ax = (cx1 + cx) / 2.0;
        double ay = (cy1 + cy) / 2.0;
        double bx = (cx2 + cx) / 2.0;
        double by = (cy2 + cy) / 2.0;
        cx = (ax + bx) / 2.0;
        cy = (ay + by) / 2.0;
        if (left != null) {
            left.setCurve(x1, y1, cx1, cy1, ax, ay, cx, cy);
        }
        if (right != null) {
            right.setCurve(cx, cy, bx, by, cx2, cy2, x2, y2);
        }
    }

    public static void subdivide(double src[], int srcOff, double left[], int leftOff, double right[], int rightOff) {
        double x1 = src[srcOff + 0];
        double y1 = src[srcOff + 1];
        double cx1 = src[srcOff + 2];
        double cy1 = src[srcOff + 3];
        double cx2 = src[srcOff + 4];
        double cy2 = src[srcOff + 5];
        double x2 = src[srcOff + 6];
        double y2 = src[srcOff + 7];
        double cx = (cx1 + cx2) / 2.0;
        double cy = (cy1 + cy2) / 2.0;
        cx1 = (x1 + cx1) / 2.0;
        cy1 = (y1 + cy1) / 2.0;
        cx2 = (x2 + cx2) / 2.0;
        cy2 = (y2 + cy2) / 2.0;
        double ax = (cx1 + cx) / 2.0;
        double ay = (cy1 + cy) / 2.0;
        double bx = (cx2 + cx) / 2.0;
        double by = (cy2 + cy) / 2.0;
        cx = (ax + bx) / 2.0;
        cy = (ay + by) / 2.0;
        if (left != null) {
            left[leftOff + 0] = x1;
            left[leftOff + 1] = y1;
            left[leftOff + 2] = cx1;
            left[leftOff + 3] = cy1;
            left[leftOff + 4] = ax;
            left[leftOff + 5] = ay;
            left[leftOff + 6] = cx;
            left[leftOff + 7] = cy;
        }
        if (right != null) {
            right[rightOff + 0] = cx;
            right[rightOff + 1] = cy;
            right[rightOff + 2] = bx;
            right[rightOff + 3] = by;
            right[rightOff + 4] = cx2;
            right[rightOff + 5] = cy2;
            right[rightOff + 6] = x2;
            right[rightOff + 7] = y2;
        }
    }

    public static int solveCubic(double eqn[]) {
        return solveCubic(eqn, eqn);
    }

    public static int solveCubic(double eqn[], double res[]) {
        return Crossing.solveCubic(eqn, res);
    }

    public boolean contains(double px, double py) {
        return Crossing.isInsideEvenOdd(Crossing.crossShape(this, px, py));
    }

    public boolean contains(double rx, double ry, double rw, double rh) {
        int cross = Crossing.intersectShape(this, rx, ry, rw, rh);
        return cross != Crossing.CROSSING && Crossing.isInsideEvenOdd(cross);
    }

    public boolean intersects(double rx, double ry, double rw, double rh) {
        int cross = Crossing.intersectShape(this, rx, ry, rw, rh);
        return cross == Crossing.CROSSING || Crossing.isInsideEvenOdd(cross);
    }

    public boolean contains(Point2D p) {
        return contains(p.getX(), p.getY());
    }

    public boolean intersects(Rectangle2D r) {
        return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public boolean contains(Rectangle2D r) {
        return contains(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    public Rectangle getBounds() {
        return getBounds2D().getBounds();
    }

    public PathIterator getPathIterator(AffineTransform t) {
        return new Iterator(this, t);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return new FlatteningPathIterator(getPathIterator(at), flatness);
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }
}