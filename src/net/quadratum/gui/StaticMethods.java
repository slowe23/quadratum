package net.quadratum.gui;

import java.util.*;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

/** Static convenience methods */
public class StaticMethods {

	/**
	 * Rounds a double to the nearest int value.
	 * 
	 * @param d A double to round
	 * @return The closest integer to the given double value
	 */
	public static int round(double d) {
		return longToInt(Math.round(d));
	}
	
	/**
	 * Calculates the floor of a given double value
	 *
	 * @param d A double value
	 * @return The largest integer which is less than or equal to the double value
	 */
	public static int floor(double d) {
		int iD = (int)d;
		if(d>=iD)
			return iD;
		else
			return iD-1;
	}
	
	/**
	 * Converts a long to an int, capping it if it excedes the minimum or 
	 * maximum int values as if it were using saturation arithmetic.
	 *
	 * @param l A long to convert
	 * @return An int which is as close as possible to the given long
	 */
	public static int longToInt(long l) {
		if(l<Integer.MIN_VALUE)
			return Integer.MIN_VALUE;
		
		if(l>Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		
		return (int)l;
	}
	
	/**
	 * Creates a Color with the RGB values of the given color and the given alpha value
	 *
	 * @param c The color to use
	 * @param alpha The alpha value to give the color (must be in the range 0-255)
	 * @return A new color with the RGB of the original color and the given alpha value
	 */
	public static Color applyAlpha(Color c, int alpha) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}
	
	/**
	 * Creates a copy of a given rectangular matrix (or null)
	 *
	 * The given matrix must either be null or rectangular
	 * That is, if the entire array is not null, then all of its rows must be non-null and the same length
	 */
	public static int[][] copy(int[][] toCopy) {
		if(toCopy==null)
			return null;
		
		int[][] copy = new int[toCopy.length][toCopy[0].length];
		for(int i = 0; i<copy.length; i++)
			for(int j = 0; j<copy[i].length; j++)
				copy[i][j] = toCopy[i][j];
		
		return copy;
	}
	
	/** Converts a String into a String array of lines based on the given max width */
	public static String[] getWrap(FontMetrics fmetr, String s, int maxw) {
		ArrayList<String> list = new ArrayList<String>();
		String[] words = s.split("\\s");  //Split based on whitespace
		String current = "";
		for(int i = 0; i<words.length; i++) {
			String word = words[i];
			
			String pot;
			if(current.length()>0)
				pot = current+" "+word;
			else
				pot = word;
			
			if(fmetr.stringWidth(pot)<=maxw)
				current = pot;
			else {
				list.add(current);
				current = "";
			}
			
			if(current=="") {
				while(fmetr.stringWidth(word)>maxw) {
					int brk = findOnscreenLength(fmetr, word, maxw);
					list.add(word.substring(0, brk));
					word = word.substring(brk);
				}
				current = word;
			}
		}
		
		if(fmetr.stringWidth(current)<=maxw)
			list.add(current);
		
		return list.toArray(new String[list.size()]);
	}
	
	/** Finds the maxiumum number of characaters of the given string that can fit within the given width */
	public static int findOnscreenLength(FontMetrics fmetr, String s, int maxw) {
		int sw = fmetr.stringWidth(s);
		
		if(sw<=maxw)
			return s.length();
		
		int minlength = 0;  //minlength is the longest substring of s known to fit
		int maxlength = s.length()-1;  //maxlength+1 is the shortest substring of s known not to fit
		
		int trylength = (maxw*s.length())/sw;
		if(trylength<=minlength)
			trylength = minlength+1;
		if(trylength>maxlength)
			trylength = maxlength;
		
		while(minlength < maxlength) {
			sw = fmetr.stringWidth(s.substring(0, trylength));

			if(sw<=maxw)
				minlength = trylength;
			else
				maxlength = trylength-1;
			
			trylength = (maxw*trylength)/sw;  //Estimate split position
			if(trylength<=minlength)
				trylength = minlength+1;
			if(trylength>maxlength)
				trylength = maxlength;
		}
		return maxlength+1;
	}
	
	/** Creates a title border with the given title */
	public static Border getTitleBorder(String title) {
		return BorderFactory.createTitledBorder(null, title, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
	}
	
	/** Creates a scrolling text area for displaying non-editable text */
	public static STD createScrollingTextDisplay(int lines) {
		JTextArea area = new JTextArea();
		area.setRows(lines);
		area.setEditable(false);
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		
		JScrollPane scrollPane = new JScrollPane(area, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		return new STD(scrollPane, area);
	}
	
	/** A wrapper class for storing a scrolling text display consisting of a JScrollPane and associated JTextArea */
	public static class STD {
		public JScrollPane _jsp;
		public JTextArea _jta;
		
		public STD(JScrollPane jsp, JTextArea jta) {
			_jsp = jsp;
			_jta = jta;
		}
		
		public STD() { }
	}
}