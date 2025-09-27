package org.example.currencyexchangeproject.DAO;

import org.example.currencyexchangeproject.ConnectionManager.ConnectionPool;
import org.example.currencyexchangeproject.DTO.CurrencyFilter;
import org.example.currencyexchangeproject.Entity.CurrencyEntity;
import org.example.currencyexchangeproject.Exceptions.DataAccessException;
import org.example.currencyexchangeproject.Exceptions.NotFoundDataException;
import org.example.currencyexchangeproject.Mappers.CurrencyMapper;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class CurrencyDAO {
    private static final String FIND_ALL_SQL = """
            SELECT
                id,
                code,
                fullname,
                sign
            FROM currencies
            """;
    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
                    WHERE id = ?
            """;
    private static final String CREATE_CURRENCY_SQL = """
                    INSERT INTO currencies (code, fullname, sign) VALUES (?, ?, ?)
            """;
    private static final String UPDATE_CURRENCY_SQL = """
                    UPDATE currencies
                    SET code = ?, fullname = ?, sign = ? WHERE id = ?
            """;

    private static final CurrencyDAO INSTANCE = new CurrencyDAO();

    public static CurrencyDAO getInstance() {
        return INSTANCE;
    }

    private CurrencyDAO() {
    }

    public Optional<CurrencyEntity> getCurrencyById(Integer id) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, id);
            ResultSet findCurrency = statement.executeQuery();

            if (findCurrency.next()) {
                return Optional.ofNullable(CurrencyMapper.mapToEntity(findCurrency));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            System.err.println("Произошла ошибка при доступе к БД: " + e.getMessage());
            throw new DataAccessException("Ошибка при доступе к данным валюты.", e);
        }
    }

    public List<CurrencyEntity> getAllCurrencies() {
        List<CurrencyEntity> currencyEntities = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                currencyEntities.add(CurrencyMapper.mapToEntity(resultSet));
            }

            return currencyEntities;
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при доступе к БД: " + e.getMessage());
            throw new DataAccessException("Ошибка при доступе к данным валюты.", e);
        }
    }

    public List<CurrencyEntity> getAllCurrencies(CurrencyFilter filter) {
        List<CurrencyEntity> entities = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();

        if (filter.getCodeEquals() != null) {
            whereSql.add("code = ?\n");
            parameters.add(filter.getCodeEquals());
        }

        String whereClause = whereSql.stream()
                .collect(Collectors.joining(" AND ", " WHERE ", ""));

        int limit = filter.getLimit() != null ? filter.getLimit() : Integer.MAX_VALUE;

        int offset = filter.getOffset() != null ? filter.getOffset() : 0;

        String sql = FIND_ALL_SQL + whereClause + """
                LIMIT ?
                OFFSET ?
                """;

        parameters.add(limit);
        parameters.add(offset);

        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                entities.add(CurrencyMapper.mapToEntity(resultSet));
            }
            return entities;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public CurrencyEntity saveCurrency(CurrencyEntity currency) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(CREATE_CURRENCY_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getName());
            statement.setString(3, currency.getSign());

            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return CurrencyMapper.mapToEntity(resultSet);
            } else {
                throw new NotFoundDataException("Ошибка получения обновленной валюты");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при сохранении валюты: " + e.getMessage());
            throw new DataAccessException("Не удалось сохранить валюту", e);
        }
    }

    public CurrencyEntity updateCurrency(CurrencyEntity currency) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(UPDATE_CURRENCY_SQL, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getName());
            statement.setString(3, currency.getSign());
            statement.setInt(4, currency.getId());


            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                return CurrencyMapper.mapToEntity(resultSet);
            } else {
                throw new NotFoundDataException("Ошибка получения обновленной валюты");
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при доступе к БД: " + e.getMessage());
            throw new DataAccessException("Ошибка при доступе к данным валюты.", e);
        }
    }


}
