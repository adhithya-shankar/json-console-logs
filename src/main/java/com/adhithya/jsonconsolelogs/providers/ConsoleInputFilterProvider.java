package com.adhithya.jsonconsolelogs.providers;

import java.util.List;
import java.util.Objects;

import com.intellij.execution.filters.ConsoleDependentInputFilterProvider;
import com.intellij.execution.filters.InputFilter;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;

import com.adhithya.jsonconsolelogs.filters.JSONFieldsFilter;
import com.adhithya.jsonconsolelogs.models.JsonLogConfig;
import com.adhithya.jsonconsolelogs.models.Profile;
import com.adhithya.jsonconsolelogs.service.ActiveProfileService;
import com.adhithya.jsonconsolelogs.service.JsonConfigService;

public class ConsoleInputFilterProvider extends ConsoleDependentInputFilterProvider {

  private JsonConfigService jsonConfigService;

  @Override
  public List<InputFilter> getDefaultFilters(
      ConsoleView consoleView, Project project, GlobalSearchScope globalSearchScope) {
    JsonLogConfig jsonLogConfig = JsonConfigService.getInstance().getState();

    if (Objects.nonNull(jsonLogConfig)) {
      Profile defaultProfile = jsonLogConfig.getDefaultProfile();
      ActiveProfileService.getInstance(project).setActiveProfile(defaultProfile, consoleView);
    }

    return List.of(new JSONFieldsFilter(consoleView, project));
  }
}
