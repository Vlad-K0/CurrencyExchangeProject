package org.example.currencyexchangeproject.DAO;

import org.example.currencyexchangeproject.ConnectionManager.ConnectionPool;
import org.example.currencyexchangeproject.DTO.ExchangeRateFilter;
import org.example.currencyexchangeproject.DTO.ExchangeRateResponseDTO;
import org.example.currencyexchangeproject.DTO.ExchangeRateUpdateDTO;
import org.example.currencyexchangeproject.Entity.CurrencyEntity;
import org.example.currencyexchangeproject.Entity.ExchangeRateEntity;
import org.example.currencyexchangeproject.Exceptions.DataAccessException;
import org.example.currencyexchangeproject.Exceptions.NotFoundDataException;
import org.example.currencyexchangeproject.Mappers.CurrencyMapper;
import org.example.currencyexchangeproject.Mappers.ExchangeRateMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
                         SET rate = ?
                         WHERE id = ?;
            \s""";

    public ExchangeRateEntity createExchangeRate(ExchangeRateEntity exchangeRateEntity) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(CREATE_EXCHANGE_RATE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, exchangeRateEntity.getBaseCurrencyEntity().getId());
            statement.setInt(2, exchangeRateEntity.getTargetCurrencyEntity().getId());
            statement.setBigDecimal(3, exchangeRateEntity.getRate());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();

            if (rs.next()) {
                return getExchangeRateById(rs.getInt("id"));
            } else {
                throw new NotFoundDataException("Запись вставлена, но сгенерированный обменный курс не был получен.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public ExchangeRateEntity updateExchangeRate(ExchangeRateEntity entityToUpdate) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_EXCHANGE_RATE_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setBigDecimal(1, entityToUpdate.getRate());
            statement.setInt(2, entityToUpdate.getId());

            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();

            if (rs.next()) {
                return getExchangeRateById(rs.getInt("id"));
            } else {
                throw new NotFoundDataException("Запись вставлена, но сгенерированный обменный курс не был получен.");
            }

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    public List<ExchangeRateEntity> getAllExchnageRates(ExchangeRateFilter filter) {
        List<ExchangeRateEntity> exchangeRateEntityList = new ArrayList<>();
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
                exchangeRateEntityList.add(ExchangeRateMapper.mapToEntity(rs));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        return exchangeRateEntityList;
    }

    public List<ExchangeRateEntity> getAllExchangeRates() {
        List<ExchangeRateEntity> exchangeRateEntities = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet allExchangeRates = statement.executeQuery();
            while (allExchangeRates.next()) {
                exchangeRateEntities.add(ExchangeRateMapper.mapToEntity(allExchangeRates));
            }

        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
        return exchangeRateEntities;
    }

    public ExchangeRateEntity getExchangeRateById(int id) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, id);
            ResultSet findExchangeRate = statement.executeQuery();

            if (findExchangeRate.next()) {
                return ExchangeRateMapper.mapToEntity(findExchangeRate);
            } else {
                throw new NotFoundDataException("Запись не найдена");
            }

        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при поиске курса валют с id=" + id, e);
        }
    }
}
