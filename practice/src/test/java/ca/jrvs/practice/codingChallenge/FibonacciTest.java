package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class FibonacciTest {

  @Test
  public void fibonacciRecursion() {
    int n = 5;
    int expected = 5;
    assertEquals(expected, Fibonacci.fibonacciRecursion(n));
  }

  @Test
  public void fibonacciDP() {
    int n = 5;
    int expected = 5;
    assertEquals(expected, Fibonacci.fibonacciDP(n));
  }
}