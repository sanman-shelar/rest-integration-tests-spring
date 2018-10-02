package com.app.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import java.util.Arrays;
import org.apache.commons.lang3.RandomUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.app.rest.response.Data;
import com.app.rest.response.UserPostResponse;

public class IntegrationTest {
  
  private TestRestTemplate testRestTemplate;
  private HttpHeaders headers;

  @Before
  public void beforeTest() {
    testRestTemplate = new TestRestTemplate();
    headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
  }

  @Test
  public void givenUserExists_whenUserInfoIsRetrieved_then200IsReceived() {
    // Given
    final int user = RandomUtils.nextInt(1, 10);
    String URL = "https://reqres.in/api/users/" + user;
    HttpEntity<String> entity = new HttpEntity<String>(null, headers);

    // When
    ResponseEntity<String> response = testRestTemplate.exchange(URL, HttpMethod.GET, entity, String.class);

    // Then
    assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
  }
  
  @Test
  public void givenUserDoesNotExist_whenUserInfoIsRetrieved_then404IsReceived() {
      // Given
      final int user = RandomUtils.nextInt(50, 100);
      String URL = "https://reqres.in/api/users/" + user;
      HttpEntity<String> entity = new HttpEntity<String>(null, headers);

      // When
      ResponseEntity<String> response = testRestTemplate.exchange(URL, HttpMethod.GET, entity, String.class);

      // Then
      assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
  }
  
  @Test
  public void givenRequestWithNoAcceptHeader_whenRequestIsExecuted_thenDefaultResponseContentTypeIsJson() {
    
     // Given
     String jsonMimeType = "[application/json; charset=utf-8]";
     String URL = "https://reqres.in/api/users/2";
     HttpEntity<String> entity = new HttpEntity<String>(null, headers);
   
     // When
     ResponseEntity<String> response = testRestTemplate.exchange(URL, HttpMethod.GET, entity, String.class);
   
     // Then
     String mimeType = response.getHeaders().get("Content-Type").toString();
     assertEquals( jsonMimeType, mimeType );
  }
  
  @Test
  public void givenUserExists_whenUserInfoIsRetrieved_thenRetrievedNameIsValid() {
      // Given      
      String URL = "https://reqres.in/api/users/2";
      HttpEntity<String> entity = new HttpEntity<String>(null, headers);

      // When
      //ResponseEntity<String> response = testRestTemplate.exchange(URL, HttpMethod.GET, entity, String.class);
      ResponseEntity<Data> user = testRestTemplate.exchange(URL, HttpMethod.GET, entity, Data.class);

      // Then
      assertThat("Janet", Matchers.is(user.getBody().getData().getFirst_name()));
      assertThat("Weaver", Matchers.is(user.getBody().getData().getLast_name()));
  }
  
  @Test
  public void givenUserDoesNotExist_whenUserInfoIsSubmitted_then201IsReceived() {
      // Given      
      String URL = "https://reqres.in/api/users";
      final String json = "{\"name\":\"John\",\"job\":\"Tester\"}";
      headers.add("Content-type", "application/json");
      HttpEntity<String> entity = new HttpEntity<String>(json, headers);

      // When
      ResponseEntity<UserPostResponse> response = testRestTemplate.exchange(URL, HttpMethod.POST, entity, UserPostResponse.class);

      // Then
      assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
  }
  
  @After
  public void afterTest() {
    headers = null;
    testRestTemplate = null;    
  }
}