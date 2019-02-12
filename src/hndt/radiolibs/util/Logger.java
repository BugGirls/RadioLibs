package hndt.radiolibs.util;

import org.apache.commons.logging.LogFactory;

public class Logger {

    private static final boolean ifPrintConsole = true;
    private static final String category = "default";

    private Logger() {
    }

    public static void info(String msg) {
        LogFactory.getLog(category).info(msg);
    }

    public static void info(Object obj) {
        LogFactory.getLog(category).info(obj);
    }

    public static void error(String msg) {
        LogFactory.getLog(category).error(msg);
    }

    public static void error(Throwable thrown, String msg) {
        LogFactory.getLog(category).error(msg,thrown);
    }

    public static void error(Throwable thrown) {
        error(thrown, null);
    }

}
