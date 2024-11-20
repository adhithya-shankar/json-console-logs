package com.adhithya.jsonconsolelogs.ui.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Objects;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import com.intellij.icons.AllIcons;

import com.adhithya.jsonconsolelogs.models.Profile;

import lombok.Setter;

public class ProfileListCellRenderer extends DefaultListCellRenderer {

  @Setter private static Profile defaultProfile = null;

  @Override
  public Component getListCellRendererComponent(
      JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

    JLabel cellComponent =
        (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(cellComponent, BorderLayout.WEST);

    if (Objects.nonNull(defaultProfile) && Objects.equals(value, defaultProfile)) {
      panel.add(new JLabel(AllIcons.Toolwindows.ToolWindowFavorites), BorderLayout.EAST);
    }

    return panel;
  }
}
