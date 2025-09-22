package org.example.currencyexchangeproject.Mappers;

import org.example.currencyexchangeproject.DTO.CurrencyResponseDTO;
import org.example.currencyexchangeproject.Entity.CurrencyEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CurrencyMapper {

    private CurrencyMapper() {
    }

    public static CurrencyEntity mapToEntity(ResultSet rs) throws SQLException {
        return CurrencyEntity.builder()
                .withId(rs.getInt("id"))
                .withCode(rs.getString("code"))
                .withFullName(rs.getString("fullname"))
                .withSign(rs.getString("sign"))
                .build();
    }

    public static CurrencyResponseDTO mapToResponseDTO(CurrencyEntity entity) {
        if (entity == null) {
            return null;
        }
        return new CurrencyResponseDTO(
                entity.getId(),
                entity.getCode(),
                entity.getFullName(),
                entity.getSign()
        );
    }

    public static List<CurrencyResponseDTO> mapToListDTO(List<CurrencyEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>(); // Возвращаем пустой список, а не null
        }
        return entities.stream()
                .map(CurrencyMapper::mapToResponseDTO)
                .collect(Collectors.toList());
    }
}
