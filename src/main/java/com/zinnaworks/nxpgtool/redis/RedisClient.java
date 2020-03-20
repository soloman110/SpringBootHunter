package com.zinnaworks.nxpgtool.redis;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

@Service
public class RedisClient implements RedisClientIf {

	@Autowired
	@Qualifier("primaryRedisTemplate")
	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	@Qualifier("secondRedisTemplate")
	private RedisTemplate<String, Object> secondRedisTemplate;
	
    @Value("${spring.redis.second.enalbed}")
    private boolean secondRedisEnabled;

	public Cursor<Entry<String, Object>> hscan(String key) {
		ScanOptions options = ScanOptions.scanOptions().count(1L).build();
		return redisTemplate.<String, Object>opsForHash().scan(key, options);
	}
	
    public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}
    
	public void set(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}
	
	public Long hlen(String key) {
		return redisTemplate.<String, Object>opsForHash().size(key);
	}
	
	public void hmset(String key, Map<String, Object> param) {
		redisTemplate.opsForHash().putAll(key, param);
	}
	
	public void hset(String key, String field, Object value) {
		redisTemplate.<String, Object>opsForHash().put(key, field, value);
	}

	public Object hget(String key, String field) {
		Object obj = null;
		obj = redisTemplate.<String, Object>opsForHash().get(key, field);
		return obj;

	}
	
	public Boolean expire(String key, Long timeout) {
		return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}
	
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}
	
	public Long hdel(String key, String field) {
		return redisTemplate.opsForHash().delete(key, field);
	}
	
	public void del(String key) {
		redisTemplate.delete(key);
	}
	
	public Long incr(String key) {
		return redisTemplate.opsForValue().increment(key, 1L);
	}

	public Long incrby(String key, Long value) {
		return redisTemplate.opsForValue().increment(key, value);
	}
}
