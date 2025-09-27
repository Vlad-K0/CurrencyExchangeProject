package org.example.currencyexchangeproject.Mappers;

import org.example.currencyexchangeproject.DTO.CurrencyResponseDTO;
import org.example.currencyexchangeproject.DTO.ExchangeRateDTO;
import org.example.currencyexchangeproject.DTO.ExchangeRateResponseDTO;
import org.example.currencyexchangeproject.DTO.ExchangeRateUpdateDTO;
import org.example.currencyexchangeproject.Entity.CurrencyEntity;
import org.example.currencyexchangeproject.Entity.ExchangeRateEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeRateMapper {
    private ExchangeRateMapper() {
    }

    public static ExchangeRateResponseDTO mapToResponseDTO(ExchangeRateEntity exchangeRateEntity) {
        ExchangeRateResponseDTO responseDTO = new ExchangeRateResponseDTO();

        CurrencyResponseDTO baseCurrencyDTO = responseDTO.getBaseCurrency();
        CurrencyResponseDTO targetCurrencyDTO = responseDTO.getTargetCurrency();

        CurrencyEntity baseCurrencyEntity = exchangeRateEntity.getBaseCurrencyEntity();
        CurrencyEntity targetCurrencyEntity = exchangeRateEntity.getTargetCurrencyEntity();

        baseCurrencyDTO.setId(baseCurrencyEntity.getId());
        baseCurrencyDTO.setCode(baseCurrencyEntity.getCode());
        baseCurrencyDTO.setName(baseCurrencyEntity.getFullName());
        baseCurrencyDTO.setSign(baseCurrencyEntity.getSign());

        targetCurrencyDTO.setId(targetCurrencyEntity.getId());
        targetCurrencyDTO.setCode(targetCurrencyEntity.getCode());
        targetCurrencyDTO.setName(targetCurrencyEntity.getFullName());
        targetCurrencyDTO.setSign(targetCurrencyEntity.getSign());

        responseDTO.setId(exchangeRateEntity.getId());
        responseDTO.setBaseCurrency(baseCurrencyDTO);
        responseDTO.setTargetCurrency(targetCurrencyDTO);
        responseDTO.setRate(exchangeRateEntity.getRate());

        return responseDTO;
    }


    public static ExchangeRateEntity mapToEntity(ExchangeRateResponseDTO exchangeRateDTO) {
        CurrencyResponseDTO baseCurrency = exchangeRateDTO.getBaseCurrency();
        CurrencyResponseDTO targetCurrency = exchangeRateDTO.getTargetCurrency();
        CurrencyEntity baseCurrencyEntity = CurrencyEntity.builder()
                .withId(baseCurrency.getId())
                .withCode(baseCurrency.getCode())
                .withFullName(baseCurrency.getName())
                .withSign(baseCurrency.getSign()).build();
        CurrencyEntity targetCurrencyEntity = CurrencyEntity.builder()
                .withId(targetCurrency.getId())
                .withCode(targetCurrency.getCode())
                .withFullName(targetCurrency.getName())
                .withSign(targetCurrency.getSign()).build();
        return ExchangeRateEntity.builder()
                .id(exchangeRateDTO.getId())
                .Rate(exchangeRateDTO.getRate())
                .baseCurrencyEntity(baseCurrencyEntity)
                .targetCurrencyEntity(targetCurrencyEntity).build();
    }

    public static ExchangeRateEntity mapToEntity(ExchangeRateDTO exchangeRateDTO) {
        CurrencyResponseDTO baseCurrency = exchangeRateDTO.getBaseCurrency();
        CurrencyResponseDTO targetCurrency = exchangeRateDTO.getTargetCurrency();
        CurrencyEntity baseCurrencyEntity = CurrencyEntity.builder()
                .withId(baseCurrency.getId())
                .withCode(baseCurrency.getCode())
                .withFullName(baseCurrency.getName())
                .withSign(baseCurrency.getSign()).build();
        CurrencyEntity targetCurrencyEntity = CurrencyEntity.builder()
                .withId(targetCurrency.getId())
                .withCode(targetCurrency.getCode())
                .withFullName(targetCurrency.getName())
                .withSign(targetCurrency.getSign()).build();
        return ExchangeRateEntity.builder()
                .Rate(exchangeRateDTO.getExchangeRate())
                .baseCurrencyEntity(baseCurrencyEntity)
                .targetCurrencyEntity(targetCurrencyEntity).build();
    }

    public static ExchangeRateEntity mapToEntity(ExchangeRateUpdateDTO exchangeRateUpdateDTO) {
        return ExchangeRateEntity.builder()
                .id(exchangeRateUpdateDTO.getId())
                .Rate(exchangeRateUpdateDTO.getExchangeRate())
                .build();
    }

    public static ExchangeRateEntity mapToEntity(ResultSet rs) throws SQLException {
        CurrencyEntity baseCurrencyEntity = CurrencyEntity.builder()
                .withId(rs.getInt("BaseCurrencyId"))
                .withCode(rs.getString("base_Code"))
                .withFullName(rs.getString("base_FullName"))
                .withSign(rs.getString("base_Sign")).build();
        CurrencyEntity targetCurrencyEntity = CurrencyEntity.builder()
                .withId(rs.getInt("TargetCurrencyId"))
                .withCode(rs.getString("Target_Code"))
                .withFullName(rs.getString("Target_FullName"))
                .withSign(rs.getString("Target_Sign")).build();
        return ExchangeRateEntity.builder()
                .id(rs.getInt("id"))
                .Rate(rs.getBigDecimal("rate"))
                .baseCurrencyEntity(baseCurrencyEntity)
                .targetCurrencyEntity(targetCurrencyEntity).build();
    }


}
