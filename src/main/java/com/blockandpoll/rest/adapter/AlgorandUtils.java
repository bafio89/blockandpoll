package com.blockandpoll.rest.adapter;

import org.apache.commons.lang3.ArrayUtils;

public class AlgorandUtils {

  public static final String[] headers = {"X-API-Key"};
  public static final String[] values = {"INSERT HERE YOUR PURESTAKE API TOKEN"};
  public static final String[] txHeaders = ArrayUtils.add(headers, "Content-Type");
  public static final String[] txValues = ArrayUtils.add(values, "application/x-binary");

}
