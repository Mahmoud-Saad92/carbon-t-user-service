package com.bazinga.eg.userservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        UserContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader(Constants.CORRELATION_ID));
        UserContextHolder.getContext().setUserId(httpServletRequest.getHeader(Constants.USER_ID));
        UserContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(Constants.AUTH_TOKEN));
        UserContextHolder.getContext().setContactId(httpServletRequest.getHeader(Constants.CONTACT_ID));

        log.debug("UserContextFilter Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

        filterChain.doFilter(httpServletRequest, servletResponse);
    }
}
