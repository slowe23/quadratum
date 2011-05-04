package net.quadratum.gui;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

//Static convenience methods
public class StaticMethods {

	/**
	 * Rounds a double to the nearest int value.
	 * @param d a double to round
	 * @return an int which is closest to the given double
	 */
	public static int round(double d) {
		return longToInt(Math.round(d));
	}
	
	/**
	 * Converts a long to an int, capping it if it excedes the minimum or 
	 * maximum int values as if it were using saturation arithmetic.
	 * @param l a long to convert
	 * @return an int which is either the same value as or the closest value 
	 * to the given long.
	 */
	public static int longToInt(long l) {
		if(l<Integer.MIN_VALUE)
			return Integer.MIN_VALUE;
		
		if(l>Integer.MAX_VALUE)
			return Integer.MAX_VALUE;
		
		return (int)l;
	}
	
	/**
	 * Creates a new scrolling text area.
	 * @return a JScrollPane which wraps a new JTextArea.
	 */
	public static JScrollPane createScrollingTextDisplay() {
		return createScrollingTextDisplay(new JTextArea());
	}
	
	/**
	 * Creates a new scrolling pane which wraps the given text area.
	 * @param textArea a text area to wrap
	 * @return a JScrollPane which wraps the given JTextAtea.
	 */
	public static JScrollPane createScrollingTextDisplay(JTextArea textArea) {
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		
		JScrollPane scrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		return scrollPane;
	}
	
	/**
	 * Creates a Color with the RGB values defined by the given Color and the
	 * alpha value given.
	 * @param c the Color which represents the RGB values
	 * @param alpha an alpha value to apply to the Color
	 * @return a new Color with the given RGBA values.
	 */
	public static Color applyAlpha(Color c, int alpha) {
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
	}
	
	/**
	 * Gets a titled border.
	 * @param title the Title of the titled border
	 * @return a new titled border.
	 */
	public static Border getTitleBorder(String title) {
		return BorderFactory.createTitledBorder(null, title, TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION);
	}
}