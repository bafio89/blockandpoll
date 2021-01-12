package com.rockandpoll.rest.adapter;

import org.apache.commons.lang3.ArrayUtils;

public class AlgorandUtils {

  public static final String[] headers = {"X-API-Key"};
  public static final String[] values = {"KmeYVcOTUFayYL9uVy9mI9d7dDewlWth7pprTlo9"};
  public static final String[] txHeaders = ArrayUtils.add(headers, "Content-Type");
  public static final String[] txValues = ArrayUtils.add(values, "application/x-binary");

}
