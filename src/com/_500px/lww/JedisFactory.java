package com._500px.lww;
/**
 * A jedis factory maintaining the Jedis resources.
 * 
 * @author Yuefei Zhu
 */
import redis.clients.jedis.*;

public class JedisFactory {
	private static JedisPool jedisPool;
	private static JedisFactory instance = null;
	
	public JedisFactory() {
	    jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
	}
	
	public JedisPool getJedisPool() {
		return jedisPool;
	}
	
	public static JedisFactory getInstance() {
		if (instance == null) {
			instance = new JedisFactory();
		}
		return instance;
	}
	
	public Jedis getResource() {
		return jedisPool.getResource();
	}
	
	public void returnResource(Jedis jedis) {
		jedis.close();
	}
	
	public void destroyJedisPool() {
		if (jedisPool != null) {
			jedisPool.destroy();
		}
	}
}
