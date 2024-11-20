package com.adhithya.jsonconsolelogs.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;

import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.AbstractTableCellEditor;

public class ActionCellEditor extends AbstractTableCellEditor {

  JBCheckBox selectionCheckBox;

  @Override
  public Component getTableCellEditorComponent(
      JTable table, Object value, boolean isSelected, int row, int column) {
    JPanel panel = new JBPanel<>();
    panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

    JButton moveUpButton;
    JButton moveDownButton;

    moveDownButton = new JButton(null, AllIcons.General.ArrowDown);
    moveUpButton = new JButton(null, AllIcons.General.ArrowUp);
    selectionCheckBox = new JBCheckBox();
    selectionCheckBox.setSelected((Boolean) value);

    selectionCheckBox.addActionListener(
        e -> {
          stopCellEditing();
        });

    initButton(
        moveUpButton,
        e -> {
          FieldConfigTableModel model = (FieldConfigTableModel) table.getModel();
          if (row > 0) {
            model.moveRow(row, row, row - 1);
          }
          stopCellEditing();
        });
    initButton(
        moveDownButton,
        e -> {
          FieldConfigTableModel model = (FieldConfigTableModel) table.getModel();
          if (row < model.getRowCount() - 1) {
            model.moveRow(row, row, row + 1);
          }
          stopCellEditing();
        });

    panel.add(selectionCheckBox);
    panel.add(moveUpButton);
    panel.add(moveDownButton);
    return panel;
  }

  @Override
  public Object getCellEditorValue() {
    return selectionCheckBox.isSelected();
  }

  private static void initButton(JButton button, ActionListener actionListener) {
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
