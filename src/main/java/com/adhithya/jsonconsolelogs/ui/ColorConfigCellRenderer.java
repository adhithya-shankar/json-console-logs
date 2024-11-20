package com.adhithya.jsonconsolelogs.ui;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.adhithya.jsonconsolelogs.models.ColorConfig;

public class ColorConfigCellRenderer implements TableCellRenderer {

  public static final ColorConfig DEFAULT = ColorConfig.builder().color("#f8a8ae").build();

  @Override
  public Component getTableCellRendererComponent(
      JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
    return new CheckBoxWithColorChooser(null, (ColorConfig) value);
  }
}
