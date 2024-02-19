package com.kt.yapp.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kt.yapp.util.YappUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "테스트 컨트롤러")
@Controller
public class FiveGTestController 
{
	private static final Logger logger = LoggerFactory.getLogger(FiveGTestController.class);

	@RequestMapping(value = "/na/fiveG", method = RequestMethod.POST)
	@ResponseBody
	public String fiveG(String param1, String param2) throws Exception
	{
		logger.info("====================================[parameter]=============================================");
		logger.info("param1 : " + param1 + ", param2 : " + param2);
		logger.info("====================================[result]=============================================");
		return "TEST";
	}
	
	@RequestMapping(value = "/na/fiveG", method = RequestMethod.GET)
	@ApiOperation(value="5g 화면을 보여준다.")
	public String fiveGH() throws Exception {
		
		ModelAndView mv = new ModelAndView();
		
		logger.info("5g 화면 start ::");
		
		
		mv.addObject("param", "");
		//mv.setViewName("eventRoom/eventRoom");
		mv.setViewName("eventRoom");
		return "errorssss"; 
	}
	
	@RequestMapping(value = "/na/index", method = RequestMethod.GET)
	public ModelAndView index() throws Exception {
		ModelAndView mv = new ModelAndView();
		
		logger.info("index  start ::");
		
		
		mv.addObject("param", "");
		mv.setViewName("index");
		return mv; 
	}
	
	@RequestMapping(value = "/na/index2", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView indexa() throws Exception {
		ModelAndView mv = new ModelAndView();
		
		logger.info("index 2 start ::");
		
		
		mv.addObject("param", "");
		mv.setViewName("index");
		return mv; 
	}
	
	@RequestMapping(value = "/na/index3", method = RequestMethod.GET)
	@ApiOperation(value="5g 화면을 보여준다.")
	public String indexb(HttpServletRequest req) throws Exception {
		String apiUrl = req.getRequestURI();
		String apiContextPath = req.getContextPath();
		String apiPathInfo = req.getPathInfo();
		String apiServletPath = req.getServletPath();
		logger.info("===================================");
		logger.info("apiUrl          :: " + apiUrl);
		logger.info("apiContextPath  :: " + apiContextPath);
		logger.info("apiPathInfo     :: " + apiPathInfo);
		logger.info("apiServletPath  :: " + apiServletPath);
		logger.info("===================================");
		return "index"; 
	}
}