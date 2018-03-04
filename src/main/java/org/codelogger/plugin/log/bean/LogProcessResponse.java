package org.codelogger.plugin.log.bean;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
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
            String body = StringUtils.replaceCRLF(WebUtil.getBody(getHttpServletRequest(), this.charsetName));
            return body == null ? null : body.trim().length() == 0 ? "" : body;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取请求参数Map
     *
     * @return 请求参数Map
     */
    public Map getRequestParameterMap() {
        return getHttpServletRequest().getParameterMap();
    }

    /**
     * {@link WebUtil#getClientIP(HttpServletRequest)}
     *
     * @return 客户端请求ip
     */
    public String getRequestClientIp() {
        return WebUtil.getClientIP(getHttpServletRequest());
    }

    /**
     * 请求返回结果
     *
     * @return 请求返回结果
     */
    public String getResponseBody() {
        try {
            String body = StringUtils.replaceCRLF(new String(((LogHttpServletResponse) getHttpServletResponse()).getBody(), this.charsetName));
            return body == null ? null : body.trim().length() == 0 ? "" : body;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
