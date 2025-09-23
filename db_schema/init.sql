-- Создаем таблицу для хранения валют
CREATE TABLE Currencies (
    ID SERIAL PRIMARY KEY,
    Code VARCHAR(3) NOT NULL UNIQUE,
    FullName VARCHAR(255) NOT NULL,
    Sign VARCHAR(5) NOT NULL
);

-- Создаем таблицу для хранения курсов обмена
-- Я предположил, что таблица называется ExchangeRates
CREATE TABLE ExchangeRates (
    ID SERIAL PRIMARY KEY,
    BaseCurrencyId INT NOT NULL,
    TargetCurrencyId INT NOT NULL,
    Rate DECIMAL(10, 6) NOT NULL,
    -- Добавляем внешние ключи для целостности данных
    CONSTRAINT fk_base_currency FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies(ID),
    CONSTRAINT fk_target_currency FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies(ID)
);

-- Добавляем несколько начальных данных для примера
INSERT INTO Currencies (Code, FullName, Sign) VALUES
('USD', 'US Dollar', '$'),
('EUR', 'Euro', '€'),
('RUB', 'Russian Ruble', '₽');

-- Добавляем примерные курсы
-- (ID валют будут 1, 2, 3, так как мы только что их вставили)
INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES
(1, 2, 0.920000), -- USD to EUR
(1, 3, 90.500000), -- USD to RUB
(2, 1, 1.080000), -- EUR to USD
(2, 3, 98.200000); -- EUR to RUB