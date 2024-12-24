package com.adhithya.jsonconsolelogs.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class JSONUtilsTest {

  private JSONUtils jsonUtils;

  @Before
  public void setup() {
    jsonUtils = new JSONUtils();
  }

  @Test
  public void testIsValidJson() {
    assertTrue(jsonUtils.isValidJSON("{}"));
    assertFalse(jsonUtils.isValidJSON("abcdef]"));
  }

  @Test
  public void testGetObjectMapper() {
    assertNotNull(jsonUtils.getObjectMapper());
  }

  @Test
  public void testWriteValueSuccess() {
    Map<String, String> data = Map.of("key", "value");
    assertEquals("{\"key\":\"value\"}", jsonUtils.writeValue(data, null));
  }

  @Test
  public void testWriteValueException() throws JsonProcessingException {
    Map<String, String> data = Map.of("key", "value");
    jsonUtils = Mockito.spy(jsonUtils);
    ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
    when(jsonUtils.getObjectMapper()).thenReturn(mockObjectMapper);
    when(mockObjectMapper.writeValueAsString(any())).thenThrow(IOException.class);
    assertEquals("{}", jsonUtils.writeValue(data, "{}"));
  }
}
