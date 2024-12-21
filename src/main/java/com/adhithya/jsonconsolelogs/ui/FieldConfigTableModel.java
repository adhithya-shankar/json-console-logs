package com.adhithya.jsonconsolelogs.ui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.table.DefaultTableModel;

import com.adhithya.jsonconsolelogs.models.ColorConfig;
import com.adhithya.jsonconsolelogs.models.FieldConfig;

import org.apache.commons.collections.CollectionUtils;

public class FieldConfigTableModel extends DefaultTableModel {

  private static final String[] COLUMN_NAMES =
      new String[] {"", "Field", "Enabled", "Print on New Line", "Foreground", "Background"};
  private static final Class<?>[] COLUMN_CLASS =
      new Class[] {
        Boolean.class,
        String.class,
        Boolean.class,
        Boolean.class,
        ColorConfig.class,
        ColorConfig.class
      };
  private final Set<Integer> selectedRows = new TreeSet<>();

  public FieldConfigTableModel() {
    super(null, COLUMN_NAMES);
  }

  @Override
  public Class<?> getColumnClass(int columnIndex) {
    return COLUMN_CLASS[columnIndex];
  }

  public void initListeners() {
    addTableModelListener(
        e -> {
          int column = e.getColumn();
          if (column == 0) {
            int row = e.getFirstRow();
            Boolean isRowSelected = (Boolean) dataVector.get(row).get(column);
            if (Boolean.TRUE.equals(isRowSelected)) {
              selectedRows.add(row);
            } else {
              selectedRows.remove(row);
            }
          }
        });
  }

  public void init(List<FieldConfig> fieldConfigs) {
    clearRows();
    if (CollectionUtils.isNotEmpty(fieldConfigs)) {
      fieldConfigs.forEach(this::addRow);
    }
  }

  public void addRow(FieldConfig fieldConfig) {
    addRow(toRow(fieldConfig));
  }

  public void deleteSelectedRows() {
    Iterator<Integer> iterator = selectedRows.iterator();
    int i = 0;
    while (iterator.hasNext()) {
      removeRow(iterator.next() - i);
      iterator.remove();
      i++;
    }
  }

  public List<FieldConfig> getFieldConfigs() {
    List<FieldConfig> fieldConfigs = new LinkedList<>();
    getDataVector()
        .forEach(
            row -> {
              fieldConfigs.add(
                  FieldConfig.builder()
                      .jsonPath((String) row.get(1))
                      .enabled((Boolean) row.get(2))
                      .shouldPrintOnNewLine((Boolean) row.get(3))
                      .foregroundColor((ColorConfig) row.get(4))
                      .backgroundColor((ColorConfig) row.get(5))
                      .build());
            });
    return fieldConfigs;
  }

  private Object[] toRow(FieldConfig fieldConfig) {
    return new Object[] {
      Boolean.FALSE,
      fieldConfig.getJsonPath(),
      fieldConfig.getEnabled(),
      fieldConfig.getShouldPrintOnNewLine(),
      fieldConfig.getForegroundColor(),
      fieldConfig.getBackgroundColor()
    };
  }

  private void clearRows() {
    while (!dataVector.isEmpty()) {
      removeRow(0);
    }
  }
}
