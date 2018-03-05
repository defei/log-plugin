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
public class DefaultLogFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLogProcessor.class);

    private Pattern ignoreUrls = Pattern.compile("/|/+.*(html)$|/+heartbeat.*|/+webjars.*|/+swagger.*|/+v\\d/api-docs.*|/+css.*|/+js.*|/+favicon.ico");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {

        final AbstractLogProcessor abstractLogProcessor = new AbstractLogProcessor() {
            @Override
            public String getCharsetName() {
                return "UTF-8";
            }

            @Override
            public boolean isRequestLogEnable() {
                return logger.isDebugEnabled();
            }

            @Override
            public boolean isResponseLogEnable() {
                return logger.isDebugEnabled();
            }

            @Override
            public Pattern getIgnoreUrls() {
                return ignoreUrls;
            }

            @Override
            protected void doThisBeforeLog(LogProcessResponse logProcessResponse) {
                try {
                    filterChain.doFilter(logProcessResponse.getHttpServletRequest(), logProcessResponse.getHttpServletResponse());
                } catch (Exception e) {
                    throw new RuntimeException("Do filter chain failed.", e);
                }
            }

            @Override
            public void log(LogProcessResponse logProcessResponse) {

                logger.debug("Received {} to {} with queryString:{} and parameterMap:{} and requestBody:{} from ip:{}, responseBody:{}",
                        logProcessResponse.getHttpMethod(), logProcessResponse.getRequestURI(),
                        logProcessResponse.getQueryString(), StringUtils.mapToString(logProcessResponse.getRequestParameterMap()),
                        logProcessResponse.getRequestBody(), logProcessResponse.getRequestClientIp(),
                        logProcessResponse.getResponseBody());
            }
        };
        LogProcessResponse processResponse = abstractLogProcessor.process((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        Boolean needDoFilter = processResponse.getSkipped() || !(abstractLogProcessor.isRequestLogEnable() || abstractLogProcessor.isResponseLogEnable());
        if (needDoFilter) {
            filterChain.doFilter(processResponse.getHttpServletRequest(), processResponse.getHttpServletResponse());
        }
    }

    @Override
    public void destroy() {

    }

    /**
     * {@linkplain DefaultLogFilter#ignoreUrls}
     */
    public void setIgnoreUrls(Pattern ignoreUrls) {
        this.ignoreUrls = ignoreUrls;
    }
}
