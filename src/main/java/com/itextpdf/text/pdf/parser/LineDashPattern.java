package com.itextpdf.text.pdf.parser;

import com.itextpdf.text.pdf.PdfArray;

public class LineDashPattern {

    private PdfArray dashArray;
    private float dashPhase;

    private int currentIndex;
    private int elemOrdinalNumber = 1;
    private DashArrayElem currentElem;

    public LineDashPattern(PdfArray dashArray, float dashPhase) {
        this.dashArray = new PdfArray(dashArray);
        this.dashPhase = dashPhase;
        initFirst(dashPhase);
    }

    public PdfArray getDashArray() {
        return dashArray;
    }

    public void setDashArray(PdfArray dashArray) {
        this.dashArray = dashArray;
    }

    public float getDashPhase() {
        return dashPhase;
    }

    public void setDashPhase(float dashPhase) {
        this.dashPhase = dashPhase;
    }

    public DashArrayElem next() {
        DashArrayElem ret = currentElem;

        if (dashArray.size() > 0) {
            currentIndex = (currentIndex + 1) % dashArray.size();
            currentElem = new DashArrayElem(dashArray.getAsNumber(currentIndex).floatValue(),
                    isEven(++elemOrdinalNumber));
        }

        return ret;
    }

    public void reset() {
        currentIndex = 0;
        elemOrdinalNumber = 1;
        initFirst(dashPhase);
    }

    private void initFirst(float phase) {
        if (dashArray.size() > 0) {
            while (phase > 0) {
                phase -= dashArray.getAsNumber(currentIndex).floatValue();
                currentIndex = (currentIndex + 1) % dashArray.size();
                elemOrdinalNumber++;
            }

            if (phase < 0) {
                --elemOrdinalNumber;
                --currentIndex;
                currentElem = new DashArrayElem(-phase, isEven(elemOrdinalNumber));
            } else {
                currentElem = new DashArrayElem(dashArray.getAsNumber(currentIndex).floatValue(),
                        isEven(elemOrdinalNumber));
            }
        }
    }

    private boolean isEven(int num) {
        return (num % 2) == 0;
    }

    public class DashArrayElem {

        private float val;
        private boolean isGap;

        public DashArrayElem(float val, boolean isGap) {
            this.val = val;
            this.isGap = isGap;
        }

        public float getVal() {
            return val;
        }

        public void setVal(float val) {
            this.val = val;
        }

        public boolean isGap() {
            return isGap;
        }

        public void setGap(boolean isGap) {
            this.isGap = isGap;
        }
    }
}