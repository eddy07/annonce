package com.dart.newsapp.web.rest;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;
import com.dart.newsapp.domain.Author;
import com.dart.newsapp.repository.AuthorRepository;

/**
 * REST controller for managing Author.
 */
@RestController
@RequestMapping("/api")
public class AuthorResource {

    private final Logger log = LoggerFactory.getLogger(AuthorResource.class);

    @Inject
    private AuthorRepository authorRepository;

    /**
     * POST  /authors -> Create a new author.
     */
    /**@RequestMapping(value = "/authors",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody Author author) {
        log.debug("REST request to save Author : {}", author);
        authorRepository.save(author);
    }
    
      /**
     * POST  /authors -> Create a new author.
     */
    @RequestMapping(value = "/authors",method = RequestMethod.POST)
    @Timed
    public ResponseEntity<Author> create(@RequestParam("firstName") String firstName,@RequestParam("lastName") String lastName,
    		@RequestParam("pseudo") String pseudo,@RequestParam("password") String password,
    		@RequestParam("email") String email) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setPseudo(pseudo);
        author.setPassword(password);
        author.setEmail(email);
        authorRepository.save(author);
        return new ResponseEntity<>(author, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/picture",method = RequestMethod.POST)
    @Timed
    public String savePicture(@RequestParam("id") Long id,@RequestParam("picture") MultipartFile picture) {
        Author author = new Author();
        author = authorRepository.findOne(id);
        if(author == null) return "OK";
        String originalPictureName = null;
    	String tmpPictureName = null;
    	System.out.println("author picture:" + picture);
    	if (!picture.isEmpty()&&picture.getSize()>0) {
    		try {
    			validateImage(picture);
    			 
    			} catch (RuntimeException re) {
    			return re.toString();
    			}
            try {
                originalPictureName = picture.getOriginalFilename();
                tmpPictureName = originalPictureName + author.getPseudo();
                author.setPicture(tmpPictureName);
                authorRepository.save(author);
                saveImage(tmpPictureName + ".png", picture);
                return "SAVED";
            } catch (Exception e) {
                return e.toString();
            }
        } else {
            return "NOT FOUND";
        }
    }

    /**
     * GET  /authors -> get all the authors.
     */
    @RequestMapping(value = "/authors",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Author> getAll() {
        log.debug("REST request to get all Authors");
        return authorRepository.findAll();
    }

    /**
     * GET  /authors/:id -> get the "id" author.
     */
    @RequestMapping(value = "/authors/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Author> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Author : {}", id);
        Author author = authorRepository.findOne(id);
        if (author == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(author, HttpStatus.OK);
    }

    /**
     * DELETE  /authors/:id -> delete the "id" author.
     */
    @RequestMapping(value = "/authors/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Author : {}", id);
        authorRepository.delete(id);
    }
    

    
    private void validateImage(MultipartFile image) {
    	if (!image.getContentType().equals("image/png")) {
    	throw new RuntimeException("Only PNG images are accepted");
    	}
    	}
    public void  saveImage(String PictureName,MultipartFile file) throws IOException{
    	try{
    	 byte[] bytes = file.getBytes();
         BufferedOutputStream buffStream = 
                 new BufferedOutputStream(
                		 new FileOutputStream(
                				 new File("/home/eddy/Documents/newsapp/src/docs/authors/pictures/" + PictureName)));
         buffStream.write(bytes);
         buffStream.close();
    	}catch (IOException e) {
    		throw e;
    	}
    }
}
