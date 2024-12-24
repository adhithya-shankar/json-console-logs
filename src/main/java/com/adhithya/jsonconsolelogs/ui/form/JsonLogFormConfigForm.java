package com.adhithya.jsonconsolelogs.ui.form;

import java.awt.event.ItemEvent;
import java.util.List;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBSplitter;
import com.intellij.uiDesigner.core.GridConstraints;

import com.adhithya.jsonconsolelogs.factory.UtilsFactory;
import com.adhithya.jsonconsolelogs.models.JsonLogConfig;
import com.adhithya.jsonconsolelogs.models.Profile;
import com.adhithya.jsonconsolelogs.service.ActiveProfileService;
import com.adhithya.jsonconsolelogs.service.JsonConfigService;
import com.adhithya.jsonconsolelogs.utils.CommonUtils;

import lombok.Getter;

public class JsonLogFormConfigForm extends JFrame {

  private final Project project;
  private final ConsoleView consoleView;
  private final CommonUtils commonUtils;
  @Getter private JPanel rootComponent;
  private JComboBox<Profile> activeProfileComboBox;
  private JPanel spacer;
  private JBSplitter splitPane;
  private JCheckBox enabledCheckbox;
  private ProfileForm profileForm;
  private ProfileListForm profileListForm;
  private DefaultComboBoxModel<Profile> activeProfilesComboBoxModel;

  private final ActiveProfileService activeProfileService;

  private final JsonLogConfig config;
  private final List<Profile> profiles;

  public JsonLogFormConfigForm(Project project, ConsoleView consoleView) {
    this.project = project;
    this.consoleView = consoleView;
    this.commonUtils = UtilsFactory.getInstance().getCommonUtils();

    activeProfileService = ActiveProfileService.getInstance(project);

    config = JsonConfigService.getInstance().getState();
    profiles = config.getProfiles();
    initializeComponents();
    initializeListeners();
  }

  public boolean isFormModified() {
    return !Objects.equals(config, JsonConfigService.getInstance().getState());
  }

  public JsonLogConfig getUpdatedConfig() {
    if (Objects.equals(
        profileForm.getUpdatedProfile(),
        ActiveProfileService.getInstance(project).getActiveProfile(consoleView))) {
      ActiveProfileService.getInstance(project)
          .getActiveProfile(consoleView)
          .setFieldConfigs(profileForm.getUpdatedProfile().getFieldConfigs());
      ActiveProfileService.getInstance(project)
          .getActiveProfile(consoleView)
          .setShowOriginalLog(profileForm.getUpdatedProfile().isShowOriginalLog());
    }
    if (Objects.equals(profileForm.getUpdatedProfile(), config.getDefaultProfile())) {
      config.getDefaultProfile().setFieldConfigs(profileForm.getUpdatedProfile().getFieldConfigs());
      config
          .getDefaultProfile()
          .setShowOriginalLog(profileForm.getUpdatedProfile().isShowOriginalLog());
    }
    return config;
  }

  private void initializeComponents() {
    // add spacer
    GridConstraints constraints = new GridConstraints();
    constraints.setHSizePolicy(
        GridConstraints.SIZEPOLICY_CAN_GROW & GridConstraints.SIZEPOLICY_CAN_SHRINK);
    constraints.setVSizePolicy(
        GridConstraints.SIZEPOLICY_CAN_GROW & GridConstraints.SIZEPOLICY_CAN_SHRINK);
    constraints.setFill(GridConstraints.FILL_BOTH);
    spacer.add(Box.createVerticalStrut(10), constraints);

    // initialize profiles list
    profileListForm =
        new ProfileListForm(
            profiles,
            config.getDefaultProfile(),
            activeProfileService.getActiveProfile(consoleView));
    JComponent profileListComponent = profileListForm.getRootComponent();

    // initialize profile form
    profileForm = new ProfileForm(null);
    JComponent profileFormComponent = profileForm.getRootComponent();

    splitPane.setFirstComponent(profileListComponent);
    splitPane.setSecondComponent(profileFormComponent);
    splitPane.setProportion(0.2F);
    splitPane.setResizeEnabled(false);

    // initialize active profile chooser
    activeProfilesComboBoxModel = new DefaultComboBoxModel<>();
    activeProfileComboBox.setModel(activeProfilesComboBoxModel);
    activeProfilesComboBoxModel.addAll(profiles);
    activeProfilesComboBoxModel.setSelectedItem(
        commonUtils.computeIfNull(
            activeProfileService.getActiveProfile(consoleView), config::getDefaultProfile));

    // initialize enabled checkbox
    enabledCheckbox.setSelected(config.isEnabled());
  }

  private void createUIComponents() {
    splitPane = new JBSplitter(true);
  }

  private void initializeListeners() {
    enabledCheckbox.addActionListener(
        e -> {
          config.setEnabled(enabledCheckbox.isSelected());
        });
    activeProfileComboBox.addItemListener(
        e -> {
          if (e.getStateChange() == ItemEvent.SELECTED) {
            Profile selectedProfile = (Profile) activeProfileComboBox.getSelectedItem();
            if (Objects.nonNull(selectedProfile)) {
              activeProfileService.updateActiveProfile(consoleView, selectedProfile);
            }
          }
        });

    profileListForm.addProfileCreationListener(
        profile -> {
          profiles.add(profile);
          profileListForm.setSelectedProfile(profile);
          activeProfilesComboBoxModel.addElement(profile);
        });

    profileListForm.addProfileDeletionListener(
        profile -> {
          if (profile.equals(config.getDefaultProfile())) {
            return;
          }
          profiles.remove(profile);
          activeProfilesComboBoxModel.removeElement(profile);
          if (profiles.isEmpty()) {
            activeProfileService.removeActiveProfile(consoleView);
            activeProfilesComboBoxModel.setSelectedItem(null);
            profileForm.setProfile(null);
          }
        });

    profileListForm.addProfileSelectionListener(profileForm::setProfile);
    profileListForm.addProfileDefaultSelectionListener(
        profile -> {
          if (Objects.nonNull(profile)) {
            config.setDefaultProfile(profile);
          }
        });

    profileForm.addProfileNameChangeListener(
        profile -> {
          profileListForm.updateSelectedProfileName(profile);
          for (int i = 0; i < activeProfilesComboBoxModel.getSize(); i++) {
            if (activeProfilesComboBoxModel.getElementAt(i).equals(profile)) {
              activeProfilesComboBoxModel.getElementAt(i).setName(profile.getName());
              activeProfileComboBox.updateUI();
              return;
            }
          }
        });
  }
}
