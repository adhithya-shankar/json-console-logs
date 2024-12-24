package com.adhithya.jsonconsolelogs.utils;

import static com.adhithya.jsonconsolelogs.utils.Constants.CONFIG_CONSOLE_LOG_TIMER_ENABLED;

import java.util.Objects;
import java.util.function.Supplier;

import com.intellij.openapi.diagnostic.Logger;

import com.adhithya.jsonconsolelogs.factory.UtilsFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.time.StopWatch;

public class CommonUtils {

  private static final Logger logger = Logger.getInstance(CommonUtils.class);
  private final JSONUtils jsonUtils;
  private final ConfigUtils configUtils;

  public CommonUtils() {
    this.jsonUtils = UtilsFactory.getInstance().getJsonUtils();
    this.configUtils = UtilsFactory.getInstance().getConfigUtils();
  }

  public <T> T computeIfNull(T obj, Supplier<T> function) {
    if (Objects.isNull(obj)) {
      return function.get();
    }
    return obj;
  }

  public <T> T deepCopy(T object) {
    try {
      String string = jsonUtils.getObjectMapper().writeValueAsString(object);
      return jsonUtils.getObjectMapper().readValue(string, (Class<T>) object.getClass());
    } catch (JsonProcessingException e) {
      logger.error("Exception occurred while deserialization", e);
      return null;
    }
  }

  public <T> T logTimer(String taskName, Supplier<T> task) {
    if (!Boolean.parseBoolean(configUtils.getEnv(CONFIG_CONSOLE_LOG_TIMER_ENABLED))) {
      return task.get();
    }
    StopWatch stopWatch = createStopWatch();
    stopWatch.start();
    T t = task.get();
    stopWatch.stop();
    logger.debug("Time taken by [%s]: %d ms".formatted(taskName, stopWatch.getTime()));
    return t;
  }

  protected StopWatch createStopWatch() {
    return new StopWatch();
  }
}
