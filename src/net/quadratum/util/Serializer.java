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
	
	// TODO use better base64 stuff
	/** Base64 encoder. */
	static BASE64Encoder _enc = new BASE64Encoder();
	/** Base64 decoder. */
	static BASE64Decoder _dec = new BASE64Decoder();
	
	/**
	 * Gets a byte array from an object.
	 * @param <T> the type of the object, must extend Serializable
	 * @param t the object to get the byte array for
	 * @return a byte array representing this object.
	 */
	public static synchronized <T extends Serializable> byte[] getByteArray(T t) {
		if (t == null) {
			return new byte[] {0};
		}
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
	public static synchronized <T extends Serializable> String getEncodedString(T t) {
		// XXX hacky bullshit with replaceall
		return _enc.encode(getByteArray(t)).replaceAll("(\r)?\n",""); 
	}
	
	/**
	 * Gets an object from a byte array.
	 * @param <T> the type of the object, must extend Serializable.
	 * @param b the byte array to get an object from
	 * @return the object represented by the given byte array.
	 */
	public static synchronized <T extends Serializable> T getObject(byte[] b) {
		if (b.length == 1 && b[0] == 0) {
			return null;
		}
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
	public static synchronized <T extends Serializable> T getObject(String s) {
		try {
			return Serializer.<T>getObject(_dec.decodeBuffer(s));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
