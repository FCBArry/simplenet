package cn.arry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * log
 */
public class Log {
    private static final Logger logger = LoggerFactory.getLogger("");

    private static final int BASE_STACK_DEPTH = 4;

    private static final String MSG_SEPARATOR = " ";

    public static void debug(String format, Object... arguments) {
        debug0(format, null, arguments);
    }

    public static void debug(String msg, Throwable t) {
        debug0(msg, t);
    }

    private static void debug0(String msgOrFormat, Throwable t, Object... objects) {
        String className = getCaller();
        if (t != null) {
            logger.debug(className + MSG_SEPARATOR + msgOrFormat, t);
        } else {
            logger.debug(className + MSG_SEPARATOR + msgOrFormat, objects);
        }
    }

    public static void info(String format, Object... arguments) {
        info0(format, null, arguments);
    }

    public static void info(String msg, Throwable t) {
        info0(msg, t);
    }

    protected static void info0(String msgOrFormat, Throwable t, Object... objects) {
        String className = getCaller();
        if (t != null) {
            logger.info(className + MSG_SEPARATOR + msgOrFormat, t);
        } else {
            logger.info(className + MSG_SEPARATOR + msgOrFormat, objects);
        }
    }

    public static void warn(String format, Object... arguments) {
        warn0(format, null, arguments);
    }

    public static void warn(String msg, Throwable t) {
        warn0(msg, t);
    }

    private static void warn0(String msgOrFormat, Throwable t, Object... objects) {
        String className = getCaller();
        if (t != null) {
            logger.warn(className + MSG_SEPARATOR + msgOrFormat, t);
        } else {
            logger.warn(className + MSG_SEPARATOR + msgOrFormat, objects);
        }
    }

    public static void error(String format, Object... arguments) {
        error0(format, null, arguments);
    }

    public static void error(String msg, Throwable t) {
        error0(msg, t);
    }

    private static void error0(String msgOrFormat, Throwable t, Object... objects) {
        String className = getCaller();
        if (t != null) {
            logger.error(className + MSG_SEPARATOR + msgOrFormat, t);
        } else {
            logger.error(className + MSG_SEPARATOR + msgOrFormat, objects);
        }
    }

    /**
     * 获得指定层级的堆栈跟踪元素
     */
    private static String getCaller() {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();
        return st[BASE_STACK_DEPTH].toString();
    }
}
