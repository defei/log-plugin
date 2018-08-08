package org.codelogger.plugin.log.core;


import java.io.IOException;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codelogger.plugin.log.bean.LogProcessResponse;
import org.codelogger.plugin.log.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志处理器，主要是包装请和返回
 *
 * @author defei
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
    public abstract boolean isRequestLogEnable();

    /**
     * 是否开启响应记录
     *
     * @return true, 开启记录
     */
    public abstract boolean isResponseLogEnable();

    /**
     * 获取不记录的日志的Url正则表达式
     *
     * @return 表达式 {@link Pattern}
     */
    protected abstract Pattern getIgnoreUrls();

    /**
     * 在记录日志之前执行一次该方法
     *
     * @param logProcessResponse 请求处理后的结果
     */
    protected abstract void doThisBeforeLog(LogProcessResponse logProcessResponse);

    /**
     * 记录日志
     *
     * @param logProcessResponse http请求方法
     */
    protected abstract void log(LogProcessResponse logProcessResponse);

    /**
     * 处理请求和响应
     *
     * @param httpRequest http请求
     * @param httpResponse http响应
     * @return 处理结果
     * @throws IOException io问题
     */
    public LogProcessResponse process(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException {

        HttpServletRequest request = httpRequest;
        HttpServletResponse response = httpResponse;
        boolean requestLogEnable = isRequestLogEnable();
        boolean responseLogEnable = isResponseLogEnable();
        if (!(requestLogEnable || responseLogEnable)) {
            return new LogProcessResponse(request, response, getCharsetName(), true);
        }
        Pattern ignoreUrls = getIgnoreUrls();
        if (ignoreUrls != null) {
            if (ignoreUrls.matcher(httpRequest.getRequestURI()).matches()) {
                return new LogProcessResponse(request, response, getCharsetName(), true);
            }
        }
        if (requestLogEnable) {
            int length = httpRequest.getContentLength();
            if (length > 0) {
                request = new BufferedRequestWrapper(httpRequest);
            }
        }
        if (responseLogEnable) {
            response = new LogHttpServletResponse(httpResponse);
        }
        LogProcessResponse logProcessResponse = new LogProcessResponse(request, response, getCharsetName(), false);
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
