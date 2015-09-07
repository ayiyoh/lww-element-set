package com._500px.test;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import static org.junit.Assert.*;

public class JedisTest {

	@Test
	public void testJedis() {
		// TODO Auto-generated method stub
		String key = "Key1";
		Jedis jedis = new Jedis("localhost");
		// Add a value with a score to the set
		jedis.zadd(key, 10, "hello");

		// get the score:
		assertEquals(Double.valueOf(10.0), jedis.zscore(key, "hello"));
		assertEquals(Double.valueOf(45.0), jedis.zscore("add", "20"));
	}

}
