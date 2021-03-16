package com.yoon.til;

public class Student {
  int id;
  String name;

  public Student(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public void showInfo(int length) {
    System.out.println(id + name);
  }
}
