package cn.arry.utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import cn.arry.type.ConstType;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 文件工具
 */
public class FileUtil {
    public static boolean loadDefaultLogbackFile() {
        File logbackFile = new File(ConstType.LOGBACK_FILE_PATH);
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
