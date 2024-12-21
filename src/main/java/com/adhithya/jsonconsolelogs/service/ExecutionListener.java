package com.adhithya.jsonconsolelogs.service;

import java.util.Objects;

import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.diagnostic.Logger;

import org.jetbrains.annotations.NotNull;

public class ExecutionListener implements com.intellij.execution.ExecutionListener {

  private static final Logger logger = Logger.getInstance(ExecutionListener.class);

  @Override
  public void processStartScheduled(@NotNull String executorId, @NotNull ExecutionEnvironment env) {
    com.intellij.execution.ExecutionListener.super.processStartScheduled(executorId, env);
    if (Objects.nonNull(env.getProject())) {
      ActiveProfileService activeProfileService =
          ActiveProfileService.getInstance(env.getProject());
      if (env.getRunProfile() instanceof RunConfigurationBase<?> runProfile) {
        activeProfileService.updateLastRunConfiguration(runProfile);
      }
    }
  }
}
