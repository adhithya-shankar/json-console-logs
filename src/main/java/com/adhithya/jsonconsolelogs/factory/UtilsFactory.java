package com.adhithya.jsonconsolelogs.factory;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;

import com.adhithya.jsonconsolelogs.utils.CommonUtils;
import com.adhithya.jsonconsolelogs.utils.ConfigUtils;
import com.adhithya.jsonconsolelogs.utils.JSONUtils;

import lombok.Getter;

@Service
public final class UtilsFactory {

  @Getter private final CommonUtils commonUtils = new CommonUtils();

  @Getter private final JSONUtils jsonUtils = new JSONUtils();

  @Getter private final ConfigUtils configUtils = new ConfigUtils();

  public static UtilsFactory getInstance() {
    return ApplicationManager.getApplication().getService(UtilsFactory.class);
  }
}
