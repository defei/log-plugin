package org.codelogger.plugin.log.filter;

import java.util.regex.Pattern;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import org.codelogger.plugin.log.bean.LogProcessResponse;
import org.codelogger.plugin.log.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认
 * @author defei
 * @data 3/4/18.
 */
public class DefaultAccessRecordFilter extends AbstractAccessRecordFilter {

    private static final Logger logger = LoggerFactory.getLogger(DefaultAccessRecordFilter.class);

    private Pattern defaultAccessLogIgnoreUrls = Pattern.compile("/|.*\\.ico$|.*\\.jsp$|/error|.*\\.js$|.*\\.css$");

    private String defaultCharsetName = "UTF-8";

    @Override
    public Pattern getIgnoreUrls() {
        return defaultAccessLogIgnoreUrls;
    }

    @Override
    public String getCharsetName() {
        return defaultCharsetName;
    }

    @Override
    public void recordAccessLog(LogProcessResponse logProcessResponse) {

        if (logger.isDebugEnabled()) {
            logger.debug("Received [method]:{}, [uri]:{}, [queryString]:{}, [parameterMap]:{}, [requestIP]:{} requestBody:{}",
                    logProcessResponse.getHttpMethod(), logProcessResponse.getRequestURI(),
                    logProcessResponse.getQueryString(), StringUtils.mapToString(logProcessResponse.getRequestParameterMap()),
                    logProcessResponse.getRequestClientIp(), logProcessResponse.getRequestBody());
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    /**
     * {@linkplain DefaultAccessRecordFilter#getIgnoreUrls()}
     */
    public void setDefaultAccessLogIgnoreUrls(Pattern defaultAccessLogIgnoreUrls) {
        this.defaultAccessLogIgnoreUrls = defaultAccessLogIgnoreUrls;
    }

    /**
     * {@linkplain DefaultAccessRecordFilter#getCharsetName()}
     */
    public void setDefaultCharsetName(String defaultCharsetName) {
        this.defaultCharsetName = defaultCharsetName;
    }
}
