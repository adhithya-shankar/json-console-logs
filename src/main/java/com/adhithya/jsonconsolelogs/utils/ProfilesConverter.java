package com.adhithya.jsonconsolelogs.utils;

import java.util.List;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.Converter;

import com.adhithya.jsonconsolelogs.factory.UtilsFactory;
import com.adhithya.jsonconsolelogs.models.Profile;

import com.fasterxml.jackson.core.type.TypeReference;

public class ProfilesConverter extends Converter<List<Profile>> {

  private static final Logger logger = Logger.getInstance(ProfilesConverter.class);
  private final JSONUtils jsonUtils;

  public ProfilesConverter() {
    this.jsonUtils = UtilsFactory.getInstance().getJsonUtils();
  }

  @Override
  public List<Profile> fromString(String s) {
    try {
      return jsonUtils.getObjectMapper().readValue(s, new TypeReference<>() {});
    } catch (Exception e) {
      logger.error("Exception occurred while parsing profile. Falling back to empty object", e);
      return List.of();
    }
  }

  @Override
  public String toString(List<Profile> profiles) {
    return jsonUtils.writeValue(profiles, "[]");
  }
}
