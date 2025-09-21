package org.example.currencyexchangeproject.DAO;

import org.example.currencyexchangeproject.ConnectionManager.ConnectionPool;
import org.example.currencyexchangeproject.DTO.CurrencyFilter;
import org.example.currencyexchangeproject.Entity.CurrencyEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    public static CurrencyDAO getInstance(){
        return INSTANCE;
    }
    private CurrencyDAO() {}

    public Optional<CurrencyEntity> getCurrencyById(Integer id) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(FIND_BY_ID_SQL)) {

            statement.setInt(1, id);
            ResultSet findCurrency = statement.executeQuery();

            if (findCurrency.next()) {
                return Optional.of(mapRow(findCurrency));
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Не удалось получить валюту", e);
        }
    }

    public List<CurrencyEntity> getAllCurrencies() {
        List<CurrencyEntity> currencyEntities = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_SQL)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                currencyEntities.add(mapRow(resultSet));
            }

            return currencyEntities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CurrencyEntity> getAllCurrencies(CurrencyFilter filter) {
        List<CurrencyEntity> currencyEntities = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();
        List<String> whereSql = new ArrayList<>();

        if (filter.getCodeEquals() != null) {
            whereSql.add("code = ?");
            parameters.add(filter.getCodeEquals());
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
            for (int i = 0; i < whereSql.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                currencyEntities.add(mapRow(resultSet));
            }
            return currencyEntities;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean saveCurrency(CurrencyEntity currency) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(CREATE_CURRENCY_SQL)) {

            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("Ошибка при сохранении валюты: " + e.getMessage());
            throw new RuntimeException("Не удалось сохранить валюту", e);
        }
    }

    public boolean updateCurrency(CurrencyEntity currency) {
        try (var connection = ConnectionPool.getConnection();
             var statement = connection.prepareStatement(UPDATE_CURRENCY_SQL)) {

            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.setInt(4, currency.getId());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("Ошибка при сохранении валюты: " + e.getMessage());
            throw new RuntimeException("Не удалось сохранить валюту", e);
        }
    }

    private CurrencyEntity mapRow(ResultSet rs) throws SQLException {
        return CurrencyEntity.builder()
                .withId(rs.getInt("id"))
                .withCode(rs.getString("code"))
                .withFullName(rs.getString("fullname"))
                .withSign(rs.getString("sign"))
                .build();
    }
}
