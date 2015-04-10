/**
 * Example written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/24404686/i-need-to-create-a-table-and-assign-the-values-into-the-table-in-pdf-using-javaf
 */
package sandbox.tables;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ArrayToTable {

    public static final String DEST = "results/tables/array_to_table.pdf";
    
    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ArrayToTable().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(8);
        table.setWidthPercentage(100);
        List<List<String>> dataset = getData();
        for (List<String> record : dataset) {
            for (String field : record) {
                table.addCell(field);
            }
        }
        document.add(table);
        document.close();
    }
    
    public List<List<String>> getData() {
        List<List<String>> data = new ArrayList<List<String>>();
        String[] tableTitleList = {" Title", " (Re)set", " Obs", " Mean", " Std.Dev", " Min", " Max", "Unit"};
        data.add(Arrays.asList(tableTitleList));
        for (int i = 0; i < 10; ) {
            List<String> dataLine = new ArrayList<String>();
            i++;
            for (int j = 0; j < tableTitleList.length; j++) {
                dataLine.add(tableTitleList[j] + " " + i);
            }
            data.add(dataLine);
        }
        return data;
    }
}
