package ca.jrvs.practice.codingChallenge;

/**
 *  ticket url: https://www.notion.so/jarvisdev/Fibonacci-Number-Climbing-Stairs-e8d1b3033c7140d2808344c2ff1e4582
 */

public class Fibonacci {

  /**
   *
   * Big-O: O(2^n)
   * Justification: Estimate the time complexity by drawing out a recursion tree. The recurrence relation for this would be T(n) = T(n-1)+T(n-2)+O(1). Each step takes constant time (comparison operation in if block)
   * There are 2^n leaves or nodes, hence the time complexity is O(2^n) as the recursion will repeat for each leaf node.
   *
   */
  public static int fibonacciRecursion(int n) {
    if (n < 2)
      return n;
    return fibonacciRecursion(n - 1) + fibonacciRecursion(n - 2);
  }

  /**
   * Big-O: Time: O(n), space: O(n)
   * Justification: Going down the longest branch of the recursion tree, each number is calculated once and stored into an array. Further calls to these values do not spawn new trees.
   * Therefore the time complexity is O(n).
   *
   */
  public static int fibonacciDP(int n) {
    Integer[] memo = new Integer[n + 1];
    return fibonacciDPHelper(memo, n);
  }

  private static int fibonacciDPHelper(Integer[] memo, int n) {
    if (n == 0 )
      memo[n] = 0;
    else if (n < 2)
      memo[n] = 1;

    if (memo[n] == null)
      memo[n] = fibonacciDPHelper(memo, n - 1) + fibonacciDPHelper(memo, n - 2);

    return memo[n];
  }

}
