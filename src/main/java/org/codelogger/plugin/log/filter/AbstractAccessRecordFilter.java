package org.codelogger.plugin.log.filter;

import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codelogger.plugin.log.bean.LogProcessResponse;
import org.codelogger.plugin.log.core.AbstractLogProcessor;
import org.codelogger.plugin.log.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author defei
 */
public abstract class AbstractAccessRecordFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAccessRecordFilter.class);

    /**
     * 获取不记录的访问请求的的Url正则表达式
     *
     * @return 正则表达式
     */
    public abstract Pattern getIgnoreUrls();

    /**
     * 请求内容字符编码
     *
     * @return The name of a supported {@linkplain java.nio.charset.Charset charset}
     */
    public abstract String getCharsetName();

    /**
     * 记录访问请求
     *
     * @param logProcessResponse 请求处理结果
     */
    public abstract void recordAccessLog(LogProcessResponse logProcessResponse);

    /**
     * 获取发起当前请求的用户
     *
     * @param httpServletRequest 请求
     * @return user identity of current request
     */
    public abstract String getAccessUser(HttpServletRequest httpServletRequest);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        final AbstractAccessRecordFilter accessRecordFilter = this;
        AbstractLogProcessor abstractLogProcessor = new AbstractLogProcessor() {

            @Override
            protected String getCharsetName() {
                return accessRecordFilter.getCharsetName();
            }

            @Override
            public boolean isRequestLogEnable() {
                return true;
            }

            @Override
            public boolean isResponseLogEnable() {
                return false;
            }

            @Override
            protected Pattern getIgnoreUrls() {
                return accessRecordFilter.getIgnoreUrls();
            }

            @Override
            protected void doThisBeforeLog(LogProcessResponse logProcessResponse) {
                recordAccessLog(logProcessResponse);
            }

            @Override
            protected void log(LogProcessResponse logProcessResponse) {

                if (logger.isTraceEnabled()) {
                    logger.trace("Received {} to {} with queryString:{} and parameterMap:{} and requestBody:{} from ip:{}",
                            logProcessResponse.getHttpMethod(), logProcessResponse.getRequestURI(),
                            logProcessResponse.getQueryString(), StringUtils.mapToString(logProcessResponse.getRequestParameterMap()),
                            isRequestLogEnable() ? logProcessResponse.getRequestBody() : "null", logProcessResponse.getRequestClientIp());
                }
            }

        };
        LogProcessResponse logProcessResponse = abstractLogProcessor.process((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        filterChain.doFilter(logProcessResponse.getHttpServletRequest(), logProcessResponse.getHttpServletResponse());
    }
}
