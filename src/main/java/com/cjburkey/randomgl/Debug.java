package com.cjburkey.randomgl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Debug {
    
    private static final Logger _logger = LogManager.getLogger("randomgl");
    
    public static void info(Object message) {
        _logger.info(sanitize(message));
    }
    
    public static void info(Object message, Object... data) {
        _logger.info(sanitize(message), data);
    }
    
    public static void warn(Object message) {
        _logger.warn(sanitize(message));
    }
    
    public static void warn(Object message, Object... data) {
        _logger.warn(sanitize(message), data);
    }
    
    public static void error(Object message) {
        _logger.error(sanitize(message));
    }
    
    public static void error(Object message, Object... data) {
        _logger.error(sanitize(message), data);
    }
    
    public static void fatal(Object message) {
        _logger.fatal(sanitize(message));
    }
    
    public static void fatal(Object message, Object... data) {
        _logger.fatal(sanitize(message), data);
    }
    
    public static void exception(Throwable exception) {
        // Make sure we bypass invocation exceptions
        while (exception.getCause() != null) {
            exception = exception.getCause();
        }
        Debug.error(" --[ BEGIN EXCEPTION ]--");
        Debug.error("     An error occurred: {}", exception.getMessage());
        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            Debug.error("       {}", stackTraceElement.toString());
        }
        Debug.error(" --[  END  EXCEPTION ]--");
    }
    
    private static String sanitize(Object data) {
        String dat = (data == null) ? "null" : data.toString();
        return (dat == null) ? "null" : dat.trim();
    }
    
}