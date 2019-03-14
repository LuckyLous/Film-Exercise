package us.luckylu.dev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 根路径以及其他请求处理
 * Created by Hello on 2018/5/31.
 */
@Controller
public class IndexController {

	/**
	 * 网页根目录请求
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView root(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("title","首页");
		mav.addObject("mainPage","dev/indexFilm");
		mav.addObject("mainPageKey", "#f");
		mav.setViewName("index");
		return mav;
	}

	/**
	 * 登录请求
	 * @return
	 */
	@RequestMapping("/login")
	public String login(){
		return "login";
	}

	/**
	 * 进入后台管理请求
	 * @return
	 */
	@RequestMapping("/admin")
	public String toAdmin(){
		return "admin/main";
	}
	
	/**
	 * 关于本站
	 * @return
	 */
	@RequestMapping("/aboutMe")
	public ModelAndView aboutMe(){
		ModelAndView mav = new ModelAndView();
		mav.addObject("title","关于本站");
		mav.addObject("mainPage","common/aboutMe");
		mav.addObject("mainPageKey", "#a");
		mav.setViewName("index");
		return mav;
	}
}
