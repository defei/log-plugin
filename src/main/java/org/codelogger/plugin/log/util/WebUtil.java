package org.codelogger.plugin.log.util;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Web请求工具类
 *
 * @author defei
 */
public class WebUtil {

    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";

    private static final String X_REAL_IP = "X-Real-IP";

    /**
     * 获取请求的输入流
     *
     * @param request http请求
     * @param charsetName 请求编码字符集
     * @return 请求流内容
     * @throws IOException 输入流异常
     */
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

    /**
     * 获取请求发起的真实IP
     *
     * @param request http请求
     * @return 请求发起的ip地址
     */
    public static String getClientIP(final HttpServletRequest request) {
        String xRealIp = request.getHeader(X_REAL_IP);
        if (xRealIp == null) {
            String ipAddress = request.getHeader(X_FORWARDED_FOR);
            return ipAddress == null || ipAddress.trim().length() == 0 ? request.getRemoteHost() : ipAddress;
        }
        return xRealIp;
    }

}
