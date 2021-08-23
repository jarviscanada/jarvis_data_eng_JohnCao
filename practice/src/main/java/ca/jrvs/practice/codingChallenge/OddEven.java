package ca.jrvs.practice.codingChallenge;

/**
 *  ticket url: https://www.notion.so/jarvisdev/Sample-Check-if-a-number-is-even-or-odd-178c6038394a401d8420f9c3d98f3067
 */
public class OddEven {

  /**
   *
   * Big-O: O(1)
   * This is an arithmetic operation and takes constant time.
   *
   */
  public static String oddEvenModulo(int n) {
    return n % 2 == 0 ? "even" : "odd";
  }

  /**
   * Big-O: O(1)
   * Same as above, this ia an arithmetic operation.
   *
   */
  public static String oddEvenBit(int n) {
    return (n & 1) == 1 ? "odd" : "even";
  }


}
