package net.quadratum.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializer {
	
	/**
	 * Gets a byte array from an object.
	 * @param <T> the type of the object, must extend Serializable
	 * @param t the object to get the byte array for
	 * @return a byte array representing this object.
	 */
	public static <T extends Serializable> byte[] getByteArray(T t) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objstream = new ObjectOutputStream(stream);
			objstream.writeObject(t);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream.toByteArray();
	}
	
	/**
	 * Gets an object from a byte array.
	 * @param <T> the type of the object, must extend Serializable.
	 * @param b the byte array to get an object from
	 * @return the object represented by the given byte array.
	 */
	public static <T extends Serializable> T getObject(byte[] b) {
		ByteArrayInputStream stream = new ByteArrayInputStream(b);
		T t = null;
		try {
			ObjectInputStream objstream = new ObjectInputStream(stream);
			t = (T)objstream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return t;
	}
}
