package cn.arry.redis;

import cn.arry.redis.pool.*;
import lombok.Getter;
import lombok.Setter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import java.util.List;
import java.util.Map;

/**
 * @author 科兴第一盖伦
 * @version 2019/5/8
 */
@Getter
@Setter
public class RedisMgr {
    private static List<RedisNode> nodeList;

    private static Pool pool;

    private static int db;

    public static boolean init(int type, List<RedisNode> nodeList) {
        if (type == 1) {
            pool = new JedisPool(nodeList);
        } else if (type == 2) {
            pool = new JodisPool(nodeList);
        } else if (type == 3) {
            pool = new JodisSentinelPool(nodeList);
        }

        return pool.init();
    }

    public static boolean stop() {
        return pool.close();
    }

    public static Object execute(RedisExecutor executor, Object key, Object value, Object... objs) {
        Object result = null;
        Jedis jedis = null;
        ShardedJedis shardedJedis = null;
        try {
            Object[] rlts = pool.getResource(key);
            jedis = (Jedis) rlts[0];
            shardedJedis = (ShardedJedis) rlts[1];
            if (jedis == null)
                return null;

            jedis.select(db);
            result = executor.exec(jedis, key, value, objs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (shardedJedis != null)
                    shardedJedis.close();

                if (jedis != null)
                    jedis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static boolean set(String key, String value) {
        RedisExecutor executor = (jedis, key1, value1, objs) -> {
            String replyRes = jedis.set((String) key1, (String) value1);
            return replyRes != null && replyRes.equals("OK");
        };

        return (boolean) execute(executor, key, value);
    }

    public static String get(String key) {
        RedisExecutor executor = (jedis, key1, value, objs) -> jedis.get((String) key1);

        return (String) execute(executor, key, null);
    }

    @SuppressWarnings("unchecked")
    static public Map<byte[], byte[]> hgetAll(byte[] key) {
        RedisExecutor executor = (jedis, key1, value, objs) -> jedis.hgetAll((byte[]) key1);

        return (Map<byte[], byte[]>) execute(executor, key, null);
    }

    public static byte[] hget(byte[] key, byte[] field) {
        RedisExecutor executor = (jedis, key1, value, objs) -> jedis.hget((byte[]) key1, (byte[]) value);

        return (byte[]) execute(executor, key, field);
    }

    public static long hset(byte[] key, byte[] field, byte[] value) {
        RedisExecutor executor = (jedis, key1, value1, objs) -> {
            Long replyRes = jedis.hset((byte[]) key1, (byte[]) objs[0], (byte[]) value1);
            return replyRes == null ? 0 : replyRes;
        };

        return (long) execute(executor, key, value, (Object) field);
    }

    public static long hdel(byte[] key, byte[] key2) {
        RedisExecutor executor = (jedis, key1, value, objs) -> {
            Long replyRes = jedis.hdel((byte[]) key1, (byte[]) value);
            return replyRes == null ? 0 : replyRes;
        };

        return (long) execute(executor, key, key2);
    }
}
