package com.example.gateway.filter;

import com.example.gateway.config.Properties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class ProxyFilter extends ZuulFilter {

    private static final String REQUEST_ENTITY = "requestEntity";

    private static final String DEV = "DEV";

    private static final String QA = "QA";

    private static final String UAT = "UAT";

    public static final String ENVIRONMENT = "ENVIRONMENT";

    @Autowired
    private Properties properties;

    @Override
    public String filterType() {
        return FilterConstants.ROUTE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String header = request.getHeader(ENVIRONMENT);
        URL url = null;
        String body = null;

        if (QA.equals(header)) {
            url = properties.getEnvironmentMap().get(QA);
        } else if (UAT.equals(header)) {
            url = properties.getEnvironmentMap().get(UAT);
        } else {
            url = properties.getEnvironmentMap().get(DEV);
        }

        try {
            InputStream in = (InputStream) context.get(REQUEST_ENTITY);
            if (in == null) {
                in = context.getRequest().getInputStream();
            }

            body = StreamUtils.copyToString(in, Charset.forName(StandardCharsets.UTF_8.name()));
        } catch (Exception e) {
            log.error("Error reading request body.", e);
            throw new ZuulException(e, 500, "Proxy Error during reading request body.");
        }

        context.setRouteHost(url);
        log.debug("{} request to {} body={}", request.getMethod(), url, body);

        return null;
    }
}