package us.luckylu.dev.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import us.luckylu.dev.entity.Link;
import us.luckylu.dev.run.StartupRunner;
import us.luckylu.dev.service.LinkService;

/**
 * 友情链接Controller类
 */
@RestController
@RequestMapping("/admin/link")
public class LinkAdminController {

	@Resource
    LinkService linkService;
	
	@Resource
	private StartupRunner startupRunner;
	
	/**
	 * 分页查询友情链接
	 * @param page
	 * @param rows
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String, Object> list(@RequestParam(value="page",required=false)Integer page,
			@RequestParam(value="rows",required=false)Integer rows) throws Exception{
		List<Link> linkList = linkService.list(page-1, rows);
		Long total = linkService.getCount();
		
		Map<String, Object> map = new HashMap<>();
		map.put("rows", linkList);
		map.put("total", total);
		return map;
	}
	
	/**
	 * 添加或者修改友情链接
	 * @param link
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public Map<String, Object> save(Link link) throws Exception{
		linkService.save(link);
		// 更新application域的缓存数据
		startupRunner.loadData();
		
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		return map;
	}
	
	/**
	 * 删除友情链接
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/delete")
	public Map<String, Object> delete(String ids) throws Exception{
		String[] idsStr = ids.split(",");
		for (int i = 0; i < idsStr.length; i++) {
			linkService.delete(Integer.parseInt(idsStr[i]));
		}
		startupRunner.loadData();
		
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		return map;
	}
	
}
