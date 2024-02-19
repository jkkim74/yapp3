package com.kt.yapp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 91303563
 * API 중 로그인 체크하지 않을 컨트롤러에 사용.(ex: yshop 전용 API)
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotCheckLogin {}
