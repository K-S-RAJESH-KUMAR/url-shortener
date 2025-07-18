package com.rajesh.url_shortener.controller;

import com.rajesh.url_shortener.dto.ClickEventDTO;
import com.rajesh.url_shortener.dto.UrlMappingDTO;
import com.rajesh.url_shortener.models.User;
import com.rajesh.url_shortener.sevice.UrlMappingService;
import com.rajesh.url_shortener.sevice.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlMappingController {

    private UrlMappingService urlMappingService;
    private UserService userService;

    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String,String> request, Principal pricipal){
        String originalUrl = request.get("originalUrl");
        User user= userService.findByUsername(pricipal.getName());
        UrlMappingDTO urlMappingDTO=urlMappingService.createShortUrl(originalUrl,user);
        return ResponseEntity.ok(urlMappingDTO);
    }

    @GetMapping("/myurls")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDTO>> getUserUrls(Principal pricipal){
        User user= userService.findByUsername(pricipal.getName());
        List<UrlMappingDTO> urls = urlMappingService.getUrlByUser(user);
        return  ResponseEntity.ok(urls);
    }

    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClickEventDTO>> getUrlAnalytics(@PathVariable String shortUrl, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate,formatter);
        LocalDateTime end = LocalDateTime.parse(endDate,formatter);

        List<ClickEventDTO> clickEventDTOS= urlMappingService.getClickEventsByDate(shortUrl,start,end);
        return ResponseEntity.ok(clickEventDTOS);
    }


    @GetMapping("/totalClicks")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<LocalDate,Long>> getTotalClicksByDate(Principal principal, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate){
        User user = userService.findByUsername(principal.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate start = LocalDate.parse(startDate,formatter);
        LocalDate end = LocalDate.parse(endDate,formatter);

        Map<LocalDate,Long> totalClicks= urlMappingService.getTotalClicksByUserAndDate(user,start,end);
        return ResponseEntity.ok(totalClicks);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteUrl(@RequestBody Map<String,String> request) {
        String shortUrl = request.get("shortUrl");
        int deleted = urlMappingService.deleteUrlByShortUrlAndUser(shortUrl);
        if (deleted==1) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
