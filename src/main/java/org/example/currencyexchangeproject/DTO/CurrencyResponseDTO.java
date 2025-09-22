package org.example.currencyexchangeproject.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponseDTO {
    Integer id;
    String code;
    String name;
    String sign;
}
