package com._500px.lww;

/**
 * 
 * An interface for grow only set classes.
 * 
 * @author Yuefei Zhu
 * 
 * @param E The type of elements maintained by the CRDTSet
 * @param T The type of timestamp of the element
 * 
 */
public interface GrowOnlySet<E, T extends Comparable<T>> extends CRDT<E, T>, Iterable<E> {
	public T getTimestampForElement(E element);
}