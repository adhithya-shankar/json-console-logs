package com.adhithya.jsonconsolelogs.utils;

import com.adhithya.jsonconsolelogs.factory.UtilsFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommonUtilsTest {

  private CommonUtils commonUtils;

  @Mock private UtilsFactory utilsFactory;

  @Mock private JSONUtils jsonUtils;

  @Mock private ConfigUtils configUtils;

  @Before
  public void setup() {
    MockitoAnnotations.openMocks(this);
    commonUtils = new CommonUtils(jsonUtils, configUtils);
  }

  @Test
  public void testComputeIfNullObjectIsNull() {
    Supplier<?> mockTask = mock(Supplier.class);
    commonUtils.computeIfNull(null, mockTask);
    verify(mockTask, times(1)).get();
  }

  @Test
  public void testComputeIfNullObjectIsNotNull() {
    Supplier<Object> mockTask = mock(Supplier.class);
    commonUtils.computeIfNull(new Object(), mockTask);
    verify(mockTask, times(0)).get();
  }

  @Test
  public void testDeepCopyException() {}

  @Test
  public void testDeepCopySuccess() {}

  @Test
  public void testLogTimerEnabled() {
    when(configUtils.getEnv("console.action.timer.enabled")).thenReturn("true");
    commonUtils = spy(commonUtils);
    Supplier<?> task = mock(Supplier.class);
    commonUtils.logTimer("some-task", task);
    verify(commonUtils, times(1)).createStopWatch();
    verify(task, times(1)).get();
  }

  @Test
  public void testLogTimerDisabled() {
    when(configUtils.getEnv("console.action.timer.enabled")).thenReturn("false");
    commonUtils = spy(commonUtils);
    Supplier<?> task = mock(Supplier.class);
    commonUtils.logTimer("some-task", task);
    verify(commonUtils, times(0)).createStopWatch();
    verify(task, times(1)).get();
  }
}
