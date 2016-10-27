package org.dynamicloud.logger;

import org.apache.log4j.Logger;

/**
 * This is a wrapper for log4j
 *
 * @author Eleazar Gomez
 * @version 1.0.0
 * @since 8/22/15
 **/
public class LoggerTool {

    private static Logger log4j;

    private LoggerTool(Class clazz) {
        log4j = Logger.getLogger(clazz);
    }

    public static LoggerTool getLogger(Class clazz) {
        return new LoggerTool(clazz);
    }

    public void debug(String m) {
        log4j.debug(m);
    }

    public void debug(String m, Throwable t) {
        log4j.debug(m, t);
    }

    public void info(String m) {
        log4j.info(m);
    }

    public void info(String m, Throwable t) {
        log4j.info(m, t);
    }

    public void warn(String m) {
        log4j.warn(m);
    }

    public void warn(String m, Throwable t) {
        log4j.warn(m, t);
    }

    public void error(String m) {
        log4j.error(m);
    }

    public void error(String m, Throwable t) {
        log4j.error(m, t);
    }
}