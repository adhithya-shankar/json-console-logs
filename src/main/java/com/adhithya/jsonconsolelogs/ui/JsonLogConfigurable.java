package com.adhithya.jsonconsolelogs.ui;

import javax.swing.JComponent;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts.ConfigurableName;

import com.adhithya.jsonconsolelogs.factory.UtilsFactory;
import com.adhithya.jsonconsolelogs.models.JsonLogConfig;
import com.adhithya.jsonconsolelogs.service.JsonConfigService;
import com.adhithya.jsonconsolelogs.ui.form.JsonLogFormConfigForm;
import com.adhithya.jsonconsolelogs.utils.CommonUtils;

import org.jetbrains.annotations.Nullable;

public class JsonLogConfigurable implements Configurable {

  private final Project project;
  private final ConsoleView consoleView;
  private final CommonUtils commonUtils;
  private JsonLogFormConfigForm form;

  public JsonLogConfigurable(Project project, ConsoleView consoleView) {
    this.project = project;
    this.consoleView = consoleView;
    this.commonUtils = UtilsFactory.getInstance().getCommonUtils();
  }

  @Override
  public @Nullable JComponent createComponent() {
    form = commonUtils.computeIfNull(form, () -> new JsonLogFormConfigForm(project, consoleView));
    return form.getRootComponent();
  }

  @Override
  public boolean isModified() {
    return true;
  }

  @Override
  public @ConfigurableName String getDisplayName() {
    return "JSON Log Format";
  }

  @Override
  public void apply() throws ConfigurationException {
    commonUtils.logTimer(
        "JsonLogConfigurable.apply",
        () -> {
          JsonLogConfig updatedConfig = form.getUpdatedConfig();
          JsonConfigService.getInstance().updateState(updatedConfig);
          return null;
        });
  }
}
