package com.example.Elastic_Search.service;
import com.example.Elastic_Search.document.CourseDocument;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseSearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public CourseSearchService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public SearchResult search(SearchRequestParam params) {
        // Initialize Criteria for query building
        Criteria criteria = new Criteria();

        // 1) Full-text search
        if (params.getQ() != null && !params.getQ().isBlank()) {
            criteria.and(new Criteria("title").contains(params.getQ())
                    .or(new Criteria("description").contains(params.getQ())));
        }

        // 2) Keyword filters
        if (params.getCategory() != null) {
            criteria.and(new Criteria("category").is(params.getCategory()));
        }
        if (params.getType() != null) {
            criteria.and(new Criteria("type").is(params.getType()));
        }

        // 3) Age range filters
        if (params.getMinAge() != null) {
            criteria.and(new Criteria("maxAge").lessThanEqual(params.getMaxAge()));
        }
        if (params.getMaxAge() != null) {
            criteria.and(new Criteria("minAge").greaterThan(params.getMinAge()));
        }

        // 4) Price range filters
        if (params.getMinPrice() != null) {
            criteria.and(new Criteria("price").greaterThanEqual(params.getMinPrice()));
        }
        if (params.getMaxPrice() != null) {
            criteria.and(new Criteria("price").lessThanEqual(params.getMaxPrice()));
        }

        // 5) Date range filter
        if (params.getNextSessionDate() != null) {
            String isoStart = params.getNextSessionDate().format(DateTimeFormatter.ISO_DATE);
            criteria.and(new Criteria("nextSessionDate").greaterThanEqual(isoStart));
        }

        // 6) Build the query with sorting and pagination
        CriteriaQuery query = new CriteriaQuery(criteria);
        query.addSort(switch (params.getSort()) {
            case "priceAsc" -> Sort.by("price").ascending();
            case "priceDesc" -> Sort.by("price").descending();
            default -> Sort.by("nextSessionDate").ascending();
        });
        query.setPageable(PageRequest.of(params.getPage(), params.getSize()));

        // 7) Execute the search
        SearchHits<CourseDocument> hits = elasticsearchOperations.search(query, CourseDocument.class);
        long total = hits.getTotalHits();
        List<CourseDocument> docs = hits.getSearchHits().stream()
                .map(hit -> hit.getContent())
                .collect(Collectors.toList());

        return new SearchResult(total, docs);
    }
}