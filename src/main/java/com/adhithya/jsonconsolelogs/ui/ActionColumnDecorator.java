package com.adhithya.jsonconsolelogs.ui;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActionColumnDecorator {

  public static void decorate(JTable table, int column) {
    TableColumnModel columnModel = table.getColumnModel();
    columnModel.getColumn(column).setCellRenderer(new ActionCellRenderer());
    columnModel.getColumn(column).setCellEditor(new ActionCellEditor());
  }
}
