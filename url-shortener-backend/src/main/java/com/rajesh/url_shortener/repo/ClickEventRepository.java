package com.rajesh.url_shortener.repo;

import com.rajesh.url_shortener.models.ClickEvent;
import com.rajesh.url_shortener.models.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepository extends JpaRepository<ClickEvent,Long> {
    List<ClickEvent> findByUrlMappingAndClickDateBetween(UrlMapping urlMapping, LocalDateTime startTime,LocalDateTime endTime);
    List<ClickEvent> findByUrlMappingInAndClickDateBetween(List<UrlMapping> urlMapping, LocalDateTime startTime,LocalDateTime endTime);

    void deleteByUrlMapping(UrlMapping byShortUrl);
}
