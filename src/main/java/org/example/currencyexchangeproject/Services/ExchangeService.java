package org.example.currencyexchangeproject.Services;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.BadRequestException;
import org.example.currencyexchangeproject.DAO.ExchangeRateDAO;
import org.example.currencyexchangeproject.DTO.CurrencyResponseDTO;
import org.example.currencyexchangeproject.DTO.ExchangeRateResponseDTO;
import org.example.currencyexchangeproject.DTO.ExchangeResponseDTO;
import org.example.currencyexchangeproject.Exceptions.NotFoundDataException;
import org.example.currencyexchangeproject.Mappers.ExchangeRateMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class ExchangeService {
    private static final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private static final CurrencyService currencyService = new CurrencyService();

    private final int SCALE = 6;
    private final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    public ExchangeResponseDTO exchangeCurrency(String fromCurrency, String toCurrency, BigDecimal amount) {
        CurrencyResponseDTO base = currencyService.getCurrencyByCode(fromCurrency);
        CurrencyResponseDTO target = currencyService.getCurrencyByCode(toCurrency);
        BigDecimal exchangeRate = getExchangeRate(fromCurrency, toCurrency);
        BigDecimal convertedAmount = amount.multiply(exchangeRate).setScale(SCALE, ROUNDING_MODE);
        return new ExchangeResponseDTO(base, target, exchangeRate, amount, convertedAmount);
    }

    private BigDecimal getExchangeRate(String fromCurrency, String toCurrency) {
        return findDirectRate(fromCurrency, toCurrency)
                .or(() -> findInverseRate(fromCurrency, toCurrency))
                .or(() -> findCrossRate(fromCurrency, toCurrency))
                .orElseThrow(() -> new NotFoundDataException("Курс " + fromCurrency + "/" + toCurrency + " не найден ни одним из методов."));
    }

    private Optional<BigDecimal> findDirectRate(String fromCurrency, String toCurrency) {
        String directCode = fromCurrency + toCurrency;

        return exchangeRateService.getExchangeRateByCode(directCode)
                .map(ExchangeRateResponseDTO::getRate);
    }

    private Optional<BigDecimal> findInverseRate(String fromCurrency, String toCurrency) {
        String inverseCode = toCurrency + fromCurrency;

        return exchangeRateService.getExchangeRateByCode(inverseCode)
                .map(inverseDTO -> {
                    // 1. Извлекаем курс из DTO
                    BigDecimal inverseRate = inverseDTO.getRate();

                    // 2. Инвертируем его для получения прямого курса (A/B = 1 / B/A)
                    return BigDecimal.ONE.divide(
                            inverseRate,
                            SCALE,
                            ROUNDING_MODE
                    );
                });
    }

    private Optional<BigDecimal> findCrossRate(String fromCurrency, String toCurrency) {
        String usdCode = "USD";

        // Получаем Optional<DTO> для USD/A и USD/B
        Optional<ExchangeRateResponseDTO> usdToA_Optional = exchangeRateService.getExchangeRateByCode(usdCode + fromCurrency);
        Optional<ExchangeRateResponseDTO> usdToB_Optional = exchangeRateService.getExchangeRateByCode(usdCode + toCurrency);

        // 1. Используем flatMap (или if-present) для безопасного извлечения курсов
        return usdToA_Optional
                .flatMap(usdToA_DTO -> {
                    return usdToB_Optional.map(usdToB_DTO -> {
                        BigDecimal rateUSDtoA = usdToA_DTO.getRate();
                        BigDecimal rateUSDtoB = usdToB_DTO.getRate();

                        return rateUSDtoB.divide(rateUSDtoA, SCALE, ROUNDING_MODE);
                    });
                });
    }
}

