package cn.arry;

import cn.arry.utils.FileUtil;

/**
 * proxy server
 * server's server
 * 转发服，只用处理服务器消息，对于server来说是服务器
 *
 * @author 云顶之弈江流儿
 * @version 2019/12/11
 */
public class ProxyServer {
    public static void main(String[] args) {
        if (!FileUtil.loadDefaultLogbackFile()) {
            return;
        }
    }
}
