package com.example.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class NoCacheFilter implements Filter {
    @Override public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;

        // Tell browsers and proxies not to cache
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP/1.1
        response.setHeader("Pragma", "no-cache"); // HTTP/1.0
        response.setDateHeader("Expires", 0);     // Proxies

        chain.doFilter(req, res);
    }

    @Override public void destroy() {}
}
