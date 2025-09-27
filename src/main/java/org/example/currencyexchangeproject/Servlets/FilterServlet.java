package org.example.currencyexchangeproject.Servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter(urlPatterns = {"/currencies", "/currency/*", "/exchangeRates", "/exchangeRate/*", "/exchange"})
public class FilterServlet implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        filterChain.doFilter(request, response);
    }
}
