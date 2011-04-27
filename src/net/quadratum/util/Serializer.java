package net.quadratum.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Serializer {
	
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
