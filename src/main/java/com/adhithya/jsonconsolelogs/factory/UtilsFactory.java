package com.adhithya.jsonconsolelogs.factory;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;

import com.adhithya.jsonconsolelogs.utils.CommonUtils;
import com.adhithya.jsonconsolelogs.utils.ConfigUtils;
import com.adhithya.jsonconsolelogs.utils.JSONUtils;

import lombok.Getter;

@Service
public final class UtilsFactory {

  @Getter private final JSONUtils jsonUtils;

  @Getter private final CommonUtils commonUtils;

  @Getter private final ConfigUtils configUtils;

  public UtilsFactory() {
    jsonUtils = new JSONUtils();
    configUtils = new ConfigUtils();
    commonUtils = new CommonUtils(jsonUtils, configUtils);
  }

  public static UtilsFactory getInstance() {
    return ApplicationManager.getApplication().getService(UtilsFactory.class);
  }
}
