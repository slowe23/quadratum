package net.quadratum.gui.test;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.quadratum.gui.LineConstraints;
import net.quadratum.gui.LineLayout;

//A simple visual test of the LineLayout class
public class LineLayoutTest {
	public static void main(String[] args) {
		/*
		 * The following code should produce a window displaying a red, orange, and yellow panel (in that order from left to right)
		 * The red panel should be half as wide as the orange panel, which should be half as wide as the yellow panel
		 * The panels's borders should be touching at the edges but not overlapping.
		 */
		
		JFrame frame = new JFrame();
		frame.setSize(640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		
		JPanel cont = new JPanel();
		cont.setLayout(new LineLayout(LineLayout.LEFT_TO_RIGHT));
		
		JPanel p = new JPanel();
		p.setBackground(Color.ORANGE);
		p.setBorder(BorderFactory.createLineBorder(Color.gray));
		cont.add(p);
		
		p = new JPanel();
		p.setBackground(Color.YELLOW);
		p.setBorder(BorderFactory.createLineBorder(Color.black));
		LineConstraints lC = new LineConstraints(2.0);
		cont.add(p, lC);
		
		p = new JPanel();
		p.setBackground(Color.RED);
		p.setBorder(BorderFactory.createLineBorder(Color.black));
		lC = new LineConstraints(0.5, 0);
		cont.add(p, lC);
		
		cont.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 20));
		
		frame.setContentPane(cont);
		
		frame.setVisible(true);
	}
}