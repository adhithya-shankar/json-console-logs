package com.adhithya.jsonconsolelogs.ui.form;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.adhithya.jsonconsolelogs.utils.CommonUtils;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.ui.EditorTextField;

import com.adhithya.jsonconsolelogs.models.ColorConfig;
import com.adhithya.jsonconsolelogs.models.Profile;
import com.adhithya.jsonconsolelogs.ui.ActionColumnDecorator;
import com.adhithya.jsonconsolelogs.ui.ColorConfigCellEditor;
import com.adhithya.jsonconsolelogs.ui.ColorConfigCellRenderer;
import com.adhithya.jsonconsolelogs.ui.FieldConfigTable;
import com.adhithya.jsonconsolelogs.ui.FieldConfigTableModel;

import lombok.Getter;

public class ProfileForm {
  @Getter private JPanel rootComponent;
  private JButton addRowButton;
  private JButton deleteRowButton;
  private FieldConfigTable fieldConfigTable;
  private EditorTextField profileTextField;
  private JCheckBox showOriginalLog;

  private final List<Consumer<Profile>> profileNameChangeListeners = new LinkedList<>();

  // model
  private Profile profile;
  private FieldConfigTableModel dataModel;

  public ProfileForm(Profile profile) {
    this.profile = profile;
    initComponent();
    initListeners();
    refreshData();
  }

  public void setProfile(Profile profile) {
    this.profile = profile;
    refreshData();
  }

  public Profile getUpdatedProfile() {
    return profile;
  }

  private void createUIComponents() {
    fieldConfigTable = new FieldConfigTable();
    profileTextField = new EditorTextField();
  }

  private void initComponent() {

    fieldConfigTable.setDefaultRenderer(ColorConfig.class, new ColorConfigCellRenderer());
    fieldConfigTable.setDefaultEditor(ColorConfig.class, new ColorConfigCellEditor());
    fieldConfigTable.setRowSelectionAllowed(false);
    dataModel = new FieldConfigTableModel();
    fieldConfigTable.setModel(dataModel);
    ActionColumnDecorator.decorate(fieldConfigTable, 0);
    dataModel.initListeners();
  }

  private void initListeners() {
    Document document = profileTextField.getDocument();
    document.addDocumentListener(
        new DocumentListener() {
          @Override
          public void documentChanged(DocumentEvent event) {
            profile.setName(profileTextField.getText());
            profileNameChangeListeners.forEach(listener -> listener.accept(profile));
          }
        });
    addRowButton.addActionListener(e -> fieldConfigTable.addRow());
    deleteRowButton.addActionListener(e -> fieldConfigTable.deleteSelectedRows());
    fieldConfigTable.addDeleteRowTriggeredListener(
        () -> SwingUtilities.invokeLater(() -> deleteRowButton.setEnabled(false)));
    fieldConfigTable.addDeleteRowCompletionListener(
        () -> SwingUtilities.invokeLater(() -> deleteRowButton.setEnabled(true)));
    fieldConfigTable
        .getModel()
        .addTableModelListener(
            e -> {
              FieldConfigTableModel model = (FieldConfigTableModel) fieldConfigTable.getModel();
              if (Objects.nonNull(profile)) {
                profile.setFieldConfigs(model.getFieldConfigs());
              }
            });
    showOriginalLog.addActionListener(
        e -> {
          profile.setShowOriginalLog(showOriginalLog.isSelected());
        });
  }

  public void addProfileNameChangeListener(Consumer<Profile> listener) {
    profileNameChangeListeners.add(listener);
  }

  private void refreshData() {
    CommonUtils.logTimer("ProfileForm.refreshData", () -> {
      if (Objects.nonNull(profile)) {
        profileTextField.setText(profile.getName());
        fieldConfigTable.initialize(profile.getFieldConfigs());
        showOriginalLog.setSelected(profile.isShowOriginalLog());
      } else {
        profileTextField.setText(null);
        fieldConfigTable.initialize(List.of());
      }
      return null;
    });
  }
}
