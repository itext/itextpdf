package com.lowagie.text.pdf;

import com.lowagie.text.Rectangle;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.Phrase;
import com.lowagie.text.Font;
import com.lowagie.text.Element;

public class PdfSignatureAppearance {
    
    private Rectangle rect;
    
    public PdfSignatureAppearance(Rectangle rect) {
        this.rect = rect;
    }
    
    public static float fitText(BaseFont font, String text, Rectangle rect, float maxFontSize) {
        try {
            System.out.println(maxFontSize);
            ColumnText ct = null;
            int status = 0;
            if (maxFontSize <= 0) {
                int cr = 0;
                int lf = 0;
                char t[] = text.toCharArray();
                for (int k = 0; k < t.length; ++k) {
                    if (t[k] == '\n')
                        ++lf;
                    else if (t[k] == '\r')
                        ++cr;
                }
                int minLines = Math.max(cr, lf) + 1;
                maxFontSize = Math.abs(rect.height()) / minLines - 0.001f;
            }
            Phrase ph = new Phrase(text, new Font(font, maxFontSize));
            ct = new ColumnText(null);
            ct.setSimpleColumn(ph, rect.left(), rect.bottom(), rect.right(), rect.top(), maxFontSize, Element.ALIGN_LEFT);
            status = ct.go(true);
            System.out.println(maxFontSize);
            if ((status & ColumnText.NO_MORE_TEXT) != 0)
                return maxFontSize;
            float precision = 0.1f;
            float min = 0;
            float max = maxFontSize;
            while (true) {
                float size = (min + max) / 2;
                System.out.println(size);
                ct = new ColumnText(null);
                ct.setSimpleColumn(new Phrase(text, new Font(font, size)), rect.left(), rect.bottom(), rect.right(), rect.top(), size, Element.ALIGN_LEFT);
                status = ct.go(true);
                if ((status & ColumnText.NO_MORE_TEXT) != 0) {
                    if (max - min < size * precision)
                        return size;
                    min = size;
                }
                else
                    max = size;
            }
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }
    
    public static String questionMark = 
        "% DSUnknown\n" +
        "q\n" +
        "1 G\n" +
        "1 g\n" +
        "0.1 0 0 0.1 9 0 cm\n" +
        "0 J 0 j 4 M []0 d\n" +
        "1 i \n" +
        "0 g\n" +
        "313 292 m\n" +
        "313 404 325 453 432 529 c\n" +
        "478 561 504 597 504 645 c\n" +
        "504 736 440 760 391 760 c\n" +
        "286 760 271 681 265 626 c\n" +
        "265 625 l\n" +
        "100 625 l\n" +
        "100 828 253 898 381 898 c\n" +
        "451 898 679 878 679 650 c\n" +
        "679 555 628 499 538 435 c\n" +
        "488 399 467 376 467 292 c\n" +
        "313 292 l\n" +
        "h\n" +
        "308 214 170 -164 re\n" +
        "f\n" +
        "0.44 G\n" +
        "1.2 w\n" +
        "1 1 0.4 rg\n" +
        "287 318 m\n" +
        "287 430 299 479 406 555 c\n" +
        "451 587 478 623 478 671 c\n" +
        "478 762 414 786 365 786 c\n" +
        "260 786 245 707 239 652 c\n" +
        "239 651 l\n" +
        "74 651 l\n" +
        "74 854 227 924 355 924 c\n" +
        "425 924 653 904 653 676 c\n" +
        "653 581 602 525 512 461 c\n" +
        "462 425 441 402 441 318 c\n" +
        "287 318 l\n" +
        "h\n" +
        "282 240 170 -164 re\n" +
        "B\n" +
        "Q\n";

}
