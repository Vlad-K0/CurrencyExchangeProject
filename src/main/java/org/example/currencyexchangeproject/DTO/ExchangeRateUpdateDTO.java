package org.example.currencyexchangeproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeRateUpdateDTO {
    Integer id;
    BigDecimal exchangeRate;
}
