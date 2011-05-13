package net.quadratum.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

/** A simple layoutmanager that stretches all elements in a container to the size of the container.  Can be used for a layered pane */
public class FillLayout implements LayoutManager {
	private boolean _includeInvisible;  //Whether currently invisible components are counted when calculating min or preferred size
	
	public FillLayout() {
		this(false);
	}
	
	public FillLayout(boolean include) {
		_includeInvisible = include;
	}
	
	public void addLayoutComponent(String name, Component comp) { }
	public void removeLayoutComponent(Component comp) { }
	
	public Dimension minimumLayoutSize(Container parent) {
		Dimension min = new Dimension(0, 0);
		
		int compCount = parent.getComponentCount();
		for(int i = 0; i<compCount; i++) {
			Component comp = parent.getComponent(i);
			if(comp.isVisible() || _includeInvisible)
				min = getMaxDimension(min, comp.getMinimumSize());
		}
		
		Insets insects = parent.getInsets();
		return new Dimension(min.width+insects.left+insects.right, min.height+insects.top+insects.bottom);
	}
	
	public Dimension preferredLayoutSize(Container parent) {
		Dimension min = new Dimension(0, 0);
		
		int compCount = parent.getComponentCount();
		for(int i = 0; i<compCount; i++) {
			Component comp = parent.getComponent(i);
			if(comp.isVisible() || _includeInvisible)
				min = getMaxDimension(min, comp.getPreferredSize());
		}
		
		Insets insects = parent.getInsets();
		return new Dimension(min.width+insects.left+insects.right, min.height+insects.top+insects.bottom);
	}
	
	private static Dimension getMaxDimension(Dimension d1, Dimension d2) {
		return new Dimension(Math.max(d1.width, d2.width), Math.max(d1.height, d2.height));
	}

	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		Dimension size = parent.getSize();
		int minx = insets.left, miny = insets.top, maxx = size.width-insets.right, maxy = size.height-insets.bottom;
		int width = maxx-minx, height = maxy-miny;
		width = Math.max(width, 0);
		height = Math.max(height, 0);
		
		int compCount = parent.getComponentCount();
		for(int i = 0; i<compCount; i++) {
			Component comp = parent.getComponent(i);
			if(comp.isVisible())
				comp.setBounds(minx, miny, width, height);
		}
	}
}