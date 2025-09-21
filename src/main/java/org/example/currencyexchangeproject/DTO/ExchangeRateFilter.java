package org.example.currencyexchangeproject.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateFilter {
    int limit;
    int offset;
    Integer baseCurrencyEquals;
    Integer targetCurrencyEquals;
}
