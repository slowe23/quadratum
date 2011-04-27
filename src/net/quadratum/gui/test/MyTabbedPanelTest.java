package net.quadratum.gui.test;

import net.quadratum.gui.MyTabbedPanel;

import javax.swing.*;
import java.awt.*;

public class MyTabbedPanelTest {
	public static void main(String[] args) {
		JFrame jf = new JFrame();
		
		Container cont = jf.getContentPane();
		cont.setLayout(new BorderLayout());
		
		JPanel p = new JPanel();
		p.setBackground(Color.GRAY);
		cont.add(p, BorderLayout.CENTER);
		
		MyTabbedPanel tabPan = new MyTabbedPanel();

		p = new JPanel();
		p.setBackground(Color.GREEN);
		p.setPreferredSize(new Dimension(100, 100));
		tabPan.addTab("Green", p);
		
		p = new JPanel();
		p.setBackground(Color.WHITE);
		p.setPreferredSize(new Dimension(50, 50));
		tabPan.addTab("White", p, true);
		
		p = new JPanel();
		p.setBackground(Color.BLUE);
		p.setPreferredSize(new Dimension(200, 0));
		tabPan.addTab("Blue", p);
		
		cont.add(tabPan, BorderLayout.NORTH);
		
		jf.setSize(640, 480);
		jf.setResizable(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}
}