package com._500px.lww;

/**
 * An interface extends CRDT with remove methods added.
 * 
 * @author Yuefei Zhu
 *
 * @param E The type of elements maintained by the CRDTSet
 * @param T The type of timestamp of the element
 */
public interface CRDTSet<E, T extends Comparable<T>> extends CRDT<E, T> {
	public void remove(E element, T timestamp);
}
