package ca.jrvs.practice.codingChallenge;

import java.util.Map;

/**
 * ticket url: https://www.notion.so/jarvisdev/How-to-compare-two-maps-fbcce62986f244f5ae91200fac165452
 */
public class CompareMaps {

  /**
   * Big-O: O(n)
   * Justification: Assuming collisions are minimal then lookup time would be O(1) and there are n elements
   *
   */
  public static <K,V> boolean compareMaps(Map<K,V> m1, Map<K,V> m2) {
    return m1.equals(m2);
  }

  public static <K,V> boolean compareMaps2(Map<K,V> m1, Map<K,V> m2) {
    if (m1 == m2)
      return true;
    return m1.entrySet().stream()
        .allMatch(e -> e.getValue().equals(m2.get(e.getKey())));
  }


}
