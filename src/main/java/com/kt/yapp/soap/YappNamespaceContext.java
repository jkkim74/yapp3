package com.kt.yapp.soap;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;

import com.kt.yapp.util.YappUtil;

/**
 * Soap 통신 결과 파싱을 위한 Namespace 정의
 */
public class YappNamespaceContext implements NamespaceContext
{
	private Map<String, Object> namespaceMap = new HashMap<>();
	
	public YappNamespaceContext() 
	{
		namespaceMap.put(SoapConnUtil.NAMESPACE_ENV, SoapConnUtil.NAMESPACE_URI_ENV);
		namespaceMap.put(SoapConnUtil.NAMESPACE_MAIN, SoapConnUtil.NAMESPACE_URI_MAIN);
		namespaceMap.put(SoapConnUtil.NAMESPACE_HEADER, SoapConnUtil.NAMESPACE_URI_HEADER);
		namespaceMap.put(SoapConnUtil.NAMESPACE_N1, SoapConnUtil.NAMESPACE_URI_N1);
		namespaceMap.put(SoapConnUtil.NAMESPACE_N2, SoapConnUtil.NAMESPACE_URI_N2);
		namespaceMap.put(SoapConnUtil.NAMESPACE_NS1, SoapConnUtil.NAMESPACE_URI_NS1);
		namespaceMap.put(SoapConnUtil.NAMESPACE_BIZ, SoapConnUtil.NAMESPACE_URI_BIZ);
		namespaceMap.put(SoapConnUtil.NAMESPACE_PGET, SoapConnUtil.NAMESPACE_URI_PGET);
		namespaceMap.put(SoapConnUtil.NAMESPACE_PUSE, SoapConnUtil.NAMESPACE_URI_PUSE);
		namespaceMap.put(SoapConnUtil.NAMESPACE_PACU, SoapConnUtil.NAMESPACE_URI_PACU);
	}
	
	@Override
	public String getNamespaceURI(String prefix) 
	{
		if ( this.namespaceMap.containsKey(prefix) )
			return YappUtil.nToStr(this.namespaceMap.get(prefix));
		else
			return null;
	}

	@Override
	public String getPrefix(String namespaceURI) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterator getPrefixes(String namespaceURI) {
		// TODO Auto-generated method stub
		return null;
	}

}
