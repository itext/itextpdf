package com.itextpdf.tool.xml.svg.graphic;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;


import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.svg.PathItem;
import com.itextpdf.tool.xml.svg.graphic.Path;


public class PathTest {	
	@Test
	public void testTranslateMoveAndLine(){		
		//one coordinate, second coordinate is 0
		checkTranslate(getPathList(getPathItem(1f, 'M')), getPathList(getPathItem(1f, 0f, 'M')));
		checkTranslate(getPathList(getPathItem(1f, 'L')), getPathList(getPathItem(1f, 0f, 'L')));
		checkTranslate(getPathList(getPathItem(1f, 'm')), getPathList(getPathItem(1f, 0f, 'M')));
		checkTranslate(getPathList(getPathItem(1f, 'l')), getPathList(getPathItem(1f, 0f, 'L')));
		
		//two coordinates, nothing changes
		checkTranslate(getPathList(getPathItem(1f, 2f, 'M')), getPathList(getPathItem(1f, 2f, 'M')));
		checkTranslate(getPathList(getPathItem(1f, 2f, 'L')), getPathList(getPathItem(1f, 2f, 'L')));
		checkTranslate(getPathList(getPathItem(1f, 2f, 'm')), getPathList(getPathItem(1f, 2f, 'M')));
		checkTranslate(getPathList(getPathItem(1f, 2f, 'l')), getPathList(getPathItem(1f, 2f, 'L')));
		
		//tree coordinates, add a line to
		checkTranslate(getPathList(getPathItem(1f, 2f, 3f, 'M')), getPathList(getPathItem(1f, 2f, 'M'), getPathItem(3f, 0f, 'L')));
		checkTranslate(getPathList(getPathItem(1f, 2f, 3f, 'L')), getPathList(getPathItem(1f, 2f, 'L'), getPathItem(3f, 0f, 'L')));
		checkTranslate(getPathList(getPathItem(1f, 2f, 3f, 'm')), getPathList(getPathItem(1f, 2f, 'M'), getPathItem(4f, 2f, 'L')));
		checkTranslate(getPathList(getPathItem(1f, 2f, 3f, 'l')), getPathList(getPathItem(1f, 2f, 'L'), getPathItem(4f, 2f, 'L')));		
	}
	
	@Test
	public void testTranslateHorizontalAndVertical(){
		PathItem move = getPathItem(2f, 3f, 'M');
		//no coordinates
		checkTranslate(getPathList(move, getPathItem('H')), getPathList(move, getPathItem(0f, 3f, 'L')));
		checkTranslate(getPathList(move, getPathItem('V')), getPathList(move, getPathItem(2f, 0f, 'L')));
		checkTranslate(getPathList(move, getPathItem('h')), getPathList(move, getPathItem(2f, 3f, 'L')));
		checkTranslate(getPathList(move, getPathItem('v')), getPathList(move, getPathItem(2f, 3f, 'L')));
		
		//one coordinate
		checkTranslate(getPathList(move, getPathItem(1f, 2f, 4f, 'H')), getPathList(move, getPathItem(4f, 3f, 'L'))); //last coordinate
		checkTranslate(getPathList(move, getPathItem(1f, 2f, 4f, 'V')), getPathList(move, getPathItem(2f, 4f, 'L')));
		checkTranslate(getPathList(move, getPathItem(1f, 2f, 4f, 'h')), getPathList(move, getPathItem(9f, 3f, 'L'))); //sum of coordinates
		checkTranslate(getPathList(move, getPathItem(1f, 2f, 4f, 'v')), getPathList(move, getPathItem(2f, 10f, 'L')));
	} 
	
	@Test
	public void testTranslateCurvesNotEnoughCoordinates(){
		char[] typeList = {'C', 'c', 'S', 's', 'Q', 'q','T','t'};
		PathItem move = getPathItem(2f, 3f, 'M'); 
		//no coordinates - only the move
		for (int i = 0; i < typeList.length; i++) {
			checkTranslate(getPathList(move, getPathItem(typeList[i])), getPathList(move));
		}
		
		//one coordinates - only the move
		for (int i = 0; i < typeList.length; i++) {
			checkTranslate(getPathList(move, getPathItem(1f, typeList[i])), getPathList(move));
		}
		
		//two coordinates (except for the last two types) - only the move
		for (int i = 0; i < typeList.length - 2; i++) {
			checkTranslate(getPathList(move, getPathItem(1f, 2f, typeList[i])), getPathList(move));
		}		
	}
	
	//"M100 300 L 200 300 C300 50 400 50 600 300" 
	//"M100 300 L 200 300 c100 -250 200 -250 400 0"	
	@Test
	public void testTranslateCurvesCubic(){
		PathItem move = getPathItem(100f, 300f, 'M');
		PathItem line = getPathItem(200f, 300f, 'L'); 
		
		float[] list = {300f, 50f, 400f, 50f, 600f, 300f};
		PathItem absoluteCubic = getPathItem(list, 'C');
		
		//check that the path is the same
		checkTranslate(getPathList(move, line, absoluteCubic), getPathList(move, line, absoluteCubic));
		
		//check the relative 'c' to absolute 'C'
		float[] relative = {100f, -250f, 200f, -250f, 400f, 0f};
		PathItem relativeCubic = getPathItem(relative, 'c');
		checkTranslate(getPathList(move, line, relativeCubic), getPathList(move, line, absoluteCubic));
	}
	
	//M100 300 L 200 300 S300 50 600 300
	//M100 300 L 200 300 s100 -250 400 0
	//M100 300 L 200 300 C200 300 300 50 600 300
	@Test
	public void testTranslateCurvesCubic2(){
		PathItem move = getPathItem(100f, 300f, 'M');
		PathItem line = getPathItem(200f, 300f, 'L'); 
		
		float[] list = {200f, 300f, 300f, 50f, 600f, 300f};
		PathItem absoluteCubic = getPathItem(list, 'C');
		
		float[] listShortHand = {300f, 50f, 600f, 300f};
		PathItem shorthand = getPathItem(listShortHand, 'S');		
		
		//check the absolute 'S' to absolute 'C'
		checkTranslate(getPathList(move, line, shorthand), getPathList(move, line, absoluteCubic));
		
		//check the absolute 's' to absolute 'C'
		float[] relative = {100f, -250f, 400f, 0f};
		PathItem shorthandRelative = getPathItem(relative, 's');
		checkTranslate(getPathList(move, line, shorthandRelative), getPathList(move, line, absoluteCubic));
	}
	
	//M100 300 S300 50 500 300 100 60 600 300
	//M100 300 s200 -250 400 0 -400 -240 100 0
	//M100 300 C100 300 300 50 500 300 700 550 100 60 600 300
	@Test
	public void testTranslateCurvesCubic3(){
		PathItem move = getPathItem(100f, 300f, 'M');
		
		float[] list1 = {100f, 300f, 300f, 50f, 500f, 300f};
		PathItem absoluteCubic1 = getPathItem(list1, 'C');
		float[] list2 = {700f, 550f, 100f, 60f, 600f, 300f};
		PathItem absoluteCubic2 = getPathItem(list2, 'C');
		
		float[] listShortHand = {300f, 50f, 500f, 300f, 100f, 60f, 600f, 300f};
		PathItem shorthand = getPathItem(listShortHand, 'S');		
		
		//check the absolute 'S' to absolute 'C'
		checkTranslate(getPathList(move, shorthand), getPathList(move, absoluteCubic1, absoluteCubic2));
		
		//check the absolute 's' to absolute 'C'
		float[] relative = {200f, -250f, 400f, 0f, -400f, -240f, 100f, 0f};
		PathItem shorthandRelative = getPathItem(relative, 's');
		checkTranslate(getPathList(move, shorthandRelative), getPathList(move, absoluteCubic1, absoluteCubic2));
	}
	
	//"M100 300 L 200 300 Q300 50 400 50 Q600 50 700 300" 
	//"M100 300 L 200 300 q100 -250 200 -250 200 0 300 250"	
	@Test
	public void testTranslateCurvesQuadraticRelative(){
		PathItem move = getPathItem(100f, 300f, 'M');
		PathItem line = getPathItem(200f, 300f, 'L'); 
		
		float[] list1 = {300f, 50f, 400f, 50f};
		PathItem absolute1 = getPathItem(list1, 'Q');
		float[] list2 = {600f, 50f, 700f, 300f};
		PathItem absolute2 = getPathItem(list2, 'Q');
		
		float[] list = {300f, 50f, 400f, 50f, 600f, 50f, 700f, 300f};
		PathItem absolute = getPathItem(list, 'Q');
		//check that the path is the same (two items Q)
		checkTranslate(getPathList(move, line, absolute), getPathList(move, line, absolute1, absolute2));
		
		//check the relative 'q' to absolute 'Q'
		float[] relativeList = {100f, -250f, 200f, -250f, 200f, 0f, 300f, 250f};
		PathItem relative = getPathItem(relativeList, 'q');
		checkTranslate(getPathList(move, line, relative), getPathList(move, line, absolute1, absolute2));
	}
	
	//"M100 300 L 200 300 T 400 100 700 300" 
	//"M100 300 L 200 300 t200 -200 300 200"
	//"M100 300 L 200 300 Q200 300 400 100 Q 600 -100 700 300"	
	@Test
	public void testTranslateCurvesQuadraticShortHand(){
		PathItem move = getPathItem(100f, 300f, 'M');
		PathItem line = getPathItem(200f, 300f, 'L'); 
		
		float[] list1 = {200f, 300f, 400f, 100f};
		PathItem absolute1 = getPathItem(list1, 'Q');
		float[] list2 = {600f, -100f, 700f, 300f};
		PathItem absolute2 = getPathItem(list2, 'Q');
		
		float[] list = {400f, 100f, 700f, 300f};
		PathItem shorthand = getPathItem(list, 'T');
		//absolute shorthanded 'T' to 'Q'
		checkTranslate(getPathList(move, line, shorthand), getPathList(move, line, absolute1, absolute2));
		
		//check the relative shorthanded 't' to absolute 'Q'
		float[] relativeList = {200f, -200f, 300f, 200f};
		PathItem relative = getPathItem(relativeList, 't');
		checkTranslate(getPathList(move, line, relative), getPathList(move, line, absolute1, absolute2));
	}	
	
	private List<PathItem> getPathList(PathItem item){
		List<PathItem> result = new ArrayList<PathItem>();
		result.add(item);
		return result;
	}
	
	private List<PathItem> getPathList(PathItem item1, PathItem item2){
		List<PathItem> result = new ArrayList<PathItem>();
		result.add(item1);
		result.add(item2);
		return result;
	}
	
	private List<PathItem> getPathList(PathItem item1, PathItem item2, PathItem item3){
		List<PathItem> result = new ArrayList<PathItem>();
		result.add(item1);
		result.add(item2);
		result.add(item3);
		return result;
	}
	
	private List<PathItem> getPathList(PathItem item1, PathItem item2, PathItem item3, PathItem item4){
		List<PathItem> result = new ArrayList<PathItem>();
		result.add(item1);
		result.add(item2);
		result.add(item3);
		result.add(item4);
		return result;
	}	
	
	private PathItem getPathItem(char type){
		List<Float> coordinates = new ArrayList<Float>();
		return new PathItem(coordinates, type);
	}	
	
	private PathItem getPathItem(float coordinate, char type){
		List<Float> coordinates = new ArrayList<Float>();
		coordinates.add(coordinate);
		return new PathItem(coordinates, type);
	}
	
	private PathItem getPathItem(float coordinate1, float coordinate2, char type){
		List<Float> coordinates = new ArrayList<Float>();
		coordinates.add(coordinate1);
		coordinates.add(coordinate2);
		return new PathItem(coordinates, type);
	}
	
	private PathItem getPathItem(float coordinate1, float coordinate2, float coordinate3, char type){
		List<Float> coordinates = new ArrayList<Float>();
		coordinates.add(coordinate1);
		coordinates.add(coordinate2);
		coordinates.add(coordinate3);
		return new PathItem(coordinates, type);
	}
	
	private PathItem getPathItem(float[] list, char type){
		List<Float> coordinates = new ArrayList<Float>();
		for (int i = 0; i < list.length; i++) {
			coordinates.add(list[i]);
		}	
		return new PathItem(coordinates, type);
	}	
	
	private void checkTranslate(List<PathItem> items, List<PathItem> expected){
		Path path = new Path(null, null);		
		List<PathItem> translatedItems = path.translate(items);
		Assert.assertEquals(expected.size(), translatedItems.size());
		
		for (int i = 0; i < expected.size(); i++) {
			PathItem item = expected.get(i);
			PathItem translatedItem = translatedItems.get(i);
			Assert.assertEquals(translatedItem.getType(), item.getType());
			
			List<Float> itemCoordinates = item.getCoordinates();
			List<Float> translatedItemCoordinates = translatedItem.getCoordinates();
			
			Assert.assertEquals(translatedItemCoordinates.size(), itemCoordinates.size());
			for (int j = 0; j < translatedItemCoordinates.size(); j++) {
				Assert.assertEquals(translatedItemCoordinates.get(j), itemCoordinates.get(j));
			}
		}
	}	
	
	class PdfContentByteTest extends PdfContentByte{
		List<String> lines = new ArrayList<String>();

		public PdfContentByteTest(PdfWriter wr) {
			super(wr);
		}
		
		@Override
		public void lineTo(float x, float y) {
			lines.add("L:" + x + "," + y);
		}
		
		@Override
		public void moveTo(float x, float y) {
			lines.add("M:" + x + "," + y);
		}
		
		public List<String> getLines(){
			return lines;
		}
	}
}
