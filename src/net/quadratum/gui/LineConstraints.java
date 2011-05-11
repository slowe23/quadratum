package net.quadratum.gui;

public class LineConstraints {
	public double weight;  //Weight relative to the other components
	public int index;  //Position from start
	
	public LineConstraints() {
		this(1.0, -1);  //Append to the end of the list, weight 1
	}
	
	public LineConstraints(double w) {
		this(w, -1);
	}
	
	public LineConstraints(double w, int i) {
		weight = w;
		index = i;
	}
}