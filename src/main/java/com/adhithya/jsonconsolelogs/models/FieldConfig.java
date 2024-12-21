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
public class FieldConfig implements Serializable {
  @Builder.Default private String jsonPath = "";
  @Builder.Default private ColorConfig foregroundColor = ColorConfig.buildDefault();
  @Builder.Default private ColorConfig backgroundColor = ColorConfig.buildDefault();
  @Builder.Default private Boolean enabled = true;
  @Builder.Default private Boolean shouldPrintOnNewLine = false;
}
