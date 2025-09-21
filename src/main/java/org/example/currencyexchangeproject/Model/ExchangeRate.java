package org.example.currencyexchangeproject.Model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangeRate {
    Integer id;
    Currency BaseCurrency;
    Currency TargetCurrency;
    BigDecimal Rate;
}
