package cn.arry.redis.pool;

import lombok.Getter;
import lombok.Setter;

/**
 * redis配置节点
 *
 * @author 科兴第一盖伦
 * @version 2019/5/8
 */
@Getter
@Setter
public class RedisNode {
    String ip;

    int port;

    String auth;

    int timeout;

    String zkProxyDir;

    int db;
}
