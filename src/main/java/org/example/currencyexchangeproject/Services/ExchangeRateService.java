package org.example.currencyexchangeproject.Services;

import org.example.currencyexchangeproject.DAO.ExchangeRateDAO;
import org.example.currencyexchangeproject.DTO.ExchangeRateDTO;
import org.example.currencyexchangeproject.DTO.ExchangeRateFilter;
import org.example.currencyexchangeproject.DTO.ExchangeRateResponseDTO;
import org.example.currencyexchangeproject.DTO.ExchangeRateUpdateDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    private static final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();

    public List<ExchangeRateResponseDTO> getAllExchangeRates() {
        return exchangeRateDAO.getAllExchangeRates();
    }

    public ExchangeRateResponseDTO getExchangeRateByCode(String code) {
        String baseCurrencyCode = code.substring(0, 3);
        String targetCurrencyCode = code.substring(3, 6);
        List<ExchangeRateResponseDTO> exchangeRateResponseDTO = exchangeRateDAO.getAllExchnageRates(ExchangeRateFilter.builder()
                .baseCurrencyCodeEquals(baseCurrencyCode)
                .targetCurrencyCodeEquals(targetCurrencyCode)
                .limit(Integer.MAX_VALUE)
                .offset(0).build());
        return exchangeRateResponseDTO.getFirst();
    }

    public ExchangeRateResponseDTO saveExchangeRate(ExchangeRateDTO exchangeRateDTO) {
        //прописать логику проверки
        Optional<ExchangeRateResponseDTO> savedRate = exchangeRateDAO.createExchangeRate(exchangeRateDTO);
        return savedRate.orElseThrow(() -> new RuntimeException("Не удалось сохранить обменный курс"));
    }

    public ExchangeRateResponseDTO updateExchangeRate(String code, BigDecimal rate) {
        ExchangeRateResponseDTO findRate = getExchangeRateByCode(code);
        ExchangeRateUpdateDTO exchangeRateUpdateDTO = new ExchangeRateUpdateDTO(findRate.getId(), rate);
        Optional<ExchangeRateResponseDTO> updatedExchangeRate = exchangeRateDAO.updateExchangeRate(exchangeRateUpdateDTO);
        return updatedExchangeRate.orElseThrow(() -> new RuntimeException("Не удалось сохранить обменный курс"));
    }
}
