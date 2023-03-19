package com.BuildUrlShortner.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Document(collection = "ShortUrl")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortUrl {
    @Id
    private ObjectId id;
    private String originalUrl;
    private String shortUrl;
    private String shortUrlId;
    private Boolean expiry;
    private String ip;
    @DateTimeFormat(pattern="MM-dd-yyyy HH:mm:ss")
    private Date createdDate = new Date();
    public ShortUrl(String originalUrl,String shortUrl,String shortUrlId,Boolean expiry,String ip){
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.shortUrlId = shortUrlId;
        this.expiry = expiry;
        this.ip = ip;
    }
   
    public String getOriginalUrl(){
        return originalUrl;
    }
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }
    public String getShortUrl(){
        return shortUrl;
    }
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
    public String getShortUrlId(){
        return shortUrlId;
    }
    public void setShortUrlId(String shortUrlId) {
        this.shortUrlId = shortUrlId;
    }
    public Boolean getExpiry(){
        return expiry;
    }
    public void setExpiry(Boolean expiry){
        this.expiry = expiry;
    }
    public String getIp(){
        return ip;
    }
    public void setIp(String ip){
        this.ip = ip;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
   
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
