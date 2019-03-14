package us.luckylu.dev.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import us.luckylu.dev.entity.WebSiteInfo;
import us.luckylu.dev.service.WebSiteInfoService;
import us.luckylu.dev.util.PageUtil;

/**
 * 电影网站动态控制器
 */
@Controller
@RequestMapping("/webSiteInfo")
public class WebSiteInfoController {
	
	@Resource
	private WebSiteInfoService webSiteInfoService;

	/**
	 * 分页查询 电影网站动态信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list/{id}")
	public ModelAndView list(@PathVariable(value="id",required=false)Integer page) throws Exception{
		List<WebSiteInfo> webSiteInfoList = webSiteInfoService.list(null, page-1, 20);
		Long total = webSiteInfoService.getCount(null);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("webSiteInfoList", webSiteInfoList);// 数据
		mav.addObject("pageCode", PageUtil.genPagination("/webSiteInfo/list", total, page, 20));// 分页
		mav.addObject("title", "电影网站动态信息列表");
		mav.addObject("mainPage", "webSiteInfo/list");
		mav.addObject("mainPageKey", "#f");
		
		mav.setViewName("index");
		return mav;
		
	}
}
