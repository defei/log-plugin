package org.codelogger.plugin.log.filter;


import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codelogger.plugin.log.bean.LogProcessResponse;
import org.codelogger.plugin.log.util.StringUtils;
import org.codelogger.plugin.log.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author defei
 * @data 3/1/18.
 */
public abstract class LogProcessor {

    private static final Logger logger = LoggerFactory.getLogger(LogProcessor.class);

    /**
     * 请求内容字符编码
     *
     * @return The name of a supported {@linkplain java.nio.charset.Charset charset}
     */
    protected abstract String getCharsetName();

    /**
     * 是否开启请求记录
     *
     * @return true, 开启记录
     */
    protected abstract boolean isRequestLogEnable();

    /**
     * 是否开启响应记录
     *
     * @return true, 开启记录
     */
    protected abstract boolean isResponseLogEnable();

    /**
     * 获取不记录的日志的Url正则表达式
     *
     * @return 表达式 {@link Pattern}
     */
    protected abstract Pattern getIgnoreUrls();

    protected abstract void doThisBeforeLog(LogProcessResponse logProcessResponse);

    /**
     * 记录日志
     *
     * @param httpMethod http请求方法
     * @param requestURI 请求的uri
     * @param queryString 请求的参数
     * @param requestBody 请求的内容
     * @param responseBody 请求返回结果
     */
    protected abstract void log(String httpMethod, String requestURI, String queryString, String requestBody, String responseBody);

    public LogProcessResponse process(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {

        HttpServletRequest request = httpRequest;
        HttpServletResponse response = httpResponse;
        boolean requestLogEnable = isRequestLogEnable();
        boolean responseLogEnable = isResponseLogEnable();
        if (!(requestLogEnable || responseLogEnable)) {
            return new LogProcessResponse(request, response);
        }
        Pattern ignoreUrls = getIgnoreUrls();
        if (ignoreUrls != null) {
            if (ignoreUrls.matcher(httpRequest.getRequestURI()).matches()) {
                return new LogProcessResponse(request, response);
            }
        }
        if (requestLogEnable) {
            int length = httpRequest.getContentLength();
            if (length > 0) {
                BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(httpRequest, length);

                InputStream is = bufferedRequest.getInputStream();
                byte[] content = new byte[length];

                int pad = 0;
                while (pad < length) {
                    pad += is.read(content, pad, length);
                }
                request = bufferedRequest;
            }
        }
        if (responseLogEnable) {
            response = new LogHttpServletResponse(httpResponse);
        }
        LogProcessResponse logProcessResponse = new LogProcessResponse(request, response);
        doThisBeforeLog(logProcessResponse);
        String requestBody = null;
        if (requestLogEnable) {
            requestBody = StringUtils.replaceCRLF(WebUtil.getBody(request, getCharsetName()));
        }
        String responseBody = null;
        if (responseLogEnable) {
            responseBody = StringUtils.replaceCRLF(new String(((LogHttpServletResponse) response).getBody(), getCharsetName()));
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Request by[{}] [url]:{}, [queryString]:{}, requestBody:{}, responseBody:{}.", httpRequest.getMethod(),
                    httpRequest.getRequestURI(), httpRequest.getQueryString(), requestBody, responseBody);
        }
        log(httpRequest.getMethod(), httpRequest.getRequestURI(), httpRequest.getQueryString(), requestBody, responseBody);
        return logProcessResponse;
    }

}
