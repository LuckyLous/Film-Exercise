package us.luckylu.dev.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import us.luckylu.dev.entity.WebSiteInfo;
import us.luckylu.dev.run.StartupRunner;
import us.luckylu.dev.service.WebSiteInfoService;

/**
 * 电影动态信息管理Controller类
 */
@RestController
@RequestMapping("/admin/webSiteInfo")
public class WebSiteInfoAdminController {
	
	@Resource
	private WebSiteInfoService webSiteInfoService;
	
	@Resource
	private StartupRunner startupRunner;
	
	/**
	 * 分页查询电影动态信息
	 * @param page
	 * @param rows
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String, Object> list(WebSiteInfo webSiteInfo, @RequestParam(value="page",required=false)Integer page,
                                    @RequestParam(value="rows",required=false)Integer rows) throws Exception{
		List<WebSiteInfo> webSiteInfoList = webSiteInfoService.list(webSiteInfo,page-1, rows);
		Long total = webSiteInfoService.getCount(webSiteInfo);
		
		Map<String, Object> map = new HashMap<>();
		map.put("rows", webSiteInfoList);
		map.put("total", total);
		return map;
	}
	
	/**
	 * 添加或者修改电影信息
	 * @param film
	 * @param file
	 */
	@RequestMapping("/save")
	public Map<String, Object> save(WebSiteInfo webSiteInfo) throws Exception{
		webSiteInfo.setPublishDate(new Date());
		webSiteInfoService.save(webSiteInfo);
		startupRunner.loadData();
		
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		return map;
	}
	
	/**
	 * 删除电影信息
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public Map<String,Object> delete(@RequestParam(value="ids")String ids)throws Exception{
		String[] idsStr = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			webSiteInfoService.delete(Integer.parseInt(idsStr[i]));
		}
		startupRunner.loadData();
		
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		return map;
	}
	
}
