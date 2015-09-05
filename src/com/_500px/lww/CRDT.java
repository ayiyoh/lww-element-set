package com._500px.lww;
import java.util.ArrayList;
/**
 * A base interface for the project. 
 * 
 * @author Yuefei Zhu
 *
 * @param <E>
 */
public interface CRDT<E> {
	public void add(E element, long timestamp);
	public void add(E element);
	public boolean exists(E element);
	public long getTimestampForElement(E element);
	public ArrayList<E> get();
}