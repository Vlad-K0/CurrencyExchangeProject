package org.example.currencyexchangeproject.Entity;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangeRateEntity {
    Integer id;
    CurrencyEntity baseCurrencyEntity;
    CurrencyEntity targetCurrencyEntity;
    BigDecimal Rate;
}
