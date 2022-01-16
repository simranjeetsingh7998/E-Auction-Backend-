package com.auction.preparation;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class AuctionStatusConverter implements AttributeConverter<AuctionStatus, String> {
 
    @Override
    public String convertToDatabaseColumn(AuctionStatus auctionStatus) {
        if (auctionStatus == null) {
            return null;
        }
        return auctionStatus.getStatus();
    }

    @Override
    public AuctionStatus convertToEntityAttribute(String status) {
        if (status == null) {
            return null;
        }

        return Stream.of(AuctionStatus.values())
          .filter(s -> s.getStatus().equals(status))
          .findFirst()
          .orElseThrow(IllegalArgumentException::new);
    }
}