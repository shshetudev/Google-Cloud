package com.shetu.entity;

public class User {
  private final String name;
  private final String age;

  public User(String name, String age) {
    this.name = name;
    this.age = age;
  }

  public String getAge() {
    return age;
  }

  public String getName() {
    return name;
  }
}
