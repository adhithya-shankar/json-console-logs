package com.adhithya.jsonconsolelogs.models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.intellij.util.xmlb.annotations.OptionTag;

import com.adhithya.jsonconsolelogs.utils.ProfilesConverter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile implements Serializable {

  public static final Profile DEFAULT =
      new ProfileBuilder()
          .id("default")
          .name("Default")
          .fieldConfigs(
              List.of(
                  FieldConfig.builder().jsonPath("$.time").enabled(true).build(),
                  FieldConfig.builder().jsonPath("$.level").enabled(true).build(),
                  FieldConfig.builder().jsonPath("$.message").enabled(true).build()))
          .build();

  @Builder.Default
  @Setter(AccessLevel.PRIVATE)
  private String id = UUID.randomUUID().toString();

  private String name;

  @Builder.Default private boolean showOriginalLog = true;

  @OptionTag(converter = ProfilesConverter.class)
  @Builder.Default
  private List<FieldConfig> fieldConfigs = new LinkedList<>();

  @Override
  public String toString() {
    return name;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Profile && hashCode() == obj.hashCode();
  }

  @Override
  public int hashCode() {
    return Objects.nonNull(id) ? id.hashCode() : 0;
  }
}
