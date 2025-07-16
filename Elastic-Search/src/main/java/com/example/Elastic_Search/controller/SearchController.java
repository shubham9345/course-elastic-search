package com.example.Elastic_Search.controller;

import com.example.Elastic_Search.service.CourseSearchService;
import com.example.Elastic_Search.service.SearchRequestParam;
import com.example.Elastic_Search.service.SearchResult;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * REST controller for searching courses using Elasticsearch.
 * <p>
 * Provides an endpoint to perform complex queries based on keywords,
 * age range, category, type, price range, and upcoming session date.
 * Supports paging and sorting of results.
 * </p>
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final CourseSearchService courseSearchService;

    /**
     * Constructs a new {@link SearchController} with the given service.
     *
     * @param courseSearchService the service responsible for executing search queries
     */
    public SearchController(CourseSearchService courseSearchService) {
        this.courseSearchService = courseSearchService;
    }

    /**
     * Searches for courses matching the provided criteria.
     *
     * @param q               optional keyword query to match course titles or descriptions
     * @param minAge          optional minimum age requirement for the course
     * @param maxAge          optional maximum age requirement for the course
     * @param category        optional category to filter courses
     * @param type            optional type of course (e.g., online, in-person)
     * @param minPrice        optional minimum course price
     * @param maxPrice        optional maximum course price
     * @param nextSessionDate optional date to return courses with sessions on or after this date
     * @param sort            sorting strategy (e.g., 'upcoming', 'price_asc', 'price_desc')
     * @param page            zero-based page index (defaults to 0)
     * @param size            number of items per page (defaults to 10)
     * @return {@link ResponseEntity} containing a JSON object with 'total' count and list of 'courses'
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate nextSessionDate,
            @RequestParam(defaultValue = "upcoming") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Build search parameters
        SearchRequestParam params = new SearchRequestParam(
                q, minAge, maxAge,
                category, type,
                minPrice, maxPrice,
                nextSessionDate,
                sort, page, size
        );

        // Execute search and map results
        SearchResult result = courseSearchService.search(params);
        List<Map<String, Object>> courses = result.getCourses().stream()
                .map(c -> Map.<String, Object>of(
                        "id", c.getId(),
                        "title", c.getTitle(),
                        "category", c.getCategory(),
                        "price", c.getPrice(),
                        "nextSessionDate", c.getNextSessionDate(),
                        "type", c.getType(),
                        "minAge", c.getMinAge(),
                        "maxAge", c.getMaxAge()
                ))
                .toList();
        // Return response with total count and course list
        return ResponseEntity.ok(Map.of(
                "total", result.getTotal(),
                "courses", courses
        ));
    }
}