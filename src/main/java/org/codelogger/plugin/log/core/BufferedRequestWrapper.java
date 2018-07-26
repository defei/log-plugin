package org.codelogger.plugin.log.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author defei
 */
public class BufferedRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 1 KB
     */
    private static final int ONE_KILOBYTE_SIZE = 1024;

    /**
     * 32 KB
     */
    private static final int BUFFER_SIZE = ONE_KILOBYTE_SIZE * 32;

    private ByteArrayInputStream byteArrayInputStream;

    private BufferedServletInputStream bufferedServletInputStream;

    protected byte[] buffer;

    public BufferedRequestWrapper(HttpServletRequest req) throws IOException {
        super(req);
        InputStream is = req.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        for (int len; (len = is.read(buffer)) != -1; ) {
            byteArrayOutputStream.write(buffer, 0, len);
        }
        this.buffer = byteArrayOutputStream.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() {
        try {
            byteArrayInputStream = new ByteArrayInputStream(buffer);
            bufferedServletInputStream = new BufferedServletInputStream(byteArrayInputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return bufferedServletInputStream;
    }
}
