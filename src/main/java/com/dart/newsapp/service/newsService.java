package com.dart.newsapp.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.dart.newsapp.repository.NewsRepository;


@Service
public class newsService {
	
	@Inject
	private NewsRepository newsRepository;

	public String getAuthorPseudo(Long authorId){
		return newsRepository.getAuthorPseudo(authorId);
	}
}
