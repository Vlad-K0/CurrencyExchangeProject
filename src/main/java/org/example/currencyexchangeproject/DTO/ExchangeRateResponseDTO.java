package org.example.currencyexchangeproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
public class ExchangeRateResponseDTO {
    Integer id;
    CurrencyResponseDTO baseCurrency;
    CurrencyResponseDTO targetCurrency;
    BigDecimal rate;

    public ExchangeRateResponseDTO() {
        baseCurrency = new CurrencyResponseDTO();
        targetCurrency = new CurrencyResponseDTO();
    }
}
