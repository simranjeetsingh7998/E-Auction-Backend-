package com.auction.properties;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class PropertiesStatusConverter implements AttributeConverter<PropertiesStatus, String> {
 
    @Override
    public String convertToDatabaseColumn(PropertiesStatus propertiesStatus) {
        if (propertiesStatus == null) {
            return null;
        }
        return propertiesStatus.getStatus();
    }

    @Override
    public PropertiesStatus convertToEntityAttribute(String status) {
        if (status == null) {
            return null;
        }

        return Stream.of(PropertiesStatus.values())
          .filter(s -> s.getStatus().equals(status))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}