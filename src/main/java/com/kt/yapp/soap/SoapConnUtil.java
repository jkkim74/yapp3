package com.kt.yapp.soap;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kt.yapp.domain.AddressInfo;
import com.kt.yapp.domain.AplyPplProdInfo;
import com.kt.yapp.domain.AplyProdParamInfo;
import com.kt.yapp.domain.AplyRoaming;
import com.kt.yapp.util.SessionKeeper;
import com.kt.yapp.util.YappUtil;

@Component
public class SoapConnUtil 
{
	@Value("${shub.subscriptioninforetrieval.endpoint.url}")
	public String soapEpUrlScpt;
	
	@Value("${shub.uumdataretrieval.endpoint.url}")
	public String soapEpUrlUUMDataRetrieval;
	
	@Value("${shub.dataretrievalmanager.endpoint.url}")
	public String soapEpUrlDataRetrievalManager;
	
	@Value("${shub.dataretrieval.endpoint.url}")
	public String soapEpUrlDataRetrieval;
	
	@Value("${shub.authentication.endpoint.url}")
	public String soapEpUrlAuth;
	
	@Value("${shub.authentication.url}")
	public String soapEpUrlAuthentication;
	
	@Value("${shub.msg.endpoint.url}")
	public String soapEpUrlMsg;
	
	@Value("${shub.vas.endpoint.url}")
	public String soapEpUrlVas;
	
	@Value("${kos.dynamicsvc.endpoint.url}")
	public String soapEpUrlKosDynamicSvc;
	
	@Value("${kos.svc.endpoint.url}")
	public String soapEpUrlKosSvc;

	@Value("${kos.svc2.endpoint.url}")
	public String soapEpUrlKosSvc2;
	
	@Value("${kos.retrievedrctlyuseqnt.endpoint.url}")
	public String soapEpUrlKosRetrieveDrctlyUseQnt;
	
	@Value("${kos.dataSharUseRetv.endpoint.url}")
	public String soapEpUrlKosDataSharUseRetv;
	
	@Value("${kos.dataSharUseRetv.endpoint.url2}")
	public String soapEpUrlKosDataSharUseRetv2;
	
	@Value("${shub.cpn.endpoint.url}")
	public String soapEpUrlCpn;

	@Value("${kos.rds.endpoint.url}")
	public String soapRdsKosDynamicSvc;

	@Value("${mem.point.get}")
	public String soapEpUrlMemPointGet;
	
	@Value("${mem.point.use}")
	public String soapEpUrlMemPointUse;
	
	@Value("${mem.point.acu}")
	public String soapEpUrlMemPointAcu;
	
	@Value("${shub.conn.id}")
	public String connId;
	
	@Value("${shub.conn.pwd}")
	public String connPwd;
	
	public static final String NAMESPACE_ENV = "env";
	public static final String NAMESPACE_URI_ENV = "http://schemas.xmlsoap.org/soap/envelope/";
	
	public static final String NAMESPACE_MAIN = "sdp";
	public static final String NAMESPACE_URI_MAIN = "http://kt.com/sdp";
	
	public static final String NAMESPACE_HEADER = "oas";
	public static final String NAMESPACE_URI_HEADER = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

	public static final String NAMESPACE_N1 = "n1";
	public static final String NAMESPACE_URI_N1 = "http://kt.com/sdp_otm";
	
	public static final String NAMESPACE_N2 = "n2";
	public static final String NAMESPACE_URI_N2 = "http://kt.com/sdp_otm";
	
	public static final String NAMESPACE_AG = "ag";
	public static final String NAMESPACE_URI_AG = "http://xml.kt.com/sdp/so/BS/MessageMTMMSReportNoNetCharge";
	
	public static final String NAMESPACE_NS1 = "ns1";
	public static final String NAMESPACE_URI_NS1 = "http://bizhub.kt.com";
	
	public static final String NAMESPACE_BIZ = "biz";
	public static final String NAMESPACE_URI_BIZ = "http://bizhub.kt.com";
	
	public static final String NAMESPACE_PGET = "pget";
	public static final String NAMESPACE_URI_PGET = "http://custcombineinfo.cust.server.websvc.point.ktds.com/";
	
	public static final String NAMESPACE_PUSE = "puse";
	public static final String NAMESPACE_URI_PUSE = "http://process.point.server.websvc.point.ktds.com/";
	
	public static final String NAMESPACE_PACU = "pacu";
	public static final String NAMESPACE_URI_PACU = "http://acu.point.server.websvc.point.ktds.com/";
	
	private static final Logger logger = LoggerFactory.getLogger(SoapConnUtil.class);

	public static SOAPMessage call(String endPointUrl, SOAPMessage soapMessage) throws Exception
	{
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			soapMessage.writeTo(baos);
	
			logger.info(new String(baos.toByteArray()));
		} catch (RuntimeException e) {
			//e.printStackTrace();
			logger.error("error : " + e);
		}catch (Exception e) {
			//e.printStackTrace();
			logger.error("error : " + e);
		}

		SOAPMessage soapResponse = null;
		SOAPConnection soapConnection = null;
		try{
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
		soapConnection = soapConnectionFactory.createConnection();
		// Send SOAP Message to SOAP Server
		soapResponse = soapConnection.call(soapMessage, new URL(new URL(endPointUrl), "", new URLStreamHandler() {
			@Override
			protected URLConnection openConnection(URL url) throws IOException {
				URL target = new URL(url.toString());
				URLConnection connection = target.openConnection();
				connection.setConnectTimeout(300000); //5min
				connection.setReadTimeout(300000);//5min
				return (connection);
			}
		}));
		// soapResponse = soapConnection.call(soapMessage,endPointUrl);
		//soapConnection.close();
		} catch (RuntimeException e) {
			throw e;
		}catch (Exception e) {
			throw e;
		} finally{
			if(soapConnection != null){
				soapConnection.close();
			}
		}
		return soapResponse;
	}
	
	/**
	 * SHUB 호출 용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessage() throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();

		SOAPPart soapPart = soapMessage.getSOAPPart();

		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_MAIN, NAMESPACE_URI_MAIN);
		envelope.addNamespaceDeclaration(NAMESPACE_AG, NAMESPACE_URI_AG);
		envelope.addNamespaceDeclaration(NAMESPACE_HEADER, NAMESPACE_URI_HEADER);

		// SOAP 헤더 생성
		makeSoapHeader(envelope.getHeader());

		return soapMessage;
	}

	/**
	 * Soap Header 를 만든다.<br>
	 * SHUB 접속 KEY(ID, PWD)를 세팅한다.
	 */
	private void makeSoapHeader(SOAPHeader header) throws Exception
	{
		SOAPElement headerEle = header.addChildElement("Security", NAMESPACE_HEADER);
		SOAPElement headerTokenEle = headerEle.addChildElement("UsernameToken", NAMESPACE_HEADER);
		
		// SHUB 접속 ID, PWD 세팅
		makeParam(headerTokenEle, NAMESPACE_HEADER, "Username", connId);
		makeParam(headerTokenEle, NAMESPACE_HEADER, "Password", connPwd);
	}
	
	/**
	 * KOS 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKos(String operNm, String[][] params) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		// SHUB 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName", 	"NBSS_OPD");
		makeParam(headerEle, null, "svcName", 	"DynamicServiceSO");
		makeParam(headerEle, null, "fnName", 	"service");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));
		
		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "DynamicServiceSO");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "service");

		SOAPElement dynamicCmnListBodyElem = svcReqBodyElem.addChildElement("DynamicCmnListDTO");
		SOAPElement dynamicCmnListBodyElem2 = dynamicCmnListBodyElem.addChildElement("DynamicCmnListDTO");
		makeParam(dynamicCmnListBodyElem2, null, "SERVICE_OBJECT", "DataboxAdmBO");
		makeParam(dynamicCmnListBodyElem2, null, "OPERATION_NAME", operNm);
		makeParam(dynamicCmnListBodyElem2, null, "dbio_total_count_", "1");
		
		SOAPElement dynamicKeyValueBodyElem = dynamicCmnListBodyElem.addChildElement("dynamicKeyValueDTO");
		for ( int i = 0; i < 30; i++ )
		{
			String paramKey = params.length > i ? params[i][0] : "";
			makeParam(dynamicKeyValueBodyElem, null, "KEY_" + YappUtil.lpad(i + 1, 2, "0") + "_SBST", paramKey);
		}
		for ( int i = 0; i < 30; i++ )
		{
			String paramValue = params.length > i ? params[i][1] : "";
			makeParam(dynamicKeyValueBodyElem, null, "KEY_VALUE_" + YappUtil.lpad(i + 1, 2, "0") + "_SBST", paramValue);
		}

		return soapMessage;
	}

	/**
	 * KOS 호출용 Soap 메시지를 생성한다.SERVICE_OBJECT 다름
	 */
	public SOAPMessage makeSoapMessageForKos2(String operNm, String[][] params) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		// SHUB 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName", 	"NBSS_OPD");
		makeParam(headerEle, null, "svcName", 	"DynamicServiceSO");
		makeParam(headerEle, null, "fnName", 	"service");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));
		
		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "DynamicServiceSO");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "service");

		SOAPElement dynamicCmnListBodyElem = svcReqBodyElem.addChildElement("DynamicCmnListDTO");
		SOAPElement dynamicCmnListBodyElem2 = dynamicCmnListBodyElem.addChildElement("DynamicCmnListDTO");
		makeParam(dynamicCmnListBodyElem2, null, "SERVICE_OBJECT", "AbtPrdcInfoCmnClsBO");
		makeParam(dynamicCmnListBodyElem2, null, "OPERATION_NAME", operNm);
		makeParam(dynamicCmnListBodyElem2, null, "dbio_total_count_", "1");
		
		SOAPElement dynamicKeyValueBodyElem = dynamicCmnListBodyElem.addChildElement("dynamicKeyValueDTO");
		for ( int i = 0; i < 30; i++ )
		{
			String paramKey = params.length > i ? params[i][0] : "";
			makeParam(dynamicKeyValueBodyElem, null, "KEY_" + YappUtil.lpad(i + 1, 2, "0") + "_SBST", paramKey);
		}
		for ( int i = 0; i < 30; i++ )
		{
			String paramValue = params.length > i ? params[i][1] : "";
			makeParam(dynamicKeyValueBodyElem, null, "KEY_VALUE_" + YappUtil.lpad(i + 1, 2, "0") + "_SBST", paramValue);
		}

		return soapMessage;
	}

	/**
	 * KOS 꺼내기 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosPullData(String operNm, String mobileNo, String dataAmtCd) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);
		
		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		// SHUB 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName", 	"NBSS_ORD");
		makeParam(headerEle, null, "svcName", 	"/ORD/PipelineSVC/NPMB/sbsctrmn/store/PL_MblProdExtrSbscTrmnTrt");
		makeParam(headerEle, null, "fnName", 	"processExtrSbscTrmn");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));
		
		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("processExtrSbscTrmnRequestElement");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "/ORD/PipelineSVC/NPMB/sbsctrmn/store/PL_MblProdExtrSbscTrmnTrt");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "processExtrSbscTrmn");
		
		SOAPElement dynamicCmnListBodyElem = svcReqBodyElem.addChildElement("ExtrSbscTrmnTrtInDTO");
		makeParam(dynamicCmnListBodyElem, null, "cpNm", "YAPP");
		makeParam(dynamicCmnListBodyElem, null, "trtDivCd", "A");
		makeParam(dynamicCmnListBodyElem, null, "svcNo", mobileNo);
		makeParam(dynamicCmnListBodyElem, null, "unitSvcId", dataAmtCd);
		
		return soapMessage;
	}
	
	/**
	 * KOS 내 데이터 상세 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosDtl(String operNm, String cntrNo, String mobileNo, String trDate, String unitSvcGroupCd) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		String retvStDt = "";
		if(trDate == null){
			retvStDt = YappUtil.getCurDate("yyyyMMdd");
		}else{
			retvStDt = trDate;
		}
		// KOS 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName", 	"NBSS_ARO");
		makeParam(headerEle, null, "svcName", 	"/BCC/PipelineSVC/NTLT/UseQnt/PL_DrctlyUseQnt");
		makeParam(headerEle, null, "fnName", 	operNm);
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		if(operNm.equals("retrieveDrctlyUseQnt")){
			SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request");
			SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
			makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
			makeParam(bizHeaderBodyElem, null, "cbSvcName", "/BCC/PipelineSVC/NTLT/UseQnt/PL_DrctlyUseQnt");
			makeParam(bizHeaderBodyElem, null, "cbFnName", "retrieveDrctlyUseQnt");

			SOAPElement drctlyUseQntRetvInDTOBodyElem = svcReqBodyElem.addChildElement("DrctlyUseQntRetvInDTO");
			makeParam(drctlyUseQntRetvInDTOBodyElem, null, "retvStDt", retvStDt);
			makeParam(drctlyUseQntRetvInDTOBodyElem, null, "svcContId", cntrNo);
			makeParam(drctlyUseQntRetvInDTOBodyElem, null, "sbscrNo", mobileNo);
			makeParam(drctlyUseQntRetvInDTOBodyElem, null, "unitSvcGroupCd", unitSvcGroupCd);
		}else{
			SOAPElement svcReqBodyElem = soapBody.addChildElement("retrieveDrctlyUseQntDtlRequest");
			SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
			makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
			makeParam(bizHeaderBodyElem, null, "cbSvcName", "/BCC/PipelineSVC/NTLT/UseQnt/PL_DrctlyUseQnt");
			makeParam(bizHeaderBodyElem, null, "cbFnName", "retrieveDrctlyUseQntDtl");

			SOAPElement drctlyUseQntRetvInDTOBodyElem = svcReqBodyElem.addChildElement("DrctlyUseQntRetvInDTO");
			makeParam(drctlyUseQntRetvInDTOBodyElem, null, "retvStDt", retvStDt);
			makeParam(drctlyUseQntRetvInDTOBodyElem, null, "svcContId", cntrNo);
			makeParam(drctlyUseQntRetvInDTOBodyElem, null, "sbscrNo", mobileNo);
			makeParam(drctlyUseQntRetvInDTOBodyElem, null, "unitSvcGroupCd", unitSvcGroupCd);
		}
		return soapMessage;
	}

	/**
	 * KOS 내 데이터 (알요금제) 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosEggDtl(String operNm, String cntrNo, String mobileNo) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		// KOS 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName", 	"NBSS_ARO");
		makeParam(headerEle, null, "svcName", 	"/BCC/PipelineSVC/NPCR/PL_PiaAdm");
		makeParam(headerEle, null, "fnName", 	"retrieveBigiChargBlnc");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("retrieveBigiChargBlncRequest");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "/BCC/PipelineSVC/NPCR/PL_PiaAdm");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "retrieveBigiChargBlnc");

		SOAPElement drctlyUseQntRetvInDTOBodyElem = svcReqBodyElem.addChildElement("BigiReqSubBody");
		makeParam(drctlyUseQntRetvInDTOBodyElem, null, "piaSvcNo", mobileNo);
		makeParam(drctlyUseQntRetvInDTOBodyElem, null, "svcContId", cntrNo);

		return soapMessage;
	}

	/**
	 * KOS 데이터 충전 잔여량
	 */
	public SOAPMessage makeSoapMessageForKosDataRefill(String operNm, String cntrNo, String mobileNo) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);

		// KOS 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName", 	"NBSS_ARO");
		makeParam(headerEle, null, "svcName", 	"/BCC/PipelineSVC/NPCR/PL_PiaAdm");
		makeParam(headerEle, null, "fnName", 	operNm);
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "/BCC/PipelineSVC/NPCR/PL_PiaAdm");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "retrieveDataRefill");

		SOAPElement drctlyUseQntRetvInDTOBodyElem = svcReqBodyElem.addChildElement("DataRefillRetvInDTO");
		makeParam(drctlyUseQntRetvInDTOBodyElem, null, "telNo", mobileNo);
		
		return soapMessage;
	}

	
	/**
	 * KOS 5G 공유 데이터
	 */
	public SOAPMessage makeSoapMessageForKosDataSharUseQntRetv(String operNm, String cntrNo, String retvDate) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);

		// KOS 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName", 	"NBSS_ARO");
		makeParam(headerEle, null, "svcName", 	"DataSharPrvQntRetvSO");
		makeParam(headerEle, null, "fnName", 	"service");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "DataSharPrvQntRetvSO");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "service");

		SOAPElement dataSharPrvQntRetvInDTOBodyElem = svcReqBodyElem.addChildElement("DataSharPrvQntRetvInDTO");
		makeParam(dataSharPrvQntRetvInDTOBodyElem, null, "svcContId", cntrNo);
		makeParam(dataSharPrvQntRetvInDTOBodyElem, null, "retvDate", retvDate);
		
		return soapMessage;
	}

	/**
	 * KOS 시간옵션 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosTimeOption(String operNm) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		// SHUB 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName", 	"NBSS_OPD");
		makeParam(headerEle, null, "svcName", 	"ParamEfctCdRetvSO");
		makeParam(headerEle, null, "fnName", 	"service");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "ParamEfctCdRetvSO");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "service");

		SOAPElement paramEfctCdRetvInDTOBodyElem = svcReqBodyElem.addChildElement("ParamEfctCdRetvInDTO");
		makeParam(paramEfctCdRetvInDTOBodyElem, null, "paramCd", "TIME_OPTION");
		makeParam(paramEfctCdRetvInDTOBodyElem, null, "paramEfctCd", "");
		makeParam(paramEfctCdRetvInDTOBodyElem, null, "paramEfctCdNm", "");
		
		return soapMessage;
	}

	/**
	 * KOS 기가입상품정보 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosAplyProdInfo(String operNm, String svcContId) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		// SHUB 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName", 	"NBSS_OPD");
		makeParam(headerEle, null, "svcName", 	"AplyProdInfoRetvSO");
		makeParam(headerEle, null, "fnName", 	"service");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "AplyProdInfoRetvSO");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "service");

		SOAPElement aplyProdInfoRetvInDTOBodyElem = svcReqBodyElem.addChildElement("AplyProdInfoRetvInDTO");
		makeParam(aplyProdInfoRetvInDTOBodyElem, null, "svcContId", svcContId);
		makeParam(aplyProdInfoRetvInDTOBodyElem, null, "svcContSttusCd", "A");
		makeParam(aplyProdInfoRetvInDTOBodyElem, null, "chCd", "08");
		
		return soapMessage;
	}

	
	/**
	 * KOS 상품사전체크 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosProdPreChk(String operNm, AplyPplProdInfo aplyPplProdInfo, AplyProdParamInfo aplyProdParamInfo) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		// SHUB 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName", 	"NBSS_RLE");
		makeParam(headerEle, null, "svcName", 	"MobileProdPrevChkSO");
		makeParam(headerEle, null, "fnName", 	"service");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "MobileProdPrevChkSO");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "service");

		SOAPElement aplyProdInfoRetvInDTOBodyElem = svcReqBodyElem.addChildElement("MobileProdChckInptMultiDTO");
		SOAPElement aplyProdInfoRetvInDTOBodyElem2 = aplyProdInfoRetvInDTOBodyElem.addChildElement("ruleWorkInfoDTO");
		SOAPElement aplyProdInfoRetvInDTOBodyElem3 = aplyProdInfoRetvInDTOBodyElem.addChildElement("ruleCustInfoDTO");
		SOAPElement aplyProdInfoRetvInDTOBodyElem4 = aplyProdInfoRetvInDTOBodyElem.addChildElement("ruleSvcInfoListDTO");
		SOAPElement aplyProdInfoRetvInDTOBodyElem5 = aplyProdInfoRetvInDTOBodyElem.addChildElement("custPplInfoIfListDTO");
		SOAPElement aplyProdInfoRetvInDTOBodyElem6 = aplyProdInfoRetvInDTOBodyElem.addChildElement("custPplUnitSvcInfoIfListDTO");

		makeParam(aplyProdInfoRetvInDTOBodyElem2, null, "ruleEvCd", "PCN");
		makeParam(aplyProdInfoRetvInDTOBodyElem2, null, "ruleAplyDivCd", "C");
		makeParam(aplyProdInfoRetvInDTOBodyElem2, null, "ruleAplyPotimCd", "CM");
		makeParam(aplyProdInfoRetvInDTOBodyElem2, null, "chCd", "08");
		makeParam(aplyProdInfoRetvInDTOBodyElem2, null, "prodCtgCdVal", "MO");
		
		makeParam(aplyProdInfoRetvInDTOBodyElem3, null, "svcContId", aplyPplProdInfo.getSvcContId());
		
		makeParam(aplyProdInfoRetvInDTOBodyElem4, null, "trtTgtSvcCd", aplyPplProdInfo.getProdId());
		makeParam(aplyProdInfoRetvInDTOBodyElem4, null, "trtSttusCd", "U");
		makeParam(aplyProdInfoRetvInDTOBodyElem4, null, "prodCtgCdVal", "MO");
		
		makeParam(aplyProdInfoRetvInDTOBodyElem5, null, "prodId", aplyPplProdInfo.getProdId());
		makeParam(aplyProdInfoRetvInDTOBodyElem5, null, "prodNm", aplyPplProdInfo.getProdNm());
		makeParam(aplyProdInfoRetvInDTOBodyElem5, null, "efctStDt", aplyPplProdInfo.getEfctStDt());
		makeParam(aplyProdInfoRetvInDTOBodyElem5, null, "efctFnsDt", aplyPplProdInfo.getEfctFnsDt());
		makeParam(aplyProdInfoRetvInDTOBodyElem5, null, "prodHstSeq", aplyPplProdInfo.getProdHstSeq());
		makeParam(aplyProdInfoRetvInDTOBodyElem5, null, "prodSbscTrmnCd", "U");
		makeParam(aplyProdInfoRetvInDTOBodyElem5, null, "prodTypeCd", "P");
		makeParam(aplyProdInfoRetvInDTOBodyElem5, null, "prodCtgCd", "MO");
		
		makeParam(aplyProdInfoRetvInDTOBodyElem6, null, "prodId", aplyProdParamInfo.getProdId());
		makeParam(aplyProdInfoRetvInDTOBodyElem6, null, "unitSvcId", aplyProdParamInfo.getUnitSvcId());
		makeParam(aplyProdInfoRetvInDTOBodyElem6, null, "efctStDt", aplyProdParamInfo.getEfctStDt());
		makeParam(aplyProdInfoRetvInDTOBodyElem6, null, "efctFnsDt", aplyProdParamInfo.getEfctFnsDt());
		makeParam(aplyProdInfoRetvInDTOBodyElem6, null, "prodHstSeq", aplyProdParamInfo.getProdHstSeq());
		makeParam(aplyProdInfoRetvInDTOBodyElem6, null, "prodSbscTrmnCd", "U");
		makeParam(aplyProdInfoRetvInDTOBodyElem6, null, "paramSbst", aplyProdParamInfo.getParamSbst());
		return soapMessage;
	}

	/**
	 * KOS 마이타임플랜 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosProdStoreBas(String operNm, AplyPplProdInfo aplyPplProdInfo, AplyProdParamInfo aplyProdParamInfo, String timeOption) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		// SHUB 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName", 	"NBSS_ORD");
		makeParam(headerEle, null, "svcName", 	"/ORD/PipelineSVC/NPMB/sbsctrmn/store/PL_ProdSbscTrmnStoreTrt");
		makeParam(headerEle, null, "fnName", 	"processProdStoreBas");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("processProdStoreBasRequest");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "/ORD/PipelineSVC/NPMB/sbsctrmn/store/PL_ProdSbscTrmnStoreTrt");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "processProdStoreBas");

		SOAPElement proceddProdStoreBasDTOBodyElem = svcReqBodyElem.addChildElement("wrkjobTrtInfoIfDTO");
		makeParam(proceddProdStoreBasDTOBodyElem, null, "wrkjobTrtCd", "PCN");
		makeParam(proceddProdStoreBasDTOBodyElem, null, "chCd", "08");
		makeParam(proceddProdStoreBasDTOBodyElem, null, "aplyPotimCd", "CM");
		
		SOAPElement proceddProdStoreBasDTOBodyElem2 = svcReqBodyElem.addChildElement("custInfoIfDTO");
		makeParam(proceddProdStoreBasDTOBodyElem2, null, "svcContId", aplyPplProdInfo.getSvcContId());
		
		SOAPElement proceddProdStoreBasDTOBodyElem3 = svcReqBodyElem.addChildElement("custPplInfoIfListDTO");
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "prodId", aplyPplProdInfo.getProdId());
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "prodNm", aplyPplProdInfo.getProdNm());
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "efctStDt", aplyPplProdInfo.getEfctStDt());
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "efctFnsDt", aplyPplProdInfo.getEfctFnsDt());
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "prodHstSeq", aplyPplProdInfo.getProdHstSeq());
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "prodSbscTrmnCd", "U");
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "prodTypeCd", "P");
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "prodCtgCd", "MO");
		
		SOAPElement proceddProdStoreBasDTOBodyElem4 = svcReqBodyElem.addChildElement("custPplUnitSvcInfoIfListDTO");
		
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "prodId", aplyProdParamInfo.getProdId());
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "unitSvcId", aplyProdParamInfo.getUnitSvcId());
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "efctStDt", aplyProdParamInfo.getEfctStDt());
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "efctFnsDt", aplyProdParamInfo.getEfctFnsDt());
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "prodHstSeq", aplyProdParamInfo.getProdHstSeq());
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "prodSbscTrmnCd", "U");
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "paramSbst", timeOption);
		
		return soapMessage;
	}

	/**
	 * KOS 부가서비스 이력 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosSvcHstByCustIdRetv(String operNm, String cntrNo) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		// 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName", 	"NBSS_OPD");
		makeParam(headerEle, null, "svcName", 	"SvcHstByCustIdRetvSO");
		makeParam(headerEle, null, "fnName", 	"service");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "SvcHstByCustIdRetvSO");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "service");

		SOAPElement proceddProdStoreBasDTOBodyElem = svcReqBodyElem.addChildElement("SvcHstByCustIdRetvDTO");
		makeParam(proceddProdStoreBasDTOBodyElem, null, "svcContId", cntrNo);
		makeParam(proceddProdStoreBasDTOBodyElem, null, "dbRetvSeq", "0");
		makeParam(proceddProdStoreBasDTOBodyElem, null, "dbRetvCascnt", "200");

		return soapMessage;
	}

	/**
	 * (사전 체크)KOS 부가서비스 가입 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosProdSbscTrmnStoreTrtPreCheck(String operNm, AplyRoaming aplyRoamingInfo) throws Exception
	{
		logger.info("makeSoapMessageForKosProdSbscTrmnStoreTrtPreCheck START");
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);
		
		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		String tmpChCd = "08";
		String listDtoName = "";
		String infoDtoName = "";
		if(YappUtil.isEq("A", aplyRoamingInfo.getProdSbscTrmnCd())) {
			listDtoName = "sbscProdInfoIfListDTO";
			infoDtoName = "sbscProdUnitSvcInfoIfListDTO";
		}
		else if(YappUtil.isEq("C", aplyRoamingInfo.getProdSbscTrmnCd())) {
			listDtoName = "trmnProdInfoIfListDTO";
			infoDtoName = "trmnProdUnitSvcInfoIfListDTO";
		}
		else if(YappUtil.isEq("U", aplyRoamingInfo.getProdSbscTrmnCd())) {
			listDtoName = "chgProdInfoIfListDTO";
			infoDtoName = "chgProdUnitSvcInfoIfListDTO";
		}
		// SHUB 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName"	, "NBSS_RLE");
		makeParam(headerEle, null, "svcName"	, "MobileProdPrevChkSO");
		makeParam(headerEle, null, "fnName"		, "service");
		makeParam(headerEle, null, "globalNo"	, YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType"	, "YD");
		makeParam(headerEle, null, "trFlag"		, "T");
		makeParam(headerEle, null, "trDate"		, YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime"		, YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp"		, SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId"		, "91192332");
		makeParam(headerEle, null, "orgId"		, "SPT8050");
		makeParam(headerEle, null, "cmpnCd"		, "KT");
		makeParam(headerEle, null, "srcId"		, "SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime"	, YappUtil.getCurDate("yyyyMMddHHmmss"));
		
		SOAPBody soapBody = envelope.getBody();
		
		// service_request
		SOAPElement svcReq = soapBody.addChildElement("service_request");
		// bizHeader
		SOAPElement bizHeader = svcReq.addChildElement("bizHeader");
		makeParam(bizHeader, null, "orderId"  , "orderId");
		makeParam(bizHeader, null, "cbSvcName", "MobileProdPrevChkSO");
		makeParam(bizHeader, null, "cbFnName" , "service");
		
		
		// MobileProdChckInptMultiDTO
		SOAPElement multiDTO = svcReq.addChildElement("MobileProdChckInptMultiDTO");
		
		
		// ruleWorkInfoDTO
		SOAPElement ruleWorkInfoDTO = multiDTO.addChildElement("ruleWorkInfoDTO");
		makeParam(ruleWorkInfoDTO, null, "ruleEvCd"			, "SRG");	// 고정
		makeParam(ruleWorkInfoDTO, null, "ruleAplyDivCd"	, "C");		// 고정
		makeParam(ruleWorkInfoDTO, null, "ruleAplyPotimCd"	, "CM");	// 고정
		makeParam(ruleWorkInfoDTO, null, "chCd"				, "08");	// 고정
		makeParam(ruleWorkInfoDTO, null, "prodCtgCdVal"		, "MO");	// 고정
		
		
		// ruleCustInfoDTO
		SOAPElement ruleCustInfoDTO = multiDTO.addChildElement("ruleCustInfoDTO");
		makeParam(ruleCustInfoDTO, null, "svcContId"		, aplyRoamingInfo.getCntrNo());
		
		
		// ruleSvcInfoListDTO
		SOAPElement ruleSvcInfoListDTO = multiDTO.addChildElement("ruleSvcInfoListDTO");
		makeParam(ruleSvcInfoListDTO, null, "trtTgtSvcCd"	, aplyRoamingInfo.getProdId());			// 부가서비스코드
		makeParam(ruleSvcInfoListDTO, null, "trtSttusCd"	, aplyRoamingInfo.getProdSbscTrmnCd());	// 가입 A, 변경 U, 해지 D
		makeParam(ruleSvcInfoListDTO, null, "prodCtgCdVal"	, "MO");								// 고정
		if(YappUtil.isEq("A", aplyRoamingInfo.getProdSbscTrmnCd())) {
			makeParam(ruleSvcInfoListDTO, null, "hstSeq"	, "0");									// 변경, 해지 일 때 단말에서 넘겨준 걸로 넣어야 함.
		}
		else if(YappUtil.isEq("C", aplyRoamingInfo.getProdSbscTrmnCd()) || YappUtil.isEq("U", aplyRoamingInfo.getProdSbscTrmnCd())) {
			makeParam(ruleSvcInfoListDTO, null, "hstSeq"	, aplyRoamingInfo.getProdHstSeq());		// 변경, 해지 일 때 단말에서 넘겨준 걸로 넣어야 함.
		}
		
		
		// A : sbscProdInfoIfListDTO
		// C : trmnProdInfoIfListDTO
		// U : chgProdInfoIfListDTO
		SOAPElement sbscProdInfoIfListDTO = multiDTO.addChildElement(listDtoName);
		makeParam(sbscProdInfoIfListDTO, null, "prodId"		, aplyRoamingInfo.getProdId());			// 부가서비스코드
	  //makeParam(ruleSvcInfoListDTO, null, "prodNm"		, "Y로밍 프리패스");						// 부가서비스명
		if(YappUtil.isEq("A", aplyRoamingInfo.getProdSbscTrmnCd())) {
			makeParam(sbscProdInfoIfListDTO, null, "prodHstSeq"	, "");								// 고정
		}
		else if(YappUtil.isEq("C", aplyRoamingInfo.getProdSbscTrmnCd()) || YappUtil.isEq("U", aplyRoamingInfo.getProdSbscTrmnCd())) {
			makeParam(sbscProdInfoIfListDTO, null, "prodHstSeq", aplyRoamingInfo.getProdHstSeq());
		}
		makeParam(sbscProdInfoIfListDTO, null, "prodSbscTrmnCd", aplyRoamingInfo.getProdSbscTrmnCd());	// 가입 A, 변경 U, 해지 C
		makeParam(sbscProdInfoIfListDTO, null, "prodTypeCd"	, "R");									// 고정
		makeParam(sbscProdInfoIfListDTO, null, "prodPtclTypeCd", "J");									// 고정
		makeParam(sbscProdInfoIfListDTO, null, "efctStDt"		, aplyRoamingInfo.getEfctStDt());		// 단말에서 받아서 처리_시작 만료일
		makeParam(sbscProdInfoIfListDTO, null, "efctFnsDt"		, aplyRoamingInfo.getEfctFnsDt());		// 단말에서 받아서 처리_시작 만료일
		makeParam(sbscProdInfoIfListDTO, null, "prodCtgCd"		, "MO");								// 고정
		
		
		// A : sbscProdUnitSvcInfoIfListDTO
		// C : trmnProdUnitSvcInfoIfListDTO
		// U : chgProdUnitSvcInfoIfListDTO
		SOAPElement sbscProdUnitSvcInfoIfListDTO = multiDTO.addChildElement(infoDtoName);
		makeParam(sbscProdUnitSvcInfoIfListDTO, null, "prodId"			, aplyRoamingInfo.getProdId());
		makeParam(sbscProdUnitSvcInfoIfListDTO, null, "unitSvcId"		, aplyRoamingInfo.getUnitSvcId());
		makeParam(sbscProdUnitSvcInfoIfListDTO, null, "efctStDt"		, aplyRoamingInfo.getEfctStDt());
		makeParam(sbscProdUnitSvcInfoIfListDTO, null, "efctFnsDt"		, aplyRoamingInfo.getEfctFnsDt());
		makeParam(sbscProdUnitSvcInfoIfListDTO, null, "prodHstSeq"		, aplyRoamingInfo.getProdHstSeq());
		makeParam(sbscProdUnitSvcInfoIfListDTO, null, "prodSbscTrmnCd"	, aplyRoamingInfo.getProdSbscTrmnCd());
		makeParam(sbscProdUnitSvcInfoIfListDTO, null, "paramSbst"		, "STRT_DT="+aplyRoamingInfo.getEfctStDt()+"|END_DT="+aplyRoamingInfo.getEfctFnsDt() + "|ALW_FLG=0|");
		
		logger.info("soapMessage:"+soapMessage);
		logger.info("makeSoapMessageForKosProdSbscTrmnStoreTrtPreCheck END");
		return soapMessage;
	}
	
	/**
	 * KOS 부가서비스 가입 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosProdSbscTrmnStoreTrt(String operNm, AplyRoaming aplyRoamingInfo) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		String tmpChCd = "08";
		String listDtoName = "sbscProdInfoIfListDTO";
		String infoDtoName = "sbscProdUnitSvcInfoIfListDTO";
		if(YappUtil.isEq("C", aplyRoamingInfo.getProdSbscTrmnCd())) {
			listDtoName = "trmnProdInfoIfListDTO";
			infoDtoName = "trmnProdUnitSvcInfoIfListDTO";
		}else if(YappUtil.isEq("U", aplyRoamingInfo.getProdSbscTrmnCd())) {
			listDtoName = "chgProdInfoIfListDTO";
			infoDtoName = "chgProdUnitSvcInfoIfListDTO";
		}
		// SHUB 접속 ID, PWD 세팅
		makeParam(headerEle, null, "appName", 	"NBSS_ORD");
		makeParam(headerEle, null, "svcName", 	"/ORD/PipelineSVC/NPMB/sbsctrmn/store/PL_ProdSbscTrmnStoreTrt");
		makeParam(headerEle, null, "fnName", 	"processProdStoreBas");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"SRC_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("processProdStoreBasRequestElement");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "orderId", "orderId");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "/ORD/PipelineSVC/NPMB/sbsctrmn/store/PL_ProdSbscTrmnStoreTrt");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "processProdStoreBas");

		SOAPElement proceddProdStoreBasDTOBodyElem = svcReqBodyElem.addChildElement("wrkjobTrtInfoIfDTO");
		makeParam(proceddProdStoreBasDTOBodyElem, null, "wrkjobTrtCd", "SRG");
		makeParam(proceddProdStoreBasDTOBodyElem, null, "chCd", tmpChCd);
		makeParam(proceddProdStoreBasDTOBodyElem, null, "aplyPotimCd", "CM");
		
		SOAPElement proceddProdStoreBasDTOBodyElem2 = svcReqBodyElem.addChildElement("custInfoIfDTO");
		makeParam(proceddProdStoreBasDTOBodyElem2, null, "svcContId", aplyRoamingInfo.getCntrNo());
		makeParam(proceddProdStoreBasDTOBodyElem2, null, "svcContSttusCd", "A");
		
		
		SOAPElement proceddProdStoreBasDTOBodyElem3 = svcReqBodyElem.addChildElement(listDtoName);
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "prodId", aplyRoamingInfo.getProdId());
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "efctStDt", aplyRoamingInfo.getEfctStDt());
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "efctFnsDt", aplyRoamingInfo.getEfctFnsDt());
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "prodHstSeq", aplyRoamingInfo.getProdHstSeq());
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "prodSbscTrmnCd", aplyRoamingInfo.getProdSbscTrmnCd());
		makeParam(proceddProdStoreBasDTOBodyElem3, null, "prodTypeCd", "R");
		if(YappUtil.isEq("C", aplyRoamingInfo.getProdSbscTrmnCd()) || YappUtil.isEq("U", aplyRoamingInfo.getProdSbscTrmnCd())) {
			makeParam(proceddProdStoreBasDTOBodyElem3, null, "prodPtclTypeCd", "J");
			makeParam(proceddProdStoreBasDTOBodyElem3, null, "prodCtgCd", "MO");
		}
		
		SOAPElement proceddProdStoreBasDTOBodyElem4 = svcReqBodyElem.addChildElement(infoDtoName);
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "prodId", aplyRoamingInfo.getProdId());
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "unitSvcId", aplyRoamingInfo.getUnitSvcId());
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "efctStDt", aplyRoamingInfo.getEfctStDt());
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "efctFnsDt", aplyRoamingInfo.getEfctFnsDt());
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "prodHstSeq", aplyRoamingInfo.getProdHstSeq());
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "prodSbscTrmnCd", aplyRoamingInfo.getProdSbscTrmnCd());
		makeParam(proceddProdStoreBasDTOBodyElem4, null, "paramSbst", "STRT_DT="+aplyRoamingInfo.getEfctStDt()+"|END_DT="+aplyRoamingInfo.getEfctFnsDt() + "|ALW_FLG=0|");
		
		return soapMessage;
	}

	/**
	 * KOS 법정대리인 정보 조회 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosParentsInfo(String operNm, String telno) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		makeParam(headerEle, null, "appName", 	"NBSS_CDM");
		makeParam(headerEle, null, "svcName", 	"/CDM/PipelineSVC/NCST/CustInfoAdm/PL_LegalAgntInfoAdm");
		makeParam(headerEle, null, "fnName", 	"retrieveLegalAgntSvcNo");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"YD_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("retrieveLegalAgntSvcNoRequest");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "/CDM/PipelineSVC/NCST/CustInfoAdm/PL_LegalAgntInfoAdm");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "retrieveLegalAgntSvcNo");

		SOAPElement legalAgntInfoAdmDTOBodyElem = svcReqBodyElem.addChildElement("LegalAgntContInDTO");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "retvDivCd", "1");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "svcNo", telno);

		return soapMessage;
	}

	/**
	 * KOS 시군구 정보조회  호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosCityAdrInfoRetv(String operNm, AddressInfo addressInfo) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		makeParam(headerEle, null, "appName", 	"NBSS_CST");
		makeParam(headerEle, null, "svcName", 	"CityAdrInfoRetvSO");
		makeParam(headerEle, null, "fnName", 	"service");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"YD_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request ");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "CityAdrInfoRetvSO");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "service");

		SOAPElement adrInfoInDTOBodyElem = svcReqBodyElem.addChildElement("SiGunGuDTO");

		makeParam(adrInfoInDTOBodyElem, null, "sidoNm", addressInfo.getSidoNm());
		makeParam(adrInfoInDTOBodyElem, null, "sggNm", addressInfo.getSggNm());

		return soapMessage;
	}

	/**
	 * KOS 도로명, 지번 정보조회  호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosAdrInfoRetv(String operNm, AddressInfo addressInfo) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		makeParam(headerEle, null, "appName", 	"NBSS_CST");
		makeParam(headerEle, null, "svcName", 	"AdrInfoRetvSO");
		makeParam(headerEle, null, "fnName", 	"service");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"YD_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request ");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "AdrInfoRetvSO");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "service");

		SOAPElement adrInfoInDTOBodyElem = svcReqBodyElem.addChildElement("AdrInfoInDTO");

		makeParam(adrInfoInDTOBodyElem, null, "sidoNm", addressInfo.getSidoNm());
		makeParam(adrInfoInDTOBodyElem, null, "sggNm", addressInfo.getSggNm());
		if("1".equals(addressInfo.getAdrDivCd())){
			makeParam(adrInfoInDTOBodyElem, null, "roadNm", addressInfo.getRoadNm());
			makeParam(adrInfoInDTOBodyElem, null, "bldgNo", YappUtil.nToStr(addressInfo.getBldgNo()));
		}else{
			makeParam(adrInfoInDTOBodyElem, null, "eupMyunDongNm", addressInfo.getEupMyunDongNm());
		}
		makeParam(adrInfoInDTOBodyElem, null, "adrDivCd", addressInfo.getAdrDivCd());
		makeParam(adrInfoInDTOBodyElem, null, "dbRetvSeq", YappUtil.nToStr(addressInfo.getDbRetvSeq()));
		makeParam(adrInfoInDTOBodyElem, null, "dbRetvCascnt", YappUtil.nToStr(addressInfo.getDbRetvCascnt()));

		return soapMessage;
	}

	/**
	 * KOS 도로명->지번, 지번 -> 도로명 변환  호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosRefineAdrInfoRetv(String operNm, AddressInfo addressInfo) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		makeParam(headerEle, null, "appName", 	"NBSS_CST");
		makeParam(headerEle, null, "svcName", 	"RefineAdrInfoRetvSO");
		makeParam(headerEle, null, "fnName", 	"service");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"YD_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request ");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "RefineAdrInfoRetvSO");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "service");

		SOAPElement adrInfoInDTOBodyElem = svcReqBodyElem.addChildElement("AdrRefineInfoInDTO");
		makeParam(adrInfoInDTOBodyElem, null, "inputAdrWhole", addressInfo.getSidoNm());
		makeParam(adrInfoInDTOBodyElem, null, "inputAdrDtl", addressInfo.getSggNm());
		makeParam(adrInfoInDTOBodyElem, null, "divCd", addressInfo.getAdrDivCd());
		makeParam(adrInfoInDTOBodyElem, null, "dbRetvSeq", YappUtil.nToStr(addressInfo.getDbRetvSeq()));
		makeParam(adrInfoInDTOBodyElem, null, "dbRetvCascnt", YappUtil.nToStr(addressInfo.getDbRetvCascnt()));

		return soapMessage;
	}

	/**
	 * KOS 문자발송 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForKosSendSms(String operNm, String telno, String smsBody, String transId, String msgId) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		makeParam(headerEle, null, "appName", 	"NBSS_B2C");
		makeParam(headerEle, null, "svcName", 	"/B2C/PipelineSVC/Ci/PL_YdbSendSms");
		makeParam(headerEle, null, "fnName", 	"setYdbSendSms");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"MGW_GR_MGW01");
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "svcName", "/B2C/PipelineSVC/Ci/PL_YdbSendSms");
		makeParam(bizHeaderBodyElem, null, "fnName", "setYdbSendSms");

		SOAPElement legalAgntInfoAdmDTOBodyElem = svcReqBodyElem.addChildElement("KtEaiKtExigentSmsRequest");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_EMP_NO", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_MSG_ID", msgId);
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_WORK_TYPE", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_MSG_TITLE", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_TYPE_CD", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_PRMS_TYPE", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_SEND_NO", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_REP_CORP_RPY_NO", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_CALBK_URL", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_RSRV_DSP_DT", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_SEND_MMS_TITLE", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_MSG_SBST", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_TRANSAC_ID", transId);
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_RCV_HNDEST_OS_TYPE", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_REP_INDV_RPY_NO", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_RCV_TEL_NO", telno);
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_CUST_KT_YN", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_CUST_PRMS", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_PARAM_VAL01", smsBody);
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_PARAM_VAL02", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_PARAM_VAL03", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_PARAM_VAL04", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_PARAM_VAL05", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_PARAM_VAL06", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_PARAM_VAL07", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_PARAM_VAL08", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_PARAM_VAL09", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "SMS_PARAM_VAL10", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "ACCOUNT_ID", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "CONTACT_ID", "");
		makeParam(legalAgntInfoAdmDTOBodyElem, null, "CSN_NUM", "");

		return soapMessage;
	}

	/**
	 * KOS 단말정보
	 */
	public SOAPMessage makeSoapMessageForKosCondByModelInfoRetv(String operNm, String inqrIndCd, String intmModelNm) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(NAMESPACE_NS1, NAMESPACE_URI_NS1);

		// Header 생성
		SOAPElement headerEle = envelope.getHeader().addChildElement("commonHeader", NAMESPACE_NS1);
		
		makeParam(headerEle, null, "appName", 	"NBSS_PHY");
		makeParam(headerEle, null, "svcName", 	"CondByModelInfoRetvSO");
		makeParam(headerEle, null, "fnName", 	"service");
		makeParam(headerEle, null, "globalNo", 	YappUtil.getUUIDStr());
		makeParam(headerEle, null, "chnlType", 	"YD");
		makeParam(headerEle, null, "trFlag", 	"T");
		makeParam(headerEle, null, "trDate", 	YappUtil.getCurDate("yyyyMMdd"));
		makeParam(headerEle, null, "trTime", 	YappUtil.getCurDate("HHmmss"));
		makeParam(headerEle, null, "clntIp", 	SessionKeeper.VAL_SERVER_IP);
		makeParam(headerEle, null, "userId", 	"91192332");
		makeParam(headerEle, null, "orgId", 	"SPT8050");
		makeParam(headerEle, null, "cmpnCd", 	"KT");
		makeParam(headerEle, null, "srcId", 	"YD_" + operNm.toUpperCase());
		makeParam(headerEle, null, "lgDateTime", YappUtil.getCurDate("yyyyMMddHHmmss"));

		SOAPBody soapBody = envelope.getBody();
		
		SOAPElement svcReqBodyElem = soapBody.addChildElement("service_request ");
		SOAPElement bizHeaderBodyElem = svcReqBodyElem.addChildElement("bizHeader");
		makeParam(bizHeaderBodyElem, null, "cbSvcName", "CondByModelInfoRetvSO");
		makeParam(bizHeaderBodyElem, null, "cbFnName", "service");

		SOAPElement modelInfoFindDTOBodyElem = svcReqBodyElem.addChildElement("ModelInfoFindDTO");
		makeParam(modelInfoFindDTOBodyElem, null, "inqrIndCd", inqrIndCd);
		//makeParam(modelInfoFindDTOBodyElem, null, "intmModelId", intmModelId);
		makeParam(modelInfoFindDTOBodyElem, null, "intmModelNm", intmModelNm);
		return soapMessage;
	}

	/**
	 * 멤버십 포인트 관련 호출용 Soap 메시지를 생성한다.
	 */
	public SOAPMessage makeSoapMessageForMemPoint(String nameSpace, String nameSpaceUri) throws Exception
	{
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		
		SOAPPart soapPart = soapMessage.getSOAPPart();
		
		// SOAP Envelope
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.addNamespaceDeclaration(nameSpace, nameSpaceUri);
		
		return soapMessage;
	}
	
	public SOAPElement makeParam(SOAPElement bodyElement, String nameSpace, String key, String val) throws SOAPException
	{
		SOAPElement soapBodyElem1 = YappUtil.isEmpty(nameSpace) ? bodyElement.addChildElement(key) : bodyElement.addChildElement(key, nameSpace);
		soapBodyElem1.addTextNode(val);

		return soapBodyElem1;
	}

}
