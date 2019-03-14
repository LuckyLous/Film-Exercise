package us.luckylu.dev.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import us.luckylu.dev.entity.Film;
import us.luckylu.dev.service.FilmService;
import us.luckylu.dev.service.WebSiteInfoService;
import us.luckylu.dev.util.PageUtil;

/**
 * 电影Contrller类
 */
@Controller
@RequestMapping("/film")
public class FilmController {
	
	@Resource
	private FilmService filmService;
	
	@Resource
	private WebSiteInfoService webSiteInfoService;

	/**
	 * 搜索电影 简单模糊查询
	 * @param s_film
	 * @param bindingResult
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/search")
	public ModelAndView search(@Valid Film s_film, BindingResult bindingResult){
		ModelAndView mav = new ModelAndView();
		
		if(bindingResult.hasErrors()){
			mav.addObject("error", bindingResult.getFieldError().getDefaultMessage());// 错误提示信息
			mav.addObject("title", "首页");
			mav.addObject("mainPage","dev/indexFilm");// 原页面
			mav.addObject("mainPageKey", "#f");
		}else{
			List<Film> filmList = filmService.list(s_film, 0, 32);
			mav.addObject("filmList", filmList);// 查询电影列表
			mav.addObject("title", s_film.getTitle());
			mav.addObject("mainPage","dev/result");// 搜索结果页面
			mav.addObject("mainPageKey", "#f");
			// 回显数据
			mav.addObject("s_film", s_film);
			mav.addObject("total", filmList.size());
		}
		
		mav.setViewName("index");
		return mav;
	}
	
	/**
	 * 分页查询电影信息
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list/{id}")
	public ModelAndView list(@PathVariable(value="id",required=false)Integer page) throws Exception{
		List<Film> filmList = filmService.list(null, page-1, 20);
		Long total = filmService.getCount(null);
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("filmList", filmList);// 数据
		mav.addObject("pageCode", PageUtil.genPagination("/dev/list", total, page, 20));// 分页
		mav.addObject("title", "电影列表");
		mav.addObject("mainPage", "dev/list");
		mav.addObject("mainPageKey", "#f");
		
		mav.setViewName("index");
		return mav;
		
	}
	
	/**
	 * 根据id获取电影详细信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/{id}")
	public ModelAndView view(@PathVariable(value="id",required=false)Integer id) throws Exception{
		Film film = filmService.findById(id);
		
		ModelAndView mav = new ModelAndView();
		// 电影基本信息
		mav.addObject("film", film);
		mav.addObject("title", film.getTitle());
		// 获取随机8条电影信息
		mav.addObject("randomFilmList", filmService.randomList(8));
		// 获取电影动态信息
		mav.addObject("webSiteInfoList", webSiteInfoService.getByFilmId(id));
		// 获取上一条、下一条电影信息
		mav.addObject("pageCode", getUpAndDownPageCode(filmService.getLast(id), filmService.getNext(id)));
		
		mav.addObject("mainPage", "dev/view");
		mav.addObject("mainPageKey", "#f");
		
		mav.setViewName("index");
		return mav;
		
	}
	/**
	 * 获取下一个电影你和上一个电影
	 * @param lastFilm
	 * @param nextFilm
	 * @return
	 */
	private String getUpAndDownPageCode(Film lastFilm,Film nextFilm){
		StringBuffer pageCode=new StringBuffer();
		if(lastFilm==null || lastFilm.getId()==null){
			pageCode.append("<p>上一篇：没有了</p>");
		}else{
			pageCode.append("<p>上一篇：<a href='/dev/"+lastFilm.getId()+"'>"+lastFilm.getTitle()+"</a></p>");
		}
		if(nextFilm==null || nextFilm.getId()==null){
			pageCode.append("<p>下一篇：没有了</p>");
		}else{
			pageCode.append("<p>下一篇：<a href='/dev/"+nextFilm.getId()+"'>"+nextFilm.getTitle()+"</a></p>");
		}
		return pageCode.toString();
	}
}
