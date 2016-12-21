package org.dynamicloud.logger;

/**
 * This is a wrapper for log4j
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/22/15
 * @deprecated will be removed in near future.
 **/
public class LoggerTool {

    private LoggerTool(Class clazz) {

    }

    public static LoggerTool getLogger(Class clazz) {
        return new LoggerTool(clazz);
    }

    public void debug(String m) {

    }

    public void debug(String m, Throwable t) {

    }

    public void info(String m) {

    }

    public void info(String m, Throwable t) {

    }

    public void warn(String m) {

    }

    public void warn(String m, Throwable t) {

    }

    public void error(String m) {

    }

    public void error(String m, Throwable t) {

    }
}