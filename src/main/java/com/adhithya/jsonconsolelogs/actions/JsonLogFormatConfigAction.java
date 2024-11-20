package com.adhithya.jsonconsolelogs.actions;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.util.IconLoader;

import com.adhithya.jsonconsolelogs.ui.JsonLogConfigurable;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JsonLogFormatConfigAction extends DumbAwareAction {

  private ConsoleView consoleView;

  public JsonLogFormatConfigAction(ConsoleView consoleView) {
    super(IconLoader.findIcon("assets/logo.svg", JsonLogFormatConfigAction.class.getClassLoader()));
    this.consoleView = consoleView;
  }

  @Override
  public void actionPerformed(AnActionEvent e) {
    JsonLogConfigurable instance = new JsonLogConfigurable(e.getProject(), consoleView);

    ShowSettingsUtil.getInstance()
        .editConfigurable(e.getProject(), "JsonLogFormatConfig", instance, false);
  }
}
