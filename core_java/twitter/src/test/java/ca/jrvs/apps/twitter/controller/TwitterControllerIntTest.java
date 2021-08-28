package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.dao.TwitterDao;
import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.dao.helper.TwitterHttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.TwitterService;
import ca.jrvs.apps.twitter.util.JsonUtils;
import ca.jrvs.apps.twitter.util.TweetUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterControllerIntTest {

  private TwitterController controller;
  private TwitterService service;
  private String tweetText;


  @Before
  public void setUp() throws Exception {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    System.out.println(consumerKey + "|" + consumerSecret + "|" + accessToken + "|" + tokenSecret);

    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
    CrdDao dao = new TwitterDao(httpHelper);
    this.service = new TwitterService(dao);
    this.controller = new TwitterController(this.service);

    String hashtag = "#abc";
    tweetText = "sometext" + hashtag + " " + System.currentTimeMillis();
  }

  @Test
  public void postTweet() {
    String[] args = {"post", tweetText, "50:50"};
    Tweet createdTweet = controller.postTweet(args);
    assertNotNull(createdTweet);
    assertEquals(tweetText, createdTweet.getText());

    assertNotNull(createdTweet.getCoordinates());
    assertEquals(2, createdTweet.getCoordinates().getCoordinates().size());
  }

  @Test
  public void showTweetNoFields() {
    Tweet tweet = service.postTweet(TweetUtils.buildTweet(tweetText, 50d, 50d));
    String[] args = {"show", tweet.getIdStr()};
    Tweet result = controller.showTweet(args);

    assertNotNull(result);
    assertEquals(tweetText, result.getText());
  }

  @Test
  public void showTweetWithFields() throws JsonProcessingException {
    Tweet tweet = service.postTweet(TweetUtils.buildTweet(tweetText, 50d, 50d));
    String[] args = {"show", tweet.getIdStr(), "id,text,coordinates"};
    Tweet result = controller.showTweet(args);

    String json = JsonUtils.toJson(result, true, false);
    System.out.println(json);
    assertTrue(json.contains("id"));
    assertTrue(json.contains("text"));
    assertTrue(json.contains("coordinates"));
    assertFalse(json.contains("created_at"));
    assertFalse(json.contains("retweeted"));
  }

  @Test
  public void deleteTweet() {
    Tweet tweet = service.postTweet(TweetUtils.buildTweet(tweetText, 50d, 50d));
    String[] args = {"delete", tweet.getIdStr()};
    List<Tweet> result = controller.deleteTweet(args);

    assertNotNull(result);
    assertEquals(tweetText, result.get(0).getText());
  }
}