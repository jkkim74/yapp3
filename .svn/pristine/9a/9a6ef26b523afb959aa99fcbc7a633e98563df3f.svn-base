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
 * 업 무 명 : gcm-server Message 변경
 * 파 일 명 : FcmMessage.java
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Getter;

/**
 * GCM message.
 *
 * <p>
 * Instances of this class are immutable and should be created using a
 * {@link Builder}. Examples:
 *
 * <strong>Simplest message:</strong>
 * <pre><code>
 * Message message = new Message.Builder().build();
 * </pre></code>
 *
 * <strong>Message with optional attributes:</strong>
 * <pre><code>
 * Message message = new Message.Builder()
 *    .collapseKey(collapseKey)
 *    .timeToLive(3)
 *    .delayWhileIdle(true)
 *    .build();
 * </pre></code>
 *
 * <strong>Message with optional attributes and payload data:</strong>
 * <pre><code>
 * Message message = new Message.Builder()
 *    .collapseKey(collapseKey)
 *    .timeToLive(3)
 *    .delayWhileIdle(true)
 *    .addData("key1", "value1")
 *    .addData("key2", "value2")
 *    .build();
 * </pre></code>
 */

@Getter
public class FcmMessage  implements Serializable{

    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inject Beans
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Member Variables
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final String collapseKey;
    private final Boolean delayWhileIdle;
    private final String priority;
    private final Integer timeToLive;
    private final Map<String, String> data;
    private final Boolean dryRun;
    private final String restrictedPacakgeName;
    private final Boolean contentAvailable;
    private final Boolean mutableContent;
    private final Notification notification; 
    
    public enum Priority {
        NORMAL, HIGH
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Public Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public Boolean isDelayWhileIdle() {
        return delayWhileIdle;
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Private/Protected Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private FcmMessage(Builder builder) {
        collapseKey = builder.collapseKey;
        delayWhileIdle = builder.delayWhileIdle;
        data = Collections.unmodifiableMap(builder.data);
        timeToLive = builder.timeToLive;
        priority = builder.priority;
        dryRun = builder.dryRun;
        restrictedPacakgeName = builder.restrictedPacakgeName;
        contentAvailable = builder.contentAvailable;
        mutableContent = builder.mutableContent;
        notification = builder.notification;
     }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overrided Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("FcmMessage(");
      if (collapseKey != null) {
        builder.append("collapseKey=").append(collapseKey).append(", ");
      }
      if (timeToLive != null) {
        builder.append("timeToLive=").append(timeToLive).append(", ");
      }
      if (delayWhileIdle != null) {
        builder.append("delayWhileIdle=").append(delayWhileIdle).append(", ");
      }
      
      if(priority != null) {
        builder.append("priority").append(priority).append(", ");
      }
      
      if (!data.isEmpty()) {
        builder.append("data: {");
        for (Map.Entry<String, String> entry : data.entrySet()) {
          builder.append(entry.getKey()).append("=").append(entry.getValue())
              .append(",");
        }
        builder.delete(builder.length() - 1, builder.length());
        builder.append("}");
      }
      
      
      if (builder.charAt(builder.length() - 1) == ' ') {
        builder.delete(builder.length() - 2, builder.length());
      }
      builder.append(")");
      return builder.toString();
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inner Classes
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
    public static final class Builder {

        private String collapseKey;
        private Boolean delayWhileIdle;
        private String priority;
        private Integer timeToLive;
        private Map<String, String> data;
        private Boolean dryRun;
        private String restrictedPacakgeName;
        private Boolean contentAvailable;
        private Boolean mutableContent;
        private Notification notification; 

        public Builder() {
          this.data = new LinkedHashMap<String, String>();
        }

        /**
         * Sets the collapseKey property.
         */
        public Builder collapseKey(String value) {
          collapseKey = value;
          return this;
        }

        /**
         * Sets the delayWhileIdle property (default value is {@literal false}).
         */
        public Builder delayWhileIdle(boolean value) {
          delayWhileIdle = value;
          return this;
        }
        

        /**
         * Sets the time to live, in seconds.
         */
        public Builder timeToLive(int value) {
          timeToLive = value;
          return this;
        }

        /**
         * Adds a key/value pair to the payload data.
         */
        public Builder addData(String key, String value) {
          data.put(key, value);
          return this;
        }

        public Builder setData(Map<String,String> data) {
          this.data.clear();
          this.data.putAll(data);
          return this;
        }

        public Builder dryRun(boolean value) {
            dryRun = value;
            return this;
        }
        
        public Builder restrictedPacakgeName(String value) {
            restrictedPacakgeName = value;
            return this;
        }
        
        public Builder priority(Priority value) {
            switch(value) {
            case NORMAL:
                priority = FcmConstants.MESSAGE_PRIORITY_NORMAL;
                break;
            case HIGH:
                priority = FcmConstants.MESSAGE_PRIORITY_HIGH;
                break;
            }
            
            return this;
        }
        

        public Builder notification(Notification value) {
            notification = value;
            return this;
        }
        
        public Builder contentAvailable(Boolean value) {
            contentAvailable = value;
            return this;
        }
        

        public Builder mutableContent(Boolean value) {
            mutableContent = value;
            return this;
        }
        
        public FcmMessage build() {
          return new FcmMessage(this);
        }
      }
    
}
