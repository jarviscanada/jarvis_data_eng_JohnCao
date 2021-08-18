package ca.jrvs.apps.twitter.service;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Tweet;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwitterService implements Service {

  private static final int MAX_ALLOWED_CHARS = 140;
  private static final double MIN_LONGITUDE = -180;
  private static final double MAX_LONGITUDE = 180;
  private static final double MIN_LATITUDE = -90;
  private static final double MAX_LATITUDE = 90;
  private static final List<String> VALID_FIELD_PARAMS = Arrays.asList("created_at", "id", "id_str", "text", "entities", "coordinates", "retweet_count", "favorite_count", "favorited", "retweeted");

  private CrdDao dao;

  //@Autowired
  public TwitterService(CrdDao dao) {
    this.dao = dao;
  }

  /**
   * Validate and post a user input Tweet
   *
   * @param tweet tweet to be created
   * @return created tweet
   * @throws IllegalArgumentException if text exceed max number of allowed characters or lat/long
   *                                  out of range
   */
  @Override
  public Tweet postTweet(Tweet tweet) {
    // invalid tweet parameters
    if (tweet.getText().length() > MAX_ALLOWED_CHARS) {
      throw new IllegalArgumentException("Tweet text exceeds max number of allowed characters.");
    }
    if (tweet.getCoordinates().getCoordinates().get(0) < MIN_LONGITUDE || tweet.getCoordinates().getCoordinates().get(1) > MAX_LONGITUDE) {
      throw new IllegalArgumentException("Longitude out of range");
    }
    if (tweet.getCoordinates().getCoordinates().get(1) < MIN_LATITUDE || tweet.getCoordinates().getCoordinates().get(1) > MAX_LATITUDE) {
      throw new IllegalArgumentException("Latitude out of range");
    }

    return (Tweet) dao.create(tweet);
  }

  /**
   * Search a tweet by ID
   *
   * @param id     tweet id
   * @param fields set fields not in the list to null
   * @return Tweet object which is returned by the Twitter API
   * @throws IllegalArgumentException if id or fields param is invalid
   */
  @Override
  public Tweet showTweet(String id, String[] fields) {
    validateTweetId(id);
    Tweet tweet = (Tweet) dao.findById(id);
    Tweet ret = new Tweet();

    if(fields != null) {
      for (String field : fields) {
        if(field.equals(VALID_FIELD_PARAMS.get(0)))
          ret.setCreatedAt(tweet.getCreatedAt());
        else if(field.equals(VALID_FIELD_PARAMS.get(1)))
          ret.setId(tweet.getId());
        else if(field.equals(VALID_FIELD_PARAMS.get(2)))
          ret.setIdStr(tweet.getIdStr());
        else if(field.equals(VALID_FIELD_PARAMS.get(3)))
          ret.setText(tweet.getText());
        else if(field.equals(VALID_FIELD_PARAMS.get(4)))
          ret.setEntities(tweet.getEntities());
        else if(field.equals(VALID_FIELD_PARAMS.get(5)))
          ret.setCoordinates(tweet.getCoordinates());
        else if(field.equals(VALID_FIELD_PARAMS.get(6)))
          ret.setRetweetCount(tweet.getRetweetCount());
        else if(field.equals(VALID_FIELD_PARAMS.get(7)))
          ret.setFavoriteCount(tweet.getFavoriteCount());
        else if(field.equals(VALID_FIELD_PARAMS.get(8)))
          ret.setFavorited(tweet.isFavorited());
        else if(field.equals(VALID_FIELD_PARAMS.get(9)))
          ret.setRetweeted(tweet.isRetweeted());
        else
          throw new IllegalArgumentException("Invalid field: " + field);
      }
    }

    return ret;
  }

  /**
   * Delete Tweet(s) by id(s).
   *
   * @param ids tweet IDs which will be deleted
   * @return A list of Tweets
   * @throws IllegalArgumentException if one of the IDs is invalid.
   */
  @Override
  public List<Tweet> deleteTweets(String[] ids) {
    List<Tweet> deletedTweets = new ArrayList<>();
    Arrays.stream(ids).forEach(id ->  {
      validateTweetId(id);
      deletedTweets.add((Tweet) dao.deleteById(id));
    });

    return deletedTweets;
  }

  /**
   * Throws exception if id is invalid
   * @param id
   * @throws IllegalArgumentException if invalid id
   */
  private void validateTweetId(String id) {
    try {
      new BigInteger(id);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid tweet id");
    }
  }

}
