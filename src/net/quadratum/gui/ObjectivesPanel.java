package net.quadratum.gui;

import javax.swing.*;

/** A simple class for displaying objectives in a panel */
public class ObjectivesPanel extends JPanel {
	private JTextArea _text;
	
	public ObjectivesPanel() {
		setBorder(StaticMethods.getTitleBorder("Game Objectives"));
		setLayout(new FillLayout());
		StaticMethods.STD objSTD = StaticMethods.createScrollingTextDisplay(5);
		_text = objSTD._jta;
		add(objSTD._jsp);
	}
	
	/** Sets the text to display */
	public void setText(String text) {
		_text.setText(text);
		_text.setCaretPosition(0);
		repaint();
	}
}