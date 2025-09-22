package org.example.currencyexchangeproject.DAO;

import org.example.currencyexchangeproject.ConnectionManager.ConnectionPool;
import org.example.currencyexchangeproject.DTO.ExchangeRateDTO;
import org.example.currencyexchangeproject.DTO.ExchangeRateFilter;
import org.example.currencyexchangeproject.DTO.ExchangeRateResponseDTO;
import org.example.currencyexchangeproject.Entity.CurrencyEntity;
import org.example.currencyexchangeproject.Entity.ExchangeRateEntity;
import org.example.currencyexchangeproject.Mappers.CurrencyMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ExchangeRateDAO {
    private static final String FIND_ALL_SQL = """
            SELECT exchangerates.id,
                   BaseCurrencyId,
                   TargetCurrencyId,
                   Rate,
                   b.code base_Code,
                   b.fullname base_FullName,
                   b.sign base_Sign,
                   t.code target_Code,
                   t.fullname target_FullName,
                   t.sign target_sign
            FROM exchangerates
                     JOIN public.currencies b
                          on b.id = exchangerates.basecurrencyid
                     JOIN public.currencies t
                          on t.id = exchangerates.targetcurrencyid
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
                WHERE exchangerates.id = ?;
            """;
    private static final String CREATE_EXCHANGE_RATE_SQL = """
                 INSERT INTO ExchangeRates (BaseCurrencyID, TargetCurrencyID, Rate)\s
                 VALUES (?, ?, ?)
            \s""";
    private static final String UPDATE_EXCHANGE_RATE_SQL = """
                         UPDATE ExchangeRates
                         SET basecurrencyid = ?, targetcurrencyid = ?, rate = ?\s
                         WHERE id = ?;
            \s""";

    public Optional<ExchangeRateResponseDTO> createExchangeRate(ExchangeRateDTO rateDTO) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_EXCHANGE_RATE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, rateDTO.getBaseCurrency().getId());
            statement.setInt(2, rateDTO.getTargetCurrency().getId());
            statement.setBigDecimal(3, rateDTO.getExchangeRate());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                ExchangeRateResponseDTO responseDTO = new ExchangeRateResponseDTO(
                        rs.getInt("id"),
                        rateDTO.getBaseCurrency(),
                        rateDTO.getTargetCurrency(),
                        rateDTO.getExchangeRate()
                );
                return Optional.of(responseDTO);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateExchangeRate(ExchangeRateEntity exchangeRateEntity) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_EXCHANGE_RATE_SQL)) {
            statement.setInt(1, exchangeRateEntity.getBaseCurrencyEntity().getId());
            statement.setInt(2, exchangeRateEntity.getTargetCurrencyEntity().getId());
            statement.setBigDecimal(3, exchangeRateEntity.getRate());
            statement.setInt(4, exchangeRateEntity.getId());
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ExchangeRateResponseDTO> getAllExchnageRates(ExchangeRateFilter filter) {
        List<ExchangeRateResponseDTO> exchangeRateResponseDTOList = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();


        if (filter.getBaseCurrencyCodeEquals() != null) {
            whereSql.add("b.code = ?");
            parameters.add(filter.getBaseCurrencyCodeEquals());
        }
        if (filter.getTargetCurrencyCodeEquals() != null) {
            whereSql.add("t.code = ?\n");
            parameters.add(filter.getTargetCurrencyCodeEquals());
        }

        String whereClause = whereSql.stream()
                .collect(Collectors.joining(" AND ", " WHERE ", ""));

        String sql = FIND_ALL_SQL + whereClause + """
                LIMIT ?
                OFFSET ?
                """;

        parameters.add(filter.getLimit());
        parameters.add(filter.getOffset());

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                exchangeRateResponseDTOList.add(toResponseDTO(mapRow(rs)));
            }
            return exchangeRateResponseDTOList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ExchangeRateResponseDTO> getAllExchangeRates() {
        List<ExchangeRateResponseDTO> exchangeRateEntities = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL)) {
            ResultSet allExchangeRates = statement.executeQuery();
            while (allExchangeRates.next()) {
                exchangeRateEntities.add(toResponseDTO(mapRow(allExchangeRates)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return exchangeRateEntities;
    }

    public Optional<ExchangeRateEntity> getExchangeRateById(int id) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, id);
            ResultSet findExchangeRate = statement.executeQuery();

            if (findExchangeRate.next()) {
                return Optional.of(mapRow(findExchangeRate));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске курса валют с id=" + id, e);
        }
    }

    private ExchangeRateEntity mapRow(ResultSet rs) throws SQLException {
        CurrencyEntity baseCurrencyEntity = CurrencyEntity.builder()
                .withId(rs.getInt("BaseCurrencyId"))
                .withCode(rs.getString("base_Code"))
                .withFullName(rs.getString("base_FullName"))
                .withSign(rs.getString("base_Sign")).build();
        CurrencyEntity targetCurrencyEntity = CurrencyEntity.builder()
                .withId(rs.getInt("TargetCurrencyId"))
                .withCode(rs.getString("Target_Code"))
                .withFullName(rs.getString("Target_FullName"))
                .withSign(rs.getString("Target_Sign")).build();
        return ExchangeRateEntity.builder()
                .id(rs.getInt("id"))
                .Rate(rs.getBigDecimal("rate"))
                .baseCurrencyEntity(baseCurrencyEntity)
                .targetCurrencyEntity(targetCurrencyEntity).build();
    }

    private ExchangeRateResponseDTO toResponseDTO(ExchangeRateEntity entity) {
        if (entity == null) {
            return null;
        }
        return new ExchangeRateResponseDTO(
                entity.getId(),
                CurrencyMapper.mapToResponseDTO(entity.getBaseCurrencyEntity()),
                CurrencyMapper.mapToResponseDTO(entity.getTargetCurrencyEntity()),
                entity.getRate()
        );
    }
}
