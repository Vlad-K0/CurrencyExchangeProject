package org.example.currencyexchangeproject.Servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchangeproject.Entity.CurrencyEntity;
import org.example.currencyexchangeproject.Services.CurrencyService;

import java.io.IOException;

@WebServlet("/currencies/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo(); // Получаем путь, который идёт после "/currency/"

        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Требуется код валюты.\"}");
            return;
        }
        String currencyCode = pathInfo.substring(1).toUpperCase();
        try {
            CurrencyEntity currency = currencyService.getCurrencyByCode(currencyCode);

            if (currency == null) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                String noContentJson = "\"Валюта не найдена\"";
                resp.getWriter().write(noContentJson);
                return;
            }

            String jsonResponse = objectMapper.writeValueAsString(currency);

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
}
