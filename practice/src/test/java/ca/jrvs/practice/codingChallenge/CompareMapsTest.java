package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CompareMapsTest {

  private static Map<String, Integer> m1 = new HashMap<>();
  private static Map<String, Integer> m2 = new HashMap<>();
  private static Map<String, Integer> m3 = new HashMap<>();

  @BeforeClass
  public static void setUp() {
    m1.put("one", 1);
    m1.put("two", 2);
    m1.put("three", 3);

    m2.put("three", 3);
    m2.put("one", 1);
    m2.put("two", 2);

    m3.put("one", 1);
    m3.put("five", 5);
  }
  @Test
  public void compareMaps() {
    assertTrue(CompareMaps.compareMaps(m1, m2));
    assertFalse(CompareMaps.compareMaps(m1, m3));
  }

  @Test
  public void compareMaps2() {
    assertTrue(CompareMaps.compareMaps2(m1, m2));
    assertFalse(CompareMaps.compareMaps2(m1, m3));
  }
}