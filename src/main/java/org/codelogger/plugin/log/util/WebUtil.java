package org.codelogger.plugin.log.util;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletRequest;

/**
 * Web请求工具类
 * @author defei
 */
public class WebUtil {

    public static String getBody(ServletRequest request, String charsetName) throws IOException {

        int length = request.getContentLength();
        if (length > 0) {
            InputStream is = request.getInputStream();
            byte[] content = new byte[length];

            int pad = 0;
            while (pad < length) {
                pad += is.read(content, pad, length);
            }
            return new String(content, charsetName);
        }
        return null;
    }


}
