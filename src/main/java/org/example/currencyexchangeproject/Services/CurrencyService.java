package org.example.currencyexchangeproject.Services;

import org.example.currencyexchangeproject.DAO.CurrencyDAO;
import org.example.currencyexchangeproject.DTO.*;
import org.example.currencyexchangeproject.Entity.CurrencyEntity;
import org.example.currencyexchangeproject.Exceptions.NotFoundDataException;
import org.example.currencyexchangeproject.Exceptions.ValidationException;
import org.example.currencyexchangeproject.Mappers.CurrencyMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private final CurrencyDAO currencyDAO = CurrencyDAO.getInstance();

    public List<CurrencyEntity> getAll() {
        return currencyDAO.getAllCurrencies();
    }

    public CurrencyResponseDTO updateCurrency(UpdateCurrencyDTO updateCurrencyDTO) {
        if (updateCurrencyDTO.getCode() == null || updateCurrencyDTO.getCode().isEmpty() ||
            updateCurrencyDTO.getName() == null || updateCurrencyDTO.getName().isEmpty() ||
            updateCurrencyDTO.getSign() == null || updateCurrencyDTO.getSign().isEmpty()) {
            throw new ValidationException("Please enter code and name and sign");
        }

        CurrencyResponseDTO currencyResponseDTO = getCurrencyByCode(updateCurrencyDTO.getCode());

        currencyResponseDTO.setName(updateCurrencyDTO.getName());
        currencyResponseDTO.setSign(updateCurrencyDTO.getSign());

        CurrencyEntity entity = CurrencyMapper.mapToEntity(currencyResponseDTO);

        CurrencyEntity updatedEntity = currencyDAO.updateCurrency(entity);

        return CurrencyMapper.mapToResponseDTO(updatedEntity);
    }

    public CurrencyResponseDTO getCurrencyByCode(String code) {
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
