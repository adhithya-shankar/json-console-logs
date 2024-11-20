package com.adhithya.jsonconsolelogs.utils;

import java.util.Objects;
import java.util.function.Supplier;

import com.intellij.openapi.diagnostic.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

  private static final Logger logger = Logger.getInstance(CommonUtils.class);

  public static <T> T computeIfNull(T obj, Supplier<T> function) {
    if (Objects.isNull(obj)) {
      return function.get();
    }
    return obj;
  }

  public static <T> T deepCopy(T object) {
    try {
      String string = JSONUtils.getObjectMapper().writeValueAsString(object);
      return JSONUtils.getObjectMapper().readValue(string, (Class<T>) object.getClass());
    } catch (JsonProcessingException e) {
      logger.error("Exception occurred while deserialization", e);
      return null;
    }
  }
}
