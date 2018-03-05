package org.codelogger.plugin.log.core;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;

/**
 * @author defei
 */
public class LogServletOutputStream extends ServletOutputStream {

    private ServletOutputStream originalStream;

    private OutputStream bodyStream;

    public LogServletOutputStream(ServletOutputStream originalStream, OutputStream bodyStream) {
        this.originalStream = originalStream;
        this.bodyStream = bodyStream;
    }

    @Override
    public void write(int b) throws IOException {
        originalStream.write(b);
        bodyStream.write(b);
    }
    
}
