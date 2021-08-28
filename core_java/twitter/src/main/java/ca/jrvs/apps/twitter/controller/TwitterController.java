package ca.jrvs.apps.twitter.controller;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.TweetUtils;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Controller
public class TwitterController implements Controller{

  private static final String COORD_SEP = ":";
  private static final String COMMA = ",";

  private static final String POST_USAGE = "USAGE: TwitterCLIApp post \"tweet_text\" \"latitude:longitude\"";
  private static final String SHOW_USAGE = "USAGE: TwitterCLIApp show tweet_id [field1, field2, ...]";
  private static final String DELETE_USAGE = "USAGE: TwitterCLIApp delete [id1,id2,...]";

  private Service service;

  @Autowired
  public TwitterController(Service service) {
    this.service = service;
  }

  /**
   * Parse user argument and post a tweet by calling service classes
   *
   * @param args
   * @return a posted tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet postTweet(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("Invalid number of arguments\n" + POST_USAGE);
    }
    List<Double> coords;
    try {
      coords = Arrays.asList(args[2].split(COORD_SEP)).stream()
          .map(Double::parseDouble)
          .collect(Collectors.toList());
    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid coordinates format\n" + POST_USAGE);
    }

    Tweet tweet = TweetUtils.buildTweet(args[1], coords.get(1), coords.get(0));

    return service.postTweet(tweet);
  }

  /**
   * Parse user argument and search a tweet by calling service classes
   *
   * @param args
   * @return a tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet showTweet(String[] args) {
    if (args.length < 2 || args.length > 3)
      throw new IllegalArgumentException("Invalid number of arguments\n" + SHOW_USAGE);
    Tweet tweet;
    if(args.length == 2)
      tweet = service.showTweet(args[1], null);
    else {
      String[] fields = (String[]) Arrays.asList(args[2].toLowerCase().split(COMMA)).toArray();
      tweet = service.showTweet(args[1], fields);
    }

    return tweet;
  }

  /**
   * Parse user argument and delete tweets by calling service classes
   *
   * @param args
   * @return a list of deleted tweets
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public List<Tweet> deleteTweet(String[] args) {
    if (args.length < 2)
      throw new IllegalArgumentException("Invalid number of arguments\n" + DELETE_USAGE);
    String[] ids = (String[]) Arrays.asList(args[1].split(COMMA)).toArray();

    return service.deleteTweets(ids);
  }
}
