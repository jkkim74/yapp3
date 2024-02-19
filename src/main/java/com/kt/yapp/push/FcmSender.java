/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * Familybox version 4.0
 *
 *  Copyright ⓒ 2020 kt corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 * 
 * 시스템명 : 패밀리박스 4.0
 * 업 무 명 : 
 * 파 일 명 : FcmSender.java
 * 작 성 자 : 이주혁
 * 작 성 일 : 2020. 3. 23.
 * 설    명 : 
 * ************************************************************
 * 변경일     | 변경자 | 변경내역
 * ************************************************************
 * 2020. 3. 23. | 이주혁 | 최초작성
 * ************************************************************
 */
package com.kt.yapp.push;

import static com.kt.yapp.push.FcmConstants.JSON_CANONICAL_IDS;
import static com.kt.yapp.push.FcmConstants.JSON_ERROR;
import static com.kt.yapp.push.FcmConstants.JSON_FAILURE;
import static com.kt.yapp.push.FcmConstants.JSON_MESSAGE_ID;
import static com.kt.yapp.push.FcmConstants.JSON_MULTICAST_ID;
import static com.kt.yapp.push.FcmConstants.JSON_NOTIFICATION;
import static com.kt.yapp.push.FcmConstants.JSON_NOTIFICATION_BADGE;
import static com.kt.yapp.push.FcmConstants.JSON_NOTIFICATION_BODY;
import static com.kt.yapp.push.FcmConstants.JSON_NOTIFICATION_BODY_LOC_ARGS;
import static com.kt.yapp.push.FcmConstants.JSON_NOTIFICATION_BODY_LOC_KEY;
import static com.kt.yapp.push.FcmConstants.JSON_NOTIFICATION_CLICK_ACTION;
import static com.kt.yapp.push.FcmConstants.JSON_NOTIFICATION_COLOR;
import static com.kt.yapp.push.FcmConstants.JSON_NOTIFICATION_ICON;
import static com.kt.yapp.push.FcmConstants.JSON_NOTIFICATION_SOUND;
import static com.kt.yapp.push.FcmConstants.JSON_NOTIFICATION_TAG;
import static com.kt.yapp.push.FcmConstants.JSON_NOTIFICATION_TITLE;
import static com.kt.yapp.push.FcmConstants.JSON_NOTIFICATION_TITLE_LOC_ARGS;
import static com.kt.yapp.push.FcmConstants.JSON_NOTIFICATION_TITLE_LOC_KEY;
import static com.kt.yapp.push.FcmConstants.JSON_PAYLOAD;
import static com.kt.yapp.push.FcmConstants.JSON_REGISTRATION_IDS;
import static com.kt.yapp.push.FcmConstants.JSON_RESULTS;
import static com.kt.yapp.push.FcmConstants.JSON_SUCCESS;
import static com.kt.yapp.push.FcmConstants.JSON_TO;
import static com.kt.yapp.push.FcmConstants.PARAM_COLLAPSE_KEY;
import static com.kt.yapp.push.FcmConstants.PARAM_CONTENT_AVAILABLE;
import static com.kt.yapp.push.FcmConstants.PARAM_DELAY_WHILE_IDLE;
import static com.kt.yapp.push.FcmConstants.PARAM_DRY_RUN;
import static com.kt.yapp.push.FcmConstants.PARAM_MUTABLE_CONTENT;
import static com.kt.yapp.push.FcmConstants.PARAM_PRIORITY;
import static com.kt.yapp.push.FcmConstants.PARAM_RESTRICTED_PACKAGE_NAME;
import static com.kt.yapp.push.FcmConstants.PARAM_TIME_TO_LIVE;
import static com.kt.yapp.push.FcmConstants.TOKEN_CANONICAL_REG_ID;
import static com.kt.yapp.push.FcmConstants.TOPIC_PREFIX;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FcmSender {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inject Beans
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Member Variables
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    protected static final String UTF8 = "UTF-8";

    /**
     * Initial delay before first retry, without jitter.
     */
    protected static final int BACKOFF_INITIAL_DELAY = 1000;
    /**
     * Maximum delay before a retry.
     */
    protected static final int MAX_BACKOFF_DELAY = 1024000;

    protected final Random random = new Random();
    protected static final Logger logger = Logger.getLogger(FcmSender.class.getName());
    
//    protected Proxy proxy = null;

    private final String key;

//    private FcmEndpoint endpoint;
    private String endpoint;
    
    private int connectTimeout;
    private int readTimeout;
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Default constructor.
     *
     * @param key API key obtained through the Google API Console.
     * @param endpoint Targeting endpoint (GCM/FCM)
     */
//    public FcmSender(String key, FcmEndpoint endpoint) {
//      this.key = nonNull(key);
//      this.endpoint = endpoint;
//    }
    
//    public FcmSender(String key, Proxy proxy, FcmEndpoint endpoint) {
//      this.key = nonNull(key);
//      this.proxy = proxy;
//      this.endpoint = endpoint;
//    }
    
    public FcmSender(String key) {
        this.key = key;
        this.endpoint = FcmConstants.FCM_SEND_ENDPOINT;
    }
    
    public FcmSender(String key, String endpoint) {
      this.key = nonNull(key);
      this.endpoint = endpoint;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Public Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public String getEndpoint() {
        return endpoint;
    }
    
    public final void setConnectTimeout(int connectTimeout) {
        if (connectTimeout < 0) {
            throw new IllegalArgumentException("timeout can not be negative");
        }
        this.connectTimeout = connectTimeout;
    }
    
    public final void setReadTimeout(int readTimeout) {
        if(readTimeout < 0) {
            throw new IllegalArgumentException("readTimeout can not be negative");
        }
        this.readTimeout = readTimeout;
    }
    
    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }
    
    /**
     * Sends a message to one device, retrying in case of unavailability.
     *
     * <p>
     * <strong>Note: </strong> this method uses exponential back-off to retry in
     * case of service unavailability and hence could block the calling thread
     * for many seconds.
     *
     * @param message message to be sent, including the device's registration id.
     * @param registrationId device where the message will be sent.
     * @param retries number of retries in case of service unavailability errors.
     *
     * @return result of the request (see its javadoc for more details)
     *
     * @throws IllegalArgumentException if registrationId is {@literal null}.
     * @throws InvalidRequestException if GCM didn't returned a 200 or 503 status.
     * @throws IOException if message could not be sent.
     */
    public FcmResult send(FcmMessage message, String to, int retries)
        throws IOException {
      int attempt = 0;
      FcmResult result = null;
      int backoff = BACKOFF_INITIAL_DELAY;
      boolean tryAgain;
      do {
        attempt++;
        if (logger.isLoggable(Level.FINE)) {
          logger.fine("Attempt #" + attempt + " to send message " +
              message + " to regIds " + to);
        }
        result = sendNoRetry(message, to);
        
        tryAgain = result == null && attempt <= retries;
        if (tryAgain) {
          int sleepTime = backoff / 2 + random.nextInt(backoff);
          sleep(sleepTime);
          if (2 * backoff < MAX_BACKOFF_DELAY) {
            backoff *= 2;
          }
        }
      } while (tryAgain);
      if (result == null) {
        logger.info("Could not send message after " + attempt +
            " attempts");
      }
      return result;
    }

    public FcmResult sendNoRetry(FcmMessage message, String to) throws IOException{
        nonNull(to);
        
        Map<Object, Object> jsonRequest = new HashMap<Object, Object>();
        messageToMap(message,jsonRequest);
        jsonRequest.put(JSON_TO, to);
        String responseBody = makeHttpRequest(jsonRequest);
        
        if(responseBody == null) {
            return null;
        }
        
        JSONParser parser = new JSONParser();
        JSONObject jsonResponse;
        
        try {
            jsonResponse = (JSONObject) parser.parse(responseBody);
            FcmResult.Builder resultBuilder = new FcmResult.Builder();
            
            if(jsonResponse.containsKey("results")) {
                JSONArray jsonResults = (JSONArray) jsonResponse.get("results");
                if(jsonResults.size() == 1) {
                    JSONObject jsonResult = (JSONObject) jsonResults.get(0);
                    String messageId = (String)jsonResult.get(JSON_MESSAGE_ID);
                    String canonicalRegId = (String)jsonResult.get(TOKEN_CANONICAL_REG_ID);
                    String error = (String)jsonResult.get(JSON_ERROR);
                    resultBuilder.messageId(messageId)
                       .canonicalRegistrationId(canonicalRegId)
                       .errorCode(error);
                } else {
                    logger.log(Level.WARNING,"Found null or" + jsonRequest.size() + " results expected one");
                    return null;
                }
            } else if (to.startsWith(TOPIC_PREFIX)) {
                if(jsonResponse.containsKey(JSON_MESSAGE_ID)) {
                //message_id is expected when this is then response from a topic message
                    Long messageId = (Long) jsonResponse.get(JSON_MESSAGE_ID);
                    resultBuilder.messageId(messageId.toString());
                } else if (jsonResponse.containsKey(JSON_ERROR)) {
                    String error = (String)jsonResponse.get(JSON_ERROR);
                    resultBuilder.errorCode(error);
                } else {
                    logger.log(Level.WARNING, "Excpected " + JSON_MESSAGE_ID + " or" + JSON_ERROR + 
                                " found : " + responseBody);
                    return null;
                }          
            } else if (jsonResponse.containsKey(JSON_SUCCESS) && jsonResponse.containsKey(JSON_FAILURE)) {
                // success and failure are expected when response is from group message
                int success = getNumber(jsonResponse, JSON_SUCCESS).intValue();
                int failure = getNumber(jsonResponse, JSON_FAILURE).intValue();
                List<String> failedIds = null;
                if (jsonResponse.containsKey("failed_registartion_ids")) {
                    JSONArray jFailedIds = (JSONArray) jsonResponse.get("failed_reistration_ids");
                    failedIds = new ArrayList<String>();
                    for(int i =0; i<jFailedIds.size(); i++) {
                        failedIds.add((String)jFailedIds.get(i));
                    }
                }
                resultBuilder.success(success).failure(failure).failedRegistrationIds(failedIds);
            } else {
                logger.warning("Unrecognized response : " + responseBody);
                logger.info("Unrecognized response.");
            }
            
            return resultBuilder.build();
        } catch(ParseException e) {
          throw newIoException(responseBody, e);  
        } catch(CustomParserException e) {
            throw newIoException(responseBody, e);              
        }
        
    }
 
    /**
     * Sends a message without retrying in case of service unavailability. See
     * {@link #send(Message, String, int)} for more info.
     *
     * @return result of the post, or {@literal null} if the GCM service was
     *         unavailable.
     *
     * @throws InvalidRequestException if GCM didn't returned a 200 or 503 status.
     * @throws IllegalArgumentException if registrationId is {@literal null}.
     */
//    public FcmResult sendNoRetry(FcmMessage message, String registrationId)
//        throws IOException {
//      StringBuilder body = newBody(PARAM_REGISTRATION_ID, registrationId);
//      Boolean delayWhileIdle = message.isDelayWhileIdle();
//      if (delayWhileIdle != null) {
//        addParameter(body, PARAM_DELAY_WHILE_IDLE, delayWhileIdle ? "1" : "0");
//      }
//      String collapseKey = message.getCollapseKey();
//      if (collapseKey != null) {
//        addParameter(body, PARAM_COLLAPSE_KEY, collapseKey);
//      }
//      Integer timeToLive = message.getTimeToLive();
//      if (timeToLive != null) {
//        addParameter(body, PARAM_TIME_TO_LIVE, Integer.toString(timeToLive));
//      }
//      for (Entry<String, String> entry : message.getData().entrySet()) {
//        String key = PARAM_PAYLOAD_PREFIX + entry.getKey();
//        String value = entry.getValue();
//        addParameter(body, key, URLEncoder.encode(value, UTF8));
//      }
//      String requestBody = body.toString();
//      logger.finest("Request body: " + requestBody);
//      HttpURLConnection conn = post(endpoint.getUrl(), requestBody);
//      int status = conn.getResponseCode();
//      if (status == 503) {
//        logger.fine("Fcm service is unavailable");
//        return null;
//      }
//      if (status != 200) {
//        throw new FcmInvalidRequestException(status);
//      }
//      try {
//        BufferedReader reader =
//            new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        try {
//          String line = reader.readLine();
//
//          if (line == null || line.equals("")) {
//            throw new IOException("Received empty response from GCM service.");
//          }
//          String[] responseParts = split(line);
//          String token = responseParts[0];
//          String value = responseParts[1];
//          if (token.equals(TOKEN_MESSAGE_ID)) {
//            Builder builder = new FcmResult.Builder().messageId(value);
//            // check for canonical registration id
//            line = reader.readLine();
//            if (line != null) {
//              responseParts = split(line);
//              token = responseParts[0];
//              value = responseParts[1];
//              if (token.equals(TOKEN_CANONICAL_REG_ID)) {
//                builder.canonicalRegistrationId(value);
//              } else {
//                logger.warning("Received invalid second line from GCM: " + line);
//              }
//            }
//
//            FcmResult result = builder.build();
//            if (logger.isLoggable(Level.FINE)) {
//              logger.fine("FcmMessage created succesfully (" + result + ")");
//            }
//            return result;
//          } else if (token.equals(TOKEN_ERROR)) {
//            return new FcmResult.Builder().errorCode(value).build();
//          } else {
//            throw new IOException("Received invalid response from GCM: " + line);
//          }
//        } finally {
//          reader.close();
//        }
//      } finally {
//        conn.disconnect();
//      }
//    }    
    /**
     * Sends a message to many devices, retrying in case of unavailability.
     *
     * <p>
     * <strong>Note: </strong> this method uses exponential back-off to retry in
     * case of service unavailability and hence could block the calling thread
     * for many seconds.
     *
     * @param message message to be sent.
     * @param regIds registration id of the devices that will receive
     *        the message.
     * @param retries number of retries in case of service unavailability errors.
     *
     * @return combined result of all requests made.
     *
     * @throws IllegalArgumentException if registrationIds is {@literal null} or
     *         empty.
     * @throws InvalidRequestException if GCM didn't returned a 200 or 503 status.
     * @throws IOException if message could not be sent.
     */
    public FcmMulticastResult send(FcmMessage message, List<String> regIds, int retries)
        throws IOException {
      int attempt = 0;
      FcmMulticastResult multicastResult;
      int backoff = BACKOFF_INITIAL_DELAY;
      // Map of results by registration id, it will be updated after each attempt
      // to send the messages
      Map<String, FcmResult> results = new HashMap<String, FcmResult>();
      List<String> unsentRegIds = new ArrayList<String>(regIds);
      boolean tryAgain;
      List<Long> multicastIds = new ArrayList<Long>();
      do {
          multicastResult = null;  
          attempt++;
          if (logger.isLoggable(Level.FINE)) {
              logger.fine("Attempt #" + attempt + " to send message " +
                  message + " to regIds " + unsentRegIds);
          }
          
          try {
              multicastResult = sendNoRetry(message, unsentRegIds);
          } catch (IOException e) {
              logger.log(Level.FINEST,"IOException on attempt " + attempt, e);
          }
          
          if(multicastResult != null) {

              long multicastId = multicastResult.getMulticastId();
              logger.fine("multicast_id on attempt # " + attempt + ": " +
                  multicastId);
              multicastIds.add(multicastId);
              unsentRegIds = updateStatus(unsentRegIds, results, multicastResult);
              tryAgain = !unsentRegIds.isEmpty() && attempt <= retries;
          } else {
              tryAgain = attempt <= retries;
          }
          
          
          if (tryAgain) {
              int sleepTime = backoff / 2 + random.nextInt(backoff);
              sleep(sleepTime);
              if (2 * backoff < MAX_BACKOFF_DELAY) {
                  backoff *= 2;
              }
          }
      } while (tryAgain);
      
      if(multicastIds.isEmpty()) {
          logger.info("Could not post JSON requests to FCM after " +
                  attempt + " attempts");
      }
      
      // calculate summary
      int success = 0, failure = 0 , canonicalIds = 0;
      for (FcmResult result : results.values()) {
        if (result.getMessageId() != null) {
          success++;
          if (result.getCanonicalRegistrationId() != null) {
            canonicalIds++;
          }
        } else {
          failure++;
        }
      }
      // build a new object with the overall result
      long multicastId = multicastIds.remove(0);
      FcmMulticastResult.Builder builder = new FcmMulticastResult.Builder(success,
          failure, canonicalIds, multicastId).retryMulticastIds(multicastIds);
      // add results, in the same order as the input
      for (String regId : regIds) {
        FcmResult result = results.get(regId);
        builder.addFcmResult(result);
      }
      return builder.build();
    }    
    
    /**
     * Sends a message without retrying in case of service unavailability. See
     * {@link #send(Message, List, int)} for more info.
     *
     * @return {@literal true} if the message was sent successfully,
     *         {@literal false} if it failed but could be retried.
     *
     * @throws IllegalArgumentException if registrationIds is {@literal null} or
     *         empty.
     * @throws InvalidRequestException if GCM didn't returned a 200 status.
     * @throws IOException if message could not be sent or received.
     */
    public FcmMulticastResult sendNoRetry(FcmMessage message,
        List<String> registrationIds) throws IOException {
      if (nonNull(registrationIds).isEmpty()) {
        throw new IllegalArgumentException("registrationIds cannot be empty");
      }
      Map<Object, Object> jsonRequest = new HashMap<Object, Object>();
      messageToMap(message, jsonRequest);
      jsonRequest.put(JSON_REGISTRATION_IDS, registrationIds);
      
      String responseBody = makeHttpRequest(jsonRequest);
      
      if(responseBody == null) {
          return null;
      }
      
      JSONParser parser = new JSONParser();
      JSONObject jsonResponse;
      try {
        jsonResponse = (JSONObject) parser.parse(responseBody);
        int success = getNumber(jsonResponse, JSON_SUCCESS).intValue();
        int failure = getNumber(jsonResponse, JSON_FAILURE).intValue();
        int canonicalIds = getNumber(jsonResponse, JSON_CANONICAL_IDS).intValue();
        long multicastId = getNumber(jsonResponse, JSON_MULTICAST_ID).longValue();
        FcmMulticastResult.Builder builder = new FcmMulticastResult.Builder(success,
            failure, canonicalIds, multicastId);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> results =
            (List<Map<String, Object>>) jsonResponse.get(JSON_RESULTS);
        if (results != null) {
          for (Map<String, Object> jsonResult : results) {
            String messageId = (String) jsonResult.get(JSON_MESSAGE_ID);
            String canonicalRegId =
                (String) jsonResult.get(TOKEN_CANONICAL_REG_ID);
            String error = (String) jsonResult.get(JSON_ERROR);
            FcmResult result = new FcmResult.Builder()
                .messageId(messageId)
                .canonicalRegistrationId(canonicalRegId)
                .errorCode(error)
                .build();
            builder.addFcmResult(result);
          }
        }
        return builder.build();
      } catch (ParseException e) {
        throw newIoException(responseBody, e);
      } catch (CustomParserException e) {
        throw newIoException(responseBody, e);
      }
    }    
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Private/Protected Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * Updates the status of the messages sent to devices and the list of devices
     * that should be retried.
     *
     * @param unsentRegIds list of devices that are still pending an update.
     * @param allResults map of status that will be updated.
     * @param multicastResult result of the last multicast sent.
     *
     * @return updated version of devices that should be retried.
     */
    private List<String> updateStatus(List<String> unsentRegIds,
        Map<String, FcmResult> allResults, FcmMulticastResult multicastResult) {
      List<FcmResult> results = multicastResult.getResults();
      if (results.size() != unsentRegIds.size()) {
        // should never happen, unless there is a flaw in the algorithm
        throw new RuntimeException("Internal error: sizes do not match. " +
            "currentResults: " + results + "; unsentRegIds: " + unsentRegIds);
      }
      List<String> newUnsentRegIds = new ArrayList<String>();
      for (int i = 0; i < unsentRegIds.size(); i++) {
        String regId = unsentRegIds.get(i);
        FcmResult result = results.get(i);
        allResults.put(regId, result);
        String error = result.getErrorCodeName();

        if (error != null && (error != null && error.equals(FcmConstants.ERROR_UNAVAILABLE)
		|| error.equals(FcmConstants.ERROR_INTERNAL_SERVER_ERROR)))  {
          newUnsentRegIds.add(regId);
        }
      }
      return newUnsentRegIds;
    }

    private IOException newIoException(String responseBody, Exception e) {
      // log exception, as IOException constructor that takes a message and cause
      // is only available on Java 6
      String msg = "Error parsing JSON response (" + responseBody + ")";
      logger.log(Level.WARNING, msg, e);
      return new IOException(msg + ":" + e);
    }
    /**
     * Sets a JSON field, but only if the value is not {@literal null}.
     */
    private void setJsonField(Map<Object, Object> json, String field,
        Object value) {
      if (value != null) {
        json.put(field, value);
      }
    }
    private Number getNumber(Map<?, ?> json, String field) {
        Object value = json.get(field);
        if (value == null) {
          throw new CustomParserException("Missing field: " + field);
        }
        if (!(value instanceof Number)) {
          throw new CustomParserException("Field " + field +
              " does not contain a number: " + value);
        }
        return (Number) value;
    }

    private String[] split(String line) throws IOException {
      String[] split = line.split("=", 2);
      if (split.length != 2) {
        throw new IOException("Received invalid response line from GCM: " + line);
      }
      return split;
    }   
    
    /**
     * Make an HTTP post to a given URL.
     *
     * @return HTTP response.
     */
    protected HttpURLConnection post(String url, String body)
        throws IOException {
      return post(url, "application/x-www-form-urlencoded;charset=UTF-8", body);
    }

    protected HttpURLConnection post(String url, String contentType, String body)
        throws IOException {
      if (url == null || body == null) {
        throw new IllegalArgumentException("arguments cannot be null");
      }
      if (!url.startsWith("https://")) {
        logger.warning("URL does not use https: " + url);
      }
      logger.fine("Sending POST to " + url);
      logger.finest("POST body: " + body);
      byte[] bytes = body.getBytes(UTF8);
      HttpURLConnection conn = getConnection(url);
      conn.setDoOutput(true);
      conn.setUseCaches(false);
      conn.setFixedLengthStreamingMode(bytes.length);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", contentType);
      conn.setRequestProperty("Authorization", "key=" + key);
      OutputStream out = conn.getOutputStream();
      out.write(bytes);
      out.close();
      return conn;
    }
    /**
     * Creates a map with just one key-value pair.
     */
    protected static final Map<String, String> newKeyValues(String key,
        String value) {
      Map<String, String> keyValues = new HashMap<String, String>(1);
      keyValues.put(nonNull(key), nonNull(value));
      return keyValues;
    }
    /**
     * Creates a {@link StringBuilder} to be used as the body of an HTTP POST.
     *
     * @param name initial parameter for the POST.
     * @param value initial value for that parameter.
     * @return StringBuilder to be used an HTTP POST body.
     */
    protected static StringBuilder newBody(String name, String value) {
      return new StringBuilder(nonNull(name)).append('=').append(nonNull(value));
    }
    /**
     * Adds a new parameter to the HTTP POST body.
     *
     * @param body HTTP POST body
     * @param name parameter's name
     * @param value parameter's value
     */
    protected static void addParameter(StringBuilder body, String name,
        String value) {
      nonNull(body).append('&')
          .append(nonNull(name)).append('=').append(nonNull(value));
    }
    /**
     * Gets an {@link HttpURLConnection} given an URL.
     */
    protected HttpURLConnection getConnection(String url) throws IOException {

      HttpURLConnection conn= conn = (HttpURLConnection) new URL(url).openConnection();
      conn.setConnectTimeout(connectTimeout);
      conn.setReadTimeout(readTimeout);
    
//      if (null != proxy) {
//        conn = (HttpURLConnection) new URL(url).openConnection(proxy);
//      } else {
//        conn = (HttpURLConnection) new URL(url).openConnection();
//      }
       
      return conn;
    }
    
    private static String getAndClose(InputStream stream) throws IOException {
        try {
            return getString(stream);
        } finally {
            if(stream != null) {
                close(stream);
            }
        }
    }
    
    /**
     * Convenience method to convert an InputStream to a String.
     *
     * <p>
     * If the stream ends in a newline character, it will be stripped.
     */
    protected static String getString(InputStream stream) throws IOException {
        
      if(stream == null) {
          return "";
      }
        
      BufferedReader reader =
          new BufferedReader(new InputStreamReader(nonNull(stream)));
      StringBuilder content = new StringBuilder();
      String newLine;
      do {
        newLine = reader.readLine();
        if (newLine != null) {
          content.append(newLine).append('\n');
        }
      } while (newLine != null);
      if (content.length() > 0) {
        // strip last newline
        content.setLength(content.length() - 1);
      }
      return content.toString();
    }
    
    static <T> T nonNull(T argument) {
        if (argument == null) {
          throw new IllegalArgumentException("argument cannot be null");
        }
        return argument;
      }
    
    void sleep(long millis) {
        try {
          Thread.sleep(millis);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
    
    private static void close(Closeable closable) {
        if(closable != null) {
            try {
               closable.close();
            } catch (IOException e) {
                logger.log(Level.FINEST, "IOException closing stream",e);
            }
            
        }
    }
    
    private String makeHttpRequest(Map<Object,Object> jsonRequest) throws FcmInvalidRequestException {
        String requestBody = JSONValue.toJSONString(jsonRequest);
        logger.finest("JSON Request :" +requestBody);
        HttpURLConnection conn;
        int status;
        try {
            conn = post(getEndpoint(),"application/json",requestBody);
            status = conn.getResponseCode();
        } catch (IOException e) {
            logger.log(Level.FINE,"IOexception posting to FCM", e);
            return null;
        }
        
        String responseBody = null;
        if (status != 200) {
            try {
                responseBody = getAndClose(conn.getErrorStream());
                logger.finest("JSON error response : " + responseBody);
            } catch (IOException e) {
                responseBody ="N/A";
                logger.log(Level.FINE,"Exception reading response ", e);
            }
            throw new FcmInvalidRequestException(status, responseBody);
        } 
        
        try {

            responseBody = getAndClose(conn.getInputStream());
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException reading response", e);
            return null;
        }
        logger.finest("JSON response : " + responseBody);
        return responseBody;
    }
    
    private void messageToMap(FcmMessage message, Map<Object,Object> mapRequest) {
        if(message == null || mapRequest == null) {
            return; 
        }
        
        setJsonField(mapRequest, PARAM_PRIORITY, message.getPriority());
        setJsonField(mapRequest, PARAM_CONTENT_AVAILABLE, message.getContentAvailable());
        setJsonField(mapRequest, PARAM_MUTABLE_CONTENT, message.getMutableContent());
        setJsonField(mapRequest, PARAM_TIME_TO_LIVE, message.getTimeToLive());
        setJsonField(mapRequest, PARAM_COLLAPSE_KEY, message.getCollapseKey());
        setJsonField(mapRequest, PARAM_RESTRICTED_PACKAGE_NAME, message.getRestrictedPacakgeName());
        setJsonField(mapRequest, PARAM_DELAY_WHILE_IDLE, message.getDelayWhileIdle());
        setJsonField(mapRequest, PARAM_DRY_RUN, message.getDryRun());
        Map<String, String> payload = message.getData();
        if(!payload.isEmpty()) {
            mapRequest.put(JSON_PAYLOAD, payload);
        }
        if(message.getNotification() != null) {
            Notification notification = message.getNotification();
            Map<Object, Object> nMap = new HashMap<Object,Object>();
            if (notification.getBadge() != null) {
                setJsonField(nMap, JSON_NOTIFICATION_BADGE,notification.getBadge().toString());
            }
            setJsonField(nMap,JSON_NOTIFICATION_BODY,notification.getBody());
            setJsonField(nMap,JSON_NOTIFICATION_BODY_LOC_KEY,notification.getBodyLocKey());
            setJsonField(nMap,JSON_NOTIFICATION_BODY_LOC_ARGS,notification.getBodyLocArgs());
            setJsonField(nMap,JSON_NOTIFICATION_CLICK_ACTION,notification.getClickAction());
            setJsonField(nMap,JSON_NOTIFICATION_COLOR,notification.getColor());
            setJsonField(nMap,JSON_NOTIFICATION_ICON,notification.getIcon());
            setJsonField(nMap,JSON_NOTIFICATION_SOUND,notification.getSound());
            setJsonField(nMap,JSON_NOTIFICATION_TAG,notification.getTag());
            setJsonField(nMap,JSON_NOTIFICATION_TITLE,notification.getTitle());
            setJsonField(nMap,JSON_NOTIFICATION_TITLE_LOC_ARGS,notification.getTitleLocArgs());
            setJsonField(nMap,JSON_NOTIFICATION_TITLE_LOC_KEY,notification.getTitleLocKey());
            mapRequest.put(JSON_NOTIFICATION, nMap);
        }
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overrided Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inner Classes
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    

    class CustomParserException extends RuntimeException {
      CustomParserException(String message) {
        super(message);
      }
    }
}
