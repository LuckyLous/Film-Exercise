package us.luckylu.dev.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import us.luckylu.dev.entity.WebSiteInfo;
import us.luckylu.dev.repository.WebSiteInfoRepository;
import us.luckylu.dev.service.WebSiteInfoService;

@Service("webSiteInfoService")
public class WebSiteInfoServiceImpl implements WebSiteInfoService {

	@Resource
    WebSiteInfoRepository webSiteInfoRepository;
	
	@Override
	public List<WebSiteInfo> list(WebSiteInfo webSiteInfo, Integer page, Integer pageSize) {
		Pageable pageable = new PageRequest(page, pageSize, Sort.Direction.DESC,"publishDate");
		
		Page<WebSiteInfo> pageWebSiteInfo = webSiteInfoRepository.findAll(new Specification<WebSiteInfo>() {
			@Override
			public Predicate toPredicate(Root<WebSiteInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				if(webSiteInfo!=null) { // 防止查询条件空指针，先检查bean
					if(StringUtils.isNotBlank(webSiteInfo.getInfo())) {
						predicate.getExpressions().add(cb.like(root.get("info"), "%"+webSiteInfo.getInfo().trim()+"%"));
					}
					
				}
				return predicate;
			}
		}, pageable);
		
		return pageWebSiteInfo.getContent();
	}

	@Override
	public Long getCount(WebSiteInfo webSiteInfo) {
		return webSiteInfoRepository.count(new Specification<WebSiteInfo>() {
			@Override
			public Predicate toPredicate(Root<WebSiteInfo> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				if(webSiteInfo!=null) { // 防止查询条件空指针，先检查bean
					if(StringUtils.isNotBlank(webSiteInfo.getInfo())) {
						predicate.getExpressions().add(cb.like(root.get("info"), "%"+webSiteInfo.getInfo().trim()+"%"));
					}
					
				}
				return predicate;
			}
		});
	}

	@Override
	public List<WebSiteInfo> getByFilmId(Integer filmId) {
		return webSiteInfoRepository.getByFilmId(filmId);
	}

	@Override
	public List<WebSiteInfo> getByWebSiteId(Integer webSiteId) {
		return webSiteInfoRepository.getByWebSiteId(webSiteId);
	}

	@Override
	public void save(WebSiteInfo webSiteInfo) {
		webSiteInfoRepository.save(webSiteInfo);
	}

	@Override
	public void delete(Integer id) {
		webSiteInfoRepository.delete(id);
	}

}
