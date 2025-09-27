package org.example.currencyexchangeproject.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.currencyexchangeproject.DTO.ExchangeRateResponseDTO;
import org.example.currencyexchangeproject.Exceptions.Message.ErrorMessage;
import org.example.currencyexchangeproject.Exceptions.NotFoundDataException;
import org.example.currencyexchangeproject.Exceptions.ValidationException;
import org.example.currencyexchangeproject.Services.ExchangeRateService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    private static final ExchangeRateService service = new ExchangeRateService();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new ValidationException("Missing path parameter");
        }

        String exchangeRateString = pathInfo.substring(1).toUpperCase();

        try {
            ExchangeRateResponseDTO rateDTO = service.getExchangeRateByCode(exchangeRateString);

            resp.setStatus(HttpServletResponse.SC_OK);
            String jsonResponse = objectMapper.writeValueAsString(rateDTO);
            resp.getWriter().write(jsonResponse);

        } catch (NotFoundDataException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            objectMapper.writeValue(resp.getWriter(), new ErrorMessage(e.getMessage()));
        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ErrorMessage(e.getMessage()));
        } catch (RuntimeException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ErrorMessage(e.getMessage()));
        }
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        String rate = null;
        if (!requestBody.isEmpty()) {
            String[] parts = requestBody.split("=");
            if (parts.length == 2 && parts[0].equals("rate")) {
                rate = parts[1];
            }
        }
        if (pathInfo == null || pathInfo.equals("/")) {
            throw new ValidationException("Missing path parameter");
        }
        String exchangeRateString = pathInfo.substring(1).toUpperCase();
        try {
            ExchangeRateResponseDTO rateDTO = service.updateExchangeRate(exchangeRateString, new BigDecimal(rate));

            String jsonResponse = objectMapper.writeValueAsString(rateDTO);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(jsonResponse);
        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ErrorMessage(e.getMessage()));
        } catch (NotFoundDataException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            objectMapper.writeValue(resp.getWriter(), new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ErrorMessage(e.getMessage()));
        }
    }
}
