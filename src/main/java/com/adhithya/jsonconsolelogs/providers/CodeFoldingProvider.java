package com.adhithya.jsonconsolelogs.providers;

import java.util.List;
import java.util.Objects;

import com.intellij.execution.ConsoleFolding;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;

import com.adhithya.jsonconsolelogs.models.JsonLogConfig;
import com.adhithya.jsonconsolelogs.models.Profile;
import com.adhithya.jsonconsolelogs.service.ActiveProfileService;
import com.adhithya.jsonconsolelogs.service.JsonConfigService;
import com.adhithya.jsonconsolelogs.utils.JSONUtils;

public class CodeFoldingProvider extends ConsoleFolding {

  private ConsoleView consoleView;

  @Override
  public boolean shouldFoldLine(Project project, String line) {
    return isEnabled(project) && JSONUtils.isValidJSON(line);
  }

  @Override
  public String getPlaceholderText(Project project, List<String> lines) {
    return "{..}";
  }

  @Override
  public boolean isEnabledForConsole(ConsoleView consoleView) {
    this.consoleView = consoleView;
    return true;
  }

  private boolean isEnabled(Project project) {
    JsonLogConfig state = JsonConfigService.getInstance().getState();
    return Objects.nonNull(state) && state.isEnabled() && isShowOriginalLog(project);
  }

  private boolean isShowOriginalLog(Project project) {
    Profile activeProfile = getActiveProfile(project);
    return Objects.nonNull(activeProfile) && activeProfile.isShowOriginalLog();
  }

  private Profile getActiveProfile(Project project) {
    ActiveProfileService activeProfileService = ActiveProfileService.getInstance(project);
    return activeProfileService.getActiveProfile(consoleView);
  }
}
