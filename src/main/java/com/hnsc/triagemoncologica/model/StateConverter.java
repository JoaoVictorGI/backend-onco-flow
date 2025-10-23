package com.hnsc.triagemoncologica.model;

import java.time.OffsetDateTime;

import jakarta.persistence.AttributeConverter;

public class StateConverter implements AttributeConverter<Boolean, OffsetDateTime> {

    @Override
    public OffsetDateTime convertToDatabaseColumn(Boolean attribute) {
        return attribute ? OffsetDateTime.now() : null;
    }

    @Override
    public Boolean convertToEntityAttribute(OffsetDateTime dbData) {
        return dbData instanceof OffsetDateTime;
    }

}
