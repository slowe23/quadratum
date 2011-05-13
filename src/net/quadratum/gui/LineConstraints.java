package net.quadratum.gui;

/** Constraints for a LineLayout */
public class LineConstraints {
	/** Weight relative to the other components */
	public double weight;
	
	/** Position from start */
	public int index;
	
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