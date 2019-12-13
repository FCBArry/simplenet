package cn.arry.utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 文件工具
 */
public class FileUtil {
    public static boolean loadDefaultLogbackFile(String configPath) {
        File logbackFile = new File(configPath);
        if (logbackFile.exists()) {
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();

            try {
                configurator.doConfigure(logbackFile);
                return true;
            } catch (JoranException e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }
}
