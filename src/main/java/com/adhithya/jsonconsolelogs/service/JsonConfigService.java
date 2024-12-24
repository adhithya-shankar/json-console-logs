package com.adhithya.jsonconsolelogs.service;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;

import com.adhithya.jsonconsolelogs.factory.UtilsFactory;
import com.adhithya.jsonconsolelogs.models.JsonLogConfig;
import com.adhithya.jsonconsolelogs.utils.CommonUtils;

@Service
@State(
    name = "json-log-config",
    storages = {@Storage(value = "json-log-config.xml")})
public final class JsonConfigService implements PersistentStateComponent<JsonLogConfig> {

  private final JsonLogConfig state = new JsonLogConfig();
  private final CommonUtils commonUtils;

  public JsonConfigService() {
    this.commonUtils = UtilsFactory.getInstance().getCommonUtils();
  }

  public static JsonConfigService getInstance() {
    return ApplicationManager.getApplication().getService(JsonConfigService.class);
  }

  @Override
  public JsonLogConfig getState() {
    return state;
  }

  @Override
  public void loadState(JsonLogConfig state) {
    XmlSerializerUtil.copyBean(state, this.state);
  }

  public void updateState(JsonLogConfig newState) {
    commonUtils.computeIfNull(
        newState,
        () -> {
          state.setEnabled(newState.isEnabled());
          state.setProfiles(newState.getProfiles());
          state.setDefaultProfile(newState.getDefaultProfile());
          return null;
        });
  }
}
