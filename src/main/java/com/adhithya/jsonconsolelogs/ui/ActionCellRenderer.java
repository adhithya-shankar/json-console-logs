package com.adhithya.jsonconsolelogs.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBPanel;

public class ActionCellRenderer implements TableCellRenderer {

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    JPanel panel = new JBPanel<>();
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

    JButton moveUpButton;
    JButton moveDownButton;
    JBCheckBox selectionCheckBox;

    moveDownButton = new JButton(AllIcons.General.ArrowDown);
    moveUpButton = new JButton(AllIcons.General.ArrowUp);
    selectionCheckBox = new JBCheckBox();
    if (Objects.nonNull(value)) {
      selectionCheckBox.setSelected((Boolean) value);
    }

    initButton(moveUpButton, null);
    initButton(moveDownButton, null);

    panel.add(selectionCheckBox);
    panel.add(moveUpButton);
    panel.add(moveDownButton);
    return panel;
  }

  private static void initButton(JButton button, ActionListener actionListener) {
    // TODO fix the button formatting
    button.setText(null);
    //    button.setContentAreaFilled(false);
    //    button.setFocusPainted(false);
    //    button.setBorderPainted(false);
    //    button.setMargin(JBUI.emptyInsets());
    //    button.setBorder(null);
    button.setPreferredSize(new Dimension(20, 10));
    button.setSize(new Dimension(20, 10));

    button.addActionListener(actionListener);
  }
}
