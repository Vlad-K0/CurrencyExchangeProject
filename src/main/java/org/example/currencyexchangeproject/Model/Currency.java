package org.example.currencyexchangeproject.Model;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Currency {
    Integer id;
    String code;
    String FullName;
    String Sign;
}
