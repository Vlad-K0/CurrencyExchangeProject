package org.example.currencyexchangeproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.currencyexchangeproject.Entity.CurrencyEntity;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateResponseDTO {
    Integer id;
    CurrencyResponseDTO baseCurrencyEntity;
    CurrencyResponseDTO targetCurrencyEntity;
    BigDecimal Rate;
}
