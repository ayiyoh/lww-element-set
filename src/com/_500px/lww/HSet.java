package com._500px.lww;

import java.util.*;

/**
 * A class implements the base set for LWW element set. 
 * @author Yuefei Zhu
 * @since 2015-09-04
 * @param E The type of elements maintained by the CRDTSet
 * @param T The type of timestamp of the element
 */

public class HSet<E, T extends Comparable<T>> implements GrowOnlySet<E, T> {

	private Map<E, T> data;
	
	public HSet() {
		data = new HashMap<E, T>();
	}

	public HSet(Map<E, T> data) {
		this.data = data;
	}

	public Map<E, T> getData() {
		return data;
	}

	public void setData(Map<E, T> data) {
		this.data = data;
	} 
	
	/**
	 *	Add an element with a specified timestamp. 
	 *  
	 *  If there exists the same element in the underlying 
	 *  hash map, it just updates the timestamp if the timestamp 
	 *  passed in is more recent.
	 *  
	 *  If there is no existing element in the hash map, it
	 *  simply puts it into the hash map.
	 *  
	 * @param element       an element to be added into the set
	 * @param timestamp     the timestamp 
	 */
	@Override
	public void add(E element, T timestamp) {
		// if there is the same element
		if (data.containsKey(element)) {
			T existingTimestamp = data.get(element);
			// we only add the element with more recent element
			if (existingTimestamp.compareTo(timestamp) < 0) {
				data.put(element, timestamp);
			}
		}
		else {
			data.put(element, timestamp);
		}

	}
	
	@Override
	public boolean exists(E element) {
		return data.containsKey(element);
	}

	@Override
	public T getTimestampForElement(E element) {
		if (exists(element))
		{
			return data.get(element);
		}
		return null;
	}

	@Override
	public ArrayList<E> get() {
		return new ArrayList<E>(data.keySet());
	}

	@Override
	public Iterator<E> iterator() {
		Iterator<E> iter = new Iterator<E> () {

			Iterator<Map.Entry<E, T>> mapiter = data.entrySet().iterator();

			@Override
			public boolean hasNext() {
				return mapiter.hasNext();
			}

			@Override
			public E next() {
				Map.Entry<E, T> entry = mapiter.next();
				return entry.getKey();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Invalid operation for grow only set iterator.");
			}
		};
		return iter;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (E element : data.keySet())
		{
			T timestamp = data.get(element);
			sb.append("val: ")
			  .append(element)
			  .append(" timestamp: ")
			  .append(timestamp)
			  .append("\n");
		}
		return sb.toString();
	}

}