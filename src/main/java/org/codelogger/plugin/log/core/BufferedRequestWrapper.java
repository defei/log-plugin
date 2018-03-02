package org.codelogger.plugin.log.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author defei
 * @data 1/11/18.
 */
public class BufferedRequestWrapper extends HttpServletRequestWrapper {

    private ByteArrayInputStream byteArrayInputStream;

    private BufferedServletInputStream bufferedServletInputStream;

    private byte[] buffer;

    public BufferedRequestWrapper(HttpServletRequest req, int length) throws IOException {
        super(req);
        InputStream is = req.getInputStream();
        buffer = new byte[length];

        int pad = 0;
        while (pad < length) {
            pad += is.read(buffer, pad, length);
        }
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
