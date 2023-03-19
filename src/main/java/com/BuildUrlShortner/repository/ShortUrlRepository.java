package com.BuildUrlShortner.repository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.BuildUrlShortner.models.ShortUrl;

public interface ShortUrlRepository extends MongoRepository <ShortUrl, String> {

    List<ShortUrl> findByIp(String ip);
    List<ShortUrl> findByShortUrlId(String shortUrlId);
    List<ShortUrl> findByShortUrl(String shortUrl);
}
