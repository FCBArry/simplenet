package cn.arry.netty.manager;

import cn.arry.meta.RedisConfig;
import cn.arry.meta.ServerConfig;
import cn.arry.meta.VariableConfig;
import cn.arry.redis.RedisMgr;
import cn.arry.utils.FileUtil;
import cn.arry.utils.XmlUtil;
import lombok.Getter;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 云顶之弈江流儿
 * @version 2019/12/12
 */
@Getter
public class ConfigManager {
    /**
     * variable配置
     */
    private VariableConfig variableConfig;

    /**
     * redis配置
     */
    private RedisConfig redisConfig;

    /**
     * server配置
     */
    private Map<Integer, List<ServerConfig>> serverConfigMap;

    private static final class LazyLoader {
        private static final ConfigManager INSTANCE = new ConfigManager();
    }

    public static ConfigManager getInstance() {
        return LazyLoader.INSTANCE;
    }

    public boolean init(String configPath) {
        return initVariable(configPath)
                && initDB()
                && initServer();
    }

    private boolean initVariable(String configPath) {
        variableConfig = new VariableConfig();
        Document root = XmlUtil.readXml(configPath);
        Element tag = (Element) root.selectSingleNode("/config");
        if (tag != null) {
            variableConfig.setLogbackFilePath(XmlUtil.readElementStr(tag, "logbackFilePath",
                    "../resources/config/logback.xml"));
            variableConfig.setDbPath(XmlUtil.readElementStr(tag, "dbPath",
                    "../resources/config/db.xml"));
            variableConfig.setServerPath(XmlUtil.readElementStr(tag, "serverPath",
                    "../resources/config/server.xml"));
        }

        return FileUtil.loadDefaultLogbackFile(variableConfig.getLogbackFilePath());
    }

    private boolean initDB() {
        redisConfig = new RedisConfig();
        Document root = XmlUtil.readXml(variableConfig.getDbPath());
        Element tag = (Element) root.selectSingleNode("/config/redisdb");
        if (tag != null) {
            List<?> nodeList = tag.elements("node");
            if (nodeList == null || nodeList.isEmpty()) {
                return false;
            }

            for (Object node : nodeList) {
                Element element = (Element) node;
                String ip = XmlUtil.readAttrStr(element, "ip", "127.0.0.1");
                int port = XmlUtil.readAttrInt(element, "port", 6379);
                String auth = XmlUtil.readAttrStr(element, "auth", "");
                int timeout = XmlUtil.readAttrInt(element, "timeout", 1000000);
                int db = XmlUtil.readAttrInt(element, "db", 1);
                redisConfig.addNode(ip, port, auth, timeout, db);
            }
        }

        return RedisMgr.init(1, redisConfig.getNodeList());
    }

    private boolean initServer() {
        serverConfigMap = new HashMap<>();
        Document root = XmlUtil.readXml(variableConfig.getServerPath());
        return initServer(root, "/config/game")
                && initServer(root, "/config/proxy");
    }

    private boolean initServer(Document root, String nodeName) {
        Element tag = (Element) root.selectSingleNode(nodeName);
        if (tag != null) {
            List<?> nodeList = tag.elements("host");
            if (nodeList == null || nodeList.isEmpty()) {
                return false;
            }

            for (Object node : nodeList) {
                Element element = (Element) node;
                int serverType = XmlUtil.readAttrInt(element, "serverType", 0);
                int serverId = XmlUtil.readAttrInt(element, "serverId", 0);
                String addr = XmlUtil.readAttrStr(element, "addr", "");
                int port = XmlUtil.readAttrInt(element, "port", 0);
                int id = XmlUtil.readAttrInt(element, "id", 0);

                List<ServerConfig> serverConfigs = serverConfigMap.computeIfAbsent(serverType, k -> new ArrayList<>());
                serverConfigs.add(new ServerConfig(serverType, serverId, addr, port, id));
            }

            return true;
        }

        return false;
    }
}
