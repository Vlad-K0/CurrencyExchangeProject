package org.example.currencyexchangeproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyCreateDTO {
    private String code;
    private String fullName;
    private String sign;
}
