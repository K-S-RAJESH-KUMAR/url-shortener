package com.rajesh.url_shortener.repo;

import com.rajesh.url_shortener.models.UrlMapping;
import com.rajesh.url_shortener.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping,Long> {
    UrlMapping findByShortUrl(String shortUrl);
    List<UrlMapping> findByUser(User user);

    int deleteByShortUrl(String shortUrl);

    @Query("SELECT u.id FROM UrlMapping u WHERE u.shortUrl = :shortUrl")
    Long findIdByShortUrl(String shortUrl);

}
