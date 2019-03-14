package us.luckylu.dev.controller.admin;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import us.luckylu.dev.entity.Film;
import us.luckylu.dev.run.StartupRunner;
import us.luckylu.dev.service.FilmService;
import us.luckylu.dev.service.WebSiteInfoService;
import us.luckylu.dev.util.DateUtil;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/film")
public class FilmAdminController {
	
	@Resource
	private FilmService filmService;
	
	@Resource
	private WebSiteInfoService webSiteInfoService;
	
	@Value("${imageFilePath}")
	private String imageFilePath;
	
	@Resource
	private StartupRunner startupRunner;
	
	/**
	 * 添加或者修改电影信息
	 * @param film
	 * @param file
	 */
	@RequestMapping("/save")
	public Map<String, Object> save(Film film, @RequestParam("imageFile")MultipartFile file) throws Exception{
		if(!file.isEmpty()) {
			String newFileName = uploadImage(file);
			film.setImageName(newFileName);
		}
		film.setPublishDate(new Date());
		
		filmService.save(film);
		// 更新application域的缓存数据
		startupRunner.loadData();
		
		Map<String, Object> map = new HashMap<>();
		map.put("success", true);
		return map;
	}
	
	
	@RequestMapping("/ckeditorUpload")
	public String ckeditorUpload(@RequestParam("upload")MultipartFile file,String CKEditorFuncNum)
			throws Exception {
		
		String newFileName = uploadImage(file);
		
		StringBuffer sb=new StringBuffer();
	    sb.append("<script type=\"text/javascript\">");
	    sb.append("window.parent.CKEDITOR.tools.callFunction("+ CKEditorFuncNum + ",'" +"/static/filmImage/"+ newFileName + "','')");
	    sb.append("</script>");
		return sb.toString();
		
	}

	/**
	 * 上传图片并返回图片名称
	 */
	private String uploadImage(MultipartFile file) throws Exception {
		// 获取文件名
		String fileName = file.getOriginalFilename();
		// 获取文件的后缀名
		String suffixName = fileName.substring(fileName.indexOf("."));
		String newFileName = DateUtil.getCurrentDateStr() + suffixName;
		FileUtils.copyInputStreamToFile(file.getInputStream(), new File(imageFilePath,newFileName));
		return newFileName;
	}
	
	/**
	 * 分页查询电影信息
	 * @param page
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public Map<String, Object> list(Film film,@RequestParam(value="page",required=false)Integer page,
			@RequestParam(value="rows",required=false)Integer rows) throws Exception{
		List<Film> filmList = filmService.list(film,page-1, rows);
		Long total = filmService.getCount(film);
		
		Map<String, Object> map = new HashMap<>();
		map.put("rows", filmList);
		map.put("total", total);
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
		boolean flag = true;
		for (int i = 0; i < idsStr.length; i++) {
			Integer filmId = Integer.parseInt(idsStr[i]);
			if(webSiteInfoService.getByFilmId(filmId).size()>0) {
				flag = false;
			}else {
				filmService.delete(filmId);
			}
		}
		// 更新application域的缓存数据
		startupRunner.loadData();
		
		Map<String, Object> map = new HashMap<>();
		if(flag) {
			map.put("success", true);
		}else {
			map.put("success", false);
			map.put("errorInfo", "电影动态信息中存在电影信息，不能删除！");
		}
		return map;
	}
	
	/**
	 * 根据id查找实体
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/findById")
	public Film findById(Integer id)throws Exception{
		return filmService.findById(id);
	}
	
	/**
	 * 下拉框模糊查询用到
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/comboList")
	public List<Film> comboList(String q)throws Exception{
		if(StringUtils.isBlank(q)) {
			return null;
		}
		Film film=new Film();
		film.setName(q);
		return filmService.list(film, 0, 30);// 最多查询30条记录
	}
}
