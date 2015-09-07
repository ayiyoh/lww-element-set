package com._500px.lww;

import java.util.*;
import java.lang.reflect.*;

import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import static redis.clients.jedis.ScanParams.SCAN_POINTER_START;

public class RedisZSet<E, T extends Comparable<T>> implements GrowOnlySet<E, T> {
	
	private static Jedis jedis;
	private static JedisPool jedisPool;
	private String type;
	private long count;
	private Class<E> dataClass;
	
	public RedisZSet(String type, Class<E> dataClass) {
		init(type, dataClass);
	}
	
	public void init(String type, Class<E> dataClass) {
		this.type = type;
		this.dataClass = dataClass;
		if (jedisPool == null)
		{
			try {
				jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
				jedis = jedisPool.getResource();
				count = jedis.zcount(type, Double.MIN_VALUE, Double.MAX_VALUE);
			} catch (JedisConnectionException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param element --- the element to be added.
	 * @param timestamp --- timestamp of the element.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void add(E element, T timestamp) {
		if (exists(element)) {
			T existingTimestamp = (T) jedis.zscore(type, String.valueOf(element));
			if (existingTimestamp.compareTo(timestamp) < 0) {
				// update score only
				jedis.zadd(type, (double) timestamp, String.valueOf(element));
			}
		}
		else {
			jedis.zadd(type, (double) timestamp, String.valueOf(element));
		}
		count = jedis.zcount(type, Double.MIN_VALUE, Double.MAX_VALUE);
		
	}

	@Override
	public boolean exists(E element) {
		// TODO: hack hack --- used zscore to see if the value exists
		return jedis.exists(type) && (jedis.zscore(type, String.valueOf(element)) != null);
	}

	/**
	 * Uses Redis's zscan to get all the results in the set.
	 * 
	 * <p>Because the values returned are strings, to support other data types (int, double, etc.),
	 * the class of E is used to dynamically cast the string --- using Method.invoke(Object obj, Object[] args) </p>   
	 * 
	 * @return ArrayList<E> --- An array list of elements in the set.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ArrayList<E> get() {
		List<Tuple> results = jedis.zscan(type, SCAN_POINTER_START).getResult();
		ArrayList<E> list = new ArrayList<E>();
		System.out.println("RedisZSet get() --- results size: " + results.size());
		for (Tuple tuple : results)
		{
			String elementAsString = tuple.getElement();
			System.out.println("RedisZSet get() --- element as string " + elementAsString);
			Method method = null;
			E element = null;
			try {
				// This is a big hack. dataClass is used for casting from 
				// String to E
				method = dataClass.getMethod("valueOf", String.class);
				element = (E) method.invoke(null, elementAsString);
				list.add(element);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	/**
	 * A iterator to iterate the elements.
	 * 
	 * <p>Uses redis's zrange to get the next element, by increasing the current position index.</p>
	 * 
	 * Casting using the the Method.invoke(Object obj, Object[] args) is used to support other data types.
	 * 
	 * @return Iterator<E>
	 */
	@Override
	public Iterator<E> iterator() {
		Iterator<E> iter = new Iterator<E> () {
			private long currentIndex = 0;

			@Override
			public boolean hasNext() {
				System.out.println("Count: " + count + " currentIndex: " + currentIndex);
				return count > 0 && currentIndex < count;
			}

			@SuppressWarnings("unchecked")
			@Override
			public E next() {
				Set<String> result = jedis.zrange(type, currentIndex, currentIndex);
				currentIndex++;
				E element = null;
				Method method = null;
				String elementAsString = result.iterator().next();
				try {
					// This is a big hack. dataClass is used for casting from 
					// String to E
					method = dataClass.getMethod("valueOf", String.class);
					element = (E) method.invoke(null, elementAsString);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return element;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Invalid operation for grow only set iterator.");
			}
		};
		return iter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getTimestampForElement(E element) {
		if (exists(element))
		{
			return (T) jedis.zscore(type, String.valueOf(element));
		}
		return null;
	}
	
		@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		for (E element : this)
		{
			T timestamp = this.getTimestampForElement(element);
			sb.append("val: ")
			  .append(element)
			  .append(" timestamp: ")
			  .append(timestamp)
			  .append("\n");
		}
		return sb.toString();
	}

}
