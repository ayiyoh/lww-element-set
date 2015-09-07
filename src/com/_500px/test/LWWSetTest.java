package com._500px.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

import com._500px.lww.*;

public class LWWSetTest {

	@Before
	public void init() {
		Jedis jedis = JedisFactory.getInstance().getJedisPool().getResource();
		RedisZSet.setJedis(jedis);
	}

	@After
	public void clean() {
		JedisFactory.getInstance().returnResource(RedisZSet.getJedis());
	}

	@Test
	public void testLWWSet() {
		LWWSet<Integer, Long> set = new LWWSet<Integer, Long>();
		assertNotNull(set);
	}

	@Test
	public void testAdd() {
		LWWSet<Integer, Long> set = new LWWSet<Integer, Long>();
		assertFalse(set.exists(2));
		set.add(2, 20l);
		assertTrue(set.exists(2));
	}

	@Test
	public void testRemove() {
		LWWSet<Integer, Long> set = new LWWSet<Integer, Long>();
		assertFalse(set.exists(2));
		set.remove(1, 3l);
		assertFalse(set.exists(1));
		set.add(2, 20l);
		set.remove(2, 21l);
		assertFalse(set.exists(2));
		set.add(3, 20l);
		set.remove(3, 18l);
		assertTrue(set.exists(3));
		set.add(4, 20l);
		set.remove(4, 20l);
		assertTrue(set.exists(4));
		LWWSet<Integer, Long> newSet = new LWWSet<Integer, Long>(LWWSet.BiaseType.REMOVE);
		newSet.add(4, 20l);
		newSet.remove(4, 20l);
		assertFalse(newSet.exists(4));
		LWWSet<Integer, Double> newSet2 = new LWWSet<Integer, Double>(LWWSet.BiaseType.ADD, 
				new RedisZSet<Integer, Double>("addForRemoveTest", Integer.class), new RedisZSet<Integer, Double>("removeForremoveTest", Integer.class));
		newSet2.add(5, 18d);
		newSet2.add(5, 8d);
		assertTrue(newSet2.exists(5));
		newSet2.remove(5, 20d);
		assertFalse(newSet2.exists(5));
	}

	@Test
	public void testExists() {
		LWWSet<Integer, Long> set = new LWWSet<Integer, Long>();
		set.add(2, 20l);
		assertTrue(set.exists(2));
		LWWSet<Integer, Double> newSet = new LWWSet<Integer, Double>(LWWSet.BiaseType.ADD, 
				new RedisZSet<Integer, Double>("add", Integer.class), new RedisZSet<Integer, Double>("remove", Integer.class));
		newSet.add(2, 20d);
		assertTrue(newSet.exists(2));
	}

	@Test
	public void testGet() {
		LWWSet<Integer, Long> set = new LWWSet<Integer, Long>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		assertArrayEquals(list.toArray(), set.get().toArray());
		set.add(10, 10l);
		list.add(10);
		assertArrayEquals(list.toArray(), set.get().toArray());
		set.add(11, 11l);
		list.add(11);
		assertArrayEquals(list.toArray(), set.get().toArray());
		set.add(12, 12l);
		list.add(12);
		assertArrayEquals(list.toArray(), set.get().toArray());
		set.remove(13, 12l);
		assertArrayEquals(list.toArray(), set.get().toArray());
		// remove 12
		set.remove(12, 13l);
		list.remove(2);
		Collections.sort(list);
		ArrayList<Integer> listFromHSet = set.get();
		Collections.sort(listFromHSet);
		assertArrayEquals(list.toArray(), listFromHSet.toArray());

		LWWSet<Integer, Double> newSet = new LWWSet<Integer, Double>(LWWSet.BiaseType.ADD, 
				new RedisZSet<Integer, Double>("addJedisGet5", Integer.class), 
				new RedisZSet<Integer, Double>("removeJedisGet5", Integer.class));

		ArrayList<Integer> list2 = new ArrayList<Integer>();
		newSet.add(10, 10d);
		newSet.add(12, 10d);
		newSet.add(13, 10d);
		newSet.remove(15, 10d);
		list2.add(10);
		list2.add(12);
		list2.add(13);
		Collections.sort(list2);
		ArrayList<Integer> listFromZSet = newSet.get();
		Collections.sort(listFromZSet);
		assertArrayEquals(list2.toArray(), listFromZSet.toArray());
	}

	
}
