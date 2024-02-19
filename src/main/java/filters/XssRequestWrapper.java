/*
 * ybox version 1.0
 *
 *  Copyright ⓒ 2020 kt corp. All rights reserved.
 *
 *  This is a proprietary software of kt corp, and you may not use this file except in
 *  compliance with license agreement with kt corp. Any redistribution or use of this
 *  software, with or without modification shall be strictly prohibited without prior written
 *  approval of kt corp, and the copyright notice above does not evidence any actual or
 *  intended publication of such software.
 * 
 * 시스템명 : Y박스 1.0
 * 업 무 명 : 
 * 파 일 명 : XssRequestWrapper.java
 * 작 성 자 : 김병훈
 * 작 성 일 : 2020. 12. 08.
 * 설    명 : 
 * ************************************************************
 * 변경일     | 변경자 | 변경내역
 * ************************************************************
 * 2020. 12. 08. | 김병훈 | 최초작성
 * ************************************************************
 */
package filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XssRequestWrapper extends HttpServletRequestWrapper{
	
	public XssRequestWrapper(HttpServletRequest request) {
		super(request);		
	}

	public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values==null)  {
                    return null;
            }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
                   encodedValues[i] = cleanXSS(values[i]);
         }
        return encodedValues;
      }
    
      public String getParameter(String parameter) {
            String value = super.getParameter(parameter);
            if (value == null) {
                   return null;
                    }
            return cleanXSS(value);
      }
    
      public String getHeader(String name) {
          String value = super.getHeader(name);
          if (value == null)
              return null;
          return cleanXSS(value);
    
      }
    
      private String cleanXSS(String value) {
    	  StringBuffer sb = null;
    	  
    	  String[] checkStr_arr = {
    		"<script>","</script>",
    		"<javascript>","</javascript>",
    		"<iframe>","</iframe>",
    		"<vbscript>","</vbscript>",
    		"<object>","</object>",    		
    		"<marquee>","</marquee>",
    		"onerror","onclick","onload","onmouseover","onstart",
    		"autofocus","onfocus","prompt" //211005 XSS 취약점 조치 처리 추가
    	  };
    	  
    	  for (String checkStr : checkStr_arr) {
			while(value.indexOf(checkStr)!=-1){
				value = value.replaceAll(checkStr, "");
			}
			while(value.toLowerCase().indexOf(checkStr)!=-1){
				sb = new StringBuffer(value);
				sb = sb.replace(value.toLowerCase().indexOf(checkStr), value.toLowerCase().indexOf(checkStr) + checkStr.length(), "");
				value = sb.toString();
			}
    	  }
    	  
          value = value.replaceAll("eval\\((.*)\\)", "");
          value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
          
          //211116
		  value =value.replaceAll("[<>#'\"]", "");

          return value;
      }  

}
