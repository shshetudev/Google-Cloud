package com.shetu.entity;

import java.util.UUID;

public final class IdGenerator {
  private IdGenerator(){}
  /**
   * Returns a String representation of uuid.
   * @return uuid
   */
  public static String uuid(){
    return UUID.randomUUID().toString();
  }
}
