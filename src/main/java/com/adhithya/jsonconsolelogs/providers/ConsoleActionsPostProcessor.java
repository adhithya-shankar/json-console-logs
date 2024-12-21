package com.adhithya.jsonconsolelogs.providers;

import java.util.LinkedList;
import java.util.List;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.AnAction;

import com.adhithya.jsonconsolelogs.actions.JsonLogFormatConfigAction;

public class ConsoleActionsPostProcessor
    extends com.intellij.execution.actions.ConsoleActionsPostProcessor {

  @Override
  public AnAction[] postProcess(ConsoleView console, AnAction[] actions) {
    List<AnAction> jsonLogActions = new LinkedList<>();
    jsonLogActions.add(new JsonLogFormatConfigAction(console));
    return jsonLogActions.toArray(new AnAction[0]);
  }
}
