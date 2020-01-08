package cn.arry.redis.pool;

import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * jedis连接池
 *
 * @author 科兴第一盖伦
 * @version 2019/5/8
 */
public class JedisPool implements Pool {
    private List<RedisNode> nodeList;

    private ShardedJedisPool shardedJedisPool;

    public JedisPool(List<RedisNode> nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public boolean init() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setTestOnBorrow(false);
        jedisPoolConfig.setMaxWaitMillis(1000);
        jedisPoolConfig.setMaxTotal(3000);

        List<JedisShardInfo> jedisShardInfos = new ArrayList<>();
        for (RedisNode node : nodeList) {
            JedisShardInfo shardInfo = new JedisShardInfo(node.getIp(), node.getPort(), node.getTimeout());
            shardInfo.setSoTimeout(node.getTimeout());
            if (node.getAuth().length() > 0)
                shardInfo.setPassword(node.getAuth());

            jedisShardInfos.add(shardInfo);
        }

        shardedJedisPool = new ShardedJedisPool(jedisPoolConfig, jedisShardInfos);
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] getResource(Object key) {
        Jedis jedis;
        ShardedJedis shardedJedis = shardedJedisPool.getResource();
        if (key instanceof String) {
            jedis = shardedJedis.getShard((String) key);
        } else if (key instanceof List) {
            jedis = shardedJedis.getShard(((List<String>) key).get(0));
        } else {
            jedis = shardedJedis.getShard((byte[]) key);
        }

        return new Object[]{jedis, shardedJedis};
    }

    @Override
    public boolean close() {
        if (shardedJedisPool != null) {
            try {
                shardedJedisPool.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            shardedJedisPool = null;
        }

        return true;
    }
}
