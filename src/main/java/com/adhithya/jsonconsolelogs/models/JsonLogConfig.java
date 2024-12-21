package com.adhithya.jsonconsolelogs.models;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.intellij.util.xmlb.annotations.OptionTag;

import com.adhithya.jsonconsolelogs.utils.ProfileConverter;
import com.adhithya.jsonconsolelogs.utils.ProfilesConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsonLogConfig implements Serializable {
  @Builder.Default private boolean enabled = false;

  @Builder.Default
  @OptionTag(converter = ProfileConverter.class)
  private Profile defaultProfile = Profile.DEFAULT;

  @Builder.Default
  @OptionTag(converter = ProfilesConverter.class)
  private List<Profile> profiles = getDefaultProfileList();

  private static List<Profile> getDefaultProfileList() {
    LinkedList<Profile> profiles = new LinkedList<>();
    profiles.add(Profile.DEFAULT);
    return profiles;
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj, false);
  }

  @Override
  public String toString() {
    return ReflectionToStringBuilder.toString(this);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
