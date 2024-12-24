package com.adhithya.jsonconsolelogs.utils;

import java.util.Objects;
import java.util.function.Supplier;

import com.intellij.openapi.diagnostic.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.StopWatch;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

  private static final Logger logger = Logger.getInstance(CommonUtils.class);

  public static <T> T computeIfNull(T obj, Supplier<T> function) {
    if (Objects.isNull(obj)) {
      return function.get();
    }
    return obj;
  }

  public static <T> T deepCopy(T object) {
    try {
      String string = JSONUtils.getObjectMapper().writeValueAsString(object);
      return JSONUtils.getObjectMapper().readValue(string, (Class<T>) object.getClass());
    } catch (JsonProcessingException e) {
      logger.error("Exception occurred while deserialization", e);
      return null;
    }
  }

  public static <T> T logTimer(String taskName, Supplier<T> task) {
    if (!Boolean.parseBoolean(System.getenv("console.action.timer.enabled"))) {
      return task.get();
    }
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    T t = task.get();
    stopWatch.stop();
    logger.debug("Time taken by [%s]: %d ms".formatted(taskName, stopWatch.getTime()));
    return t;
  }
}
