package ca.jrvs.apps.twitter.dao;

import ca.jrvs.apps.twitter.dao.helper.HttpHelper;
import ca.jrvs.apps.twitter.model.Tweet;
import ca.jrvs.apps.twitter.util.JsonUtils;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterDao implements CrdDao<Tweet, String>{

  //URI constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy";
  //URI symbols
  private static final String QUERY_SYM = "?";
  private static final String AMPERSAND = "&";
  private static final String EQUAL = "=";

  //Response code
  private static final int HTTP_OK = 200;

  private HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  /**
   * Create an entity(Tweet) to the underlying storage
   *
   * @param entity entity that to be created
   * @return created entity
   */
  @Override
  public Tweet create(Tweet entity) {
    URI uri = getUri(entity);

    HttpResponse response = httpHelper.httpPost(uri);
    return parseResponse(response, HTTP_OK);
  }

  /**
   * Find an entity(Tweet) by its id
   *
   * @param s entity id
   * @return Tweet entity
   */
  @Override
  public Tweet findById(String s) {
    String uriStr = API_BASE_URI + SHOW_PATH + QUERY_SYM + "id" + EQUAL + s;
    URI uri = getUri(uriStr);

    HttpResponse response = httpHelper.httpGet(uri);
    return parseResponse(response, HTTP_OK);
  }

  /**
   * Delete an entity(Tweet) by its ID
   *
   * @param s of the entity to be deleted
   * @return deleted entity
   */
  @Override
  public Tweet deleteById(String s) {
    String uriStr = API_BASE_URI + DELETE_PATH + "/" + s + ".json";
    URI uri = getUri(uriStr);

    HttpResponse response = httpHelper.httpPost(uri);
    return parseResponse(response, HTTP_OK);
  }

  /**
   * Convert response entity into a Tweet
   * @param response
   * @param expectedStatus
   * @return Tweet entity
   */
  public Tweet parseResponse (HttpResponse response, int expectedStatus) {
    int status = response.getStatusLine().getStatusCode();
    if (status != expectedStatus) {
      throw new RuntimeException("Unexpected HTTP status: " + status);
    }

    Tweet entity;
    try {
      String jsonStr = EntityUtils.toString(response.getEntity());
      entity = JsonUtils.toObjectFromJson(jsonStr, Tweet.class);
    } catch (IOException e) {
      throw new RuntimeException("Failed to parse JSON", e);
    }

    return entity;
  }

  private URI getUri(String s) {
    URI uri;
    try {
      uri = new URI(s);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid uri string", e);
    }

    return uri;
  }

  private URI getUri(Tweet tweet) {
    String textStatus = tweet.getText();
    Double longitude = tweet.getCoordinates().getCoordinates().get(0);
    Double latitude = tweet.getCoordinates().getCoordinates().get(1);

    PercentEscaper percentEscaper = new PercentEscaper("", false);
    String uriStr = API_BASE_URI + POST_PATH + QUERY_SYM + "status" + EQUAL + percentEscaper.escape(textStatus) +
        AMPERSAND + "long" + EQUAL + longitude + AMPERSAND + "lat" + EQUAL + latitude;

    return getUri(uriStr);
  }
}
