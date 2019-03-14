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

import us.luckylu.dev.entity.Film;
import us.luckylu.dev.repository.FilmRepository;
import us.luckylu.dev.service.FilmService;

/**
 * 电影Service接口实现类
 *
 */
@Service("filmService")
public class FilmServiceImpl implements FilmService {
	
	@Resource
	private FilmRepository filmRepository;

	@Override
	public void save(Film film) {
		filmRepository.save(film);
	}

	@Override
	public List<Film> list(Film film, Integer page, Integer pageSize) {
		Pageable pageable = new PageRequest(page, pageSize, Sort.Direction.DESC,"publishDate");
		
		Page<Film> pageFilm = filmRepository.findAll(new Specification<Film>() {
			@Override
			public Predicate toPredicate(Root<Film> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				if(film!=null) { // 防止查询条件空指针，先检查bean
					if(StringUtils.isNotBlank(film.getName())) {
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+film.getName().trim()+"%"));
					}
					
				}
				return predicate;
			}
		}, pageable);
		
		return pageFilm.getContent();
	}

	@Override
	public Long getCount(Film film) {
		return filmRepository.count(new Specification<Film>() {
			@Override
			public Predicate toPredicate(Root<Film> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				Predicate predicate = cb.conjunction();
				if(film!=null) { // 防止查询条件空指针，先检查bean
					if(StringUtils.isNotBlank(film.getName())) {
						predicate.getExpressions().add(cb.like(root.get("name"), "%"+film.getName().trim()+"%"));
					}
					
				}
				return predicate;
			}
		});
	}

	@Override
	public Film findById(Integer id) {
		return filmRepository.findOne(id);
	}

	@Override
	public void delete(Integer id) {
		filmRepository.delete(id);
	}

	@Override
	public Film getLast(Integer id) {
		return filmRepository.getLast(id);
	}

	@Override
	public Film getNext(Integer id) {
		return filmRepository.getNext(id);
	}

	@Override
	public List<Film> randomList(Integer n) {
		return filmRepository.randomList(n);
	}

}
