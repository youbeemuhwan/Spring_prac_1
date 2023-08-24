package com.practice.simpleWeb.Converter;

import javax.persistence.AttributeConverter;
import java.util.Objects;

public class BooleanConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return attribute ? "YES" : "NO";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return ("YES".equals(dbData));
    }
}
