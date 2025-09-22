package org.example.currencyexchangeproject.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchangeproject.DTO.ExchangeResponseDTO;
import org.example.currencyexchangeproject.Services.ExchangeService;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private static final ExchangeService exchangeService = new ExchangeService();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fromCurrencyCode = req.getParameter("from");
        String toCurrencyCode = req.getParameter("to");
        String amountString = req.getParameter("amount");

        if (fromCurrencyCode == null || toCurrencyCode == null || amountString == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            String badRequest = "Invalid request";
            resp.getWriter().write(badRequest);
            return;
        }
        try{
            ExchangeResponseDTO response = exchangeService.exchangeCurrency(fromCurrencyCode, toCurrencyCode, new BigDecimal(amountString));
            String json = objectMapper.writeValueAsString(response);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(json);

        }catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errorMsg = e.getMessage();
            resp.getWriter().write(errorMsg);
        }

    }
}
