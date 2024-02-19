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
 * 업 무 명 : gcm-server Result 변경
 * 파 일 명 : FcmResult.java
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

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
/**
 * Result of a GCM message request that returned HTTP status code 200.
 *
 * <p>
 * If the message is successfully created, the {@link #getMessageId()} returns
 * the message id and {@link #getErrorCodeName()} returns {@literal null};
 * otherwise, {@link #getMessageId()} returns {@literal null} and 
 * {@link #getErrorCodeName()} returns the code of the error.
 *
 * <p>
 * There are cases when a request is accept and the message successfully
 * created, but GCM has a canonical registration id for that device. In this
 * case, the server should update the registration id to avoid rejected requests
 * in the future.
 * 
 * <p>
 * In a nutshell, the workflow to handle a result is:
 * <pre>
 *   - Call {@link #getMessageId()}:
 *     - {@literal null} means error, call {@link #getErrorCodeName()}
 *     - non-{@literal null} means the message was created:
 *       - Call {@link #getCanonicalRegistrationId()}
 *         - if it returns {@literal null}, do nothing.
 *         - otherwise, update the server datastore with the new id.
 * </pre>
 */
@Getter
public final class FcmResult implements Serializable {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inject Beans
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Member Variables
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final String messageId;
    private final String canonicalRegistrationId;
    private final String errorCode;
    private final Integer success;
    private final Integer failure;
    private List<String> failedRegistrationIds;
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Public Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public String getErrorCodeName() {
        return this.errorCode;
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Private/Protected Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private FcmResult(Builder builder) {
        canonicalRegistrationId = builder.canonicalRegistrationId;
        messageId = builder.messageId;
        errorCode = builder.errorCode;
        success = builder.success;
        failure = builder.failure;
        failedRegistrationIds = builder.failedRegistrationIds;
      }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overrided Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("[");
      if (messageId != null) { 
        builder.append(" messageId=").append(messageId);
      }
      if (canonicalRegistrationId != null) {
        builder.append(" canonicalRegistrationId=")
            .append(canonicalRegistrationId);
      }
      if (errorCode != null) { 
        builder.append(" errorCode=").append(errorCode);
      }
      if(success != null) {
          builder.append(" success=").append(success);   
      }
      if(failure != null) {
          builder.append(" failure=").append(failure);   
      }
      if(failedRegistrationIds != null) {

          builder.append(" failedRegistrationIds=").append(failedRegistrationIds);   
      }
       
      return builder.append(" ]").toString();
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inner Classes
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
    public static final class Builder {

        // optional parameters
        private String messageId;
        private String canonicalRegistrationId;
        private String errorCode;
        private Integer success;
        private Integer failure;
        private List<String> failedRegistrationIds;

        public Builder canonicalRegistrationId(String value) {
          canonicalRegistrationId = value;
          return this;
        }

        public Builder messageId(String value) {
          messageId = value;
          return this;
        }

        public Builder errorCode(String value) {
          errorCode = value;
          return this;
        }
        
        public Builder success(Integer value) {
            success = value;
            return this;
        }
        
        public Builder failure(Integer value) {
            failure = value;
            return this;
        }
        
        public Builder failedRegistrationIds(List<String> value) {
            failedRegistrationIds = value;
            return this;
        }
        
        public FcmResult build() {
          return new FcmResult(this);
        }
      }
}
