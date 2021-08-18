package ca.jrvs.apps.twitter.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.dao.CrdDao;
import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.TweetUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

  private Tweet tweet = new Tweet();

  @Mock
  CrdDao dao;

  @InjectMocks
  TwitterService service;

  @Before
  public void setUp() throws Exception {
    String text = "test " + System.currentTimeMillis();
    tweet = TweetUtils.buildTweet(text, 50.0, 50.0);
  }

  @Test
  public void postTweet() {
    when(dao.create(any())).thenReturn(tweet);
    Tweet result = service.postTweet(tweet);
    assertNotNull(result);
    assertNotNull(result.getText());
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetTooLong() {
    String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur commodo eros enim, ac elementum dui aliquet et. Suspendisse fringilla convallis porttitor. Aliquam maximus magna ac pharetra tincidunt. Nunc feugiat sapien at ipsum laoreet porta.";
    tweet.setText(text);
    service.postTweet(tweet);
  }

  @Test(expected = IllegalArgumentException.class)
  public void postTweetBadCoordinates() {
    Coordinates badCoords = new Coordinates();
    List<Double> ls = new ArrayList<>();
    ls.add(500d);
    ls.add(500d);
    badCoords.setCoordinates(ls);
    tweet.setCoordinates(badCoords);
    service.postTweet(tweet);
  }

  @Test
  public void showTweet() {
    when(dao.findById(any())).thenReturn(tweet);
    tweet.setIdStr("1234567890");
    String[] fields = {"id", "text"};
    Tweet result = service.showTweet(tweet.getIdStr(), fields);
    assertNotNull(result);
    assertNotNull(result.getText());
    assertNull(result.getCoordinates());
  }

  @Test(expected = IllegalArgumentException.class)
  public void showTweetBadId() {
   service.showTweet("123abc", null);
  }

  @Test
  public void deleteTweets() {
    tweet.setIdStr("987654321");
    String[] ids = {tweet.getIdStr()};
    when(dao.deleteById(any())).thenReturn(tweet);
    List<Tweet> deleted = service.deleteTweets(ids);
    assertNotNull(deleted);
  }

  @Test(expected = IllegalArgumentException.class)
  public void deleteTweetsBadId() {
    String[] ids = {tweet.getIdStr(), "123445r"};
    service.deleteTweets(ids);
  }
}