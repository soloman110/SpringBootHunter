package com.zinnaworks.nxpgtool.redis;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.data.redis.core.Cursor;

public interface RedisClientIf {

	Cursor<Entry<String, Object>> hscan(String key);

	Object get(String key);

	void set(String key, Object value);

	Long hlen(String key);

	void hmset(String key, Map<String, Object> param);

	void hset(String key, String field, Object value);

	Object hget(String key, String field);

	Boolean expire(String key, Long timeout);

	Set<String> keys(String pattern);

	Long hdel(String key, String field);

	void del(String key);

	Long incr(String key);

	Long incrby(String key, Long value);

}