package com.kt.yapp.validator;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.initech.inisafenet.iniplugin.log.Logger;
import com.kt.yapp.util.YappUtil;
import com.kt.yapp.web.YFriendsController;

/**
 * 파라미터 체크 Validate Util
 */
@Component
public class YappVldr 
{
	private static final String CONTEXT_ROOT = "/yapp3";
	
	/** 유효성 체크 클래스명 prefix */
	private static final String VLD_PREFIX = "com.kt.yapp.validator.";
	/** 유효성 체크 클래스명 uffix */
	private static final String VLD_SUFFIX = "ChkValidator";
	
	/** URL 별 파라미터 체크 정보 보유 맵 */
	private static Map<String, List<ApiParam>> apiMap = new HashMap<>();

	private Document doc;
	private XPath xpath = XPathFactory.newInstance().newXPath();

	public YappVldr() throws Exception {
		init();
	}
	
	/**
	 * 입력된 API Url로 정의된 파라미터 유효성 체크를 실행한다.
	 */
	public static String validate(String url, String method, Object[] paramObjArray)
	{
		// "URL + METHOD 명"으로 검색 후 데이터가 없으면 "URL" 로 검색한다.
		List<ApiParam> apiParamList = apiMap.get(url + method);
		if ( YappUtil.isEmpty(apiParamList) )
			apiParamList = apiMap.get(url);
		
		if ( YappUtil.isEmpty(apiParamList) )
			return null;
		
		// 정의된 파라미터 수만큼 체크한다.
		for ( ApiParam apiParam : apiParamList )
		{
			Object paramObj = paramObjArray[apiParam.getOrder()];
			
			Object value = null;
			try {
				if ( YappUtil.isEq(apiParam.getParamType(), "string") ) {
					value = paramObj;
				} else {
					if(apiParam != null && paramObj != null){
						value = getMethodValue(apiParam.getParamNm(), paramObj);
					}else{
						value = null;
					}
					
				}
			} catch (RuntimeException e) {
				return "Exception Error";
				//System.out.println("error : " + e);
				//logger.error("error : " + e);
				//e.printStackTrace();
			} catch (Exception e) {
				return "Exception Error";
				//System.out.println("error : " + e);
				//logger.error("error : " + e);
				//e.printStackTrace();
			}
			// 필수가 아니면서 NULL이면 체크하지 않는다.
			 if ( apiParam != null && (apiParam.isRequired() == false && YappUtil.isEmpty(value)) )
				continue;
			
			// 유효성 체크 객체 수만큼 체크한다.
			for ( IApiParamValidator validator : apiParam.getValidatorClassNmList() )
			{
				String validateResultMsg = validator.validate(value);
				if ( validateResultMsg != null && apiParam != null)
					return apiParam.getParamNm() + "(" + apiParam.getParamDesc() + ") : " + validateResultMsg;
			}
		}
		
		return null;
	}
	
	/**
	 * 도메인 객체 속의 getXXX 메소드명을 만든다.
	 */
	private static Object getMethodValue(String paramNm, Object obj) throws RuntimeException
	{
		Object tarObj = obj;
		String tarParamNm = paramNm;
		
		String [] paramNmArray = paramNm.split("[.]");
		
		// 객체 IN 객체의 경우 처리(2레벨만 처리)
		if ( paramNmArray.length > 1 ) {
			String getMethodNm = "get" + paramNmArray[0].substring(0, 1).toUpperCase() + paramNmArray[0].substring(1);
			
			try {
				tarObj = obj.getClass().getMethod(getMethodNm, null).invoke(obj);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				throw new RuntimeException();
			}
			tarParamNm = paramNmArray[1];
		}
		
		if ( tarObj == null )
			return null;
		
		String getMethodNm = "get" + tarParamNm.substring(0, 1).toUpperCase() + tarParamNm.substring(1);
		Method method = null;
		try {
			method = tarObj.getClass().getMethod(getMethodNm, null);
		} catch (NoSuchMethodException e) {
			System.out.println("error : " + e);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		try {
			return method.invoke(tarObj);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException();
			//e.printStackTrace();
		}
	}
	
	/**
	 * 파라미터 체크 정의 파일을 읽어 init
	 */
	public void init() throws Exception
	{
		ClassPathResource res = new ClassPathResource("/yappValidator.xml");
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(true);
		
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.parse(res.getFile());
		
		NodeList nodeList = (NodeList) xpath.evaluate("//api", doc, XPathConstants.NODESET);
		for ( int i = 0; i < nodeList.getLength(); i++ )
		{
			Node apiNode = nodeList.item(i);
			String url = CONTEXT_ROOT + (String) xpath.evaluate("@url", apiNode, XPathConstants.STRING);
			String method = YappUtil.nToStr(xpath.evaluate("@method", apiNode, XPathConstants.STRING));
			url += method;
			
			NodeList paramNodeList = (NodeList) xpath.evaluate("param", apiNode, XPathConstants.NODESET);
			
			List<ApiParam> apiParamList = new ArrayList<>();
			for ( int k = 0; k < paramNodeList.getLength(); k++ )
			{
				Node paramNode = paramNodeList.item(k);
				apiParamList.add(makeApiParam(paramNode));
			}
			apiMap.put(url, apiParamList);
		}
	}
	
	/**
	 * 파라미터 체크 클래스를 생성한다.
	 */
	private ApiParam makeApiParam(Node paramNode) throws Exception
	{
		ApiParam apiParam = new ApiParam();
		apiParam.setParamNm(getNodeAttrText(paramNode, "name"));
		apiParam.setParamDesc(getNodeAttrText(paramNode, "desc"));
		apiParam.setParamType(getNodeAttrText(paramNode, "type"));
		apiParam.setMethod(getNodeAttrText(paramNode, "method"));
		apiParam.setOrder(YappUtil.toInt(getNodeAttrText(paramNode, "order"), 0));
		apiParam.setRequired(YappUtil.isNotEq(getNodeAttrText(paramNode, "required"), "false"));
		
		String validator = getNodeAttrText(paramNode, "validator");
		String[] validatorClassNames = validator.split(",");
		if ( YappUtil.isEmpty(validatorClassNames) )
			return apiParam;
		
		for ( String validatorClassName : validatorClassNames )
		{
			IApiParamValidator validatorObj = (IApiParamValidator) Class.forName(VLD_PREFIX + validatorClassName.trim() + VLD_SUFFIX).newInstance();
			apiParam.addValidator(validatorObj);
		}
		
		return apiParam;
	}
	
	
	/**
	 * Node 타입의 구문에서 값을 조회한다.
	 */
	private String getNodeAttrText(Node parentNode, String attrName) throws Exception
	{
		return (String) xpath.evaluate("@" + attrName, parentNode, XPathConstants.STRING);
	}
}
