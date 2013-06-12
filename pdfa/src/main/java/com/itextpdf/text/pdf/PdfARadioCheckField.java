package com.itextpdf.text.pdf;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;

import java.io.IOException;

public class PdfARadioCheckField extends RadioCheckField {

    private static final PdfName off = new PdfName("Off");

    public PdfARadioCheckField(PdfWriter writer, Rectangle box, String fieldName, String onValue) {
        super(writer, box, fieldName, onValue);
    }

    @Override
    protected PdfFormField getField(boolean isRadio) throws IOException, DocumentException {
        PdfFormField field = super.getField(isRadio);
        PdfDictionary ap = field.getAsDict(PdfName.AP);
        if (ap != null) {
            PdfDictionary n = ap.getAsDict(PdfName.N);
            if (n != null) {
                PdfObject stream = null;
                if (isChecked())
                    stream = n.get(new PdfName(getOnValue()));
                else
                    stream = n.get(off);
                if (stream != null) {
                    ap.put(PdfName.N, stream);
                }
            }
        }
        return field;
    }

    @Override
    public void setCheckType(int checkType) {
        if (checkType < TYPE_CHECK || checkType > TYPE_STAR)
            checkType = TYPE_CIRCLE;
        this.checkType = checkType;
        setText(typeChars[checkType - 1]);
        try {
            setFont(getRealFont());
        }
        catch (Exception e) {
            throw new ExceptionConverter(e);
        }
    }



}
