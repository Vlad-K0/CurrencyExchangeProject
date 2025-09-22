package org.example.currencyexchangeproject.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchangeproject.DAO.ExchangeRateDAO;
import org.example.currencyexchangeproject.DTO.CurrencyResponseDTO;
import org.example.currencyexchangeproject.DTO.ExchangeRateDTO;
import org.example.currencyexchangeproject.DTO.ExchangeRateResponseDTO;
import org.example.currencyexchangeproject.Entity.ExchangeRateEntity;
import org.example.currencyexchangeproject.Services.CurrencyService;
import org.example.currencyexchangeproject.Services.ExchangeRateService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    private static final ExchangeRateService service = new ExchangeRateService();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final CurrencyService currencyService = new CurrencyService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<ExchangeRateResponseDTO> rates = service.getAllExchangeRates();

            if (rates.isEmpty()){
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                String noContentMsg = "\"No exchange rates found\"";
                resp.getWriter().write(noContentMsg);
                return;
            }

            String jsonResponse = objectMapper.writeValueAsString(rates);
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonResponse);
        }catch (Exception e){
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errorJson = "{\"error\": \"Произошла непредвиденная ошибка на сервере.\"}";
            resp.getWriter().write(errorJson);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");
            String exchangeRate = req.getParameter("rate");

            CurrencyResponseDTO baseCurrency = currencyService.getCurrencyByCode(baseCurrencyCode);
            CurrencyResponseDTO targetCurrency = currencyService.getCurrencyByCode(targetCurrencyCode);

            ExchangeRateDTO exchangeRateSaveDTO = new ExchangeRateDTO(baseCurrency, targetCurrency, new BigDecimal(exchangeRate));

            ExchangeRateResponseDTO exchangeRateResponseDTO = service.saveExchangeRate(exchangeRateSaveDTO);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(objectMapper.writeValueAsString(exchangeRateResponseDTO));

        }catch (Exception e){
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(e.getMessage());
        }
    }
}
