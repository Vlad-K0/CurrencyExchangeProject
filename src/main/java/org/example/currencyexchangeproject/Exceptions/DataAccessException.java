package org.example.currencyexchangeproject.Exceptions;

import java.sql.SQLException;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String s, SQLException e) {
        super(s, e);
    }
}
