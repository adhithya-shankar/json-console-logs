package com.adhithya.jsonconsolelogs.ui;

import java.awt.Color;
import java.awt.Component;
import java.util.Objects;

import javax.swing.JTable;

import com.intellij.util.ui.AbstractTableCellEditor;

import com.adhithya.jsonconsolelogs.models.ColorConfig;

public class ColorConfigCellEditor extends AbstractTableCellEditor {

  private CheckBoxWithColorChooser checkBoxWithColorChooser;

  @Override
  public Component getTableCellEditorComponent(
      JTable table, Object value, boolean isSelected, int row, int column) {
    checkBoxWithColorChooser = new CheckBoxWithColorChooser(null, (ColorConfig) value);
    checkBoxWithColorChooser.addEnabledChangeListener(enabled -> stopCellEditing());
    checkBoxWithColorChooser.addColorChangeListener(color -> stopCellEditing());
    return checkBoxWithColorChooser;
  }

  @Override
  public Object getCellEditorValue() {
    Color color = checkBoxWithColorChooser.getColor();
    if (Objects.nonNull(color)) {
      return new ColorConfig(
          checkBoxWithColorChooser.isSelected(),
          String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue()));
    }
    return new ColorConfig(checkBoxWithColorChooser.isSelected(), null);
  }
}
