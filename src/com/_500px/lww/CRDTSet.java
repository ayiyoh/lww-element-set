package com._500px.lww;

/**
 * An interface extends CRDT with remove methods added.
 * 
 * @author Yuefei Zhu
 *
 * @param <E>
 */
public interface CRDTSet<E> extends CRDT<E> {
	public void remove(E element, long timestamp);
	public void remove(E element);
}
