package com._500px.test;

import java.util.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com._500px.lww.HSet;

public class HSetTest {

	/**
	 * We only test integers and strings here.
	 */
	@Test
	public void testAdd() {
		HSet<Integer> iset = new HSet<Integer>();
		assertTrue(iset.getData().isEmpty());
		iset.add(20, 12l);
		assertEquals(1, iset.getData().size());
		// add the same element with older timestamp
		iset.add(20, 10l);
		assertEquals(1, iset.getData().size());
		// add the same element with newer timestamp
		iset.add(20, 14l);
		assertEquals(1, iset.getData().size());
		// add a different element
		iset.add(30, 14l);
		assertEquals(2, iset.getData().size());
		HSet<String> sset = new HSet<String>();
		assertTrue(sset.getData().isEmpty());
		sset.add("Hello", 12l);
		assertEquals(1, sset.getData().size());
		// add the same element with older timestamp
		sset.add("Hello", 10l);
		assertEquals(1, sset.getData().size());
		// add the same element with newer timestamp
		sset.add("Hello", 14l);
		assertEquals(1, sset.getData().size());
		// add a different element
		sset.add("World", 14l);
		assertEquals(2, sset.getData().size());
	}

	@Test
	public void testExists() {
		HSet<Integer> set = new HSet<Integer>();
		assertFalse(set.exists(10));
		set.add(10);
		assertTrue(set.exists(10));
		assertFalse(set.exists(11));
	}

	@Test
	public void testGetTimestampForElement() {
		HSet<Integer> set = new HSet<Integer>();
		assertEquals(-1l, set.getTimestampForElement(10));
		set.add(10, 10l);
		assertEquals(10l, set.getTimestampForElement(10));
		// add the same element with an older time stamp
		set.add(10, 8l);
		assertEquals(10l, set.getTimestampForElement(10));
		// add the same element with a newer time stamp
		set.add(10, 12l);
		assertEquals(12l, set.getTimestampForElement(10));
	}

	@Test
	public void testGet() {
		HSet<Integer> set = new HSet<Integer>();
		ArrayList<Integer> list = new ArrayList<Integer>();
		assertArrayEquals(list.toArray(), set.get().toArray());
		set.add(10, 10l);
		list.add(10);
		assertArrayEquals(list.toArray(), set.get().toArray());
		set.add(10, 8l);
		assertArrayEquals(list.toArray(), set.get().toArray());
		set.add(10, 12l);
		assertArrayEquals(list.toArray(), set.get().toArray());
		set.add(11, 12l);
		list.add(11);
		assertArrayEquals(list.toArray(), set.get().toArray());
	}

}
