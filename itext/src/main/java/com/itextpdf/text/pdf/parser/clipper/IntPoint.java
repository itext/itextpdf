package com.itextpdf.text.pdf.parser.clipper;

public class IntPoint {
    public int X;
    public int Y;

    IntPoint() {
    }

    public IntPoint(int X, int Y) {
        this.X = X;
        this.Y = Y;
    }

    public IntPoint(double x, double y) {
        this.X = (int) x;
        this.Y = (int) y;
    }

    public IntPoint(IntPoint pt) {
        this.X = pt.X;
        this.Y = pt.Y;
    }


    public static boolean equals(IntPoint a, IntPoint b) {
        return a.X == b.X && a.Y == b.Y;
    }

    public static boolean notEquals(IntPoint a, IntPoint b) {
        return a.X != b.X || a.Y != b.Y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntPoint intPoint = (IntPoint) o;

        if (X != intPoint.X) return false;
        if (Y != intPoint.Y) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = X;
        result = 31 * result + Y;
        return result;
    }
}
