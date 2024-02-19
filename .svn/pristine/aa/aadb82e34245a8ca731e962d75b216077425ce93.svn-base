package com.kt.yapp.soap.response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kt.yapp.em.EnumYn;
import com.kt.yapp.soap.YappNamespaceContext;
import com.kt.yapp.util.YappUtil;

import lombok.Getter;

public abstract class YappSoapResponse 
{
	private static final Logger logger = LoggerFactory.getLogger(YappSoapResponse.class);

	@Getter	/** 반환코드 */
	protected String rtnCd;
	
	@Getter	/** transaction id */
	protected String transId;
	
	@Getter	/** 반환 상세 Desc */
	protected String rtnDesc;
	
	@Getter	/** 에러코드 */
	protected String errCd;
	
	@Getter	/** 에러 상세 */
	protected String errDesc;

	protected XPath xpath = XPathFactory.newInstance().newXPath();
	protected Document doc;
	
	private static YappNamespaceContext namespaceContext = new YappNamespaceContext();
	
	/**
	 * Soap 통신 결과 Response 값을 Paring 한다.
	 */
	protected void parsingData(SOAPMessage response) throws Exception
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		response.writeTo(baos);

		logger.info(new String(baos.toByteArray(), "utf-8"));
		
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(true);
		
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.parse(new ByteArrayInputStream(baos.toByteArray()));
		
		baos.close();

		xpath.setNamespaceContext(namespaceContext);
		
		this.setCmnData();
	}

	/**
	 * 조회 결과값(공통)을 세팅한다.
	 */
	protected void setCmnData() throws Exception
	{
		setRtnCd(YappUtil.findNotNullVal(getNodeText("sdp:return_code"), getNodeText("sdp:returnCode")));
		setRtnDesc(YappUtil.findNotNullVal(getNodeText("sdp:return_desc"), getNodeText("sdp:returnDesc")));
		transId = YappUtil.findNotNullVal(getNodeText("sdp:transaction_id"), getNodeText("sdp:TRANSACTIONID"));

		setData();
	}
	
	/**
	 * Node 타입의 구문에서 값을 조회한다.
	 */
	protected String getNodeText(String nodeName) throws Exception
	{
		return getNodeText(doc, "//" + nodeName);
	}
	protected String getNodeText(Node parentNode, String nodeName) throws Exception
	{
		return (String) xpath.evaluate(nodeName, parentNode, XPathConstants.STRING);
	}
	
	/**
	 * Node List 타입의 구문에서 값을 조회한다.
	 */
	protected List<String> getNodeListText(String nodeName) throws Exception
	{
		NodeList nodeList = (NodeList) xpath.evaluate("//" + nodeName, doc, XPathConstants.NODESET);
		List<String> rtnList = new ArrayList<>();
		
		if ( nodeList == null )
			return rtnList;
		
		for ( int i = 0; i < nodeList.getLength(); i++ )
		{
			Node node = nodeList.item(i);
			if ( node != null && YappUtil.isNotEmpty(node.getTextContent()))
				rtnList.add(node.getTextContent());
		}
		
		return rtnList;
	}

	/**
	 * 결과코드를 세팅한다.
	 */
	protected void setRtnCd(String rtnCd) throws Exception
	{
		if ( YappUtil.isEq(rtnCd, EnumYn.C_1.getValue()) )
			this.rtnCd = rtnCd;
		else if ( YappUtil.isEq(rtnCd, EnumYn.C_0.getValue()) )
			this.rtnCd = getErrCdPrefix() + YappUtil.ifNull(getNodeText("sdp:errorcode"), rtnCd);
		else
			this.rtnCd = getErrCdPrefix() + rtnCd;
	}

	/**
	 * 결과 상세 메시지를 세팅한다.
	 */
	protected void setRtnDesc(String rtnDesc) throws Exception
	{
		if ( YappUtil.isEq(rtnCd, EnumYn.C_0.getValue()) )
			this.rtnDesc = YappUtil.ifNull(getNodeText("sdp:errordescription"), rtnDesc);
		else
			this.rtnDesc = rtnDesc;
	}
	
	/**
	 * 에러 코드 Prefix 조회
	 */
	protected abstract String getErrCdPrefix();

	/**
	 * 에러 코드 Prefix 조회
	 */
	protected abstract void setData() throws Exception;
	
}
