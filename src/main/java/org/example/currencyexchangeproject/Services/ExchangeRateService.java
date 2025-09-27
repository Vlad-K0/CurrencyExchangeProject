package org.example.currencyexchangeproject.Services;

import org.example.currencyexchangeproject.DAO.ExchangeRateDAO;
import org.example.currencyexchangeproject.DTO.ExchangeRateDTO;
import org.example.currencyexchangeproject.DTO.ExchangeRateFilter;
import org.example.currencyexchangeproject.DTO.ExchangeRateResponseDTO;
import org.example.currencyexchangeproject.Entity.ExchangeRateEntity;
import org.example.currencyexchangeproject.Exceptions.NotFoundDataException;
import org.example.currencyexchangeproject.Mappers.ExchangeRateMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    private static final ExchangeRateDAO exchangeRateDAO = new ExchangeRateDAO();

    public List<ExchangeRateResponseDTO> getAllExchangeRates() {
        List<ExchangeRateResponseDTO> responseDTOList = new ArrayList<>();

        List<ExchangeRateEntity> responseEntities = exchangeRateDAO.getAllExchangeRates();
        for (ExchangeRateEntity entity : responseEntities) {
            responseDTOList.add(ExchangeRateMapper.mapToResponseDTO(entity));
        }
        if (responseDTOList.isEmpty()) throw new NotFoundDataException("No exchange rates found");

        return responseDTOList;
    }

    public Optional<ExchangeRateResponseDTO> getExchangeRateByCode(String code) {
        String baseCurrencyCode = code.substring(0, 3);
        String targetCurrencyCode = code.substring(3, 6);
        ExchangeRateFilter filterByCode = ExchangeRateFilter.builder()
                .baseCurrencyCodeEquals(baseCurrencyCode)
                .targetCurrencyCodeEquals(targetCurrencyCode)
                .limit(Integer.MAX_VALUE)
                .offset(0).build();
        List<ExchangeRateEntity> exchangeRateResponseDTO = exchangeRateDAO.getAllExchnageRates(filterByCode);
        List<ExchangeRateResponseDTO> responseDTOList = new ArrayList<>();
        for (ExchangeRateEntity entity : exchangeRateResponseDTO) {
            responseDTOList.add(ExchangeRateMapper.mapToResponseDTO(entity));
        }
        if (responseDTOList.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(responseDTOList.getFirst());
    }

    public ExchangeRateResponseDTO saveExchangeRate(ExchangeRateDTO exchangeRateDTO) {
        ExchangeRateEntity targetExchangeRate = ExchangeRateMapper.mapToEntity(exchangeRateDTO);
        ExchangeRateEntity savedRate = exchangeRateDAO.createExchangeRate(targetExchangeRate);
        return ExchangeRateMapper.mapToResponseDTO(savedRate);
    }

    public ExchangeRateResponseDTO updateExchangeRate(String code, BigDecimal rate) {
        ExchangeRateResponseDTO findRate = getExchangeRateByCode(code).orElseThrow(() -> new NotFoundDataException("Обменный курс не найден"));
        findRate.setRate(rate);
        ExchangeRateEntity updatedEntity = ExchangeRateMapper.mapToEntity(findRate);
        ExchangeRateEntity exchangeRateUpdateDTO = exchangeRateDAO.updateExchangeRate(updatedEntity);

        return ExchangeRateMapper.mapToResponseDTO(exchangeRateUpdateDTO);
    }
}
