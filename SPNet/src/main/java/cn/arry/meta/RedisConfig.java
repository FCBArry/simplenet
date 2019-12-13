package cn.arry.meta;

import cn.arry.redis.pool.RedisNode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class RedisConfig {
    private List<RedisNode> nodeList = new ArrayList<>();

    public void addNode(String ip, int port, String auth, int timeout, int db) {
        RedisNode node = new RedisNode();
        node.setIp(ip);
        node.setPort(port);
        node.setAuth(auth);
        node.setTimeout(timeout);
        node.setDb(db);
        node.setZkProxyDir("/jodis/sdtest");
        nodeList.add(node);
    }
}
