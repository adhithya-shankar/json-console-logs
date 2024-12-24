package com.adhithya.jsonconsolelogs.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;

import com.adhithya.jsonconsolelogs.factory.UtilsFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CommonUtilsTest {

  private CommonUtils commonUtils;
  private MockedStatic<UtilsFactory> utilsFactoryStaticMock;

  @Mock private UtilsFactory utilsFactory;

  @Mock private JSONUtils jsonUtils;

  @Mock private ConfigUtils configUtils;

  @Before
  public void setup() {
    MockitoAnnotations.openMocks(this);
    this.utilsFactoryStaticMock = Mockito.mockStatic(UtilsFactory.class);
    utilsFactoryStaticMock.when(() -> UtilsFactory.getInstance()).thenReturn(utilsFactory);
    when(utilsFactory.getJsonUtils()).thenReturn(jsonUtils);
    when(utilsFactory.getConfigUtils()).thenReturn(configUtils);

    commonUtils = new CommonUtils();
  }

  @After
  public void cleanUp() {
    utilsFactoryStaticMock.close();
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
