package com.adhithya.jsonconsolelogs.ui.form;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.ui.components.JBList;
import com.intellij.uiDesigner.core.GridConstraints;

import com.adhithya.jsonconsolelogs.factory.UtilsFactory;
import com.adhithya.jsonconsolelogs.models.Profile;
import com.adhithya.jsonconsolelogs.ui.ActionButtonsFactory;
import com.adhithya.jsonconsolelogs.utils.CommonUtils;

import lombok.Getter;

public class ProfileListForm {
  @Getter private JPanel rootComponent;
  private JBList<Profile> profilesList;
  private JPanel profileActionToolbarContainer;
  private JButton setDefaultButton;

  private final List<Consumer<Profile>> profileCreationListeners = new LinkedList<>();
  private final List<Consumer<Profile>> profileDeletionListeners = new LinkedList<>();
  private final List<Consumer<Profile>> profileSelectionListeners = new LinkedList<>();
  private final List<Consumer<Profile>> defaultProfileChangeListeners = new LinkedList<>();

  private final CommonUtils commonUtils;
  private final List<Profile> profiles;
  private Profile defaultProfile;
  private Profile currentProfile;

  public ProfileListForm(List<Profile> profiles, Profile defaultProfile, Profile currentProfile) {
    this.profiles = profiles;
    this.defaultProfile = defaultProfile;
    this.commonUtils = UtilsFactory.getInstance().getCommonUtils();
    this.currentProfile = this.commonUtils.computeIfNull(currentProfile, () -> defaultProfile);
    initComponents();
    initListeners();
  }

  private void updateDefaultProfile(Profile profile) {
    defaultProfile = profile;
    ProfileListCellRenderer.setDefaultProfile(defaultProfile);
    profilesList.updateUI();
  }

  private void initListeners() {

    setDefaultButton.addActionListener(
        e -> {
          if (Objects.nonNull(profilesList.getSelectedValue())
              && !Objects.equals(defaultProfile, profilesList.getSelectedValue())) {
            updateDefaultProfile(profilesList.getSelectedValue());
            defaultProfileChangeListeners.forEach(listener -> listener.accept(defaultProfile));
          }
        });

    ((DefaultListModel<Profile>) profilesList.getModel()).addAll(profiles);
    profilesList.addListSelectionListener(
        e -> {
          // previous selection
          // current selection
          profileSelectionListeners.forEach(
              listener -> listener.accept(profilesList.getSelectedValue()));
        });
    if (Objects.nonNull(currentProfile)) {
      SwingUtilities.invokeLater(() -> profilesList.setSelectedValue(currentProfile, true));
    }
  }

  private void initComponents() {
    // TODO disable button instead
    ActionToolbar listActionToolbar =
        ActionManager.getInstance()
            .createActionToolbar(
                "Toolbar",
                new DefaultActionGroup(
                    ActionButtonsFactory.createAddAction(
                        e -> {
                          Profile profile = Profile.builder().build();
                          ((DefaultListModel) profilesList.getModel()).addElement(profile);
                          profileCreationListeners.forEach(listener -> listener.accept(profile));
                        }),
                    ActionButtonsFactory.createRemoveAction(
                        e -> {
                          int selectedIndex = profilesList.getSelectedIndex();
                          Profile selectedValue = profilesList.getSelectedValue();
                          if (Objects.equals(defaultProfile, selectedValue)) {
                            // TODO disable button instead
                            return;
                          }
                          Profile profile =
                              (Profile)
                                  ((DefaultListModel) profilesList.getModel())
                                      .remove(selectedIndex);
                          profileDeletionListeners.forEach(listener -> listener.accept(profile));
                        })),
                true);
    listActionToolbar.setTargetComponent(profileActionToolbarContainer);
    profileActionToolbarContainer.add(listActionToolbar.getComponent(), new GridConstraints());

    ProfileListCellRenderer.setDefaultProfile(defaultProfile);
    profilesList.setCellRenderer(new ProfileListCellRenderer());
    profilesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  private void createUIComponents() {
    profilesList = new JBList<>(new DefaultListModel<>());
  }

  public void setSelectedProfile(Profile profile) {
    if (Objects.nonNull(profile)) {
      profilesList.setSelectedValue(profile, true);
    }
  }

  public void addProfileCreationListener(Consumer<Profile> listener) {
    profileCreationListeners.add(listener);
  }

  public void addProfileDeletionListener(Consumer<Profile> listener) {
    profileDeletionListeners.add(listener);
  }

  public void addProfileSelectionListener(Consumer<Profile> listener) {
    profileSelectionListeners.add(listener);
  }

  public void addProfileDefaultSelectionListener(Consumer<Profile> listener) {
    defaultProfileChangeListeners.add(listener);
  }

  public void updateSelectedProfileName(Profile profile) {
    profilesList.getSelectedValue().setName(profile.getName());
    profilesList.updateUI();
  }
}
