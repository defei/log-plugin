package org.codelogger.plugin.log.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * @author defei
 */
public class LogHttpServletResponse extends HttpServletResponseWrapper {

    private ByteArrayOutputStream responseBody = new ByteArrayOutputStream();

    public byte[] getBody() {
        return responseBody.toByteArray();
    }

    /**
     * @param response
     */
    public LogHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new LogServletOutputStream(super.getOutputStream(), responseBody);
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return new LogPrintWriter(super.getWriter(), responseBody);
    }

}
