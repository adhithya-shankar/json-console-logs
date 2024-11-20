package com.adhithya.jsonconsolelogs.ui;

import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.Icon;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionButtonsFactory {

  public static AnAction createAddAction(Consumer<AnActionEvent> eventConsumer) {
    return new ActionButton(AllIcons.General.Add, eventConsumer);
  }

  public static AnAction createRemoveAction(Consumer<AnActionEvent> eventConsumer) {
    return new ActionButton(AllIcons.General.Remove, eventConsumer);
  }

  public static class ActionButton extends AnAction {

    private final Consumer<AnActionEvent> actionEventConsumer;

    public ActionButton(Icon icon, Consumer<AnActionEvent> actionEventConsumer) {
      super(icon);
      this.actionEventConsumer = actionEventConsumer;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
      if (Objects.nonNull(actionEventConsumer)) {
        actionEventConsumer.accept(anActionEvent);
      }
    }
  }
}
