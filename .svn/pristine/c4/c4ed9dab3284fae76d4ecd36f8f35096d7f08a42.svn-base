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
 * 업 무 명 : gcm-server MulticastFcmResult 변경
 * 파 일 명 : FcmMulticastFcmResult.java
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

@Getter
public final class FcmMulticastResult implements Serializable {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inject Beans
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Member Variables
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private final int success;
    private final int failure;
    private final int canonicalIds;
    private final long multicastId;
    private final List<FcmResult> results;
    private final List<Long> retryMulticastIds;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Public Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public int getTotal() {
        return success + failure;
      }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Private/Protected Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private FcmMulticastResult(Builder builder) {
        success = builder.success;
        failure = builder.failure;
        canonicalIds = builder.canonicalIds;
        multicastId = builder.multicastId;
        results = Collections.unmodifiableList(builder.results);
        List<Long> tmpList = builder.retryMulticastIds;
        if (tmpList == null) {
          tmpList = Collections.emptyList();
        }
        retryMulticastIds = Collections.unmodifiableList(tmpList);
      }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overrided Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder("FcmMulticastResult(")
          .append("multicast_id=").append(multicastId).append(",")
          .append("total=").append(getTotal()).append(",")
          .append("success=").append(success).append(",")
          .append("failure=").append(failure).append(",")
          .append("canonical_ids=").append(canonicalIds).append(",");
      if (!results.isEmpty()) {
        builder.append("results: " + results);
      }
      return builder.toString();
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inner Classes
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
    public static final class Builder {

        private final List<FcmResult> results = new ArrayList<FcmResult>();

        // required parameters
        private final int success;
        private final int failure;
        private final int canonicalIds;
        private final long multicastId;

        // optional parameters
        private List<Long> retryMulticastIds;

        public Builder(int success, int failure, int canonicalIds,
            long multicastId) {
          this.success = success;
          this.failure = failure;
          this.canonicalIds = canonicalIds;
          this.multicastId = multicastId;
        }

        public Builder addFcmResult(FcmResult FcmResult) {
          results.add(FcmResult);
          return this;
        }

        public Builder retryMulticastIds(List<Long> retryMulticastIds) {
          this.retryMulticastIds = retryMulticastIds;
          return this;
        }

        public FcmMulticastResult build() {
          return new FcmMulticastResult(this);
        }
    }

}
