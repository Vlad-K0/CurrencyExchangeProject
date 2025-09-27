package org.example.currencyexchangeproject.Entity;

import java.util.Objects;

public class CurrencyEntity {
    Integer id;
    String name;
    String code;
    String Sign;

    private CurrencyEntity() {}

    public static CurrencyBuilderImpl builder() {
        return new CurrencyBuilderImpl();
    }

    public interface CurrencyBuilder{
        CurrencyBuilder withId(Integer id);
        CurrencyBuilder withCode(String code);
        CurrencyBuilder withFullName(String fullName);
        CurrencyBuilder withSign(String sign);
        CurrencyEntity build();
    }

    @Override
    public String toString() {
        return "Currency{" +
               "id=" + id +
               ", code='" + code + '\'' +
               ", FullName='" + name + '\'' +
               ", Sign='" + Sign + '\'' +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyEntity currencyEntity = (CurrencyEntity) o;
        return Objects.equals(id, currencyEntity.id);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }

    public static class CurrencyBuilderImpl implements CurrencyBuilder{
        private final CurrencyEntity currencyEntity = new CurrencyEntity();

        @Override
        public CurrencyBuilder withId(Integer id) {
            currencyEntity.id = id;
            return this;
        }

        @Override
        public CurrencyBuilder withCode(String code) {
            currencyEntity.code = code;
            return this;
        }

        @Override
        public CurrencyBuilder withFullName(String fullName) {
            currencyEntity.name = fullName;
            return this;
        }

        @Override
        public CurrencyBuilder withSign(String sign) {
            currencyEntity.Sign = sign;
            return this;
        }

        @Override
        public CurrencyEntity build() {
            return currencyEntity;
        }
    }
}