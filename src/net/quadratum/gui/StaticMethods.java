package net.quadratum.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.Color;

//Static convenience methods
public class StaticMethods {
	// Rounds a double to the nearest int value
	public static int round(double d) {
		return longToInt(Math.round(d));
	}
	
	//Converts a long to an int, capping it if it excedes the minimum or maximum int values
	public static int longToInt(long l) {
		if(l<Integer.MIN_VALUE)
			return Integer.MIN_VALUE;
		
		if(l>Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		
		return (int)l;
	}
	
	//Creates a scrolling text area
	public static JScrollPane createScrollingTextDisplay() {
		return createScrollingTextDisplay(new JTextArea());
	}
	
	//Creates a scrolling text area around the given JTextArea
	public static JScrollPane createScrollingTextDisplay(JTextArea textArea) {
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		
		JScrollPane scrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		return scrollPane;
	}
	
	//Creates a Color with the RGB values of the given color and the given alpha value (must be in the range 0-255)
	public static Color applyAlpha(Color c, int alpha) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}
	
	public static Border getTitleBorder(String title) {
		return BorderFactory.createTitledBorder(null, title, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
	}
}