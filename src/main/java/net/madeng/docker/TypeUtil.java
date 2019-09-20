package net.madeng.docker;

import groovy.lang.GString;
import java.util.Map;
import java.util.stream.Collectors;

public class TypeUtil {
  static String makeString(Object obj) {
    if (null == obj) {
      return null;
    } else if (obj instanceof String || obj instanceof GString) {
      return obj.toString();
    } else {
      throw new IllegalArgumentException(
          String.format("value should be String or GString: [%s]", obj.toString()));
    }
  }

  static Map<String, String> makeStringMap(Map<Object, Object> map) {
    if (null == map) {
      return null;
    }
    return map.entrySet().stream()
        .collect(Collectors.toMap(e -> makeString(e.getKey()), e -> makeString(e.getValue())));
  }
}
