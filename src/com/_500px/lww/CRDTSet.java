package com._500px.lww;

/**
 * An interface extends CRDT with remove methods added.
 * 
 * @author Yuefei Zhu
 *
 * @param <E>
 */
public interface CRDTSet<E, T extends Comparable<T>> extends CRDT<E, T> {
	public void remove(E element, T timestamp);
}
