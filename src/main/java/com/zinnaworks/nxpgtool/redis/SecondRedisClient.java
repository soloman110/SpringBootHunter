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
public class SecondRedisClient implements RedisClientIf {

	@Autowired
	@Qualifier("secondRedisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
	@Value("${spring.redis.second.enalbed}")
	private boolean useSecond;

	/* (non-Javadoc)
	 * @see com.skb.xpg.nxpg.redis.RedisClientIf#hscan(java.lang.String)
	 */
	@Override
	public Cursor<Entry<String, Object>> hscan(String key) {
		ScanOptions options = ScanOptions.scanOptions().count(1L).build();
		return redisTemplate.<String, Object>opsForHash().scan(key, options);
	}
	
    /* (non-Javadoc)
	 * @see com.skb.xpg.nxpg.redis.RedisClientIf#get(java.lang.String)
	 */
    @Override
	public Object get(String key) {
		return redisTemplate.opsForValue().get(key);
	}
    
	/* (non-Javadoc)
	 * @see com.skb.xpg.nxpg.redis.RedisClientIf#set(java.lang.String, java.lang.Object)
	 */
	@Override
	public void set(String key, Object value) {
		redisTemplate.opsForValue().set(key, value);
	}
	
	/* (non-Javadoc)
	 * @see com.skb.xpg.nxpg.redis.RedisClientIf#hlen(java.lang.String)
	 */
	@Override
	public Long hlen(String key) {
		return redisTemplate.<String, Object>opsForHash().size(key);
	}
	
	/* (non-Javadoc)
	 * @see com.skb.xpg.nxpg.redis.RedisClientIf#hmset(java.lang.String, java.util.Map)
	 */
	@Override
	public void hmset(String key, Map<String, Object> param) {
		redisTemplate.opsForHash().putAll(key, param);
		
	}
	
	/* (non-Javadoc)
	 * @see com.skb.xpg.nxpg.redis.RedisClientIf#hset(java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public void hset(String key, String field, Object value) {
		if (useSecond) {
			redisTemplate.<String, Object>opsForHash().put(key, field, value);
		}
	}

	/* (non-Javadoc)
	 * @see com.skb.xpg.nxpg.redis.RedisClientIf#hget(java.lang.String, java.lang.String)
	 */
	@Override
	public Object hget(String key, String field) {
		Object obj = null;
		obj = redisTemplate.<String, Object>opsForHash().get(key, field);
		return obj;

	}
	
	/* (non-Javadoc)
	 * @see com.skb.xpg.nxpg.redis.RedisClientIf#expire(java.lang.String, java.lang.Long)
	 */
	@Override
	public Boolean expire(String key, Long timeout) {
		return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
	}
	
	/* (non-Javadoc)
	 * @see com.skb.xpg.nxpg.redis.RedisClientIf#keys(java.lang.String)
	 */
	@Override
	public Set<String> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}
	
	/* (non-Javadoc)
	 * @see com.skb.xpg.nxpg.redis.RedisClientIf#hdel(java.lang.String, java.lang.String)
	 */
	@Override
	public Long hdel(String key, String field) {
		return redisTemplate.opsForHash().delete(key, field);
	}
	
	/* (non-Javadoc)
	 * @see com.skb.xpg.nxpg.redis.RedisClientIf#del(java.lang.String)
	 */
	@Override
	public void del(String key) {
		redisTemplate.delete(key);
	}

	/* (non-Javadoc)
	 * @see com.skb.xpg.nxpg.redis.RedisClientIf#incr(java.lang.String)
	 */
	@Override
	public Long incr(String key) {
		return redisTemplate.opsForValue().increment(key, 1L);
	}

	/* (non-Javadoc)
	 * @see com.skb.xpg.nxpg.redis.RedisClientIf#incrby(java.lang.String, java.lang.Long)
	 */
	@Override
	public Long incrby(String key, Long value) {
		return redisTemplate.opsForValue().increment(key, value);
	}
}
