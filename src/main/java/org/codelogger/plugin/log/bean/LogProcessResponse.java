package org.codelogger.plugin.log.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author defei
 * @data 3/1/18.
 */
public class LogProcessResponse {

    private HttpServletRequest httpServletRequest;

    private HttpServletResponse httpServletResponse;

    public LogProcessResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    /**
     * {@linkplain LogProcessResponse#httpServletRequest}
     */
    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    /**
     * {@linkplain LogProcessResponse#httpServletResponse}
     */
    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }
}
