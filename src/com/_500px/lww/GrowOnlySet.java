package com._500px.lww;

/**
 * 
 * 
 * @author Yuefei Zhu
 *
 * @param <E>
 */
public interface GrowOnlySet<E, T extends Comparable<T>> extends CRDT<E, T>, Iterable<E> {
	public T getTimestampForElement(E element);
}