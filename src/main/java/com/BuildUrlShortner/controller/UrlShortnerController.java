package com.BuildUrlShortner.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.BuildUrlShortner.UrlGenerator.RandomIdGenerator;
import com.BuildUrlShortner.models.ShortUrl;
import com.BuildUrlShortner.repository.ShortUrlRepository;
import com.BuildUrlShortner.response.ResponseHandler;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class UrlShortnerController extends RandomIdGenerator {
    @Autowired
    ShortUrlRepository shortUrlRepo;
    @PostMapping("/createUrl")
    public ResponseEntity<Object> createUrl(@RequestBody ShortUrl short_url) {
      try {
        String checkShortUrlId = short_url.getShortUrl();
        List<ShortUrl> data =shortUrlRepo.findByShortUrl(checkShortUrlId);
        if(data.size()>0){
          return ResponseHandler.generateResponse("Alredy Exist Url", HttpStatus.BAD_REQUEST, data);
        }
        String ip = short_url.getIp();
        String originalUrl = short_url.getOriginalUrl();
        String shortId = getRandomId(8);
        String shortLink = short_url.getShortUrl()+shortId;
        String shortLinkId = shortId;
        Boolean expiry = false;
        short_url.setIp(ip);
        short_url.setOriginalUrl(originalUrl);
        short_url.setShortUrl(shortLink);
        short_url.setShortUrlId(shortLinkId);
        short_url.setExpiry(expiry);
        ShortUrl  shortUrl = shortUrlRepo.save(short_url);
        return new ResponseEntity<>(shortUrl, HttpStatus.CREATED);
      } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
    @GetMapping("/fetchAllUrl")
    public ResponseEntity<Object> getAllShortUrl(@RequestParam(required = false) String ip) {
      try {
        List<ShortUrl> data = new ArrayList<ShortUrl>();
  
        if (ip == null)
          shortUrlRepo.findAll().forEach(data::add);
        else
          shortUrlRepo.findByIp(ip).forEach(data::add);
  
        if (data.isEmpty()) {
          return ResponseHandler.generateResponse("No Data Found", HttpStatus.NOT_FOUND, null);
        }
        List<ShortUrl> update_data = new ArrayList<ShortUrl>();
        Boolean expiry_key = false;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        for(ShortUrl x : data){
          x.setIp(x.getIp());
          x.setOriginalUrl(x.getOriginalUrl());
          x.setShortUrl(x.getShortUrl());
          x.setShortUrlId(x.getShortUrlId());
          String formattedDate = formatter.format(x.getCreatedDate());
          expiry_key= isExpiredCheck(5,formattedDate);
          x.setExpiry(expiry_key);
          shortUrlRepo.save(x);
          update_data.add(x);
        }
  
        return ResponseHandler.generateResponse("Successfully fetch Url", HttpStatus.CREATED, data);
      } catch (Exception e) {
        return ResponseHandler.generateResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR,null);
      }
    }
    @GetMapping("/checkShortUrl")
    public ResponseEntity<Object> checkShortUrl(@RequestParam(required = false) String shortUrlId) {
      try {
        List<ShortUrl> data = shortUrlRepo.findByShortUrlId(shortUrlId);
        if(data.size()>0){
          if(data.get(0).getExpiry()){
            return ResponseHandler.generateResponse("Url Expired", HttpStatus.BAD_REQUEST, data);
          }
          else{
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            String formattedDate = formatter.format(data.get(0).getCreatedDate());
            Boolean isExpiry= isExpiredCheck(5,formattedDate);
            if(isExpiry){ 
              return ResponseHandler.generateResponse("Url Expired", HttpStatus.BAD_REQUEST, data);
            }
          }
        }
        if (data.isEmpty()) {
          return ResponseHandler.generateResponse("No Data Found", HttpStatus.NOT_FOUND, null);
        }
        return ResponseHandler.generateResponse("Data Fetch Successfully",HttpStatus.OK,data);
      } catch (Exception e) {
        return ResponseHandler.generateResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR,null);
      }
    }
    @DeleteMapping("/deleteUrl/{id}")
    public ResponseEntity<Object> deleteUrl(@PathVariable("id") String id) {
      try {
        shortUrlRepo.deleteById(id);
        return ResponseHandler.generateResponse("No Data Found", HttpStatus.NO_CONTENT, null);
      } catch (Exception e) {
        return ResponseHandler.generateResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR,null);
      }
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Object> deleteAllUrl() {
      try {
        shortUrlRepo.deleteAll();
        return ResponseHandler.generateResponse("Delete Successfully", HttpStatus.NO_CONTENT, null);
      } catch (Exception e) {
        return ResponseHandler.generateResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR,null);
      }
    }
}
