package cn.arry.utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileUtil {
    public static boolean loadDefaultLogbackFile() {
        File logbackFile = new File("../resources/config/logback.xml");
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
