package com._500px.lww;
import java.util.ArrayList;
/**
 * A base interface for the project. 
 * 
 * @author Yuefei Zhu
 *  
 * @param E The type of elements maintained by the CRDTSet
 * @param T The type of timestamp of the element
 * 
 */
public interface CRDT<E, T extends Comparable<T>> {
	public void add(E element, T timestamp);
	public boolean exists(E element);
	public ArrayList<E> get();
}