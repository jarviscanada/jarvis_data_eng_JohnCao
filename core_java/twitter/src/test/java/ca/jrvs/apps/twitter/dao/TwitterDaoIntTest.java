package ca.jrvs.apps.twitter.dao;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtils;
import ca.jrvs.apps.twitter.util.TweetUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterDaoIntTest {

  private TwitterDao dao;
  private String tweetText;
  private Double lon;
  private Double lat;
  private Tweet tweet;

  @Before
  public void setUp() throws Exception {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);

    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
    this.dao = new TwitterDao(httpHelper);

    // Create test Tweet entity
    String hashtag = "#abc";
    lat = 1d;
    lon = -1d;
    tweetText = "sometext" + hashtag + " " + System.currentTimeMillis();
    tweet = TweetUtils.buildTweet(tweetText, lon, lat);
  }

  @Test
  public void create() throws Exception{
    Tweet createdTweet = dao.create(tweet);
    assertNotNull(createdTweet);
    assertEquals(tweetText, createdTweet.getText());

    assertNotNull(createdTweet.getCoordinates());
    assertEquals(2, createdTweet.getCoordinates().getCoordinates().size());
    assertEquals(lon, createdTweet.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, createdTweet.getCoordinates().getCoordinates().get(1));
  }

  @Test
  public void findById() {
    Tweet createdTweet = dao.create(tweet);
    Tweet foundTweet = dao.findById(createdTweet.getIdStr());

    assertEquals(tweetText, foundTweet.getText());
    assertEquals(lon, foundTweet.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, foundTweet.getCoordinates().getCoordinates().get(1));
  }

  @Test
  public void deleteById() {
    Tweet createdTweet = dao.create(tweet);
    Tweet deletedTweet = dao.deleteById(createdTweet.getIdStr());

    assertEquals(tweetText, deletedTweet.getText());
    assertEquals(lon, deletedTweet.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, deletedTweet.getCoordinates().getCoordinates().get(1));
  }
}