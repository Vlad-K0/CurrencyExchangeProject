package org.example.currencyexchangeproject.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchangeproject.DTO.ExchangeRateResponseDTO;
import org.example.currencyexchangeproject.Services.CurrencyService;
import org.example.currencyexchangeproject.Services.ExchangeRateService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private static final ExchangeRateService service = new ExchangeRateService();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final CurrencyService currencyService = new CurrencyService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Требуется код валюты.\"}");
            return;
        }
        String exchangeRateString = pathInfo.substring(1).toUpperCase();
        try {
            ExchangeRateResponseDTO rateDTO = service.getExchangeRateByCode(exchangeRateString);
            if (rateDTO == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

            String jsonResponse = objectMapper.writeValueAsString(rateDTO);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonResponse);
        } catch (Exception e) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errorJson = "{\"error\": \"Произошла непредвиденная ошибка на сервере.\"}";
            resp.getWriter().write(errorJson);
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String rate = null;
        if (requestBody != null && !requestBody.isEmpty()) {
            String[] parts = requestBody.split("=");
            if (parts.length == 2 && parts[0].equals("rate")) {
                rate = parts[1];
            }
        }
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Требуется код валюты.\"}");
            return;
        }
        String exchangeRateString = pathInfo.substring(1).toUpperCase();
        try {
            ExchangeRateResponseDTO rateDTO = service.updateExchangeRate(exchangeRateString, new BigDecimal(rate));
            if (rateDTO == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }

            String jsonResponse = objectMapper.writeValueAsString(rateDTO);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonResponse);
        } catch (Exception e) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errorJson = "{\"error\": \"Произошла непредвиденная ошибка на сервере.\"}";
            resp.getWriter().write(errorJson);
        }
    }
}
