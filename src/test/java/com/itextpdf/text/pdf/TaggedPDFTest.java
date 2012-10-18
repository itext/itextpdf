package com.itextpdf.text.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class TaggedPDFTest {
    private Document document;
    private PdfWriter writer;

    private static final String text = new String("Lorem ipsum dolor sit amet," +
            "consectetur adipiscing elit." +
            "Pellentesque a lectus sit amet lectus accumsan aliquam." +
            "Quisque facilisis ullamcorper dolor, quis gravida leo faucibus in. Donec a dolor ligula, quis placerat nunc. Etiam enim velit, egestas in lacinia at, ultricies eu massa." +
            "Cras ornare felis id quam vehicula lobortis. Ut semper malesuada nulla, in vulputate dui eleifend at. Phasellus pulvinar nisl a lorem volutpat pellentesque. In vitae" +
            "ligula et quam vestibulum iaculis eget vitae massa. Fusce vitae leo ut diam suscipit dictum in id sapien. Praesent mi ligula, auctor vitae ultrices in, venenatis non" +
            "odio. Nullam sit amet velit pellentesque lectus consectetur lacinia nec quis mi. In hac habitasse platea dictumst." +
            "Quisque facilisis ullamcorper dolor, quis gravida leo faucibus in." +
            "Donec a dolor ligula, quis placerat nunc.\n" +
            "1. Etiam enim velit, egestas in lacinia at, ultricies eu massa. Cras ornare felis id quam vehicula lobortis. Ut semper malesuada nulla, in vulputate dui eleifend at." +
            "Phasellus pulvinar nisl a lorem volutpat pellentesque. In vitae ligula et quam vestibulum iaculis eget vitae massa. Fusce vitae leo ut diam suscipit dictum in id" +
            "sapien. Praesent mi ligula, auctor vitae ultrices in, venenatis non odio. Nullam sit amet velit pellentesque lectus consectetur lacinia nec quis mi. In hac" +
            "habitasse platea dictumst.\n" +
            "2. Morbi euismod, nunc quis malesuada feugiat, dui nibh rhoncus leo, quis cursus erat tellus vel tortor. Mauris nibh dolor, iaculis et pharetra pretium," +
            "pellentesque vitae erat. Aenean enim nisi, euismod quis ultricies vel, convallis nec nulla. Suspendisse nisl purus, molestie et egestas ac, cursus in mauris." +
            "Aliquam erat volutpat. Donec at nulla in elit faucibus mollis ac vel enim. Nullam dapibus dui sit amet sem consectetur ac vulputate est sagittis. Aliquam luctus" +
            "ornare nulla. Mauris adipiscing congue pharetra. Proin tempus, nibh sed pretium tempor, arcu est hendrerit est, et dignissim odio leo non purus." +
            "Suspendisse non elit massa. Vestibulum tincidunt ipsum vitae dui congue sagittis. Aenean porttitor tristique euismod. Nulla id justo in quam imperdiet" +
            "facilisis ut non turpis. Lorem ipsum dolor sit amet, consectetur adipiscing elit.\n" +
            "3. Aliquam non elit ligula, nec hendrerit urna. Mauris ut velit sapien. Sed in convallis diam. Nulla faucibus, purus a porttitor ultrices, est quam convallis magna," +
            "molestie aliquam sapien nulla eget metus. Integer nec enim mi, eu mattis massa. Integer quis sapien vel purus pretium ullamcorper ac id dui. Suspendisse" +
            "pellentesque tellus sit amet neque pulvinar egestas lacinia diam imperdiet.\n" +
            "4. Curabitur hendrerit, sem et facilisis vestibulum, massa felis vestibulum ligula, ut faucibus massa nisi in neque. Nulla facilisi. Etiam diam mauris, pellentesque" +
            "lacinia dapibus at, lobortis non quam. Nullam et neque quis diam vestibulum scelerisque ullamcorper non mauris. Cras massa enim, commodo malesuada" +
            "tincidunt ac, lobortis eu erat. Sed sed risus velit. Suspendisse tellus tortor, ullamcorper nec tristique ac, semper non nulla. Maecenas vitae diam orci, sed" +
            "fermentum enim. Curabitur a libero nisl, vel laoreet nulla. Integer id volutpat sem. Pellentesque blandit, tellus at consequat dictum, urna sem elementum nisi," +
            "a bibendum nisi ipsum sit amet felis. Donec mattis ipsum nec metus lobortis eget volutpat nisl volutpat.\n" +
            "5. Fusce in aliquet nibh. Etiam quis varius ipsum. Vivamus sit amet mauris a libero iaculis semper in a neque. Nam faucibus congue posuere. Cras vitae nibh" +
            "sed magna ultricies pretium. Proin eget lacus quis dui ullamcorper cursus commodo in lacus. Quisque et sem id leo venenatis dictum dignissim et felis." +
            "Vestibulum enim urna, vehicula vel dictum in, congue quis sapien. Quisque ac mauris tellus. Nulla cursus pellentesque mauris viverra bibendum. Fusce" +
            "molestie dui id sem blandit in convallis justo euismod. Curabitur velit nisi, adipiscing sed consequat et, dignissim eget dolor. Aenean malesuada quam id mi" +
            "vestibulum pulvinar. Nullam diam quam, lobortis sit amet semper vitae, tempus eget dolor.");

    private void initializeDocument(String path) throws DocumentException, FileNotFoundException {
        new File("./target/com/itextpdf/test/pdf/TaggedPDFTest/").mkdirs();
        Document.compress = false;
        document = new Document();
        writer = PdfWriter.getInstance(document, new FileOutputStream(path));
        writer.setTagged();
        document.open();
    }

    @Test
    public void createTaggedPDF1() throws DocumentException, FileNotFoundException {
        initializeDocument("./target/com/itextpdf/test/pdf/TaggedPDFTest/out1.pdf");
        Paragraph paragraph = new Paragraph(text);
        paragraph.setFont(new Font(Font.FontFamily.HELVETICA,8));
        ColumnText columnText = new ColumnText(writer.getDirectContent());
        columnText.setSimpleColumn(36, 36, 250, 800);
        columnText.addElement(paragraph);
        columnText.go();
        columnText.setSimpleColumn(300,36,500,800);
        columnText.go();
        document.close();
    }

    @Test
    public void createTaggedPDF2() throws DocumentException, FileNotFoundException {
        initializeDocument("./target/com/itextpdf/test/pdf/TaggedPDFTest/out2.pdf");
        Paragraph paragraph = new Paragraph(text);
        ColumnText columnText = new ColumnText(writer.getDirectContent());
        columnText.setSimpleColumn(36,36,400,800);
        columnText.addElement(paragraph);
        columnText.go();
        document.newPage();
        columnText.setSimpleColumn(36,36,400,800);
        columnText.go();
        document.close();
    }

    @Test
    public void createTaggedPDF3() throws DocumentException, FileNotFoundException {
        initializeDocument("./target/com/itextpdf/test/pdf/TaggedPDFTest/out3.pdf");
        document.add(new Paragraph("Hello World!"));
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();
    }

    @Test
    public void createTaggedPDF4() throws DocumentException, FileNotFoundException {
        initializeDocument("./target/com/itextpdf/test/pdf/TaggedPDFTest/out4.pdf");

        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginMarkedContentSequence(PdfName.P);
        canvas.beginMarkedContentSequence(PdfName.SPAN);
        canvas.endMarkedContentSequence();
        canvas.endMarkedContentSequence();

        PdfStructureElement p = new PdfStructureElement(writer.getStructureTreeRoot(), PdfName.P);
        canvas.beginMarkedContentSequence(p);
        PdfStructureElement span = new PdfStructureElement(p, PdfName.SPAN);
        canvas.beginMarkedContentSequence(span);
        canvas.endMarkedContentSequence();
        canvas.endMarkedContentSequence();

        document.close();
    }


}
