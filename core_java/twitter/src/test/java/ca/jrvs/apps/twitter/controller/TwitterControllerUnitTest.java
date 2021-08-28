package ca.jrvs.apps.twitter.controller;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.service.Service;
import ca.jrvs.apps.twitter.util.TweetUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {

  private Tweet tweet = new Tweet();

  @Mock
  Service service;

  @InjectMocks
  TwitterController controller;

  @Before
  public void setUp() {
    String text = "test " + System.currentTimeMillis();
    tweet = TweetUtils.buildTweet(text, 50.0, 50.0);
    tweet.setIdStr("1234567890");
  }

  @Test
  public void postTweet() {
    when(service.postTweet(any())).thenReturn(tweet);
    String[] args = {"post", tweet.getText(), "50:50"};
    Tweet result = controller.postTweet(args);

    assertNotNull(result);
    assertNotNull(result.getText());
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetWrongNumberOfArgs() {
    String[] args = {"post"};
    controller.postTweet(args);
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetInvalidLocationFormat() {
    String[] args = {"post", "test", "50:7j"};
    controller.postTweet(args);
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetInvalidLocationFormat2() {
    String[] args = {"post", "test", "50;50"};
    controller.postTweet(args);
  }

  @Test
  public void showTweet() {
    when(service.showTweet(any(), any())).thenReturn(tweet);
    String[] args = {"show", tweet.getIdStr(), "id,text"};
    Tweet result = controller.showTweet(args);

    assertNotNull(result);
    assertNotNull(result.getText());
  }

  @Test(expected = IllegalArgumentException.class)
  public void showTweetInvalidArgs() {
    String[] args = {"show", "text", "text", "text", "text"};
    controller.showTweet(args);
  }

  @Test
  public void deleteTweet() {
    List<Tweet> tweets = new ArrayList<>();
    tweets.add(tweet);
    when(service.deleteTweets(any())).thenReturn(tweets);
    String[] args = {"delete", "11111,222222"};
    List<Tweet> result = controller.deleteTweet(args);

    assertNotNull(result);
  }
}