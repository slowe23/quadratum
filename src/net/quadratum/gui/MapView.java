package net.quadratum.gui;

public interface MapView {
	public double getViewX();
	public double getViewY();
	
	public double getViewWidth();
	public double getViewHeight();
	
	public void setViewPos(double x, double y);
}