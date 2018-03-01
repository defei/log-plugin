package org.codelogger.plugin.log.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.servlet.ServletInputStream;

/**
 * @author defei
 * @data 1/11/18.
 */
public class BufferedServletInputStream extends ServletInputStream {

    private ByteArrayInputStream byteArrayInputStream;

    public BufferedServletInputStream(ByteArrayInputStream bais) {
        this.byteArrayInputStream = bais;
    }

    @Override
    public int read() throws IOException {
        return byteArrayInputStream.read();
    }
}
