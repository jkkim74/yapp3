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
 * 업 무 명 : gcm-server Constants 변경
 * 파 일 명 : FcmConstants.java
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

public class FcmConstants {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inject Beans
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Member Variables
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /**
     * 20200325 constant customizing for fcm  
     * 
     */
    public static final String FCM_SEND_ENDPOINT = "https://fcm.googleapis.com/fcm/send";
    
    public static final String JSON_NOTIFICATION = "notification";
    
    
    public static final String JSON_NOTIFICATION_TITLE = "title";

    public static final String JSON_NOTIFICATION_BODY = "body";
    
    public static final String JSON_NOTIFICATION_ICON = "icon";
    
    public static final String JSON_NOTIFICATION_BADGE = "badge";
    
    public static final String JSON_NOTIFICATION_SOUND = "sound";
    
    public static final String JSON_NOTIFICATION_TAG = "tag";
    
    public static final String JSON_NOTIFICATION_COLOR = "color";
    
    public static final String JSON_NOTIFICATION_CLICK_ACTION = "click_action";
    
    public static final String JSON_NOTIFICATION_BODY_LOC_KEY = "body_loc_key";
    public static final String JSON_NOTIFICATION_BODY_LOC_ARGS = "body_loc_args";
    public static final String JSON_NOTIFICATION_TITLE_LOC_KEY = "title_loc_key";
    public static final String JSON_NOTIFICATION_TITLE_LOC_ARGS = "title_loc_args";

    public static final String JSON_PRIORITY = "priority";

    public static final String JSON_TO = "to";
    
    public static final String PARAM_TO = "to";
    
    public static final String TOPIC_PREFIX = "/topics/";
    
    public static final String PARAM_DRY_RUN ="dry_run";
    
    public static final String PARAM_RESTRICTED_PACKAGE_NAME = "restricted_package_name";
    
    public static final String PARAM_PRIORITY ="priority";
    
    public static final String PARAM_CONTENT_AVAILABLE = "content_available";
    
    public static final String PARAM_MUTABLE_CONTENT = "mutable_content";
    
    public static final String MESSAGE_PRIORITY_NORMAL = "normal";
    
    public static final String MESSAGE_PRIORITY_HIGH = "high";
    
    
    
    /**
     * HTTP parameter for registration id.
     */
    public static final String PARAM_REGISTRATION_ID = "registration_id";

    /**
     * HTTP parameter for collapse key.
     */
    public static final String PARAM_COLLAPSE_KEY = "collapse_key";

    /**
     * HTTP parameter for delaying the message delivery if the device is idle.
     * Used only FCM
     */
    public static final String PARAM_DELAY_WHILE_IDLE = "delay_while_idle";

    /**
     * Prefix to HTTP parameter used to pass key-values in the message payload.
     */
    public static final String PARAM_PAYLOAD_PREFIX = "data.";

    /**
     * Prefix to HTTP parameter used to set the message time-to-live.
     */
    public static final String PARAM_TIME_TO_LIVE = "time_to_live";

    /**
     * Too many messages sent by the sender. Retry after a while.
     */
    public static final String ERROR_QUOTA_EXCEEDED = "QuotaExceeded";

    /**
     * Too many messages sent by the sender to a specific device.
     * Retry after a while.
     */
    public static final String ERROR_DEVICE_QUOTA_EXCEEDED =
        "DeviceQuotaExceeded";

    /**
     * Missing registration_id.
     * Sender should always add the registration_id to the request.
     */
    public static final String ERROR_MISSING_REGISTRATION = "MissingRegistration";

    /**
     * Bad registration_id. Sender should remove this registration_id.
     */
    public static final String ERROR_INVALID_REGISTRATION = "InvalidRegistration";

    /**
     * The sender_id contained in the registration_id does not match the
     * sender_id used to register with the GCM servers.
     */
    public static final String ERROR_MISMATCH_SENDER_ID = "MismatchSenderId";

    /**
     * The user has uninstalled the application or turned off notifications.
     * Sender should stop sending messages to this device and delete the
     * registration_id. The client needs to re-register with the GCM servers to
     * receive notifications again.
     */
    public static final String ERROR_NOT_REGISTERED = "NotRegistered";

    /**
     * The payload of the message is too big, see the limitations.
     * Reduce the size of the message.
     */
    public static final String ERROR_MESSAGE_TOO_BIG = "MessageTooBig";

    /**
     * Collapse key is required. Include collapse key in the request.
     */
    public static final String ERROR_MISSING_COLLAPSE_KEY = "MissingCollapseKey";

    /**
     * A particular message could not be sent because the GCM servers were not
     * available. Used only on JSON requests, as in plain text requests
     * unavailability is indicated by a 503 response.
     */
    public static final String ERROR_UNAVAILABLE = "Unavailable";

    /**
     * A particular message could not be sent because the GCM servers encountered
     * an error. Used only on JSON requests, as in plain text requests internal
     * errors are indicated by a 500 response.
     */
    public static final String ERROR_INTERNAL_SERVER_ERROR =
        "InternalServerError";

    /**
     * Time to Live value passed is less than zero or more than maximum.
     */
    public static final String ERROR_INVALID_TTL= "InvalidTtl";

    /**
     * Token returned by GCM when a message was successfully sent.
     */
    public static final String TOKEN_MESSAGE_ID = "id";

    /**
     * Token returned by GCM when the requested registration id has a canonical
     * value.
     */
    public static final String TOKEN_CANONICAL_REG_ID = "registration_id";

    /**
     * Token returned by GCM when there was an error sending a message.
     */
    public static final String TOKEN_ERROR = "Error";

    /**
     * JSON-only field representing the registration ids.
     */
    public static final String JSON_REGISTRATION_IDS = "registration_ids";

    /**
     * JSON-only field representing the payload data.
     */
    public static final String JSON_PAYLOAD = "data";

    /**
     * JSON-only field representing the number of successful messages.
     */
    public static final String JSON_SUCCESS = "success";

    /**
     * JSON-only field representing the number of failed messages.
     */
    public static final String JSON_FAILURE = "failure";

    /**
     * JSON-only field representing the number of messages with a canonical
     * registration id.
     */
    public static final String JSON_CANONICAL_IDS = "canonical_ids";

    /**
     * JSON-only field representing the id of the multicast request.
     */
    public static final String JSON_MULTICAST_ID = "multicast_id";

    /**
     * JSON-only field representing the result of each individual request.
     */
    public static final String JSON_RESULTS = "results";

    /**
     * JSON-only field representing the error field of an individual request.
     */
    public static final String JSON_ERROR = "error";

    /**
     * JSON-only field sent by GCM when a message was successfully sent.
     */
    public static final String JSON_MESSAGE_ID = "message_id";

    private FcmConstants() {
      throw new UnsupportedOperationException();
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Public Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Private/Protected Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overrided Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inner Classes
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    

}
