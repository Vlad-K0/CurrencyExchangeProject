package org.example.currencyexchangeproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@Builder
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
