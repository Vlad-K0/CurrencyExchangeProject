package org.example.currencyexchangeproject.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyFilter {
    int offset;
    int limit;
    Integer codeEquals;
}
