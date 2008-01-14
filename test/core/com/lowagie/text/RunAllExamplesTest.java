package com.lowagie.text;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import java.io.File;
import org.junit.Test;

public class RunAllExamplesTest {

    public static void main(String args[]) throws Exception {       
        RunAllExamplesTest r = new RunAllExamplesTest();
        r.testImageExamples();

        r.testFormExamples();
        r.testGeneralExamples();
        r.testHtmlExamples();
        r.testBookmarkExamples();
        r.testMiscExamples();
        r.testTableExamples();
        r.testAnchorExamples();
        r.testColumnExamples();
        r.testChunkExamples();
        r.testDirectContentExamples();
        r.testFontExamples();
        r.testRtfExamples();
        
        r.testWindowsFonts();
    }

    public void runSingleTest(Class c, String... args) {
        try {
            Method m = c.getMethod("main", String[].class);
            m.invoke(null, new Object[] {args});
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test " + c.getName() + " failed: " + e.getCause());
        }
    }

    @Test
    public void testFormExamples() {
        runSingleTest(com.lowagie.examples.forms.TextFields.class);
        runSingleTest(com.lowagie.examples.forms.FormTextField.class);
        runSingleTest(com.lowagie.examples.forms.FormCheckbox.class);
        runSingleTest(com.lowagie.examples.forms.SimpleRegistrationForm.class);
        runSingleTest(com.lowagie.examples.forms.FormSignature.class);
        runSingleTest(com.lowagie.examples.forms.create.StudentCard.class);
        runSingleTest(com.lowagie.examples.forms.create.StudentCardForm.class);
        runSingleTest(com.lowagie.examples.forms.FormCombo.class);
        runSingleTest(com.lowagie.examples.forms.fill.FdfExample.class);
        runSingleTest(com.lowagie.examples.forms.fill.Register.class);
        runSingleTest(com.lowagie.examples.forms.fill.XfdfExample.class);
        runSingleTest(com.lowagie.examples.forms.FormPushButton.class);
        runSingleTest(com.lowagie.examples.forms.FormList.class);
        runSingleTest(com.lowagie.examples.forms.FormRadioButton.class);
        runSingleTest(com.lowagie.examples.forms.ListFields.class,
                "pushbutton.pdf",
                "radiobutton.pdf",
                "checkbox.pdf",
                "textfield.pdf",
                "SimpleRegistrationForm.pdf",
                "combo.pdf",
                "list.pdf",
                "TextFields.pdf",
                "signature.pdf"
                ); // FIXME: test output
    }

    @Test
    public void testGeneralExamples() {
        runSingleTest(com.lowagie.examples.general.HelloWorld.class);
        runSingleTest(com.lowagie.examples.general.faq.NewPage.class);
        runSingleTest(com.lowagie.examples.general.faq.Measurements.class);
        runSingleTest(com.lowagie.examples.general.faq.PdfVersion.class);
        runSingleTest(com.lowagie.examples.general.faq.iTextVersion.class);
        runSingleTest(com.lowagie.examples.general.HelloEncrypted.class);
        runSingleTest(com.lowagie.examples.general.HelloWorldMeta.class);
        runSingleTest(com.lowagie.examples.general.DefaultPageSize.class);
        runSingleTest(com.lowagie.examples.general.copystamp.AddWatermarkPageNumbers.class);
        runSingleTest(com.lowagie.examples.general.copystamp.Register.class);
        runSingleTest(com.lowagie.examples.general.copystamp.EncryptorExample.class);
        runSingleTest(com.lowagie.examples.general.copystamp.Concatenate.class, "HelloWorldMeta.pdf", "ChapterSection.pdf", "Concatenate.pdf");
        runSingleTest(com.lowagie.examples.general.copystamp.ConcatenateForms.class);
        runSingleTest(com.lowagie.examples.general.copystamp.TwoOnOne.class);
        runSingleTest(com.lowagie.examples.general.Margins.class);
        runSingleTest(com.lowagie.examples.general.read.Info.class, "HelloWorldMeta.pdf", "ChapterSection.pdf", "Concatenate.pdf");
        runSingleTest(com.lowagie.examples.general.read.ReadEncrypted.class);
        runSingleTest(com.lowagie.examples.general.LandscapePortrait.class);
        runSingleTest(com.lowagie.examples.general.CustomPageSize.class);
        runSingleTest(com.lowagie.examples.general.HelloSystemOut.class);
        runSingleTest(com.lowagie.examples.general.HelloWorldMultiple.class);
    }

    @Test
    public void testHtmlExamples() {
        runSingleTest(com.lowagie.examples.html.Images.class);
        runSingleTest(com.lowagie.examples.html.HelloWorldMeta.class);
        runSingleTest(com.lowagie.examples.html.JavaScriptAction.class);
        runSingleTest(com.lowagie.examples.html.HelloHtml.class);
        runSingleTest(com.lowagie.examples.html.ImagesURL.class);
    }

    @Test
    public void testBookmarkExamples() {
        runSingleTest(com.lowagie.examples.objects.bookmarks.ViewerPreferences.class);
        runSingleTest(com.lowagie.examples.objects.bookmarks.PageLabels.class);
        runSingleTest(com.lowagie.examples.objects.bookmarks.ChapterSection.class);
        runSingleTest(com.lowagie.examples.objects.bookmarks.OutlineActions.class);
        runSingleTest(com.lowagie.examples.objects.bookmarks.Layers.class);
        runSingleTest(com.lowagie.examples.objects.bookmarks.Destinations.class);
        runSingleTest(com.lowagie.examples.objects.bookmarks.Bookmarks.class);
    }

    @Test
    public void testMiscExamples() {
        runSingleTest(com.lowagie.examples.objects.SymbolSubstitution.class);

        runSingleTest(com.lowagie.examples.objects.NegativeLeading.class);
        runSingleTest(com.lowagie.examples.objects.FancyLists.class);

        runSingleTest(com.lowagie.examples.objects.Paragraphs.class);
        runSingleTest(com.lowagie.examples.objects.DifferentFonts.class);
        runSingleTest(com.lowagie.examples.objects.ParagraphAttributes.class);

        runSingleTest(com.lowagie.examples.objects.SpaceWordRatio.class);
        runSingleTest(com.lowagie.examples.objects.Chunks.class);
        runSingleTest(com.lowagie.examples.objects.Lists.class);

        runSingleTest(com.lowagie.examples.objects.Phrases.class);
        runSingleTest(com.lowagie.examples.objects.FontSelection.class);
    }

    @Test
    public void testImageExamples() {
        runSingleTest(com.lowagie.examples.objects.images.ImageSequence.class);
        runSingleTest(com.lowagie.examples.objects.images.Images.class);
        runSingleTest(com.lowagie.examples.objects.images.DvdCover.class,
                "dvdcover.pdf",
                "My Sunflower Movie",
                "808080",
                "sunflower-front.jpg",
                "sunflower-back.jpg"
                );
        runSingleTest(com.lowagie.examples.objects.images.ImagesAlignment.class);
        runSingleTest(com.lowagie.examples.objects.images.Scaling.class);
        runSingleTest(com.lowagie.examples.objects.images.AnnotatedImage.class);
        runSingleTest(com.lowagie.examples.objects.images.Alignment.class);
        runSingleTest(com.lowagie.examples.objects.images.ImageChunks.class);
        runSingleTest(com.lowagie.examples.objects.images.Rotating.class);
        runSingleTest(com.lowagie.examples.objects.images.AbsolutePositions.class);
        runSingleTest(com.lowagie.examples.objects.images.ImageMasks.class);
        runSingleTest(com.lowagie.examples.objects.images.tiff.ExamplePDF417.class);
        runSingleTest(com.lowagie.examples.objects.images.tiff.Barcodes.class);
        runSingleTest(com.lowagie.examples.objects.images.tiff.ExampleEAN128.class);
        runSingleTest(com.lowagie.examples.objects.images.tiff.OddEven.class, "odd.tif", "even.tif", "OddEven.pdf");
        runSingleTest(com.lowagie.examples.objects.images.tiff.Tiff2Pdf.class, "12.tif", "338814-00.tif", "odd.tif", "even.tif");
        runSingleTest(com.lowagie.examples.objects.images.RawData.class);
        /**/
    }

    @Test
    public void testTableExamples() {
        runSingleTest(com.lowagie.examples.objects.tables.pdfptable.FloatingBoxes.class);
        runSingleTest(com.lowagie.examples.objects.tables.pdfptable.SplitTable.class);
        runSingleTest(com.lowagie.examples.objects.tables.pdfptable.FragmentTable.class, new String[]{"50"});
        runSingleTest(com.lowagie.examples.objects.tables.pdfptable.VerticalTextInCells.class);
        runSingleTest(com.lowagie.examples.objects.tables.pdfptable.TableEvents2.class);
        runSingleTest(com.lowagie.examples.objects.tables.pdfptable.Tables.class);
        runSingleTest(com.lowagie.examples.objects.tables.pdfptable.WriteSelectedRows.class);
        runSingleTest(com.lowagie.examples.objects.tables.pdfptable.TableEvents1.class);
        runSingleTest(com.lowagie.examples.objects.tables.pdfptable.CellEvents.class);
        runSingleTest(com.lowagie.examples.objects.tables.CellWidths.class);
        runSingleTest(com.lowagie.examples.objects.tables.MyFirstTable.class);
        runSingleTest(com.lowagie.examples.objects.tables.SplitRows.class);
        runSingleTest(com.lowagie.examples.objects.tables.DefaultCell.class);
        runSingleTest(com.lowagie.examples.objects.tables.TableSpacing.class);
        runSingleTest(com.lowagie.examples.objects.tables.AddBigTable.class);
        runSingleTest(com.lowagie.examples.objects.tables.TableBorders.class);
        runSingleTest(com.lowagie.examples.objects.tables.NestedTables.class);
        runSingleTest(com.lowagie.examples.objects.tables.ImageCell.class);
        runSingleTest(com.lowagie.examples.objects.tables.CellAlignment.class);
        runSingleTest(com.lowagie.examples.objects.tables.TableWidthAlignment.class);
        runSingleTest(com.lowagie.examples.objects.tables.CellColors.class);
        runSingleTest(com.lowagie.examples.objects.tables.CellPaddingLeading.class);
        runSingleTest(com.lowagie.examples.objects.tables.CellHeights.class);
        runSingleTest(com.lowagie.examples.objects.tables.alternatives.TableWithImage.class);
        runSingleTest(com.lowagie.examples.objects.tables.alternatives.SpecificCells.class);
        runSingleTest(com.lowagie.examples.objects.tables.alternatives.LargeCell.class);
        runSingleTest(com.lowagie.examples.objects.tables.alternatives.TablePdfPTable.class);
        runSingleTest(com.lowagie.examples.objects.tables.alternatives.OldTable.class);
        runSingleTest(com.lowagie.examples.objects.tables.alternatives.MyFirstTable.class);
        runSingleTest(com.lowagie.examples.objects.tables.alternatives.PaddingBorders.class);
        runSingleTest(com.lowagie.examples.objects.tables.alternatives.RepeatingTable.class);
        runSingleTest(com.lowagie.examples.objects.tables.alternatives.NestedTables.class);
    }

    @Test
    public void testAnchorExamples() {
        runSingleTest(com.lowagie.examples.objects.anchors.OpenApplication.class, System.getenv("WINDIR") + File.separatorChar);
        runSingleTest(com.lowagie.examples.objects.anchors.ChainedActions.class);
        runSingleTest(com.lowagie.examples.objects.anchors.JavaScriptAction.class);
        runSingleTest(com.lowagie.examples.objects.anchors.Actions.class);
        runSingleTest(com.lowagie.examples.objects.anchors.NamedActions.class);
        runSingleTest(com.lowagie.examples.objects.anchors.AHref.class);
        runSingleTest(com.lowagie.examples.objects.anchors.SimpleAnnotations.class);
        runSingleTest(com.lowagie.examples.objects.anchors.LocalGoto.class);
        runSingleTest(com.lowagie.examples.objects.anchors.Annotations.class);
        runSingleTest(com.lowagie.examples.objects.anchors.RemoteGoto.class);
        runSingleTest(com.lowagie.examples.objects.anchors.LocalDestination.class);
    }

    @Test
    public void testColumnExamples() {
        runSingleTest(com.lowagie.examples.objects.columns.ColumnObjects.class);
        runSingleTest(com.lowagie.examples.objects.columns.Column.class);
        runSingleTest(com.lowagie.examples.objects.columns.ColumnSimple.class);
        runSingleTest(com.lowagie.examples.objects.columns.MultiColumnIrregular.class);
        runSingleTest(com.lowagie.examples.objects.columns.ColumnIrregular.class);
        runSingleTest(com.lowagie.examples.objects.columns.MultiColumnR2L.class);
        runSingleTest(com.lowagie.examples.objects.columns.MultiColumnSimple.class);
    }

    @Test
    public void testChunkExamples() {
        runSingleTest(com.lowagie.examples.objects.chunk.Width.class);
        runSingleTest(com.lowagie.examples.objects.chunk.Generic.class);
        runSingleTest(com.lowagie.examples.objects.chunk.Skew.class);
        runSingleTest(com.lowagie.examples.objects.chunk.Glossary.class);
        runSingleTest(com.lowagie.examples.objects.chunk.Lines.class);
        runSingleTest(com.lowagie.examples.objects.chunk.Background.class);
        runSingleTest(com.lowagie.examples.objects.chunk.EndOfLine.class);
        runSingleTest(com.lowagie.examples.objects.chunk.ChunkColor.class);
        runSingleTest(com.lowagie.examples.objects.chunk.Rendering.class);
        runSingleTest(com.lowagie.examples.objects.chunk.Hyphenation.class);
        runSingleTest(com.lowagie.examples.objects.chunk.SubSupScript.class);
        runSingleTest(com.lowagie.examples.objects.chunk.SplitChar.class);
    }

    @Test
    public void testDirectContentExamples() {
        runSingleTest(com.lowagie.examples.directcontent.optionalcontent.OrderedLayers.class);
        runSingleTest(com.lowagie.examples.directcontent.optionalcontent.Layers.class);
        runSingleTest(com.lowagie.examples.directcontent.optionalcontent.Automatic.class);
        runSingleTest(com.lowagie.examples.directcontent.optionalcontent.OptionalContent.class);
        runSingleTest(com.lowagie.examples.directcontent.optionalcontent.ContentGroups.class);
        runSingleTest(com.lowagie.examples.directcontent.optionalcontent.NestedLayers.class);
        runSingleTest(com.lowagie.examples.directcontent.coordinates.XandYcoordinates.class);
        runSingleTest(com.lowagie.examples.directcontent.coordinates.UpsideDown.class);
        runSingleTest(com.lowagie.examples.directcontent.coordinates.AffineTransformation.class);
        runSingleTest(com.lowagie.examples.directcontent.coordinates.TransformImage.class);
        runSingleTest(com.lowagie.examples.directcontent.coordinates.Transformations.class);
        runSingleTest(com.lowagie.examples.directcontent.pageevents.EndPage.class);
        runSingleTest(com.lowagie.examples.directcontent.pageevents.PageNumbersWatermark.class);
        runSingleTest(com.lowagie.examples.directcontent.pageevents.Events.class);
        runSingleTest(com.lowagie.examples.directcontent.pageevents.Bookmarks.class);
        runSingleTest(com.lowagie.examples.directcontent.graphics.GState.class);
        runSingleTest(com.lowagie.examples.directcontent.graphics.Circles.class);
        runSingleTest(com.lowagie.examples.directcontent.graphics.State.class);
        runSingleTest(com.lowagie.examples.directcontent.graphics.Shapes.class);
        runSingleTest(com.lowagie.examples.directcontent.graphics.Literal.class);
        runSingleTest(com.lowagie.examples.directcontent.colors.Transparency.class);
        runSingleTest(com.lowagie.examples.directcontent.colors.ShadingPattern.class);
        runSingleTest(com.lowagie.examples.directcontent.colors.SoftMask.class);
        runSingleTest(com.lowagie.examples.directcontent.colors.Groups.class);
        runSingleTest(com.lowagie.examples.directcontent.colors.Pattern.class);
        runSingleTest(com.lowagie.examples.directcontent.colors.Patterns.class);
        runSingleTest(com.lowagie.examples.directcontent.colors.Shading.class);
        runSingleTest(com.lowagie.examples.directcontent.colors.SpotColors.class);
        runSingleTest(com.lowagie.examples.directcontent.TemplateImages.class);
        runSingleTest(com.lowagie.examples.directcontent.Layers.class);
        runSingleTest(com.lowagie.examples.directcontent.Templates.class);
        runSingleTest(com.lowagie.examples.directcontent.text.Logo.class);
        runSingleTest(com.lowagie.examples.directcontent.text.Text.class);
        runSingleTest(com.lowagie.examples.directcontent.graphics2D.ArabicText.class);
        runSingleTest(com.lowagie.examples.directcontent.graphics2D.G2D.class);
        runSingleTest(com.lowagie.examples.directcontent.graphics2D.JFreeChartExample.class);
    }

    @Test
    public void testFontExamples() {
        runSingleTest(com.lowagie.examples.fonts.FontEncoding.class);
        runSingleTest(com.lowagie.examples.fonts.EncodingFont.class);
        runSingleTest(com.lowagie.examples.fonts.getting.UsingFontFactory.class);
        runSingleTest(com.lowagie.examples.fonts.getting.ChineseJapaneseKorean.class);
        runSingleTest(com.lowagie.examples.fonts.getting.OpenTypeFont.class);
        runSingleTest(com.lowagie.examples.fonts.styles.Vertical.class);
        runSingleTest(com.lowagie.examples.fonts.styles.FontStylePropagation.class);
        runSingleTest(com.lowagie.examples.fonts.styles.FixedFontWidth.class);
        runSingleTest(com.lowagie.examples.fonts.styles.ExtraStyles.class);
        runSingleTest(com.lowagie.examples.fonts.styles.FontColor.class);
        runSingleTest(com.lowagie.examples.fonts.StandardType1Fonts.class);
        runSingleTest(com.lowagie.examples.fonts.FontFactoryType1Fonts.class);
    }

    @Test
    public void testWindowsFonts() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            runSingleTest(com.lowagie.examples.fonts.TrueType.class);
            runSingleTest(com.lowagie.examples.fonts.FullFontNames.class);
            runSingleTest(com.lowagie.examples.fonts.getting.TrueType.class);
            runSingleTest(com.lowagie.examples.fonts.getting.RegisterFont.class);
            runSingleTest(com.lowagie.examples.fonts.getting.TrueTypeCollections.class);
            runSingleTest(com.lowagie.examples.fonts.getting.FontFactoryStyles.class);
            runSingleTest(com.lowagie.examples.fonts.styles.WidthHeight.class);
            runSingleTest(com.lowagie.examples.fonts.styles.ComplexText.class);
            runSingleTest(com.lowagie.examples.fonts.styles.RightToLeft.class);
            runSingleTest(com.lowagie.examples.fonts.ListEncodings.class);
            runSingleTest(com.lowagie.examples.fonts.UnicodeExample.class);
        }
    }

    @Test
    public void testRtfExamples() {
        runSingleTest(com.lowagie.examples.rtf.HelloWorld.class);
        runSingleTest(com.lowagie.examples.rtf.extensions.hf.ExtendedHeaderFooter.class);
        runSingleTest(com.lowagie.examples.rtf.extensions.hf.ChapterHeaderFooter.class);
        runSingleTest(com.lowagie.examples.rtf.extensions.hf.MultipleHeaderFooter.class);
        runSingleTest(com.lowagie.examples.rtf.extensions.table.ExtendedTableCell.class);
        runSingleTest(com.lowagie.examples.rtf.extensions.font.ExtendedFontStyles.class);
        runSingleTest(com.lowagie.examples.rtf.extensions.font.ExtendedFont.class);
        runSingleTest(com.lowagie.examples.rtf.features.pagenumber.PageNumber.class);
        runSingleTest(com.lowagie.examples.rtf.features.pagenumber.TotalPageNumber.class);
        runSingleTest(com.lowagie.examples.rtf.features.shape.DrawingFreeform.class);
        runSingleTest(com.lowagie.examples.rtf.features.shape.DrawingAnchor.class);
        runSingleTest(com.lowagie.examples.rtf.features.shape.DrawingObjects.class);
        runSingleTest(com.lowagie.examples.rtf.features.shape.DrawingText.class);
        runSingleTest(com.lowagie.examples.rtf.features.shape.DrawingWrap.class);
        runSingleTest(com.lowagie.examples.rtf.features.toc.TableOfContents.class);
        runSingleTest(com.lowagie.examples.rtf.features.tabs.BasicTabs.class);
        runSingleTest(com.lowagie.examples.rtf.features.tabs.TabGroups.class);
        runSingleTest(com.lowagie.examples.rtf.features.styles.BasicStylesheets.class);
        runSingleTest(com.lowagie.examples.rtf.features.styles.ChangingStylesheets.class);
        runSingleTest(com.lowagie.examples.rtf.features.styles.ExtendingStylesheets.class);
        runSingleTest(com.lowagie.examples.rtf.features.direct.SoftLineBreak.class);
        runSingleTest(com.lowagie.examples.rtf.RtfTest.class);
        runSingleTest(com.lowagie.examples.rtf.documentsettings.DocumentSettings.class);
        runSingleTest(com.lowagie.examples.rtf.RtfTOCandCellborders.class);
    }

}

