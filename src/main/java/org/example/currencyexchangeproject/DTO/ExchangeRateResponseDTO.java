package org.example.currencyexchangeproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
public class ExchangeRateResponseDTO {
    Integer id;
    CurrencyResponseDTO baseCurrencyDTO;
    CurrencyResponseDTO targetCurrencyDTO;
    BigDecimal Rate;

    public ExchangeRateResponseDTO() {
        baseCurrencyDTO = new CurrencyResponseDTO();
        targetCurrencyDTO = new CurrencyResponseDTO();
    }
}
