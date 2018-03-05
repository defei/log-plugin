package org.codelogger.plugin.log.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.servlet.ServletInputStream;

/**
 * @author defei
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
