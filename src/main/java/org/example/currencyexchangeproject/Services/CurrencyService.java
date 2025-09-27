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

    public List<CurrencyResponseDTO> getAll() {
        List<CurrencyEntity> currencyEntities = currencyDAO.getAllCurrencies();
        if (currencyEntities.isEmpty())
            throw new NotFoundDataException("No currencies found");
        List<CurrencyResponseDTO> currencies = new ArrayList<>();
        for (CurrencyEntity currencyEntity : currencyEntities) {
            currencies.add(CurrencyMapper.mapToResponseDTO(currencyEntity));
        }
        return currencies;
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
        List<CurrencyEntity> responseEntitiesList = currencyDAO.getAllCurrencies(filter);

        List<CurrencyResponseDTO> responseDTOList = new ArrayList<>();
        for (CurrencyEntity entity : responseEntitiesList) {
            responseDTOList.add(CurrencyMapper.mapToResponseDTO(entity));
        }
        if (responseDTOList.isEmpty()) {
            throw new NotFoundDataException("Currency with code " + code + " not found");
        }
        return responseDTOList.getFirst();
    }

    public CurrencyResponseDTO saveCurrency(CurrencyCreateDTO currency) {
        if (currency.getCode() == null || currency.getCode().isEmpty()) {
            throw new ValidationException("Код валюты не может быть пустым.");
        }
        CurrencyEntity currencyEntity = CurrencyMapper.mapToEntity(currency);

        CurrencyEntity savedCurrency = currencyDAO.saveCurrency(currencyEntity);

        return CurrencyMapper.mapToResponseDTO(savedCurrency);
    }
}
