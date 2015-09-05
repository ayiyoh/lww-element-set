package com._500px.lww;

import java.util.*;

/**
 * A class implements the base set for LWW element set. 
 * @author Yuefei Zhu
 * @version 0.1.0
 * @since 2015-09-04
 * @param <E>
 */

public class HSet<E> implements GrowOnlySet<E> {

	private Map<E, Long> data;
	
	public HSet() {
		data = new HashMap<E, Long>();
	}

	public HSet(Map<E, Long> data) {
		this.data = data;
	}

	public Map<E, Long> getData() {
		return data;
	}

	public void setData(Map<E, Long> data) {
		this.data = data;
	} 
	
	/**
	 * Add an element with the current timestamp in the system.
	 * 
	 * @param element an element to be added into the set
	 */

	@Override
	public void add(E element) {
		add(element, System.currentTimeMillis());
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
	 * @param timestamp     the UNIX timestamp in long 
	 */
	@Override
	public void add(E element, long timestamp) {
		// if there is the same element
		if (data.containsKey(element)) {
			long existingTimestamp = data.get(element);
			// we only add the element with more recent element
			if (existingTimestamp < timestamp) {
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
	public long getTimestampForElement(E element) {
		if (exists(element))
		{
			return data.get(element);
		}
		return -1l;
	}

	@Override
	public ArrayList<E> get() {
		return null;
	}

	@Override
	public Iterator<E> iterator() {
		Iterator<E> iter = new Iterator<E> () {

			Iterator<Map.Entry<E, Long>> mapiter = data.entrySet().iterator();

			@Override
			public boolean hasNext() {
				return mapiter.hasNext();
			}

			@Override
			public E next() {
				Map.Entry<E, Long> entry = mapiter.next();
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
			long timestamp = data.get(element);
			sb.append("val: ")
			  .append(element)
			  .append(" timestamp: ")
			  .append(timestamp)
			  .append("\n");
		}
		return sb.toString();
	}

}