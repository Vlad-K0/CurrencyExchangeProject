package org.example.currencyexchangeproject.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ValidationException;
import jakarta.ws.rs.NotFoundException;
import org.example.currencyexchangeproject.DTO.CurrencyResponseDTO;
import org.example.currencyexchangeproject.Exceptions.DataAccessException;
import org.example.currencyexchangeproject.Exceptions.Message.ErrorMessage;
import org.example.currencyexchangeproject.Exceptions.NotFoundDataException;
import org.example.currencyexchangeproject.Services.CurrencyService;

import java.io.IOException;

@WebServlet("/currencies/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            throw new ValidationException("Отсутствует код валюты");
        }
        String currencyCode = pathInfo.substring(1).toUpperCase();
        try {

            CurrencyResponseDTO currency = currencyService.getCurrencyByCode(currencyCode);

            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getOutputStream(), currency);

        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            objectMapper.writeValue(resp.getWriter(), new ErrorMessage(e.getMessage()));
        } catch (DataAccessException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ErrorMessage(e.getMessage()));
        } catch (NotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            objectMapper.writeValue(resp.getWriter(), new ErrorMessage(e.getMessage()));
        } catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            objectMapper.writeValue(resp.getWriter(), new ErrorMessage(e.getMessage()));
        }
    }
}
