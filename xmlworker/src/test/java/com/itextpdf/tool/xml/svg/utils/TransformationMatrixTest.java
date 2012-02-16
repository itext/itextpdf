package com.itextpdf.tool.xml.svg.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import com.itextpdf.text.geom.AffineTransform;
import com.itextpdf.tool.xml.svg.utils.TransformationMatrix;


public class TransformationMatrixTest {
	@Test
	public void testSplitString(){
		String str = "vanalles ( 154, -887,4), eendit() eendit()";		
		List<String> result = TransformationMatrix.splitString(str);
		Assert.assertEquals(3, result.size());
		Assert.assertEquals("vanalles ( 154, -887,4)", result.get(0));
		Assert.assertEquals(", eendit()", result.get(1));
		Assert.assertEquals("eendit()", result.get(2));
	}
	
	
	@Test
	public void testGetValuesFromStr() {
		String str = "vanalles ( 154, -887,4)";		
		List<Float> result = TransformationMatrix.getValuesFromStr(str);		
		Assert.assertEquals(3, result.size());
		Assert.assertEquals(154f, result.get(0));
		Assert.assertEquals(-887f, result.get(1));
		Assert.assertEquals(4f, result.get(2));
	}
	
	@Test
	public void testGetValuesFromStrTwo() {
		String str = ", vanalles ( 154, -887,4)";		
		List<Float> result = TransformationMatrix.getValuesFromStr(str);		
		Assert.assertEquals(3, result.size());
		Assert.assertEquals(154f, result.get(0));
		Assert.assertEquals(-887f, result.get(1));
		Assert.assertEquals(4f, result.get(2));
	}	
	
	@Test
	public void testGetValuesFromStrWrongFormat() {
		try{
			String str = "aa (154, -887,4";		
			TransformationMatrix.getValuesFromStr(str);
			Assert.fail();
		}catch(Exception exp){
			Assert.assertEquals("Could not parse the transform", exp.getMessage());
		}	
	}	
	
	@Test
	public void testCreateWithMatrix(){
		List<Float> values = new ArrayList<Float>();
		for (int i = 0; i < 6; i++) {
			values.add(new Float(i+1));
		}
		checkValues(TransformationMatrix.createWithMatrix(values), 1, 2, 3, 4, 5, 6);
	}
	
	private void checkValues(AffineTransform matrix, double a, double b, double c, double d, double e, double f){
		Assert.assertEquals(a, matrix.getScaleX()); //00
		Assert.assertEquals(b, matrix.getShearY()); //10
		Assert.assertEquals(c, matrix.getShearX()); //01
		Assert.assertEquals(d, matrix.getScaleY()); //11
		Assert.assertEquals(e, matrix.getTranslateX()); //02
		Assert.assertEquals(f, matrix.getTranslateY()); //12
	}
	
	@Test
	public void testCreateWithMatrixException(){
		try{
			List<Float> values = new ArrayList<Float>();
			TransformationMatrix.createWithMatrix(values);
			Assert.fail();
		}catch(Exception exp){
			Assert.assertEquals("Could not parse the transform", exp.getMessage());
		}		
	}
	
	@Test
	public void testCreateForTranslateTwoCoordinates(){
		List<Float> values = new ArrayList<Float>();
		values.add(new Float(10));
		values.add(new Float(20));
		checkValues(TransformationMatrix.createForTranslate(values), 1, 0, 0, 1, 10, 20);
	}
	
	@Test
	public void testCreateForTranslateOneCoordinates(){
		List<Float> values = new ArrayList<Float>();
		values.add(new Float(10));
		checkValues(TransformationMatrix.createForTranslate(values), 1, 0, 0, 1, 10, 0);
	}	

	@Test
	public void testCreateForScaleTwoCoordinates(){
		List<Float> values = new ArrayList<Float>();
		values.add(new Float(10));
		values.add(new Float(20));
		checkValues(TransformationMatrix.createForScale(values), 10, 0, 0, 20, 0, 0);
	}
	
	@Test
	public void testCreateForScaleOneCoordinates(){
		List<Float> values = new ArrayList<Float>();
		values.add(new Float(10));
		checkValues(TransformationMatrix.createForScale(values), 10, 0, 0, 10, 0, 0);
	}
	
	@Test
	public void testCreateForRotate(){
		List<Float> values = new ArrayList<Float>();
		double angleInDegrees = 10;
		double angleInRadians = Math.toRadians(angleInDegrees);
		values.add(new Float(angleInDegrees));
		
		checkValues(TransformationMatrix.createForRotate(values), Math.cos(angleInRadians), Math.sin(angleInRadians), -1*Math.sin(angleInRadians), Math.cos(angleInRadians), 0, 0);
	}
	
	@Test
	public void testCreateForSkewX(){		
		double angleInDegrees = 10;
		double angleInRadians = Math.toRadians(angleInDegrees);
		
		List<Float> values = new ArrayList<Float>();
		values.add(new Float(angleInDegrees));
		checkValues(TransformationMatrix.createForSkewX(values), 1, 0, Math.tan(angleInRadians), 1, 0, 0);
	}
	
	@Test
	public void testCreateForSkewY(){
		double angleInDegrees = 10;
		double angleInRadians = Math.toRadians(angleInDegrees);
		
		List<Float> values = new ArrayList<Float>();
		values.add(new Float(angleInDegrees));
		checkValues(TransformationMatrix.createForSkewY(values), 1, Math.tan(angleInRadians), 0, 1, 0, 0);
	}	
	
}
