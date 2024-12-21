package com.adhithya.jsonconsolelogs.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.intellij.ui.table.JBTable;

import com.adhithya.jsonconsolelogs.models.FieldConfig;
import com.adhithya.jsonconsolelogs.utils.Action;

public class FieldConfigTable extends JBTable {

  private FieldConfigTableModel fieldConfigTableModel;

  private final List<Action> deleteRowTriggeredListeners = new LinkedList<>();
  private final List<Action> deleteRowCompletionListeners = new LinkedList<>();

  public FieldConfigTable() {
    setRowSelectionAllowed(true);
    setCellSelectionEnabled(false);
    setColumnSelectionAllowed(false);
    updateColumnWidth();
  }

  private void updateColumnWidth() {
    for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
      if (i == 0) {
        getColumnModel().getColumn(i).setPreferredWidth(80);
        getColumnModel().getColumn(i).setMaxWidth(80);
      } else if (i != 1) {
        getColumnModel().getColumn(i).setPreferredWidth(100);
        getColumnModel().getColumn(i).setMaxWidth(100);
      }
    }
    getTableHeader().setResizingAllowed(false);
  }

  public void setModel(FieldConfigTableModel fieldConfigTableModel) {
    super.setModel(fieldConfigTableModel);
    this.fieldConfigTableModel = fieldConfigTableModel;
    updateColumnWidth();
  }

  public void initialize(List<FieldConfig> fieldConfigs) {
    if (Objects.nonNull(getCellEditor())) {
      // removes stale cell editing
      getCellEditor().stopCellEditing();
    }
    fieldConfigTableModel.init(fieldConfigs);
    updateUI();
  }

  public void addRow() {
    fieldConfigTableModel.addRow(FieldConfig.builder().build());
  }

  public void deleteSelectedRows() {
    deleteRowTriggeredListeners.forEach(Action::doAction);
    fieldConfigTableModel.deleteSelectedRows();
    deleteRowCompletionListeners.forEach(Action::doAction);
  }

  @Override
  public boolean isRowSelected(int row) {
    return super.isRowSelected(row);
  }

  public void addDeleteRowTriggeredListener(Action action) {
    if (Objects.nonNull(action)) {
      deleteRowTriggeredListeners.add(action);
    }
  }

  public void addDeleteRowCompletionListener(Action action) {
    if (Objects.nonNull(action)) {
      deleteRowCompletionListeners.add(action);
    }
  }
}
