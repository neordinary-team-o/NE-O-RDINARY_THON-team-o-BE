package com.woojin.nerdinary_taem_o.common.converter;

import com.woojin.nerdinary_taem_o.domain.dig.entity.DigSnapshot;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tools.jackson.databind.ObjectMapper;

@Converter
public class DigSnapshotConverter implements AttributeConverter<DigSnapshot, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(DigSnapshot attribute) {
        if (attribute == null) return null;
        return MAPPER.writeValueAsString(attribute);
    }

    @Override
    public DigSnapshot convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return null;
        return MAPPER.readValue(dbData, DigSnapshot.class);
    }
}
