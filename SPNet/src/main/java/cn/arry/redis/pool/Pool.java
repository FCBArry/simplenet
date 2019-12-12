package cn.arry.redis.pool;

/**
 * 连接池接口
 *
 * @author 科兴第一盖伦
 * @version 2019/5/8
 */
public interface Pool {
    boolean init();

    Object[] getResource(Object obj);

    boolean close();
}
