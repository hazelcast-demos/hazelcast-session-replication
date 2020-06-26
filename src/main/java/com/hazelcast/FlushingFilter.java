package com.hazelcast;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.vaadin.flow.server.VaadinSession;

@WebFilter("/*")
public class FlushingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // nop
    }

    @Override
    public void destroy() {
        // nop
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);

        if (request instanceof HttpServletRequest) {
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            if (session == null) {
                return;
            }

            for (Enumeration<String> attributeNames = session.getAttributeNames(); attributeNames.hasMoreElements();) {
                String name = attributeNames.nextElement();
                Object value = session.getAttribute(name);
                if (value instanceof VaadinSession) {
                    session.setAttribute(name, value);
                }
            }
        }
    }
}
