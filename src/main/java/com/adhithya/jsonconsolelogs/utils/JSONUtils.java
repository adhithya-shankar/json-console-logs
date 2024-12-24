package com.adhithya.jsonconsolelogs.utils;

import com.intellij.openapi.diagnostic.Logger;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtils {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final Logger logger = Logger.getInstance(JSONUtils.class);

  static {
    OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  public ObjectMapper getObjectMapper() {
    return OBJECT_MAPPER;
  }

  public boolean isValidJSON(String statement) {
    try {
      getObjectMapper().readTree(statement);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String writeValue(Object o, String defaultValue) {
    try {
      return getObjectMapper().writeValueAsString(o);
    } catch (Exception e) {
      logger.error("Exception occurred while generating json string", e);
      return defaultValue;
    }
  }
}
