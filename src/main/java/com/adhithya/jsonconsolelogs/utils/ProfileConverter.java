package com.adhithya.jsonconsolelogs.utils;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.xmlb.Converter;

import com.adhithya.jsonconsolelogs.models.Profile;

import com.fasterxml.jackson.core.type.TypeReference;

public class ProfileConverter extends Converter<Profile> {

  private static final Logger logger = Logger.getInstance(ProfileConverter.class);

  @Override
  public Profile fromString(String s) {
    try {
      return JSONUtils.getObjectMapper().readValue(s, new TypeReference<>() {});
    } catch (Exception e) {
      logger.error("Exception occurred while parsing profile. Falling back to empty object", e);
      return Profile.builder().build();
    }
  }

  @Override
  public String toString(Profile profile) {
    return JSONUtils.writeValue(profile, "{}");
  }
}
