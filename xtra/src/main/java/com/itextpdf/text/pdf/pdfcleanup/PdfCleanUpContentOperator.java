package com.itextpdf.text.pdf.pdfcleanup;

import com.itextpdf.text.DocWriter;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.parser.ContentOperator;
import com.itextpdf.text.pdf.parser.PdfContentStreamProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class PdfCleanUpContentOperator implements ContentOperator {

    static private final byte[] TStar = DocWriter.getISOBytes("T*\n");
    static private final byte[] Tw = DocWriter.getISOBytes(" Tw ");
    static private final byte[] TcTStar = DocWriter.getISOBytes(" Tc T*\n");
    static private final byte[] TJ = DocWriter.getISOBytes("] TJ\n");
    static private final byte[] Tc = DocWriter.getISOBytes(" Tc\n");

    static private final Set<String> textShowingOperators = new HashSet<String>() {{
        add("TJ");
        add("Tj");
        add("'");
        add("\"");
    }};

    protected PdfCleanUpRenderListener cleanUpStrategy;
    protected ContentOperator originalContentOperator;

    public static void populateOperators(PdfContentStreamProcessor contentProcessor,
                                         PdfCleanUpRenderListener pdfCleanUpRenderListener) {
        String[] operators = new String[]{
                PdfContentStreamProcessor.DEFAULTOPERATOR, "q", "Q", "g", "G", "rg", "RG", "k", "K",
                "cs", "CS", "sc", "SC", "scn", "SCN", "cm", "gs", "Tc", "Tw", "Tz", "TL", "Tf", "Tr",
                "Ts", "BT", "ET", "BMC", "BDC", "EMC", "Td", "TD", "Tm", "T*", "Tj", "'", "\"", "TJ", "Do"
        };
        for (String operator : operators) {
            PdfCleanUpContentOperator contentOperator = new PdfCleanUpContentOperator(pdfCleanUpRenderListener);
            contentOperator.originalContentOperator = contentProcessor.registerContentOperator(operator, contentOperator);
        }
    }

    public PdfCleanUpContentOperator(PdfCleanUpRenderListener cleanUpStrategy) {
        this.cleanUpStrategy = cleanUpStrategy;
    }

    public void invoke(PdfContentStreamProcessor pdfContentStreamProcessor, PdfLiteral operator, ArrayList<PdfObject> operands) throws Exception {
        String operatorStr = operator.toString();
        PdfContentByte canvas = cleanUpStrategy.getContext().getCanvas();
        PRStream xFormStream = null;

        if ("Do".equals(operatorStr)) {
            if (operands.size() == 2 && operands.get(0).isName()) {
                PdfDictionary xObjResources = cleanUpStrategy.getContext().getResources().getAsDict(PdfName.XOBJECT);
                if (xObjResources != null) {
                    PdfStream xObj = xObjResources.getAsStream((PdfName) operands.get(0));
                    if (xObj instanceof PRStream && xObj.getAsName(PdfName.SUBTYPE) != null &&
                            xObj.getAsName(PdfName.SUBTYPE).compareTo(PdfName.FORM) == 0) {
                        xFormStream = (PRStream) xObj;
                        cleanUpStrategy.registerNewContext(xObj.getAsDict(PdfName.RESOURCES), null);
                    }
                }
            }
        } else if ("ET".equals(operatorStr)) {
            cleanUpStrategy.getContext().resetTextMatrixElement22();
        } else if ("q".equals(operatorStr)) {
            cleanUpStrategy.getContext().saveGraphicsState();
        } else if ("Q".equals(operatorStr)) {
            cleanUpStrategy.getContext().restoreGraphicsState();
        } else if ("Tf".equals(operatorStr)) {
            cleanUpStrategy.getContext().setFontSize(((PdfNumber) operands.get(1)).floatValue());
        } else if ("Tm".equals(operatorStr)) {
            cleanUpStrategy.getContext().setTextMatrixElement22(((PdfNumber) operands.get(3)).floatValue());
        }

        originalContentOperator.invoke(pdfContentStreamProcessor, operator, operands);
        List<PdfCleanUpContentChunk> chunks = cleanUpStrategy.getChunks();
        boolean disableOutput = false;
        if (xFormStream != null) {
            xFormStream.setData(cleanUpStrategy.getContext().getCanvas().toPdf(cleanUpStrategy.getContext().getCanvas().getPdfWriter()));
            cleanUpStrategy.popContext();
            canvas = cleanUpStrategy.getContext().getCanvas();
        }
        if ("Do".equals(operatorStr)) {
            if (chunks.size() > 0 && chunks.get(0).isImage()) {
                PdfCleanUpContentChunk chunk = chunks.get(0);

                if (chunk.isVisible()) {
                    PdfDictionary xObjResources = cleanUpStrategy.getContext().getResources().getAsDict(PdfName.XOBJECT);
                    xObjResources.remove((PdfName) operands.get(0));

                    Image image = Image.getInstance(chunk.getNewImageData());
                    cleanUpStrategy.getContext().getCanvas().addImage(image, 1, 0, 0, 1, 0, 0);
                }

                disableOutput = true;
            }
        } else if ("Tc".equals(operatorStr)) {
            cleanUpStrategy.getContext().setCharacterSpacing(((PdfNumber) operands.get(0)).floatValue());
        } else if (textShowingOperators.contains(operatorStr) && !allChunksAreVisible(cleanUpStrategy.getChunks())) {
            disableOutput = true;
            if (operatorStr.equals("'")) {
                canvas.getInternalBuffer().append(TStar);
            } else if ("\"".equals(operatorStr)) {
                operands.get(0).toPdf(canvas.getPdfWriter(), canvas.getInternalBuffer());
                canvas.getInternalBuffer().append(Tw);
                operands.get(1).toPdf(canvas.getPdfWriter(), canvas.getInternalBuffer());
                canvas.getInternalBuffer().append(TcTStar);
                cleanUpStrategy.getContext().setCharacterSpacing(((PdfNumber) operands.get(1)).floatValue());
            }
            canvas.setCharacterSpacing(0);
            canvas.getInternalBuffer().append((byte) '[');
            PdfCleanUpContentChunk prevChunk = null;
            float shift = 0;
            for (PdfCleanUpContentChunk chunk : chunks) {
                if (prevChunk != null) {
                    shift = shift + (-(chunk.getStartX() - prevChunk.getEndX()) * 1000 / chunk.getSize());
                }
                if (chunk.isVisible()) {
                    if (shift != 0)
                        canvas.getInternalBuffer().append(shift).append(' ');
                    chunk.getString().toPdf(canvas.getPdfWriter(), canvas.getInternalBuffer());
                    canvas.getInternalBuffer().append(' ');
                    shift = 0;
                } else {
                    float length = chunk.getEndX() - chunk.getStartX();
                    shift = shift + -length * 1000 / chunk.getSize();
                }
                prevChunk = chunk;
            }
            if (shift != 0)
                canvas.getInternalBuffer().append(shift);
            canvas.getInternalBuffer().append(TJ);

            float characterSpacing = cleanUpStrategy.getContext().getCharacterSpacing();

            if (characterSpacing != 0) {
                new PdfNumber(characterSpacing).toPdf(canvas.getPdfWriter(), canvas.getInternalBuffer());
                canvas.getInternalBuffer().append(Tc);
            }
        } else if ("\"".equals(operatorStr)) {
            cleanUpStrategy.getContext().setCharacterSpacing(((PdfNumber) operands.get(1)).floatValue());
        }
        if (!disableOutput) {
            int index = 0;
            for (PdfObject o : operands) {
                toPdf(o, canvas.getPdfWriter(), canvas.getInternalBuffer());
                canvas.getInternalBuffer().append(operands.size() > ++index ? (byte) ' ' : (byte) '\n');
            }
        }
        chunks.clear();
    }

    private boolean allChunksAreVisible(List<PdfCleanUpContentChunk> chunks) {
        for (PdfCleanUpContentChunk chunk : chunks) {
            if (!chunk.isVisible())
                return false;
        }
        return true;
    }

    // Overriding standard PdfObject.toPdf because we need sorted PdfDictionaries.
    static private void toPdf(PdfObject object, PdfWriter writer, OutputStream os) throws IOException {
        if (object instanceof PdfDictionary) {
            os.write('<');
            os.write('<');
            List<PdfName> keys = new ArrayList<PdfName>(((PdfDictionary) object).getKeys());
            Collections.sort(keys);
            for (PdfName key : keys) {
                toPdf(key, writer, os);
                PdfObject value = ((PdfDictionary) object).get(key);
                int type = value.type();
                if (type != PdfObject.ARRAY && type != PdfObject.DICTIONARY && type != PdfObject.NAME && type != PdfObject.STRING)
                    os.write(' ');
                toPdf(value, writer, os);
            }
            os.write('>');
            os.write('>');
        } else {
            object.toPdf(writer, os);
        }
    }

}
