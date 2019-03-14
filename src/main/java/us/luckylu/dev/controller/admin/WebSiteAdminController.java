package us.luckylu.dev.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import us.luckylu.dev.entity.WebSite;
import us.luckylu.dev.run.StartupRunner;
import us.luckylu.dev.service.WebSiteInfoService;
import us.luckylu.dev.service.WebSiteService;

/**
 * 收录电影网址Controller类
 */
@RestController
@RequestMapping("/admin/webSite")
public class WebSiteAdminController {

	@Resource
    WebSiteService webSiteService;
	
	@Resource
	private WebSiteInfoService webSiteInfoService;
	
	@Resource
	private StartupRunner startupRunner;
	
	/**
	 * 分页查询收录电影网址
	 * @param page
	 * @param rows
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String, Object> list(WebSite webSite, @RequestParam(value="page",required=false)Integer page,
                                    @RequestParam(value="rows",required=false)Integer rows) throws Exception{
		List<WebSite> webSiteList = webSiteService.list(webSite,page-1, rows);
		Long total = webSiteService.getCount(webSite);
		
		Map<String, Object> map = new HashMap<>();
		map.put("rows", webSiteList);
		map.put("total", total);
		return map;
	}
	
	/**
	 * 添加或者修改收录电影网址
	 * @param link
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public Map<String, Object> save(WebSite webSite) throws Exception{
		webSiteService.save(webSite);
		startupRunner.loadData();
		
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		return map;
	}
	
	/**
	 * 删除收录电影网址
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public Map<String, Object> delete(String ids) throws Exception{
		String[] idsStr = ids.split(",");
		boolean flag = true;
		for (int i = 0; i < idsStr.length; i++) {
			Integer webSiteId = Integer.parseInt(idsStr[i]);
			if(webSiteInfoService.getByWebSiteId(webSiteId).size()>0) {
				flag = false;
			}else {
				webSiteService.delete(webSiteId);
			}
			
		}
		startupRunner.loadData();
		
		Map<String, Object> map = new HashMap<>();
		if(flag) {
			map.put("success", true);
		}else {
			map.put("success", false);
			map.put("errorInfo", "电影动态信息中存在电影网址信息，不能删除！");
		}
		return map;
	}
	
	/**
	 * 下拉框模糊查询用到
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/comboList")
	public List<WebSite> comboList(String q)throws Exception{
		if(StringUtils.isBlank(q)) {
			return null;
		}
		WebSite webSite = new WebSite();
		webSite.setUrl(q);
		return webSiteService.list(webSite, 0, 30);// 最多查询30条记录
	}
	
}
