package com.yoon.til.Thread;

public class Ex01 {

  static class A extends Thread {
    @Override
    public void run() {
      System.out.println("A : Thread를 상속 받아서 사용");
    }
  }

  static class B implements Runnable {
    @Override
    public void run() {
      System.out.println("B : Runnable 인터페이스 구현");
    }
  }

  public static void main(String[] args) {
    A a = new A();
    a.start();

    Thread b = new Thread(new B());
    b.start();

    new Thread(() -> System.out.println("Thread 람다")).start();

    new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("Runnable을 이용한 익명 클래스");
      };
    }).start();

  }
}
