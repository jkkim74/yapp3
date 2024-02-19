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
 * 파 일 명 : Notificaion.java
 * 작 성 자 : 이주혁
 * 작 성 일 : 2020. 3. 25.
 * 설    명 : 
 * ************************************************************
 * 변경일     | 변경자 | 변경내역
 * ************************************************************
 * 2020. 3. 25. | 이주혁 | 최초작성
 * ************************************************************
 */
package com.kt.yapp.push;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

@Getter
public class Notification implements Serializable {

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inject Beans
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Member Variables
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private final String icon;
    
    private final String title;
    private final String body;
    private final String sound;
    private final String badge;
    private final String tag;
    private final String color;
    private final String clickAction;
    private final String bodyLocKey;
    private final List<String> bodyLocArgs;
    private final String titleLocKey;
    private final List<String> titleLocArgs;
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Constructors
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Public Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Private/Protected Methodss
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    private Notification(Builder builder) {
        title = builder.title;
        body  = builder.body;
        icon = builder.icon;
        sound = builder.sound;
        badge = builder.badge;
        tag = builder.tag;
        color = builder.color;
        clickAction = builder.clickAction;
        bodyLocKey = builder.bodyLocKey;
        bodyLocArgs = builder.bodyLocArgs;
        titleLocKey = builder.titleLocKey;
        titleLocArgs = builder.titleLocArgs;
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Overrided Methods
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("notification(");
        if(title != null) {
            builder.append("title=").append(title).append(", ");
        }
        if(body != null) {
            builder.append("body=").append(body).append(", ");
            
        }
        if(icon != null) {
            builder.append("icon=").append(icon).append(", ");
            
        }
        if(sound != null) {
            builder.append("sound=").append(sound).append(", ");
            
        }
        if(badge != null) {
            builder.append("badge=").append(badge).append(", ");
            
        }
        if(tag != null) {
            builder.append("tag=").append(tag).append(", ");
            
        }
        if(color != null) {
            builder.append("color=").append(color).append(", ");
            
        }
        if(clickAction != null) {
            builder.append("clickAction=").append(clickAction).append(", ");
            
        }
        if(bodyLocKey != null) {
            builder.append("bodyLocKey").append(bodyLocKey).append(", ");
            
        }
        if(bodyLocArgs != null) {
            builder.append("bodyLocArgs=").append(bodyLocArgs).append(", ");
            
        }
        if(titleLocKey != null) {
            builder.append("titleLocKey=").append(titleLocKey).append(", ");
            
        }
        if(titleLocArgs != null) {
            builder.append("titleLocArgs=").append(titleLocArgs).append(", ");
            
        }
        
        if(builder.charAt(builder.length() -1 ) == ' ') {
            builder.delete(builder.length() - 2,  builder.length());
        }
        
        
        
        builder.append(")");
        return builder.toString();
    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    // Inner Classes
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~    
    public static final class Builder {
        
        private final String icon;
        
        private String title;
        private String body;
        private String sound;
        private String badge;
        private String tag;
        private String color;
        private String clickAction;
        private String bodyLocKey;
        private List<String> bodyLocArgs;
        private String titleLocKey;
        private List<String> titleLocArgs;
        
        public Builder(String icon) {
            this.icon = icon ;
            this.sound = "default"; 
        }

        public Builder title(String value) {
            title = value;
            return this;
        }
        
        public Builder body(String value) {
            body = value;
            return this;
        }
        

        public Builder sound(String value) {
            sound = value;
            return this;
        }

        public Builder badge(String value) {
            badge = value;
            return this;
        }

        public Builder tag(String value) {
            tag = value;
            return this;
        }

        public Builder color(String value) {
            color = value;
            return this;
        }

        public Builder clickAction(String value) {
            clickAction = value;
            return this;
        }

        public Builder bodyLocKey(String value) {
            bodyLocKey = value;
            return this;
        }

        public Builder bodyLocArgs(List<String> value) {
            bodyLocArgs = Collections.unmodifiableList(value);
            return this;
        }

        public Builder titleLocKey(String value) {
            titleLocKey = value;
            return this;
        }
        public Builder titleLocArgs(List<String> value) {
            titleLocArgs = Collections.unmodifiableList(value);
            return this;
        }
        
        public Notification build() {
            return new Notification(this);
        }
    }
    
}
