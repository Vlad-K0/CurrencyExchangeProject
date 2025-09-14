package org.example.currencyexchangeproject.Model;

import java.util.Objects;

public class Currency {
    Integer id;
    String code;
    String FullName;
    String Sign;

    private Currency() {}

    public static CurrencyBuilderImpl builder() {
        return new CurrencyBuilderImpl();
    }

    public interface CurrencyBuilder{
        CurrencyBuilder withId(Integer id);
        CurrencyBuilder withCode(String code);
        CurrencyBuilder withFullName(String fullName);
        CurrencyBuilder withSign(String sign);
        Currency build();
    }

    @Override
    public String toString() {
        return "Currency{" +
               "id=" + id +
               ", code='" + code + '\'' +
               ", FullName='" + FullName + '\'' +
               ", Sign='" + Sign + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return Objects.equals(id, currency.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public static class CurrencyBuilderImpl implements CurrencyBuilder{
        private final Currency currency = new Currency();

        @Override
        public CurrencyBuilder withId(Integer id) {
            currency.id = id;
            return this;
        }

        @Override
        public CurrencyBuilder withCode(String code) {
            currency.code = code;
            return this;
        }

        @Override
        public CurrencyBuilder withFullName(String fullName) {
            currency.FullName = fullName;
            return this;
        }

        @Override
        public CurrencyBuilder withSign(String sign) {
            currency.Sign = sign;
            return this;
        }

        @Override
        public Currency build() {
            return currency;
        }
    }
}