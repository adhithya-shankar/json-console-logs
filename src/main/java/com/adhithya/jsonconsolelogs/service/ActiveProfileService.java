package com.adhithya.jsonconsolelogs.service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import com.adhithya.jsonconsolelogs.models.Profile;

@Service(Service.Level.PROJECT)
public final class ActiveProfileService {

  private static final Logger logger = Logger.getInstance(ActiveProfileService.class);

  private final Map<ConsoleView, Profile> activeProfileMap = new ConcurrentHashMap<>();
  private final Map<ConsoleView, RunConfigurationBase<?>> consoleMap = new ConcurrentHashMap<>();
  private final Map<RunConfigurationBase<?>, Profile> runConfigurationProfileMap =
      new ConcurrentHashMap<>();
  private RunConfigurationBase<?> lastRunConfiguration = null;

  public static ActiveProfileService getInstance(Project project) {
    return project.getService(ActiveProfileService.class);
  }

  /**
   * Invoked by ExecutionListener
   *
   * <p>Updates a reference of runConfigurationBase. This is the unique object between multiple runs
   * of the same console window
   *
   * <p>A new ConsoleView is generated everytime, a run is triggered
   *
   * @param runConfigurationBase
   */
  public void updateLastRunConfiguration(RunConfigurationBase<?> runConfigurationBase) {
    this.lastRunConfiguration = runConfigurationBase;
  }

  /**
   * Updated by JsonLogConfigForm
   *
   * <p>Updates the active profile for the current console
   *
   * @param consoleView
   * @param profile
   */
  public void updateActiveProfile(ConsoleView consoleView, Profile profile) {
    if (Objects.nonNull(consoleView) && Objects.nonNull(profile)) {
      RunConfigurationBase<?> runConfigurationBase = consoleMap.get(consoleView);
      if (Objects.nonNull(runConfigurationBase)) {
        runConfigurationProfileMap.put(runConfigurationBase, profile);
      }
    }
  }

  // invoked by filter provider

  /**
   * Invoked by the filter provider
   *
   * <p>Sets the active profile for the current console, if it has not been set already
   *
   * <p>If set already, then this is a subsequent run of the console
   *
   * @param profile
   * @param consoleView
   */
  public void setActiveProfile(Profile profile, ConsoleView consoleView) {
    if (Objects.isNull(lastRunConfiguration)) {
      return;
    }
    consoleMap.put(consoleView, lastRunConfiguration);
    runConfigurationProfileMap.computeIfAbsent(
        lastRunConfiguration,
        (runConfiguration) -> {
          return profile;
        });
  }

  /**
   * Invoked by filter to apply the current active profile
   *
   * <p>Invoked by JsonLogConfigForm to render the UI
   *
   * @param consoleView
   * @return
   */
  public Profile getActiveProfile(ConsoleView consoleView) {
    if (Objects.nonNull(consoleView) && consoleMap.containsKey(consoleView)) {
      RunConfigurationBase<?> runConfigurationBase = consoleMap.get(consoleView);
      return runConfigurationProfileMap.getOrDefault(runConfigurationBase, Profile.DEFAULT);
    }
    return JsonConfigService.getInstance().getState().getDefaultProfile();
  }

  // invoked by form

  /**
   * Invoked by JsonLogConfig form, when the profile removed is also the active profile
   *
   * <p>The associated profile is removed from other consoles as well
   *
   * @param currentConsole
   */
  public void removeActiveProfile(ConsoleView currentConsole) {
    if (Objects.nonNull(currentConsole)) {
      RunConfigurationBase<?> runConfigurationBase = consoleMap.get(currentConsole);
      if (Objects.nonNull(runConfigurationBase)) {
        Profile staleProfile = runConfigurationProfileMap.get(runConfigurationBase);
        if (Objects.nonNull(staleProfile)) {
          runConfigurationProfileMap.forEach(
              (runConfiguration, activeProfile) -> {
                if (Objects.equals(staleProfile, activeProfile)) {
                  runConfigurationProfileMap.remove(runConfiguration);
                }
              });
        }
      }
    }
  }
}
