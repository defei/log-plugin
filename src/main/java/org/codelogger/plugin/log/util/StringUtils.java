package org.codelogger.plugin.log.util;

/**
 * @author defei
 * @data 3/1/18.
 */
public class StringUtils {

    public static String replaceCRLF(String value) {
        return value == null ? null : value.replaceAll("(\r\n|\r|\n|\n\r)", "");
    }
}
