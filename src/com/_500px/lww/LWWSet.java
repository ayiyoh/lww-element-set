package com._500px.lww;

import java.util.*;
/**
 * A class implements last writer wins (LWW) element set CRDT
 * 
 * @author Yuefei Zhu
 *
 * @param <E>
 */

public class LWWSet<E> implements CRDTSet<E> {
	
	public enum BiaseType {ADD, REMOVE}
	
	private GrowOnlySet<E> addSet;
	private GrowOnlySet<E> removeSet;
	
	private final BiaseType biaseType;
	
	public LWWSet() {
		this(BiaseType.ADD);
	}

	public LWWSet(BiaseType biaseType) {
		addSet = new HSet<E>();
		removeSet = new HSet<E>();
		this.biaseType = biaseType;
	}

	public GrowOnlySet<E> getAddSet() {
		return addSet;
	}
	public void setAddSet(GrowOnlySet<E> addSet) {
		this.addSet = addSet;
	}
	public GrowOnlySet<E> getRemoveSet() {
		return removeSet;
	}
	public void setRemoveSet(GrowOnlySet<E> removeSet) {
		this.removeSet = removeSet;
	}

	@Override
	public void add(E element) {
		addSet.add(element, System.currentTimeMillis());
	}
	
	@Override
	public void add(E element, long timestamp) {
		addSet.add(element, timestamp);
	}
	
	@Override 
	public void remove(E element) {
		removeSet.add(element, System.currentTimeMillis());
	}
	
	@Override
	public void remove(E element, long timestamp)
	{
		removeSet.add(element, timestamp);
	}

	/**
	 * Checks if the element exists in the LWW set.
	 * 
	 * @param element
	 */
	
	@Override
	public boolean exists(E element) {
		long [] typedTimestamp = getTypedTimestampForElement(element);
		if (typedTimestamp[0] == 1 && typedTimestamp[1] != -1l)
			return true;
		return false;
	}
	
	/**
	 * Returns the more recent timestamp for the element, either 
	 * in the add set or remove set
	 */
	@Override
	public long getTimestampForElement(E element) {
		return getTypedTimestampForElement(element)[1];
	}
	
	/**
	 * This is a helper function which returns an array of longs, in which 
	 * [1, t] (t != -1) means the element with timestamp t either
	 * exists in the add set only or the element exists in both sets
	 * but the one in the add set is more recent, and [-1, t] (t != -1)
	 * means the element with timestamp t either exists in the remove set only
	 * or the one in the remove set is more recent.
	 * <p>
	 * When t = -1, it means the element doesn't exist in both of the sets.
	 * <p>
	 * The phrase "more recent" when the timestamps in the add set and remove 
	 * set are equal for the same element has different meanings for different 
	 * biase type:
	 * <br>
	 * 1. When biase type is ADD, the element in the add set is taken 
	 * 2. When biase type is REMOVE, the element in the remove set is taken
	 * 
	 * @param element 
	 */
	private long[] getTypedTimestampForElement(E element)
	{
		long[] typedTimestamp = new long[2];
		long addTime = addSet.getTimestampForElement(element);
		long removeTime = removeSet.getTimestampForElement(element);
		typedTimestamp[1] = -1l;
		if (biaseType == BiaseType.ADD) {
			if (removeTime > addTime) {
				typedTimestamp[0] = -1l;
				typedTimestamp[1] = removeTime;
			} 
			else {
				typedTimestamp[0] = 1l;
				typedTimestamp[1] = addTime;
			}
		}
		else {
			if (addTime > removeTime)
			{
				typedTimestamp[0] = 1l;
				typedTimestamp[1] = addTime;
			}
			else {
				typedTimestamp[0] = -1l;
				typedTimestamp[1] = removeTime;
			}
		}	
		return typedTimestamp;
	}

	/**
	 * This function returns all 'live' elements as an ArrayList,
	 * i.e., the elements in add set without also being in the 
	 * remove set, or where timestamp for the element in the add set 
	 * is more recent than the timestamp for the same element in the 
	 * remove set 
	 * 
	 */
	@Override
	public ArrayList<E> get() {

		ArrayList<E> result = new ArrayList<E>(); 

		for (E element : addSet)
		{
			long addTimestamp = addSet.getTimestampForElement(element);
			if (removeSet.exists(element))
			{
				long removeTime = removeSet.getTimestampForElement(element);
				if (removeTime < addTimestamp || (removeTime == addTimestamp && biaseType == BiaseType.ADD))
					result.add(element);
			}
			else {
				result.add(element);
			}
			
		}
		return result;
	}

	public BiaseType getBiaseType() {
		return biaseType;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Add set: \n");
		sb.append(addSet.toString());
		sb.append("Remove set: \n");
		sb.append(removeSet.toString());
		return sb.toString();
	}

}