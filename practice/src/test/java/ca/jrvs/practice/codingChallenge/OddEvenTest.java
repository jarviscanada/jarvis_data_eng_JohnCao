package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class OddEvenTest {

  @Test
  public void oddEvenModulo() {
    int n = 13;
    assertEquals("odd", OddEven.oddEvenModulo(n));

    n = 158;
    assertEquals("even", OddEven.oddEvenModulo(n));
  }

  @Test
  public void oddEvenBit() {
    int n = 13;
    assertEquals("odd", OddEven.oddEvenModulo(n));

    n = 158;
    assertEquals("even", OddEven.oddEvenModulo(n));
  }
}