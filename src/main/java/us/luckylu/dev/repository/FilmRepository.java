package us.luckylu.dev.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import us.luckylu.dev.entity.Film;

/**
 * 电影Repository接口
 *
 */
public interface FilmRepository extends JpaRepository<Film, Integer>,JpaSpecificationExecutor<Film>{

	/**
	 * 获取上一个电影
	 * @param id
	 * @return
	 */
	@Query(value="SELECT * from tb_film where id < ?1 ORDER BY id desc limit 1",nativeQuery=true)
	public Film getLast(Integer id);
	
	@Query(value="SELECT * from tb_film where id > ?1 ORDER BY id asc limit 1",nativeQuery=true)
	public Film getNext(Integer id);
	
	@Query(value="SELECT * from tb_film order by RAND() LIMIT ?1",nativeQuery=true)
	public List<Film> randomList(Integer n);
}
