package us.luckylu.dev.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import us.luckylu.dev.entity.WebSite;
import us.luckylu.dev.service.WebSiteService;
import us.luckylu.dev.util.PageUtil;

/**
 * 收录电影网站控制器
 */
@Controller
@RequestMapping("/webSite")
public class WebSiteController {
	
	@Resource
	private WebSiteService webSiteService;

	/**
	 * 分页查询 收录电影网站信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list/{id}")
	public ModelAndView list(@PathVariable(value="id",required=false)Integer page) throws Exception{
		List<WebSite> webSiteList = webSiteService.list(null, page-1, 20);
		Long total = webSiteService.getCount(null);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("webSiteList", webSiteList);// 数据
		mav.addObject("pageCode", PageUtil.genPagination("/webSite/list", total, page, 20));// 分页
		mav.addObject("title", "电影网站列表");
		mav.addObject("mainPage", "webSite/list");
		mav.addObject("mainPageKey", "#f");
		
		mav.setViewName("index");
		return mav;
		
	}
}
