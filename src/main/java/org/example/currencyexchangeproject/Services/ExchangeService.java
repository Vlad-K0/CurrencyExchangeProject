package org.example.currencyexchangeproject.Services;

import org.example.currencyexchangeproject.DAO.ExchangeRateDAO;
import org.example.currencyexchangeproject.DTO.ExchangeRateResponseDTO;
import org.example.currencyexchangeproject.DTO.ExchangeResponseDTO;

import java.math.BigDecimal;

public class ExchangeService {
    private static final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();
    private static final ExchangeRateService exchangeRateService = new ExchangeRateService();


    public ExchangeResponseDTO exchangeCurrency(String fromCurrency, String toCurrency, BigDecimal amount) {
        ExchangeResponseDTO responseDTO = new ExchangeResponseDTO();
        ExchangeRateResponseDTO exchangeRateResponseDTO = exchangeRateService.getExchangeRateByCode(fromCurrency + toCurrency);
        responseDTO.setFromCurrency(exchangeRateResponseDTO.getBaseCurrencyDTO());
        responseDTO.setToCurrency(exchangeRateResponseDTO.getTargetCurrencyDTO());
        responseDTO.setRate(exchangeRateResponseDTO.getRate());
        responseDTO.setAmount(amount);
        responseDTO.setConvertedAmount(amount.multiply(exchangeRateResponseDTO.getRate()));
        return responseDTO;
    }
}
