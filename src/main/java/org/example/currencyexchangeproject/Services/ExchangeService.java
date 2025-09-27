package org.example.currencyexchangeproject.Services;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import org.example.currencyexchangeproject.DAO.ExchangeRateDAO;
import org.example.currencyexchangeproject.DTO.ExchangeRateResponseDTO;
import org.example.currencyexchangeproject.DTO.ExchangeResponseDTO;
import org.example.currencyexchangeproject.Exceptions.NotFoundDataException;

import java.math.BigDecimal;
import java.util.Optional;

public class ExchangeService {
    private static final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();
    private static final ExchangeRateService exchangeRateService = new ExchangeRateService();


    public ExchangeResponseDTO exchangeCurrency(String fromCurrency, String toCurrency, BigDecimal amount) {
        return null;
    }
}

