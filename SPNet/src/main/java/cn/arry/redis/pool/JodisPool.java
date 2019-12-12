package cn.arry.redis.pool;

import io.codis.jodis.JedisResourcePool;
import io.codis.jodis.RoundRobinJedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

/**
 * jodis连接池
 *
 * @author 科兴第一盖伦
 * @version 2019/5/8
 */
public class JodisPool implements Pool {
    private List<Node> nodeList;

    private JedisResourcePool jedisResourcePool;

    public JodisPool(List<Node> nodeList) {
        this.nodeList = nodeList;
    }

    @Override
    public boolean init() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setTestOnBorrow(false);
        jedisPoolConfig.setMaxWaitMillis(1000);
        jedisPoolConfig.setMaxTotal(3000);

        try {
            Node node = nodeList.get(0);
            StringBuilder urlBuilder = new StringBuilder();
            int timeout = node.getTimeout();
            String zkProxyDir = node.getZkProxyDir();

            for (Node info : nodeList) {
                urlBuilder.append(String.format("%s:%d", info.getIp(), info.getPort()));
                urlBuilder.append(",");
            }

            if (urlBuilder.length() > 0)
                urlBuilder.deleteCharAt(urlBuilder.length() - 1);

            int database = 0;
            jedisResourcePool = RoundRobinJedisPool.create().curatorClient(urlBuilder.toString(), timeout)
                    .zkProxyDir(zkProxyDir).database(database).poolConfig(jedisPoolConfig).build();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public Object[] getResource(Object key) {
        return new Object[]{jedisResourcePool.getResource(), null};
    }

    @Override
    public boolean close() {
        if (jedisResourcePool != null) {
            try {
                jedisResourcePool.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            jedisResourcePool = null;
        }

        return true;
    }
}
