package org.example.currencyexchangeproject.Servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchangeproject.DTO.CurrencyDTO;
import org.example.currencyexchangeproject.DTO.CurrencyResponseDTO;
import org.example.currencyexchangeproject.Entity.CurrencyEntity;
import org.example.currencyexchangeproject.Services.CurrencyService;

import java.io.IOException;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {

            List<CurrencyEntity> currencies = currencyService.getAll();

            if (currencies.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                String noContentJson = "\"Валюта не найдена\"";
                resp.getWriter().write(noContentJson);
                return;
            }


            String jsonResponse = objectMapper.writeValueAsString(currencies);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonResponse);

        } catch (JsonProcessingException e) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            String errorJson = "{\"error\": \"Ошибка при обработке JSON данных.\"}";
            resp.getWriter().write(errorJson);
        } catch (Exception e) {
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
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");

            CurrencyDTO newCurrency = new CurrencyDTO(name, code, sign);

            CurrencyResponseDTO savedCurrency = currencyService.saveCurrency(newCurrency);

            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_CREATED); // 201 Created
            resp.getWriter().write(objectMapper.writeValueAsString(savedCurrency));

        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"Произошла ошибка при сохранении валюты.\"}");
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
