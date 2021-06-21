package com.es.phoneshop.web;

import com.es.phoneshop.security.DefaultDosProtectionService;
import com.es.phoneshop.security.DosProtectionService;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DosFilter implements Filter {
    private DosProtectionService dosProtectionService;
    private final int TOO_MANY_REQUEST = 429;

    @Override
    public void init(FilterConfig filterConfig)  {
        dosProtectionService = DefaultDosProtectionService.getInstance();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (dosProtectionService.isAllowed(request.getRemoteAddr())) {
            filterChain.doFilter(request, response);
        } else {
            ((HttpServletResponse)response).setStatus(TOO_MANY_REQUEST);
        }
    }

    @Override
    public void destroy() {

    }
}
