package com._500px.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.Test;

import com._500px.lww.RedisZSet;

public class RedisZSetTest {

	@Test
	public void testRedisZSet() {
		RedisZSet<Integer, Double> zset = new RedisZSet<Integer, Double>("add", Integer.class);
	}

	@Test
	public void testAdd() {
		RedisZSet<Integer, Double> zset = new RedisZSet<Integer, Double>("add", Integer.class);
		zset.add(20, 45d);
		zset.add(20, 40d);
		assertTrue(zset.exists(20));
		assertEquals(Double.valueOf(45d), zset.getTimestampForElement(20));
	}

	@Test
	public void testExists() {
		RedisZSet<Integer, Double> zset = new RedisZSet<Integer, Double>("add", Integer.class);
		zset.add(20, 45d);
		assertTrue(zset.exists(20));
	}

	@Test
	public void testGet() {
		RedisZSet<Integer, Double> zset = new RedisZSet<Integer, Double>("addGet2", Integer.class);
		ArrayList<Integer> list = new ArrayList<Integer>();
		zset.add(20, 4d);
		list.add(20);
		zset.add(22, 5d);
		list.add(22);
		zset.add(24, 6d);
		list.add(24);
		Collections.sort(list);
		ArrayList<Integer> arrayListResult = zset.get();
		Collections.sort(arrayListResult);
		assertArrayEquals(list.toArray(), arrayListResult.toArray());
	}

	@Test
	public void testGetTimestampForElement() {
		RedisZSet<Integer, Double> zset = new RedisZSet<Integer, Double>("addGetT2", Integer.class);
		zset.add(20, 5d);
		assertEquals(Double.valueOf(5d), zset.getTimestampForElement(20));
		zset.add(20, 4d);
		assertEquals(Double.valueOf(5d), zset.getTimestampForElement(20));
		zset.add(20, 8d);
		assertEquals(Double.valueOf(8d), zset.getTimestampForElement(20));
	}

}
