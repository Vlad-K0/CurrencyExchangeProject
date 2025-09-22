package org.example.currencyexchangeproject.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyFilter {
    Integer offset;
    Integer limit;
    String codeEquals;
}
