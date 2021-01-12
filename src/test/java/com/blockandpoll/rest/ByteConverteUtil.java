package com.blockandpoll.rest;

import java.nio.ByteBuffer;

public class ByteConverteUtil {

  public static byte[] convertLongToByteArray(long value) {
    return ByteBuffer.allocate(8).putLong(value).array();
  }
}