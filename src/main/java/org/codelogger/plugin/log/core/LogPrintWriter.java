package org.codelogger.plugin.log.core;

import java.io.*;

/**
 * @author defei
 */
public class LogPrintWriter extends PrintWriter {

    private OutputStreamWriter bodyWriter;

    LogPrintWriter(PrintWriter originalWriter, OutputStream bodyStream)
            throws FileNotFoundException {
        super(originalWriter);
        this.bodyWriter = new OutputStreamWriter(bodyStream);
    }

    @Override
    public void write(char buf[], int off, int len) {
        super.write(buf, off, len);
        try {
            bodyWriter.write(buf, off, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
