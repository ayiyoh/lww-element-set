package com._500px.lww;

import java.util.*;
/**
 * A class implements last writer wins (LWW) element set CRDT
 * 
 * @author Yuefei Zhu
 * 
 * @param E The type of elements maintained by the CRDTSet
 * @param T The type of timestamp of the element
 */

public class LWWSet<E, T extends Comparable<T>> implements CRDTSet<E, T> {
	
	public enum BiaseType {ADD, REMOVE}
	
	private GrowOnlySet<E, T> addSet;
	private GrowOnlySet<E, T> removeSet;
	
	private final BiaseType biaseType;
	
	public LWWSet() {
		this(BiaseType.ADD);
	}

	public LWWSet(BiaseType biaseType) {
		addSet = new HSet<E, T>();
		removeSet = new HSet<E, T>();
		this.biaseType = biaseType;
	}
	
	public LWWSet(BiaseType biaseType, GrowOnlySet<E, T> addSet, GrowOnlySet<E, T> removeSet)
	{
		this.biaseType = biaseType;
		this.addSet = addSet;
		this.removeSet = removeSet;
	}

	public GrowOnlySet<E, T> getAddSet() {
		return addSet;
	}
	public void setAddSet(GrowOnlySet<E, T> addSet) {
		this.addSet = addSet;
	}
	public GrowOnlySet<E, T> getRemoveSet() {
		return removeSet;
	}
	public void setRemoveSet(GrowOnlySet<E, T> removeSet) {
		this.removeSet = removeSet;
	}

	@Override
	public void add(E element, T timestamp) {
		addSet.add(element, timestamp);
	}
	
	@Override
	public void remove(E element, T timestamp)
	{
		removeSet.add(element, timestamp);
	}

	/**
	 * Checks if the element exists in the LWW set.
	 * 
	 * Returns true if:
	 * 1. The element is in the add set but not in the remove set.
	 * 2. The element is both in the add set and remove set, but has 
	 *    a larger timestamp. 
	 * 3. The biase type is ADD and the element is both in the add set and remove set, with the 
	 *    same timestamp.
	 * 
	 * @param element The interested element
	 */
	
	@Override
	public boolean exists(E element) {
		T addTime = addSet.getTimestampForElement(element);
		T removeTime = removeSet.getTimestampForElement(element);
		// element does not exist in add set
		if (!addSet.exists(element))
		{
			return false;
		}
		else {
			// exists in add set but not in remove set
			if (!removeSet.exists(element))
				return true;
		}
		if (biaseType == BiaseType.ADD) {
			if (removeTime.compareTo(addTime) > 0) {
				return false;
			} 
			else {
				return true;
			}
		}
		else {
			if (addTime.compareTo(removeTime) > 0)
			{
				return true;
			}
			else {
				return false;
			}
		}	
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
			T addTimestamp = addSet.getTimestampForElement(element);
			if (removeSet.exists(element))
			{
				T removeTime = removeSet.getTimestampForElement(element);
				if (removeTime.compareTo(addTimestamp) < 0 || (removeTime.compareTo(addTimestamp) == 0 && biaseType == BiaseType.ADD))
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