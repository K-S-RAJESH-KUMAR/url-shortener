package com.rajesh.url_shortener.sevice;

import com.rajesh.url_shortener.dto.ClickEventDTO;
import com.rajesh.url_shortener.dto.UrlMappingDTO;
import com.rajesh.url_shortener.models.ClickEvent;
import com.rajesh.url_shortener.models.UrlMapping;
import com.rajesh.url_shortener.models.User;
import com.rajesh.url_shortener.repo.ClickEventRepository;
import com.rajesh.url_shortener.repo.UrlMappingRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UrlMappingService {

    private UrlMappingRepository urlMappingRepository;
    private ClickEventRepository clickEventRepository;

    public UrlMappingDTO createShortUrl(String originalUrl, User user) {
        String shortUrl = generateShortUrl();
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setUser(user);
        urlMapping.setCreatedDate(LocalDateTime.now());
        urlMapping.setShortUrl(shortUrl);

        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
        return convertToDto(savedUrlMapping);
    }

    private UrlMappingDTO convertToDto(UrlMapping urlMapping)
    {
        return new UrlMappingDTO(urlMapping.getId(),urlMapping.getOriginalUrl(),urlMapping.getShortUrl(),urlMapping.getClickCount(),urlMapping.getCreatedDate(),urlMapping.getUser().getUsername());
    }


    private String generateShortUrl() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder shortUrl = new StringBuilder(8);
        Random random = new Random();

        for(int i=0;i<8;i++)
            shortUrl.append(characters.charAt(random.nextInt(characters.length())));

        return shortUrl.toString();
    }

    public List<UrlMappingDTO> getUrlByUser(User user) {
        return urlMappingRepository.findByUser(user).stream().map(this::convertToDto).toList();
    }

    public UrlMapping getOriginalUrl(String shortUrl) {
        UrlMapping urlMapping= urlMappingRepository.findByShortUrl(shortUrl);

        if(urlMapping!=null)
        {
            urlMapping.setClickCount(urlMapping.getClickCount()+1);
            urlMappingRepository.save(urlMapping);
            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setClickDate(LocalDateTime.now());
            clickEvent.setUrlMapping(urlMapping);
            clickEventRepository.save(clickEvent);
        }
        return urlMapping;
    }



    public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
        if(urlMapping!=null)
        {
            return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping,start,end).stream().collect(Collectors.groupingBy(clickEvent -> clickEvent.getClickDate().toLocalDate(),Collectors.counting())).entrySet().stream().map(entry-> new ClickEventDTO(entry.getKey(),entry.getValue())).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
        List<UrlMapping> urlMappings= urlMappingRepository.findByUser(user);
        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings,start.atStartOfDay(),end.plusDays(1).atStartOfDay());
        return clickEvents.stream().collect(Collectors.groupingBy(clickEvent -> clickEvent.getClickDate().toLocalDate(),Collectors.counting()));
    }

    @Transactional
    public int deleteUrlByShortUrlAndUser(String shortUrl) {
        clickEventRepository.deleteByUrlMapping(urlMappingRepository.findByShortUrl(shortUrl));
        return urlMappingRepository.deleteByShortUrl(shortUrl);
    }
}
