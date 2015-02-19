package com.dart.newsapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dart.newsapp.domain.News;

/**
 * Spring Data JPA repository for the News entity.
 */
public interface NewsRepository extends JpaRepository<News,Long>{

	@Query("SELECT a.pseudo FROM Author AS a WHERE a.id = ?0")
	public String getAuthorPseudo(Long authorId);
}
