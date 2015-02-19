package com.dart.newsapp.web.rest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.dart.newsapp.domain.Author;
import com.dart.newsapp.domain.News;
import com.dart.newsapp.repository.NewsRepository;

/**
 * REST controller for managing News.
 */
@RestController
@RequestMapping("/api")
public class NewsResource {

    private final Logger log = LoggerFactory.getLogger(NewsResource.class);

    @Inject
    private NewsRepository newsRepository;

    /**
     * POST  /newss -> Create a new news.
     */
    @RequestMapping(value = "/newss",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody News news) {
        log.debug("REST request to save News : {}", news);
        newsRepository.save(news);
    }

    /**
     * GET  /newss -> get all the newss.
     */
    @RequestMapping(value = "/newss",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<News> getAll() {
        log.debug("REST request to get all Newss");
        return newsRepository.findAll();
    }

    /**
     * GET  /newss/:id -> get the "id" news.
     */
    @RequestMapping(value = "/newss/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<News> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get News : {}", id);
        News news = newsRepository.findOne(id);
        if (news == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(news, HttpStatus.OK);
    }

    /**
     * DELETE  /newss/:id -> delete the "id" news.
     */
    @RequestMapping(value = "/newss/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete News : {}", id);
        newsRepository.delete(id);
    }
    
    /**
     * VAVE NEWS WIHT HIS IMAGE SAVE HIS PICTURE ON DISK  /news/ -> save
     */
    @RequestMapping(value="/saveNews", method=RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public  ResponseEntity<News>  saveNews(@RequestParam("image") MultipartFile file, @RequestBody News news ){
    	String originalImageName = null;
    	String tmpImageName = null;
    	News nws = new News();
    	if (!file.isEmpty()) {
            try {
                originalImageName = file.getOriginalFilename();
                nws.setSubject(news.getSubject());
                nws.setDescription(news.getDescription());
                nws.setPublicationDate(news.getPublicationDate());
                nws.setAuthor(news.getAuthor());
                Author a = nws.getAuthor();
                tmpImageName = originalImageName + newsRepository.getAuthorPseudo(a.getId());
                nws.setImage(tmpImageName);
                byte[] bytes = file.getBytes();
                BufferedOutputStream buffStream = 
                        new BufferedOutputStream(new FileOutputStream(new File("/home/eddy/Documents/newsapp/src/docs/news/images/" + tmpImageName)));
                buffStream.write(bytes);
                buffStream.close();
                newsRepository.save(nws);
                return new ResponseEntity<>(nws, HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(nws, HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(news, HttpStatus.NOT_FOUND);
        }
    }
}
