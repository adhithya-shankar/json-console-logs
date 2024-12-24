package com.adhithya.jsonconsolelogs.filters;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.intellij.execution.filters.InputFilter;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;

import com.adhithya.jsonconsolelogs.models.FieldConfig;
import com.adhithya.jsonconsolelogs.models.JsonLogConfig;
import com.adhithya.jsonconsolelogs.models.Profile;
import com.adhithya.jsonconsolelogs.service.ActiveProfileService;
import com.adhithya.jsonconsolelogs.service.JsonConfigService;
import com.adhithya.jsonconsolelogs.utils.CommonUtils;
import com.adhithya.jsonconsolelogs.utils.JSONUtils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.collections.CollectionUtils;

public class JSONFieldsFilter implements InputFilter {

  private static final Logger logger = Logger.getInstance(JSONFieldsFilter.class);
  private static final String LINE_BREAK = "\n";
  private static final String SPACE = " ";

  private final ConsoleView consoleView;
  private final Project project;
  private final ActiveProfileService activeProfileService;

  public JSONFieldsFilter(ConsoleView consoleView, Project project) {
    this.consoleView = consoleView;
    this.project = project;
    this.activeProfileService = ActiveProfileService.getInstance(project);
  }

  private boolean isEnabled(JsonLogConfig state) {
    return Objects.nonNull(state) && state.isEnabled();
  }

  @Override
  public List<Pair<String, ConsoleViewContentType>> applyFilter(
      String statement, ConsoleViewContentType contentType) {
    return CommonUtils.logTimer("JSONFieldsFilter.applyFilter", () -> {
      return applyFilterLogic(statement, contentType);
    });
  }

  public List<Pair<String, ConsoleViewContentType>> applyFilterLogic(
      String statement, ConsoleViewContentType contentType) {
    // fetching the config
    JsonLogConfig state = JsonConfigService.getInstance().getState();

    if (!isEnabled(state)) {
      logger.trace("JSON Formatting disabled");
      return null;
    }

    if (!JSONUtils.isValidJSON(statement)) {
      logger.trace("Invalid json statement");
      return null;
    }

    Profile currentProfile = activeProfileService.getActiveProfile(consoleView);

    if (Objects.isNull(currentProfile)
        || CollectionUtils.isEmpty(currentProfile.getFieldConfigs())) {
      logger.trace("Configuration not valid");
      return null;
    }

    List<Pair<String, ConsoleViewContentType>> result = new LinkedList<>();
    extractFields(statement, currentProfile.getFieldConfigs())
        .forEach(
            (value) -> {
              String text = value.getFirst();
              FieldConfig fieldConfig = value.getSecond();
              ConsoleViewContentType consoleViewContentType =
                  buildConsoleViewContentType(fieldConfig, contentType);
              if (Boolean.TRUE.equals(fieldConfig.getShouldPrintOnNewLine())) {
                result.add(Pair.create(LINE_BREAK, contentType));
              }
              result.add(Pair.create(text, consoleViewContentType));
              result.add(Pair.create(SPACE, contentType));
            });
    result.add(Pair.create(LINE_BREAK, contentType));
    if (currentProfile.isShowOriginalLog()) {
      result.add(Pair.create(statement, contentType));
    }
    logger.trace("Successfully built console statement");
    return result;
  }

  private static ConsoleViewContentType buildConsoleViewContentType(
      FieldConfig fieldConfig, ConsoleViewContentType contentType) {
    TextAttributes textAttributes =
        CommonUtils.computeIfNull(
            contentType.getAttributes(),
            () -> {
              TextAttributesKey.TextAttributeKeyDefaultsProvider service =
                  ApplicationManager.getApplication()
                      .getService(TextAttributesKey.TextAttributeKeyDefaultsProvider.class);
              if (Objects.nonNull(service) && Objects.nonNull(contentType.getAttributesKey())) {
                return service.getDefaultAttributes(contentType.getAttributesKey());
              }
              return new TextAttributes();
            });

    // always modify the copy
    textAttributes = CommonUtils.deepCopy(textAttributes);

    if (Objects.nonNull(fieldConfig.getForegroundColor())
        && Boolean.TRUE.equals(fieldConfig.getForegroundColor().getEnabled())
        && Objects.nonNull(fieldConfig.getForegroundColor().getColor())) {
      Color foregroundColor = Color.decode(fieldConfig.getForegroundColor().getColor());
      textAttributes.setForegroundColor(foregroundColor);
      logger.trace("Set foreground color");
    }
    if (Objects.nonNull(fieldConfig.getBackgroundColor())
        && Boolean.TRUE.equals(fieldConfig.getBackgroundColor().getEnabled())
        && Objects.nonNull(fieldConfig.getBackgroundColor().getColor())) {
      Color backgroundColor = Color.decode(fieldConfig.getBackgroundColor().getColor());
      textAttributes.setBackgroundColor(backgroundColor);
      logger.trace("Set background color");
    }
    return new ConsoleViewContentType(null, textAttributes);
  }

  private List<Pair<String, FieldConfig>> extractFields(
      String statement, List<FieldConfig> fieldConfigs) {
    List<Pair<String, FieldConfig>> resultList = new LinkedList<>();
    Object parsedDocument = Configuration.defaultConfiguration().jsonProvider().parse(statement);
    fieldConfigs.forEach(
        fieldConfig -> {
          if (!Boolean.TRUE.equals(fieldConfig.getEnabled())) {
            return;
          }
          String jsonPath = fieldConfig.getJsonPath();
          if (JsonPath.isPathDefinite(jsonPath)) {
            try {
              Object value = JsonPath.read(parsedDocument, jsonPath);
              if (Objects.nonNull(value)) {
                resultList.add(Pair.create(value.toString(), fieldConfig));
                logger.trace("JSON Path lookup success");
                return;
              }
            } catch (Exception e) {
              logger.warn(
                  "Exception occurred while looking up json path [%s]".formatted(jsonPath), e);
            }
          } else {
            logger.warn("JsonPath: %s not definite".formatted(jsonPath));
          }
        });
    return resultList;
  }
}
