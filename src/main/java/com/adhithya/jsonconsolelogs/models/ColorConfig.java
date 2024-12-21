package com.adhithya.jsonconsolelogs.models;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColorConfig implements Serializable {

  @Builder.Default private Boolean enabled = true;
  private String color;

  public static ColorConfig buildDefault() {
    return ColorConfig.builder().build();
  }
}
