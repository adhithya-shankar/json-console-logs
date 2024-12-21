package com.adhithya.jsonconsolelogs.utils;

import java.util.function.Supplier;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JSONUtils {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  static {
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public static ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  public static boolean isValidJSON(String statement) {
    try {
      OBJECT_MAPPER.readTree(statement);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static <T> T readValue(String json, Supplier<T> defaultValueSupplier) {
    try {
      return OBJECT_MAPPER.readValue(json, new TypeReference<T>() {});
    } catch (Exception e) {
      return defaultValueSupplier.get();
    }
  }

  public static String writeValue(Object o, String defaultValue) {
    try {
      return OBJECT_MAPPER.writeValueAsString(o);
    } catch (Exception e) {
      return defaultValue;
    }
  }
}
