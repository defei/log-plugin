package org.codelogger.plugin.log.core;

import java.io.*;

/**
 * @author defei
 */
public class LogPrintWriter extends PrintWriter {

    private final int writeBufferSize = 1024;

    private OutputStreamWriter bodyWriter;

    LogPrintWriter(PrintWriter originalWriter, OutputStream bodyStream)
            throws FileNotFoundException {
        super(originalWriter);
        this.bodyWriter = new OutputStreamWriter(bodyStream);
    }

    /**
     * Flushes the stream.
     *
     * @see #checkError()
     */
    @Override
    public void flush() {
        super.flush();
        try {
            bodyWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(String s) {
        // Don't permanently allocate very large buffers.
        char[] dst = new char[s.length()];
        s.getChars(0, s.length(), dst, 0);
        write(dst, 0, s.length());
    }

    @Override
    public void write(char buf[], int off, int len) {
        super.write(buf, off, len);
        writeToBodyWriter(buf, off, len);
    }

    private void writeToBodyWriter(char[] buf, int off, int len) {
        try {
            bodyWriter.write(buf, off, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
