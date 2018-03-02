package org.codelogger.plugin.log.bean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codelogger.plugin.log.core.LogHttpServletResponse;
import org.codelogger.plugin.log.util.StringUtils;
import org.codelogger.plugin.log.util.WebUtil;

/**
 * @author defei
 * @data 3/1/18.
 */
public class LogProcessResponse {

    private String charsetName;

    private HttpServletRequest httpServletRequest;

    private HttpServletResponse httpServletResponse;

    public LogProcessResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String charsetName) {
        if (httpServletRequest == null || httpServletResponse == null || charsetName == null) {
            throw new IllegalArgumentException("Argument httpServletRequest and httpServletResponse and charsetName can not be null.");
        }
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.charsetName = charsetName;
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

    /**
     * http请求方法
     *
     * @return http请求方法
     */
    public String getHttpMethod() {
        return getHttpServletRequest().getMethod();
    }

    /**
     * 请求的uri
     *
     * @return 请求的uri
     */
    public String getRequestURI() {
        return getHttpServletRequest().getRequestURI();
    }

    /**
     * 请求的参数
     *
     * @return 请求的参数
     */
    public String getQueryString() {
        return getHttpServletRequest().getQueryString();
    }

    /**
     * 请求的内容
     *
     * @return 请求的内容
     */
    public String getRequestBody() {
        try {
            return StringUtils.replaceCRLF(WebUtil.getBody(getHttpServletRequest(), this.charsetName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 请求返回结果
     *
     * @return 请求返回结果
     */
    public String getResponseBody() {
        try {
            return StringUtils.replaceCRLF(new String(((LogHttpServletResponse) getHttpServletResponse()).getBody(), this.charsetName));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
