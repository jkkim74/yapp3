package com.kt.yapp.config;

import javax.annotation.PostConstruct;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.kt.yapp.service.IdMappingCacheService;

/**
 * MyBatis Interceptor 등록 설정 클래스 (v3 - SNS 임시 ID 라이프사이클 대응)
 * 
 * <pre>
 * v3 변경사항:
 *   - SNS 임시 아이디(SNS!XXXXX) -> KT ID 자동 변환 지원
 *   - 3단계 조회: 캐시 → SNS_TEMP_ID → CREDENTIAL_ID 폴백
 * </pre>
 */
@Configuration
public class MyBatisInterceptorConfig {

    private static final Logger logger = LoggerFactory.getLogger(MyBatisInterceptorConfig.class);

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private IdMappingCacheService idMappingCacheService;

    @PostConstruct
    public void addInterceptor() {
        UserIdResolvingInterceptor interceptor = new UserIdResolvingInterceptor();
        
        interceptor.setIdMappingCacheService(idMappingCacheService);
        
        sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
        
        logger.info("===================================================");
        logger.info("[MyBatis] UserIdResolvingInterceptor v3 등록 완료");
        logger.info("[MyBatis] SNS 임시 ID(SNS!XXXXX) -> KT ID 자동 변환 활성화");
        logger.info("[MyBatis] 듀얼 PK: TB_USER(CNTR_NO), TB_USER_KT(USER_ID)");
        logger.info("[MyBatis] 3단계 조회: 캐시 -> SNS_TEMP_ID -> CREDENTIAL_ID");
        logger.info("[MyBatis] 제외 namespace: user, userkt, idmapping, common");
        logger.info("===================================================");
    }
}
