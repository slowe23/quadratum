package net.quadratum.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Serializer {
	
	static BASE64Encoder _enc = new BASE64Encoder();
	static BASE64Decoder _dec = new BASE64Decoder();
	
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
	 * Gets a Base64 encoded string from an object.
	 * @param <T> the type of the object, must extend Serializable
	 * @param t the object to get an encoded string for
	 * @return a string representing this object.
	 */
	public static <T extends Serializable> String getEncodedString(T t) {
		return _enc.encode(getByteArray(t));
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
	
	/**
	 * Gets an object from a Base64 encoded string.
	 * @param <T> the type of the object, must extend Serializable.
	 * @param s the string to get an object from
	 * @return the object represented by the given string.
	 */
	public static <T extends Serializable> T getObject(String s) {
		try {
			return (T)getObject(_dec.decodeBuffer(s));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
