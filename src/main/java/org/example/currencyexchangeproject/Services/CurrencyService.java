package org.example.currencyexchangeproject.Services;

import org.example.currencyexchangeproject.DAO.CurrencyDAO;
import org.example.currencyexchangeproject.DTO.CurrencyDTO;
import org.example.currencyexchangeproject.DTO.CurrencyFilter;
import org.example.currencyexchangeproject.DTO.CurrencyResponseDTO;
import org.example.currencyexchangeproject.Entity.CurrencyEntity;

import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final CurrencyDAO currencyDAO = CurrencyDAO.getInstance();

    public List<CurrencyEntity> getAll() {
        return currencyDAO.getAllCurrencies();
    }

    public CurrencyEntity getCurrencyByCode(String code) {
        CurrencyFilter filter = CurrencyFilter.builder()
                .codeEquals(code).build();
        return currencyDAO.getAllCurrencies(filter).getFirst();
    }

    public CurrencyResponseDTO saveCurrency(CurrencyDTO currency) {
        if (currency.getCode() == null || currency.getCode().isEmpty()) {
            throw new IllegalArgumentException("Код валюты не может быть пустым.");
        }
        Optional<CurrencyResponseDTO> savedCurrency = currencyDAO.saveCurrency(currency);
        return savedCurrency.orElseThrow(() -> new RuntimeException("Не удалось сохранить валюту в БД"));
    }
}
