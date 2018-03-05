package org.codelogger.plugin.log.util;

import java.util.Iterator;
import java.util.Map;

/**
 * @author defei
 */
public class StringUtils {

    public static String replaceCRLF(String value) {
        return value == null ? null : value.replaceAll("(\r\n|\r|\n|\n\r)", "");
    }

    public static String mapToString(Map map) {

        if (map == null || map.isEmpty()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("{");
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            stringBuilder.append(wrapperString(entry.getKey())).append(":").append(collectionToString(entry.getValue()));
            if (iterator.hasNext()) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.append("}").toString();
    }

    private static String collectionToString(Object obj) {

        if (obj instanceof String[]) {
            String[] array = (String[]) obj;
            if (array.length == 1) {
                return wrapperString(array[0]);
            }
            StringBuilder stringBuilder = new StringBuilder("[");
            for (int i = 0; i < array.length; i++) {
                if (i > 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(wrapperString(array[i]));
            }
            return stringBuilder.append("]").toString();
        } else {
            return wrapperString(obj);
        }
    }

    private static String wrapperString(Object obj) {
        return obj instanceof String ? "\"" + obj.toString() + "\"" : String.valueOf(obj);
    }

}
