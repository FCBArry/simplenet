package cn.arry.redis;

import redis.clients.jedis.Jedis;

public interface RedisExecutor {
    Object exec(Jedis jedis, Object key, Object value, Object... objs);
}
