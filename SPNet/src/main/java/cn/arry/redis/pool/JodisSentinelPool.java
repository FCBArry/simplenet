package cn.arry.redis.pool;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * jodis哨兵模式连接池
 *
 * @author 科兴第一盖伦
 * @version 2019/5/8
 */
public class JodisSentinelPool implements Pool {
    private List<RedisNode> nodeList;

    private JedisSentinelPool sentinelPool;

    public JodisSentinelPool(List<RedisNode> nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public boolean init() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setTestOnBorrow(false);
        jedisPoolConfig.setMaxWaitMillis(1000);
        jedisPoolConfig.setMaxTotal(3000);

        Set<String> sentinels = new HashSet<>();
        for (RedisNode node : nodeList)
            sentinels.add(new HostAndPort(node.getIp(), node.getPort()).toString());

        RedisNode typicalNode = nodeList.get(0);
        sentinelPool = new JedisSentinelPool("mymaster", sentinels, jedisPoolConfig, typicalNode.getTimeout(),
                typicalNode.getAuth().length() > 0 ? typicalNode.getAuth() : null, typicalNode.getDb());

        return true;
    }

    @Override
    public Object[] getResource(Object key) {
        return new Object[]{sentinelPool.getResource(), null};
    }

    @Override
    public boolean close() {
        sentinelPool.close();
        return true;
    }
}
