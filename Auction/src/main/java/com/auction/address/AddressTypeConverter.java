package com.auction.address;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class AddressTypeConverter implements AttributeConverter<AddressType, String> {
 
    @Override
    public String convertToDatabaseColumn(AddressType addressType) {
        if (addressType == null) {
            return null;
        }
        return addressType.getCode();
    }

    @Override
    public AddressType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(AddressType.values())
          .filter(c -> c.getCode().equals(code))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}