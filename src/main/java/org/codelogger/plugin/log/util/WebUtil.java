package org.codelogger.plugin.log.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletInputStream;
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
     * 1 KB
     */
    private static final int ONE_KILOBYTE_SIZE = 1024;

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
            byte[] buffer = new byte[ONE_KILOBYTE_SIZE];
            ServletInputStream is = request.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (int len; (len = is.read(buffer)) != -1; ) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byte[] bytes = byteArrayOutputStream.toByteArray();
            byteArrayOutputStream.close();
            return new String(bytes, charsetName);
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
