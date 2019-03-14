package us.luckylu.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import us.luckylu.dev.entity.Link;

/**
 * 友情链接Repository接口
 *
 */
public interface LinkRepository extends JpaRepository<Link, Integer>{

}
