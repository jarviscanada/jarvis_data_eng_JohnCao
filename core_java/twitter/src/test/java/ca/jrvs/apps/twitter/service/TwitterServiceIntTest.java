package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtils;
import ca.jrvs.apps.twitter.util.TweetUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterServiceIntTest {

  private TwitterService service;
  private TwitterDao dao;
  private Double lat;
  private Double lon;
  private String tweetText;
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
    this.service = new TwitterService(dao);

    // Create test Tweet entity
    String hashtag = "#abc";
    lat = 1d;
    lon = -1d;
    tweetText = "sometext" + hashtag + " " + System.currentTimeMillis();
    tweet = TweetUtils.buildTweet(tweetText, lon, lat);
  }

  @Test
  public void postTweet() {
    Tweet createdTweet = service.postTweet(tweet);
    assertNotNull(createdTweet);
    assertEquals(tweetText, createdTweet.getText());

    assertNotNull(createdTweet.getCoordinates());
    assertEquals(2, createdTweet.getCoordinates().getCoordinates().size());
    assertEquals(lon, createdTweet.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, createdTweet.getCoordinates().getCoordinates().get(1));
  }

  @Test
  public void showTweet() throws JsonProcessingException {
    Tweet createdTweet = dao.create(tweet);
    String[] fields = {"id", "text", "coordinates"};
    Tweet foundTweet = service.showTweet(createdTweet.getIdStr(), fields);

    assertEquals(tweetText, foundTweet.getText());
    assertEquals(lon, foundTweet.getCoordinates().getCoordinates().get(0));
    assertEquals(lat, foundTweet.getCoordinates().getCoordinates().get(1));

    // assert json only contains selected fields
    String json = JsonUtils.toJson(foundTweet, true, false);
    System.out.println(json);
    assertTrue(json.contains("id"));
    assertTrue(json.contains("text"));
    assertTrue(json.contains("coordinates"));
    assertFalse(json.contains("created_at"));
    assertFalse(json.contains("retweeted"));
  }

  @Test
  public void deleteTweets() {
    Tweet[] tweets = new Tweet[3];
    String[] ids = new String[tweets.length];
    for(int i = 0; i < tweets.length; i++) {
      String text = "sometext" + " " + System.currentTimeMillis();
      tweets[i] = dao.create(TweetUtils.buildTweet(text, lon, lat));
      ids[i] = tweets[i].getIdStr();
    }
    List<Tweet> deletedTweets = service.deleteTweets(ids);
    assertNotNull(deletedTweets);
    assertEquals(deletedTweets.get(0).getIdStr(), ids[0]);
    assertEquals(deletedTweets.get(0).getText(), tweets[0].getText());
    assertEquals(deletedTweets.get(1).getIdStr(), ids[1]);
    assertEquals(deletedTweets.get(1).getText(), tweets[1].getText());
    assertEquals(deletedTweets.get(2).getIdStr(), ids[2]);
    assertEquals(deletedTweets.get(2).getText(), tweets[2].getText());
  }
}