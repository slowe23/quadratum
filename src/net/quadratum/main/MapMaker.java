package net.quadratum.main;

import java.io.*;
import java.util.Random;

/**
 * Creates a random map given certain constraints.
 */
public class MapMaker {
	
	public static char delimiter = '|';
	public static final char[] H = {'h', 'e', 'i', 'g', 'h', 't'};
	
	
	public static String generateMap(
			String fileName, int width, int height, int players, boolean water,
			boolean bunkers, boolean mountains, boolean resources,
			double[] frequencies)
	{
		
		PrintWriter out;
		try {
			out = new PrintWriter(new FileWriter(fileName));
			
			Random rng = new Random();
			
			// height|y
			StringBuilder sb = new StringBuilder();
			sb.append("height");
			sb.append(delimiter);
			out.println(sb.toString());
			//  width|x
			//players|n
			//  start|coord pairs
			//  water|coord pairs
			//bunkers|
			//mountains|
			//resources
			
			
		} catch (IOException e) {
			
		}
		
		return null;
	}

}
///*
///** Constant representing land. 
//public static final int LAND = 0;
///** Constant representing water. */
//public static final int WATER = 1;
///** Constant representing a bunker overlay. */
//public static final int BUNKER = 2;
///** Constant representing mountains. */
//public static final int MOUNTAIN = 4;
///** Constant representing a resource-gathering location. */
//public static final int RESOURCES = 8;
//*/