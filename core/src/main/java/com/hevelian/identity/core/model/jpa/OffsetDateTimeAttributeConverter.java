package com.hevelian.identity.core.model.jpa;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
// TODO check time conversion
public class OffsetDateTimeAttributeConverter
    implements AttributeConverter<OffsetDateTime, Timestamp> {

  @Override
  public Timestamp convertToDatabaseColumn(OffsetDateTime offsetDateTime) {
    return (offsetDateTime == null ? null
        : Timestamp.valueOf(LocalDateTime.ofInstant(offsetDateTime.toInstant(), ZoneOffset.UTC)));
  }

  @Override
  public OffsetDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
    return (sqlTimestamp == null ? null
        : OffsetDateTime.ofInstant(sqlTimestamp.toInstant(), ZoneOffset.UTC));
  }
}
