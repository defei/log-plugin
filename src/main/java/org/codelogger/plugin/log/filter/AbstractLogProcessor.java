package org.codelogger.plugin.log.filter;


import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codelogger.plugin.log.bean.LogProcessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志处理器，主要是包装请和返回
 *
 * @author defei
 * @data 3/1/18.
 */
public abstract class AbstractLogProcessor {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLogProcessor.class);

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
     * @param logProcessResponse http请求方法
     */
    protected abstract void log(LogProcessResponse logProcessResponse);

    public LogProcessResponse process(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {

        HttpServletRequest request = httpRequest;
        HttpServletResponse response = httpResponse;
        boolean requestLogEnable = isRequestLogEnable();
        boolean responseLogEnable = isResponseLogEnable();
        if (!(requestLogEnable || responseLogEnable)) {
            return new LogProcessResponse(request, response, getCharsetName());
        }
        Pattern ignoreUrls = getIgnoreUrls();
        if (ignoreUrls != null) {
            if (ignoreUrls.matcher(httpRequest.getRequestURI()).matches()) {
                return new LogProcessResponse(request, response, getCharsetName());
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
        LogProcessResponse logProcessResponse = new LogProcessResponse(request, response, getCharsetName());
        doThisBeforeLog(logProcessResponse);
        String requestBody = null;
        if (requestLogEnable) {
            requestBody = logProcessResponse.getRequestBody();
        }
        String responseBody = null;
        if (responseLogEnable) {
            responseBody = logProcessResponse.getResponseBody();
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Request by[{}] [url]:{}, [queryString]:{}, requestBody:{}, responseBody:{}.", httpRequest.getMethod(),
                    httpRequest.getRequestURI(), httpRequest.getQueryString(), requestBody, responseBody);
        }
        log(logProcessResponse);
        return logProcessResponse;
    }

}
